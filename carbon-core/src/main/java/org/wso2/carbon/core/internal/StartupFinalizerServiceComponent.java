
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

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.ListenerManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.core.util.ClusteringUtil;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;
import org.wso2.carbon.utils.ServerConfiguration;
import org.wso2.carbon.utils.ServerConstants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.esbhive.node.addition.NodeAdditionInterface;


/**
 * Responsible for finalizing startup of the Carbon server. This component will run after all other
 * components & service required for the Carbon server to reach a stable state become available.
 *
 * This service component is mainly responsible for starting the Axis2 Transport ListenerManager
 * once all the required OSGi services in the system become available. This is because of the
 * fact  that requests from external parties should only be serviced after the Axis2 engine
 * & Carbon has  reached a stable and consistent state.
 *
 * @scr.component name="org.wso2.carbon.core.internal.StartupFinalizerServiceComponent"
 * immediate="true"
 * @scr.reference name="org.wso2.carbon.configCtx"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContext" unbind="unsetConfigurationContext"
 * @scr.reference name="org.wso2.carbon.serverConfig" interface="org.wso2.carbon.utils.ServerConfiguration"
 * cardinality="1..1" policy="dynamic" bind="setServerConfiguration" unbind="unsetServerConfiguration
 * @scr.reference name="user.realmservice.default" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"  unbind="unsetRealmService"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="org.esbhive.startup.handler" interface="org.esbhive.node.addition.NodeAdditionInterface"
 * cardinality="1..1" policy="dynamic" bind="setNodeAdditionHandler" unbind="unSetNodeAdditionHandler"
 **/
public class StartupFinalizerServiceComponent implements ServiceListener {
    private static final Log log = LogFactory.getLog(StartupFinalizerServiceComponent.class);
    private static final String TRANSPORT_MANAGER =
            "org.wso2.carbon.server.transports.TransportManager";

    private ConfigurationContext configCtx;
    private List<String> requiredServices = new ArrayList<String>();
    private BundleContext bundleContext;

    private Timer pendingServicesObservationTimer = new Timer();
    private ServerConfiguration serverConfig;
    private ServiceRegistration listerManagerServiceRegistration;
          private NodeAdditionInterface ah;

   
    protected synchronized void activate(ComponentContext ctxt) {

        bundleContext = ctxt.getBundleContext();

        populateRequiredServices();
        if (requiredServices.isEmpty()) {
            completeInitialization(bundleContext);
            return;
        }

        String ldapFilter = "(|";
        for (String service : requiredServices) {
            ldapFilter += "(" + Constants.OBJECTCLASS + "=" + service + ")";
        }
        ldapFilter += ")";

        try {
            bundleContext.addServiceListener(this, ldapFilter);
            ServiceReference[] serviceReferences =
                    bundleContext.getServiceReferences(null, ldapFilter);
            if (serviceReferences != null) {
                for (ServiceReference reference : serviceReferences) {
                    String service = ((String[]) reference.getProperty(Constants.OBJECTCLASS))[0];
                    requiredServices.remove(service);
                    if (log.isDebugEnabled()) {
                        log.debug("Removed pending service " + service);
                    }
                }
            }
            if (requiredServices.isEmpty()) {
                completeInitialization(bundleContext);
            } else {
                schedulePendingServicesObservationTimer();
            }
        } catch (Throwable e) {
            log.fatal("Cannot activate StartupFinalizerServiceComponent", e);
        }
    }

    protected synchronized void deactivate(ComponentContext ctxt) {
      listerManagerServiceRegistration.unregister();
      ListenerManager.defaultConfigurationContext = null;
    }

    private void populateRequiredServices() {
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            String requiredServiceList =
                    (String) bundle.getHeaders().
                            get(CarbonConstants.CarbonManifestHeaders.LISTENER_MANAGER_INIT_REQUIRED_SERVICE);
            if (requiredServiceList != null) {
                String[] values = requiredServiceList.split(",");
                for (String value : values) {
                    requiredServices.add(value);
                }
            }
        }
    }

    private void schedulePendingServicesObservationTimer() {
        pendingServicesObservationTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!requiredServices.isEmpty()) {
                    String services = "";
                    for (String service : requiredServices) {
                        services += service + ",";
                    }
                    log.warn("Waiting for required OSGi services: " + services);
                }
            }
        }, 5000, 10000);
    }

    private void completeInitialization(BundleContext bundleContext) {
        bundleContext.removeServiceListener(this);
        pendingServicesObservationTimer.cancel();
        ListenerManager listenerManager = configCtx.getListenerManager();
        if (listenerManager == null) {
            listenerManager = new ListenerManager();
            listenerManager.setShutdownHookRequired(false);
            listenerManager.startSystem(configCtx);
        }

        // Need to initialize the cluster after transports are initialized since the transport
        // port information is needed when populating Member information
        try {
            ClusteringUtil.enableClustering(configCtx);
        } catch (AxisFault e) {
            String msg = "Cannot initialize cluster";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        
        if (System.getProperty(ServerConstants.STANDALONE_MODE, "false").equalsIgnoreCase("true")) {
            try {
                Class<?> transportManagerClass = Class.forName(TRANSPORT_MANAGER);
                Object transportManager = transportManagerClass.newInstance();
                Method method = transportManagerClass.getMethod("startTransports");
                method.invoke(transportManager);
            } catch (Exception e) {
                String msg = "Cannot start transports";
                log.fatal(msg, e);
                return;
            }
        }
        listerManagerServiceRegistration =
                bundleContext.registerService(ListenerManager.class.getName(), listenerManager, null);
        log.info("Started Transport Listener Manager");
        setServerStartTimeParam();
        printInfo();
        ah.DeployProxies();

      }

    
    private void setServerStartTimeParam() {
        Parameter startTimeParam = new Parameter();
        startTimeParam.setName(CarbonConstants.SERVER_START_TIME);
        startTimeParam.setValue(System.getProperty(CarbonConstants.START_TIME));
        try {
            configCtx.getAxisConfiguration().addParameter(startTimeParam);
        } catch (AxisFault e) {
            log.error("Could not set the  server start time parameter", e);
        }
    }

    private void printInfo() {
        long startTime = Long.parseLong(System.getProperty(CarbonConstants.START_TIME));
        long startupTime = (System.currentTimeMillis() - startTime) / 1000;
        log.info("Server           :  " + serverConfig.getFirstProperty("Name") + "-" +
                 serverConfig.getFirstProperty("Version"));
        log.info("WSO2 Carbon started in " + startupTime + " sec");
//        System.getProperties().remove(CarbonConstants.START_TIME);
        System.getProperties().remove("setup"); // Clear the setup System property
    }

    protected void setConfigurationContext(ConfigurationContextService configCtx) {
        this.configCtx = configCtx.getServerConfigContext();
    }

    protected void unsetConfigurationContext(ConfigurationContextService configCtx) {
        this.configCtx = null;
    }

    protected void setServerConfiguration(ServerConfiguration serverConfig) {
        this.serverConfig = serverConfig;
    }

    protected void unsetServerConfiguration(ServerConfiguration serverConfig) {
        this.serverConfig = null;
    }

    protected void setRealmService(RealmService realmService) {
    }

    protected void unsetRealmService(RealmService realmService) {
    }

    protected void setRegistryService(RegistryService registryService) {
    }

    protected void unsetRegistryService(RegistryService registryService) {
    }

    public synchronized void serviceChanged(ServiceEvent event) {
        if (event.getType() == ServiceEvent.REGISTERED) {
            String service =
                    ((String[]) event.getServiceReference().getProperty(Constants.OBJECTCLASS))[0];
            requiredServices.remove(service);
            if (log.isDebugEnabled()) {
                log.debug("Removed pending service " + service);
            }
            if (requiredServices.isEmpty()) {
                completeInitialization(bundleContext);
            }
        }
    }
    
       protected void setNodeAdditionHandler(NodeAdditionInterface ah) {
    this.ah = ah;
  }

  protected void unSetNodeAdditionHandler(NodeAdditionInterface ah) {
    this.ah = null;
  }
}
