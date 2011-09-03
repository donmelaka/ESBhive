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

package org.wso2.carbon.core.transports.util;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.wso2.carbon.core.multitenancy.utils.TenantUtils;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.core.transports.HttpGetRequestProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public abstract class AbstractWsdlProcessor implements HttpGetRequestProcessor {

    protected void printWSDL(ConfigurationContext configurationContext,
                             String serviceName,
                             HttpServletResponse response,
                             WSDLPrinter wsdlPrinter) throws IOException {
        AxisService axisService =
                configurationContext.getAxisConfiguration().getServiceForActivation(serviceName);
        if (axisService == null) {
            // Try to see whether the service is available in a tenant
            axisService = TenantUtils.getAxisService(serviceName, configurationContext);
        }

        OutputStream outputStream = response.getOutputStream();
        if (axisService != null) {
            if (!axisService.isActive()) {
                response.setContentType("text/html");
                outputStream.write(("<h4>Service " +
                                    serviceName +
                                    " is inactive. Cannot display WSDL document.</h4>").getBytes());
                outputStream.flush();
            } else {
                response.setContentType("text/xml");
                wsdlPrinter.printWSDL(axisService);
            }
        } else {
            response.setContentType("text/html");
            outputStream.write(("<h4>Service " + serviceName +
                                " not found. Cannot display WSDL document.</h4>").getBytes());
            outputStream.flush();
        }
    }

    /**
     * This method check for annotation=true query param. If it is available
     * this method return true, otherwise false.
     *
     * @param request
     * @return boolean
     */
    protected boolean checkForAnnotation(HttpServletRequest request) {
        String parameter = request.getParameter(ServerConstants.HTTPConstants.ANNOTATION);
        if (parameter != null && parameter.length() != 0) {
            if (parameter.equals("true")) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method will return the value of the parameter
     *
     * @param request
     * @param paramName
     * @return String
     */
    protected String getImportedWSDL(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);

        if (paramValue != null && paramValue.length() != 0) {
            return paramValue;
        }
        return "";

    }

    protected interface WSDLPrinter {
        void printWSDL(AxisService axisService) throws IOException;
    }
}
