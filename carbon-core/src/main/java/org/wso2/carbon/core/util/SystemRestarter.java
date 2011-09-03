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

import org.apache.axis2.AxisFault;
import org.apache.axis2.clustering.ClusteringAgent;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.bridge.FrameworkLauncher;
import org.wso2.carbon.bridge.FrameworkLauncherFactory;
import org.wso2.carbon.core.ServerManagement;
import org.wso2.carbon.core.ServerStatus;
import org.wso2.carbon.utils.MBeanRegistrar;
import org.wso2.carbon.utils.ServerConfiguration;

import java.util.Map;

/**
 * This class is responsible for restarting the WSO2 Carbon server. The restart can be either done
 * in a graceful manner or can be forced.
 */
public class SystemRestarter implements Runnable {

    private static final Log log = LogFactory.getLog(SystemRestarter.class);
    private boolean gracefulRestart;
    private ConfigurationContext configurationContext;

    public SystemRestarter(boolean gracefulRestart,
                           ConfigurationContext configurationContext) {
        this.configurationContext = configurationContext;
        this.gracefulRestart = gracefulRestart;
    }

    public void run() {

        try {
            try {
                ServerStatus.setServerRestarting();
            } catch (AxisFault e) {
                String msg = "Cannot set server to restarting mode";
                log.error(msg, e);
            }
            Map inTransports = configurationContext.getAxisConfiguration().getTransportsIn();
            String serverName = ServerConfiguration.getInstance().getFirstProperty("Name");
            if (gracefulRestart) {
                log.info("Gracefully restarting " + serverName + "...");
                new ServerManagement(inTransports).startMaintenance();
            } else {
                log.info("Restarting " + serverName + "...");
            }
	    CarbonCoreServiceComponent.restart();
            MBeanRegistrar.unregisterAllMBeans();
            ClusteringAgent clusteringAgent =
                    configurationContext.getAxisConfiguration().getClusteringAgent();
            if (clusteringAgent != null) {
                clusteringAgent.finalize();
            }

            // Restart the OSGi framework
            FrameworkLauncher frameworkLauncher = FrameworkLauncherFactory.getFrameworkLauncher();
            frameworkLauncher.stop();

            System.setProperty(CarbonConstants.START_TIME,
                               String.valueOf(System.currentTimeMillis()));
            //frameworkLauncher.deploy();
            frameworkLauncher.start();

            try {
                ServerStatus.setServerRunning();
            } catch (AxisFault e) {
                String msg = "Cannot set server to running mode";
                log.error(msg, e);
            }
        } catch (Exception e) {
            String msg = "Cannot restart system";
            log.fatal(msg, e);
        }
    }
}
