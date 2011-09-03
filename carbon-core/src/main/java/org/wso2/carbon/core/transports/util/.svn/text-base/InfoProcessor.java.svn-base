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
import org.wso2.carbon.core.transports.HttpGetRequestProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class InfoProcessor implements HttpGetRequestProcessor {

    public void process(HttpServletRequest request,
                        HttpServletResponse response,
                        ConfigurationContext configurationContext) throws Exception {
        String requestURI = request.getRequestURI();
        String contextPath = configurationContext.getServiceContextPath();
        String serviceName = requestURI.substring(requestURI.indexOf(contextPath) + contextPath.length() + 1);
        response.setStatus(200);
        response.setContentType("text/html");
        response.getOutputStream().
                print(ServiceHTMLProcessor.printServiceHTML(serviceName, configurationContext));

    }
}
