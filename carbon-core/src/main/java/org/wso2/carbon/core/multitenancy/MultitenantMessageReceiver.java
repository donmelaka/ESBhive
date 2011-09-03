/*                                                                             
 * Copyright 2004,2005 The Apache Software Foundation.                         
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");             
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,           
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package org.wso2.carbon.core.multitenancy;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisEngine;
import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.local.LocalTransportSender;
import org.apache.axis2.util.MessageContextBuilder;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.multitenancy.utils.TenantUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This MessageReceiver will try to locate the tenant specific AxisConfiguration and dispatch the
 * request to that AxisConfiguration. Dispatching to the actual service & operation will happen
 * within the tenant specific AxisConfiguration.
 */
public class MultitenantMessageReceiver implements MessageReceiver {

    private static final Log log = LogFactory.getLog(MultitenantMessageReceiver.class);

    public void receive(MessageContext mainInMsgContext) throws AxisFault {

        ConfigurationContext mainConfigCtx = mainInMsgContext.getRootContext();
        String to = mainInMsgContext.getTo().getAddress();

        int tenantDelimiterIndex = to.indexOf("/t/");
        if (tenantDelimiterIndex == -1) {
            throw new AxisFault("Cannot find service & operation for " + to);
        }

        try {
            // Locate the proper ConfigurationContext
            String tenant = TenantUtils.getTenantFromUrl(to);
            if (tenant == null) {
                // Throw an AxisFault: Tenant not specified
                handleException(mainInMsgContext, new AxisFault("Tenant not specified"));
                return;
            }

            ConfigurationContext tenantConfigCtx =
                    TenantUtils.getTenantConfigurationContext(tenant, mainConfigCtx);
            if (tenantConfigCtx == null) {
                // Throw AxisFault: Tenant does not exist
                handleException(mainInMsgContext, new AxisFault("Tenant " + tenant + "  not found"));
                return;
            }

            // Find the service
            String serviceName = getServiceName(to, tenantDelimiterIndex, tenant);

            if (mainInMsgContext.isDoingREST()) { // Handle REST requests
                doREST(mainInMsgContext, to, tenant, tenantConfigCtx, serviceName);
                return;
            } else {
                doSOAP(mainInMsgContext, tenantConfigCtx, serviceName);
            }
        } catch (Exception e) {
            String msg = "Exception occurred while trying to route message to tenant. To:" + to;
            log.error(msg, e);
            handleException(mainInMsgContext, new AxisFault(msg, e));
        }
    }

    private void doSOAP(MessageContext mainInMsgContext,
                        ConfigurationContext tenantConfigCtx,
                        String serviceName) throws AxisFault {

        // Call the correct tenant's configuration
        MessageContext tenantClientOutMsgCtx = tenantConfigCtx.createMessageContext();
        Options options = tenantClientOutMsgCtx.getOptions();

        options.setTo(new EndpointReference("local://"+ tenantConfigCtx.getServicePath() +"/" + serviceName));
        options.setAction(mainInMsgContext.getSoapAction());
        ServiceClient serviceClient = new ServiceClient();
        AxisService axisService = serviceClient.getAxisService();
        serviceClient.setOptions(options);
        OperationClient tenantOpClient =
                MultitenantOperationClient.getClient(tenantConfigCtx,
                                                     axisService,
                                                     mainInMsgContext); // The client in the main config which talks to the tenant config

        tenantClientOutMsgCtx.setEnvelope(mainInMsgContext.getEnvelope());

        tenantOpClient.addMessageContext(tenantClientOutMsgCtx);

        // Call the tenant. The tenant is responsible for dispatching to the correct service.
//        tenantOpClient.execute(true); // true - blocking

        System.out.println("---------- new one!!!!!!!!!!");
        new LocalTransportSender().invoke(tenantClientOutMsgCtx);

        // Send the response out
        MessageContext tenantClientInMsgCtx =
                tenantOpClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        MessageContext tenantClientInFaultMsgCtx =
                tenantOpClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_FAULT_VALUE);
        if (tenantClientInMsgCtx != null) {
            SOAPEnvelope response = tenantClientInMsgCtx.getEnvelope();
            if (log.isDebugEnabled()) {
                log.debug("Response: " + response);
            }
            if (response != null) {
                MessageContext mainOutMsgContext =
                        MessageContextBuilder.createOutMessageContext(mainInMsgContext);

                OperationContext mainOpContext = mainInMsgContext.getOperationContext();
                mainOpContext.addMessageContext(mainOutMsgContext);
                mainOutMsgContext.setOperationContext(mainOpContext);
                mainOutMsgContext.setEnvelope(tenantClientInMsgCtx.getEnvelope());
                if (response.hasFault()) {
                    AxisEngine.sendFault(mainOutMsgContext);
                } else {
                    AxisEngine.send(mainOutMsgContext);
                }
            }
        } else if (tenantClientInFaultMsgCtx != null) {
            SOAPEnvelope fault = tenantClientInFaultMsgCtx.getEnvelope();
            if (log.isDebugEnabled()) {
                log.debug("Fault: " + fault);
            }
            MessageContext mainOutMsgContext =
                    MessageContextBuilder.createOutMessageContext(mainInMsgContext);

            OperationContext mainOpContext = mainInMsgContext.getOperationContext();
            mainOpContext.addMessageContext(mainOutMsgContext);
            mainOutMsgContext.setOperationContext(mainOpContext);
            mainOutMsgContext.setEnvelope(tenantClientInFaultMsgCtx.getEnvelope());
            if (fault.hasFault()) {
                AxisEngine.sendFault(mainOutMsgContext);
            }
        }
    }

    private void doREST(MessageContext mainInMsgContext,
                        String to,
                        String tenant,
                        ConfigurationContext tenantConfigCtx,
                        String serviceName) throws ServletException, IOException {
        HttpServletRequest request =
                (HttpServletRequest) mainInMsgContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
        HttpServletResponse response =
                (HttpServletResponse) mainInMsgContext.getProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE);
        String serviceWithSlashT = "/t/" + tenant + "/" + serviceName;
        String requestUri = "local://" + tenantConfigCtx.getServicePath() + "/" + serviceName +
                            (to.endsWith(serviceWithSlashT) ?
                             "" :
                             "/" + to.substring(to.indexOf(serviceWithSlashT) + serviceWithSlashT.length() + 1));
        System.out.println("URI: " + requestUri);

        MultitenantRESTServlet restServlet = new MultitenantRESTServlet(tenantConfigCtx, requestUri);

        String httpMethod = (String) mainInMsgContext.getProperty(HTTPConstants.HTTP_METHOD);
        if (httpMethod.equals(Constants.Configuration.HTTP_METHOD_GET)) {
            restServlet.doGet(request, response);
        } else if (httpMethod.equals(Constants.Configuration.HTTP_METHOD_POST)) {
            restServlet.doPost(request, response);
        } else if (httpMethod.equals(Constants.Configuration.HTTP_METHOD_PUT)) {
            restServlet.doPut(request, response);
        } else if (httpMethod.equals(Constants.Configuration.HTTP_METHOD_DELETE)) {
            restServlet.doDelete(request, response);
        } else {
            // TODO: throw exception: Invalid verb
        }

        // Send the response
        MessageContext tenantOutMsgContext = restServlet.getOutMessageContext();
        MessageContext tenantOutFaultMsgContext = restServlet.getOutFaultMessageContext();
        OperationContext mainOpContext = mainInMsgContext.getOperationContext();

        if (tenantOutMsgContext != null) {
            MessageContext mainOutMsgContext =
                    MessageContextBuilder.createOutMessageContext(mainInMsgContext);
            mainOpContext.addMessageContext(mainOutMsgContext);
            mainOutMsgContext.setOperationContext(mainOpContext);
            mainOutMsgContext.setEnvelope(tenantOutMsgContext.getEnvelope());
            AxisEngine.send(mainOutMsgContext);
        } else if (tenantOutFaultMsgContext != null) {
            MessageContext mainOutFaultMsgContext =
                    MessageContextBuilder.createFaultMessageContext(mainInMsgContext, null); // TODO: set the fault exception
            mainOpContext.addMessageContext(mainOutFaultMsgContext);
            mainOutFaultMsgContext.setOperationContext(mainOpContext);
            mainOutFaultMsgContext.setEnvelope(tenantOutFaultMsgContext.getEnvelope());
            AxisEngine.sendFault(mainOutFaultMsgContext);
        }
    }

    private String getServiceName(String to, int tenantDelimiterIndex, String tenant) {
        String temp = to.substring(tenantDelimiterIndex + 3 + tenant.length() + 1);
        int indexOfSlash = temp.indexOf("/");
        String serviceName = (indexOfSlash != -1) ? temp.substring(0, indexOfSlash) : temp;
        if (log.isDebugEnabled()) {
            log.debug("Service:" + serviceName);
        }
        return serviceName;
    }

    private void handleException(MessageContext mainInMsgContext, AxisFault fault)
            throws AxisFault {
        MessageContext mainOutMsgContext =
                MessageContextBuilder.createFaultMessageContext(mainInMsgContext, fault);
        OperationContext mainOpContext = mainInMsgContext.getOperationContext();
        mainOpContext.addMessageContext(mainOutMsgContext);
        mainOutMsgContext.setOperationContext(mainOpContext);
        AxisEngine.sendFault(mainOutMsgContext);
    }
}
