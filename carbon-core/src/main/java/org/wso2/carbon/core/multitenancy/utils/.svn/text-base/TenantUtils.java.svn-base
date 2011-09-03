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
package org.wso2.carbon.core.multitenancy.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.wso2.carbon.core.CarbonAxisConfigurator;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.core.multitenancy.MultitenantConstants;
import org.wso2.carbon.core.multitenancy.TenantAxisConfigurator;
import org.wso2.carbon.core.transports.TransportPersistenceManager;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.utils.CarbonUtils;

/**
 *
 */
public class TenantUtils {

    private static final Log log = LogFactory.getLog(TenantUtils.class);
    
    public static String getTenantFromUrl(String url) {
        int tenantDelimiterIndex = url.indexOf("/t/");
        String tenant;
        if (tenantDelimiterIndex != -1) {
            String temp = url.substring(tenantDelimiterIndex + 3);
            tenant = temp.substring(0, temp.indexOf("/"));
            return tenant;
        }
        return url;
    }

    public static AxisConfiguration getTenantAxisConfiguration(String tenant,
                                                               ConfigurationContext mainConfigCtx) {
        ConfigurationContext tenantConfigCtx = getTenantConfigurationContext(tenant, mainConfigCtx);
        if (tenantConfigCtx != null) {
            return tenantConfigCtx.getAxisConfiguration();
        }
        return null;
    }

    public static ConfigurationContext getTenantConfigurationContextFromUrl(String url,
                                                                            ConfigurationContext mainConfigCtx) {
        Map tenantConfigCtxs =
                (Map) mainConfigCtx.getProperty(MultitenantConstants.TENANT_CONFIGURATION_CONTEXTS);
        return (ConfigurationContext) tenantConfigCtxs.get(getTenantFromUrl(url));
    }

    public static ConfigurationContext getTenantConfigurationContext(String tenant,
                                                                     ConfigurationContext mainConfigCtx) {
        Map tenantConfigCtxs =
                (Map) mainConfigCtx.getProperty(MultitenantConstants.TENANT_CONFIGURATION_CONTEXTS);
        return (ConfigurationContext) tenantConfigCtxs.get(tenant);
    }

    /**
     * @param url           - will have the pattern <some-string>/t/<tenant>/<service>?<some-params>
     * @param mainConfigCtx The main ConfigurationContext from the server
     * @return The tenant's AxisService
     * @throws org.apache.axis2.AxisFault If an error occurs while retrieving the AxisService
     */
    public static AxisService getAxisService(String url, ConfigurationContext mainConfigCtx)
            throws AxisFault {
        String[] strings = url.split("/");
        boolean foundTenantDelimiter = false;
        String tenant = null;
        String service = null;
        for (String str : strings) {
            if (str.equals("t")) {
                foundTenantDelimiter = true;
                continue;
            }
            if (foundTenantDelimiter & tenant == null) {
                tenant = str;
                continue;
            }
            if (tenant != null) {
                service = str;
            }
        }
        if (service != null) {
            service = service.split("\\?")[0];
            AxisConfiguration tenantAxisConfig = getTenantAxisConfiguration(tenant, mainConfigCtx);
            if (tenantAxisConfig != null) {
                return tenantAxisConfig.getServiceForActivation(service);
            }
        }
        return null;
    }

    public static void initTenantConfigurationContext(ConfigurationContext mainConfigCtx,
                                                      String tenantDomain, int tenantId) {
        String tenantsDir = CarbonUtils.getCarbonTenantsDirPath();
        AxisConfiguration mainAxisConfig = mainConfigCtx.getAxisConfiguration();
        Map<String, ConfigurationContext> tenantConfigCtxs
            = (Map<String, ConfigurationContext>) mainConfigCtx.getProperty(MultitenantConstants.TENANT_CONFIGURATION_CONTEXTS);
        String tenantPath = tenantsDir + File.separator + tenantDomain;

        String repository = tenantPath + File.separator + "repository";  //TODO" Get from Registry

        // Create tenant repository
        File tenantDir = new File(tenantPath);
        if(!tenantDir.exists()) {
            tenantDir.mkdirs();
            String servicesDir = repository + File.separator + "axis2services";
            String modulesDir = repository + File.separator + "axis2modules";
            new File(servicesDir).mkdirs();
            new File(modulesDir).mkdirs();
        }
        try {
            UserRegistry tenantUserRegistry =
                    CarbonCoreServiceComponent.getRegistryService().getConfigSystemRegistry(tenantId);
            Bundle[] moduleBundles =
                    ((CarbonAxisConfigurator) mainAxisConfig.getConfigurator()).getConfigItemHolder().getModuleBundles();
            TenantAxisConfigurator tenantAxisConfigurator =
                    new TenantAxisConfigurator(mainAxisConfig, tenantDomain,
                                               repository, tenantUserRegistry, moduleBundles );
            ConfigurationContext tenantConfigCtx =
                    ConfigurationContextFactory.createConfigurationContext(tenantAxisConfigurator);

            tenantConfigCtx.setServicePath("axis2services");
            tenantConfigCtx.setContextRoot("local:/");
            tenantConfigCtxs.put(tenantDomain, tenantConfigCtx);

            AxisConfiguration tenantAxisConfig = tenantConfigCtx.getAxisConfiguration();

            // TODO: Get all the AxisObservers, Deployers etc. services and register it with this tenant AxisConfig
            ArrayList<AxisObserver> axisObservers = mainAxisConfig.getObserversList();
            for (AxisObserver axisObserver : axisObservers){
                tenantAxisConfig.addObservers(axisObserver);
            }

            TransportPersistenceManager.updateEnabledTransports(
                    tenantAxisConfig.getTransportsIn().values(),
                    tenantAxisConfig.getTransportsOut().values());

        } catch (Throwable e) {
            log.error("Error occurred while running deployment for tenant " + tenantDomain);
        }
    }
}
