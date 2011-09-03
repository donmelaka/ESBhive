/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.core.transports;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.AxisServlet;
import org.apache.axis2.transport.http.HTTPConstants;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.wso2.carbon.utils.ServerConfiguration;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.core.transports.util.AtomProcessor;
import org.wso2.carbon.core.transports.util.RssProcessor;
import org.wso2.carbon.CarbonException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the main HTTP and HTTPS transport
 */
public class CarbonServlet extends AxisServlet {

    private static final long serialVersionUID = 3460108128756524161L;

    private Map<String, HttpGetRequestProcessor> getRequestProcessors =
            new LinkedHashMap<String, HttpGetRequestProcessor>();
    private static final QName ITEM_QN = new QName(ServerConstants.CARBON_SERVER_XML_NAMESPACE, "Item");
    private static final QName CLASS_QN = new QName(ServerConstants.CARBON_SERVER_XML_NAMESPACE, "Class");
    private RssProcessor rssProcessor = new RssProcessor();
    private AtomProcessor atomProcessor = new AtomProcessor();

    public CarbonServlet(ConfigurationContext configurationContext){
        this.configContext = configurationContext;
    }

    public void init(ServletConfig config) throws ServletException {
        this.axisConfiguration = this.configContext.getAxisConfiguration();
        this.servletConfig = config;
        populateGetRequestProcessors();
        initParams();
    }

    private void populateGetRequestProcessors() throws ServletException {
        try {
            OMElement docEle = ServerConfiguration.getInstance().getDocumentElement();
            if (docEle != null) {
                SimpleNamespaceContext nsCtx = new SimpleNamespaceContext();
                nsCtx.addNamespace("wsas", ServerConstants.CARBON_SERVER_XML_NAMESPACE);
                XPath xp = new AXIOMXPath("//wsas:HttpGetRequestProcessors/wsas:Processor");
                xp.setNamespaceContext(nsCtx);
                List nodeList = xp.selectNodes(docEle);
                for (Iterator iter = nodeList.iterator(); iter.hasNext();) {
                    OMElement processorEle = (OMElement) iter.next();
                    OMElement itemEle = processorEle.getFirstChildWithName(ITEM_QN);
                    if (itemEle == null) {
                        throw new ServletException("Required element, 'Item' not found!");
                    }
                    OMElement classEle = processorEle.getFirstChildWithName(CLASS_QN);
                    HttpGetRequestProcessor processor;
                    if (classEle == null) {
                        throw new ServletException("Required element, 'Class' not found!");
                    } else {
                        processor =
                                (HttpGetRequestProcessor)
                                        Class.forName(classEle.getText().trim()).newInstance();
                    }
                    getRequestProcessors.put(itemEle.getText().trim(),
                                             processor);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * WSAS specific GET implementation
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        initContextRoot(request);
        boolean isRequestHandled = false;

        try {
            String requestURI = request.getRequestURI();
            if (requestURI.endsWith("/rss")) {
                rssProcessor.process(request, response, configContext);
                isRequestHandled = true;
            }
            if (requestURI.endsWith("/atom")) {
                atomProcessor.process(request, response, configContext);
                isRequestHandled = true;
            }

            String queryString = request.getQueryString();
            if (queryString != null) {
                for (Iterator<String> iter = getRequestProcessors.keySet().iterator(); iter.hasNext();) {
                    String item = iter.next();
                    if (queryString.indexOf(item) == 0 &&
                        (queryString.equals(item) ||
                         queryString.indexOf("&") == item.length() ||
                         queryString.indexOf("=") == item.length())) {
                        (getRequestProcessors.get(item)).process(request, response, configContext);
                        isRequestHandled = true;
                        break;
                    }
                }
            }
            if (!isRequestHandled) {
                handleRestRequest(request, response); // Assume that this is a REST request
            }
        } catch (Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    protected void handleRestRequest(HttpServletRequest request,
                                     HttpServletResponse response) throws IOException,
                                                                          ServletException {
        if (!disableREST) {
            new RestRequestProcessor(HTTPConstants.HTTP_METHOD_GET,
                                     request,
                                     response).processURLRequest();
        } else {
            showRestDisabledErrorMessage(response);
        }
    }

    public void addGetRequestProcessor(String key, HttpGetRequestProcessor processor) {
        getRequestProcessors.put(key, processor);
    }

    public void removeGetRequestProcessor(String key) {
        getRequestProcessors.remove(key);
    }
}
