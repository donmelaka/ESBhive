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

import com.ctc.wstx.exc.WstxEOFException;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.Callback;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.ClientUtils;
import org.apache.axis2.engine.AxisEngine;
import org.apache.axis2.i18n.Messages;
import org.apache.axis2.transport.TransportUtils;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.util.Utils;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.Map;

/**
 * This OperationClient will invoke the service deployed within a tenant AxisConfiguration, from
 * the main server AxisConfiguration, using the local transport.
 */
public class MultitenantOperationClient extends OperationClient {

    private static Log log = LogFactory.getLog(MultitenantOperationClient.class);

    private MessageContext msgContext;

    public static MultitenantOperationClient getClient(ConfigurationContext tenantConfigCtx,
                                                       AxisService axisService,
                                                       MessageContext msgContext) throws AxisFault {
        AxisServiceGroup axisServiceGroup = axisService.getAxisServiceGroup();
        ServiceGroupContext sgc = tenantConfigCtx.createServiceGroupContext(axisServiceGroup);
        return new MultitenantOperationClient(axisService.getOperation(ServiceClient.ANON_OUT_IN_OP),
                                              sgc.getServiceContext(axisService),
                                              new Options(),
                                              msgContext);
    }

    MultitenantOperationClient(AxisOperation axisOp, ServiceContext sc,
                               Options options,MessageContext msgContext) {
        super(axisOp, sc, options);
        this.msgContext = msgContext;
    }

    /**
     * Adds message context to operation context, so that it will handle the
     * logic correctly if the OperationContext is null then new one will be
     * created, and Operation Context will become null when some one calls reset().
     *
     * @param msgContext the MessageContext to add
     * @throws org.apache.axis2.AxisFault
     */
    public void addMessageContext(MessageContext msgContext) throws AxisFault {
        msgContext.setServiceContext(sc);
        if (msgContext.getMessageID() == null) {
            setMessageID(msgContext);
        }
        axisOp.registerOperationContext(msgContext, oc);
    }

    /**
     * Returns the message context for a given message label.
     *
     * @param messageLabel :
     *                     label of the message and that can be either "Out" or "In" and
     *                     nothing else
     * @return Returns MessageContext.
     * @throws AxisFault
     */
    public MessageContext getMessageContext(String messageLabel)
            throws AxisFault {
        return oc.getMessageContext(messageLabel);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * Executes the MEP. What this does depends on the specific MEP client. The
     * basic idea is to have the MEP client execute and do something with the
     * messages that have been added to it so far. For example, if its an Out-In
     * MEP, then if the Out message has been set, then executing the client asks
     * it to send the message and get the In message, possibly using a different
     * thread.
     *
     * @param block Indicates whether execution should block or return ASAP. What
     *              block means is of course a function of the specific MEP
     *              client. IGNORED BY THIS MEP CLIENT.
     * @throws AxisFault if something goes wrong during the execution of the MEP.
     */
    public void executeImpl(boolean block) throws AxisFault {
        if (completed) {
            throw new AxisFault(Messages.getMessage("mepiscomplted"));
        }
        ConfigurationContext cc = sc.getConfigurationContext();

        // copy interesting info from options to message context.
        MessageContext mc = oc.getMessageContext(WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
        if (mc == null) {
            throw new AxisFault(Messages.getMessage("outmsgctxnull"));
        }
        mc.setProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST,
                       msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST));
        mc.setProperty(MessageContext.REMOTE_ADDR,
                       msgContext.getProperty(MessageContext.REMOTE_ADDR));
        prepareMessageContext(cc, mc);

        if (options.getTransportIn() == null && mc.getTransportIn() == null) {
            mc.setTransportIn(ClientUtils.inferInTransport(cc.getAxisConfiguration(), options, mc));
        } else if (mc.getTransportIn() == null) {
            mc.setTransportIn(options.getTransportIn());
        }
        // Send the SOAP Message and receive a response
        send(mc);
        completed = true;
    }

    /**
     * Synchronously send the request and receive a response.  This relies on the transport
     * correctly connecting the response InputStream!
     *
     * @param msgContext the request MessageContext to send.
     * @return Returns MessageContext.
     * @throws AxisFault Sends the message using a two way transport and waits for a response
     */
    protected MessageContext send(MessageContext msgContext) throws AxisFault {

        // create the responseMessageContext

        MessageContext responseMessageContext =
                msgContext.getConfigurationContext().createMessageContext();

        responseMessageContext.setServerSide(false);
        responseMessageContext.setOperationContext(msgContext.getOperationContext());
        responseMessageContext.setOptions(new Options(options));
        responseMessageContext.setMessageID(msgContext.getMessageID());
        addMessageContext(responseMessageContext);
        responseMessageContext.setServiceContext(msgContext.getServiceContext());
        responseMessageContext.setAxisMessage(
                axisOp.getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE));

        //sending the message
        AxisEngine.send(msgContext);

        responseMessageContext.setDoingREST(msgContext.isDoingREST());

        // Copy RESPONSE properties which the transport set onto the request message context when it processed
        // the incoming response recieved in reply to an outgoing request.
        responseMessageContext.setProperty(MessageContext.TRANSPORT_HEADERS,
                                           msgContext.getProperty(MessageContext.TRANSPORT_HEADERS));
        responseMessageContext.setProperty(HTTPConstants.MC_HTTP_STATUS_CODE,
                                           msgContext.getProperty(HTTPConstants.MC_HTTP_STATUS_CODE));

        responseMessageContext.setProperty(MessageContext.TRANSPORT_IN, msgContext
                .getProperty(MessageContext.TRANSPORT_IN));
        responseMessageContext.setTransportIn(msgContext.getTransportIn());
        responseMessageContext.setTransportOut(msgContext.getTransportOut());
        handleResponse(responseMessageContext);
        return responseMessageContext;
    }

    /**
     * When synchronous send() gets back a response MessageContext, this is the workhorse
     * method which processes it.
     *
     * @param respMsgCtx the active response MessageContext
     * @throws AxisFault if something went wrong
     */
    protected void handleResponse(MessageContext respMsgCtx) throws AxisFault {
        // Options object reused above so soapAction needs to be removed so
        // that soapAction+wsa:Action on response don't conflict
        respMsgCtx.setSoapAction(null);

        if (respMsgCtx.getEnvelope() == null) {
            // If request is REST we assume the responseMessageContext is REST, so
            // set the variable
            /*
             * old code here was using the outbound message context to set the inbound SOAP namespace,
             * as such and passing it to TransportUtils.createSOAPMessage
             *
             * msgctx.getEnvelope().getNamespace().getNamespaceURI()
             *
             * However, the SOAP1.2 spec, appendix A indicates that if a SOAP1.2 message is sent to a SOAP1.1
             * endpoint, we will get a SOAP1.1 (fault) message response.  We need another way to set
             * the inbound SOAP version.  Best way to do this is to trust the content type and let
             * createSOAPMessage take care of figuring out what the SOAP namespace is.
             */
            try {
                SOAPEnvelope response = TransportUtils.createSOAPMessage(respMsgCtx);
                if (response != null) {
                    respMsgCtx.setEnvelope(response);
                }
            } catch (AxisFault e) {
                Throwable cause;
                if ((cause = e.getCause()) != null &&
                    (cause = cause.getCause()) != null &&
                    cause instanceof WstxEOFException) { // Is it an InOnly operation?
                    InputStream inStream =
                            (InputStream) respMsgCtx.getProperty(MessageContext.TRANSPORT_IN);
                    if (inStream != null) {
                        SOAPEnvelope response =
                                TransportUtils.createSOAPMessage(respMsgCtx);
                        respMsgCtx.setEnvelope(response);
                    }
                } else {
                    throw e;
                }
            }
            return;
        }
        SOAPEnvelope resenvelope = respMsgCtx.getEnvelope();
        if (resenvelope != null) {
            AxisEngine.receive(respMsgCtx);
            if (respMsgCtx.getReplyTo() != null) {
                sc.setTargetEPR(respMsgCtx.getReplyTo());
            }
            if (resenvelope.hasFault() || respMsgCtx.isProcessingFault()) {
                if (options.isExceptionToBeThrownOnSOAPFault()) {
                    // does the SOAPFault has a detail element for Excpetion
                    throw Utils.getInboundFaultFromMessageContext(respMsgCtx);
                }
            }
        }
    }


    /*protected void handleResponse(MessageContext responseMessageContext) throws AxisFault {
            SOAPEnvelope envelope = responseMessageContext.getEnvelope();
            if (envelope == null) {
                // If request is REST we assume the responseMessageContext is REST, so
                // set the variable
                InputStream inStream = (InputStream) responseMessageContext.
                        getProperty(MessageContext.TRANSPORT_IN);
                if (inStream != null) {
                    envelope = TransportUtils.createSOAPMessage(
                            responseMessageContext);
                    responseMessageContext.setEnvelope(envelope);
                }
                responseMessageContext.setEnvelope(envelope);
            }
            if (envelope != null) {
                if (envelope.hasFault()|| responseMessageContext.isProcessingFault()) {
                    //receiving a fault
                    AxisEngine.receive(responseMessageContext);
                    throw Utils.getInboundFaultFromMessageContext(responseMessageContext);
                }
            }
        }*/
}
