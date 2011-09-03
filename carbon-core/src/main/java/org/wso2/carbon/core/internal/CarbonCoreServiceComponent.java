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
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.wso2.carbon.application.deployer.service.ApplicationManagerService;
import org.wso2.carbon.core.ServerRestartHandler;
import org.wso2.carbon.core.ServerShutdownHandler;
import org.wso2.carbon.core.init.CarbonServerManager;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ServerConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @scr.component name="carbon.core.dscomponent"" immediate="true"
 * @scr.reference name="user.realmservice.default" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"  unbind="unsetRealmService"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="application.manager" interface="org.wso2.carbon.application.deployer.service.ApplicationManagerService"
 * cardinality="1..1" policy="dynamic" bind="setAppManager" unbind="unsetAppManager"
 * @scr.reference name="http.service" interface="org.osgi.service.http.HttpService"
 * cardinality="1..1" policy="dynamic"  bind="setHttpService" unbind="unsetHttpService"
 * @scr.reference name="serverShutdownHandler" interface="org.wso2.carbon.core.ServerShutdownHandler"
 * cardinality="0..n" policy="dynamic"  bind="addServerShutdownHandler" unbind="removeServerShutdownHandler"
 * @scr.reference name="serverRestartHandler" interface="org.wso2.carbon.core.ServerRestartHandler"
 * cardinality="0..n" policy="dynamic"  bind="addServerRestartHandler" unbind="removeServerRestartHandler"
 */
public class CarbonCoreServiceComponent {

    private static Log log = LogFactory.getLog(CarbonCoreServiceComponent.class);

    private static RealmService realmService;
    private static RegistryService registryServiceInstance;
    private static HttpService httpServiceInstance;
    private static ApplicationManagerService applicationManager;
    private static List<ServerShutdownHandler> shutdownHandlers = new ArrayList<ServerShutdownHandler>();
    private static List<ServerRestartHandler> restartHandlers = new ArrayList<ServerRestartHandler>();
    private CarbonServerManager carbonServerManager;

    protected void activate(ComponentContext ctxt) {
        try {
            carbonServerManager = new CarbonServerManager(ctxt.getBundleContext());
        } catch (Throwable e) {
            log.error("Failed to activate Carbon Core bundle ", e);
        }
    }

    protected void deactivate(ComponentContext ctxt) {

        try {
            carbonServerManager.cleanupSystem();
        } catch (Throwable e) {
            log.error("Failed clean up Carbon core", e);
        }

        if ("false".equals(ServerConfiguration.getInstance().getFirstProperty("RequireCarbonServlet"))) {
            return;
        }
        try {
            getHttpService().unregister("/services");
        } catch (Exception e) {
            log.error("Failed to unregister servlets ", e);
        }
        log.debug("Carbon Core bundle is deactivated ");
    }

    protected void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

    protected void unsetRealmService(RealmService realmService) {
        this.realmService = null;
    }

    protected void setHttpService(HttpService httpService) {
        httpServiceInstance = httpService;
    }

    protected void unsetHttpService(HttpService httpService) {
        httpServiceInstance = null;
    }

    protected void setRegistryService(RegistryService registryService) {
        registryServiceInstance = registryService;
    }

    protected void unsetRegistryService(RegistryService registryService) {
        registryServiceInstance = null;
    }

    protected void setAppManager(ApplicationManagerService appManager) {
        applicationManager = appManager;
    }

    protected void unsetAppManager(ApplicationManagerService appManager) {
        applicationManager = null;
    }

    protected void addServerShutdownHandler(ServerShutdownHandler shutdownHandler) {
        shutdownHandlers.add(shutdownHandler);
    }

    protected void removeServerShutdownHandler(ServerShutdownHandler shutdownHandler) {
        shutdownHandlers.remove(shutdownHandler);
    }

    protected void addServerRestartHandler(ServerRestartHandler restartHandler) {
        restartHandlers.add(restartHandler);
    }

    protected void removeServerRestartHandler(ServerRestartHandler restartHandler) {
        restartHandlers.remove(restartHandler);
    }

    public static void shutdown() {
        for (ServerShutdownHandler shutdownHandler : shutdownHandlers) {
            shutdownHandler.invoke();
        }
    }

    public static void restart() {
        for (ServerRestartHandler restartHandler : restartHandlers) {
            restartHandler.invoke();
        }
    }

    public static HttpService getHttpService() throws Exception {
        if (httpServiceInstance == null) {
            String msg = "Before activating Carbon Core bundle, an instance of "
                    + HttpService.class.getName() + " should be in existance";
            log.error(msg);
            throw new Exception(msg);
        }
        return httpServiceInstance;
    }

    public static RealmService getRealmService() throws Exception {
        if (realmService == null) {
            String msg = "Before activating Carbon Core bundle, an instance of "
                    + "UserRealm service should be in existance";
            log.error(msg);
            throw new Exception(msg);
        }
        return realmService;
    }

    public static RegistryService getRegistryService() throws Exception {
        if (registryServiceInstance == null) {
            String msg = "Before activating Carbon Core bundle, an instance of "
                    + "RegistryService should be in existance";
            log.error(msg);
            throw new Exception(msg);
        }
        return registryServiceInstance;
    }

    public static ApplicationManagerService getAppManager() throws Exception {
        if (applicationManager == null) {
            String msg = "Before activating Carbon Core bundle, an instance of "
                    + "ApplicationManager should be in existance";
            log.error(msg);
            throw new Exception(msg);
        }
        return applicationManager;
    }
}
