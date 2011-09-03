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
package org.wso2.carbon.core.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.File;

/**
 *
 */
public class CarbonCoreActivator implements BundleActivator {

    private static final Log log = LogFactory.getLog(CarbonCoreActivator.class);

    public void start(BundleContext context) throws Exception {
        log.info("Starting WSO2 Carbon...");
        log.info("Operating System : " + System.getProperty("os.name") + " " +
                 System.getProperty("os.version") + ", " + System.getProperty("os.arch"));
        log.info("Java Home        : " + System.getProperty("java.home"));
        log.info("Java Version     : " + System.getProperty("java.version"));
        log.info("Java VM          : " + System.getProperty("java.vm.name") +  " " +
                 System.getProperty("java.vm.version") +
                 "," +
                 System.getProperty("java.vendor"));

        String carbonHome;
        if ((carbonHome = System.getProperty("carbon.home")).equals(".")) {
            carbonHome = new File(".").getAbsolutePath();
        }
        log.info("Carbon Home      : " + carbonHome);
        log.info("Java Temp Dir    : " + System.getProperty("java.io.tmpdir"));
        log.info("User             : " + System.getProperty("user.name") + ", " +
                 System.getProperty("user.language") + "-" + System.getProperty("user.country") +
                 ", " + System.getProperty("user.timezone"));
    }

    public void stop(BundleContext context) throws Exception {
    }
}
