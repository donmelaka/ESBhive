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
package org.wso2.carbon.core.util;

import org.osgi.framework.BundleContext;
import org.wso2.carbon.core.transports.HttpGetRequestProcessor;
import org.wso2.carbon.utils.component.xml.Component;
import org.wso2.carbon.utils.component.xml.ComponentConfigFactory;
import org.wso2.carbon.utils.component.xml.ComponentConstants;
import org.wso2.carbon.utils.component.xml.config.HTTPGetRequestProcessorConfig;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.application.deployer.handler.DefaultAppDeployer;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;

import java.io.InputStream;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

public class Utils {

    /**
     * Given a bundleContext this method will register any HTTPGetRequestProcessors found in that
     * bundle
     * @param bundleContext The bundleContext of the bundle that may have HTTPGetRequestProcessors
     * @throws Exception Thrown in case the component.xml cannot be processes
     */
    public static void registerHTTPGetRequestProcessors(BundleContext bundleContext)
            throws Exception {
        URL url = bundleContext.getBundle().getEntry("META-INF/component.xml");
        if (url == null) {
            return;
        }

        InputStream inputStream = url.openStream();
        Component component = ComponentConfigFactory.build(inputStream);
        HTTPGetRequestProcessorConfig[] getRequestProcessorConfigs = null;
        if (component != null) {
            getRequestProcessorConfigs = (HTTPGetRequestProcessorConfig[])
                    component.getComponentConfig(ComponentConstants.HTTP_GET_REQUEST_PROCESSORS);
        }

        if (getRequestProcessorConfigs != null) {
            for (HTTPGetRequestProcessorConfig getRequestProcessorConfig :
                    getRequestProcessorConfigs) {
                Class getRequestProcessorClass;
                try {
                    getRequestProcessorClass = bundleContext.getBundle().
                            loadClass(getRequestProcessorConfig.getClassName());
                } catch (ClassNotFoundException e) {
                    getRequestProcessorClass = Class.forName(getRequestProcessorConfig.
                            getClassName());
                }
                HttpGetRequestProcessor getRequestProcessor =
                        (HttpGetRequestProcessor) getRequestProcessorClass.newInstance();
                String item = getRequestProcessorConfig.getItem();
                Dictionary<String,String> propsMap = new Hashtable<String,String>(2);
                propsMap.put(ComponentConstants.ELE_ITEM, item);
                propsMap.put(CarbonConstants.HTTP_GET_REQUEST_PROCESSOR_SERVICE, 
                        HttpGetRequestProcessor.class.getName());

                //Registering the HttpGetRequestProcessor implementation in the OSGi registry
                bundleContext.registerService(HttpGetRequestProcessor.class.getName(),
                        getRequestProcessor, propsMap);
            }
        }
    }

    public static String getArtifactTypeFromService(AxisService service) {
        String artifactType = null;

        Parameter serviceTypeParam = service.getParameter(ServerConstants.SERVICE_TYPE);
        String serviceType;
        if (serviceTypeParam != null) {
            serviceType = (String) serviceTypeParam.getValue();
        } else {
            serviceType = "axis2";
        }

        if (serviceType.equals("axis2")) {
            artifactType = DefaultAppDeployer.AAR_TYPE;
        } else if (serviceType.equals("jaxws")) {
            artifactType = DefaultAppDeployer.JAXWS_TYPE;
        } else if (serviceType.equals("data_service")) {
            artifactType = DefaultAppDeployer.DS_TYPE;
        }
        return artifactType;
    }
}
