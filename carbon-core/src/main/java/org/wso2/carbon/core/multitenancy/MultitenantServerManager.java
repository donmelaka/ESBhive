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

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.InOutAxisOperation;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.core.multitenancy.utils.TenantUtils;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.tenant.Tenant;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles server startup & iniatialization when a multitenant deployment is available
 */
public class MultitenantServerManager {

    private static final Log log = LogFactory.getLog(MultitenantServerManager.class);

    /**
     * Start multitenant deployment
     *
     * @param mainConfigCtx The ConfigurationContext of the main server
     * @throws AxisFault If an error occurs while doing tenant specific deployments
     */
    public static void start(ConfigurationContext mainConfigCtx) throws Exception {

        Map<String, ConfigurationContext> tenantConfigCtxs = new HashMap<String, ConfigurationContext>();
        mainConfigCtx.setProperty(MultitenantConstants.TENANT_CONFIGURATION_CONTEXTS, tenantConfigCtxs);

        AxisConfiguration mainAxisConfig = mainConfigCtx.getAxisConfiguration();

        // Create per tenant config contexts
        createTenantAxisConfigurations(mainConfigCtx);

        deployMultitenantService(mainAxisConfig);

    }

    private static void deployMultitenantService(AxisConfiguration axisCfg) throws AxisFault {
        AxisService service = new AxisService(MultitenantConstants.MULTITENANT_DISPATCHER_SERVICE);
        AxisOperation operation =
                new InOutAxisOperation(MultitenantConstants.MULTITENANT_DISPATCHER_OPERATION);
        operation.setMessageReceiver(new MultitenantMessageReceiver());
        service.addOperation(operation);
        AxisServiceGroup synapseSvcGroup = new AxisServiceGroup(axisCfg);
        synapseSvcGroup.setServiceGroupName(MultitenantConstants.MULTITENANT_DISPATCHER_SERVICE);
        synapseSvcGroup.addParameter(CarbonConstants.HIDDEN_SERVICE_PARAM_NAME, "true");
        synapseSvcGroup.addService(service);
        axisCfg.addServiceGroup(synapseSvcGroup);
    }

    private static void createTenantAxisConfigurations(ConfigurationContext mainConfigCtx)
            throws Exception {

        RealmService realmService = CarbonCoreServiceComponent.getRealmService();
        Tenant[] tenants = realmService.getTenantManager().getAllTenants();
        for (Tenant tenant : tenants) {
            TenantUtils.initTenantConfigurationContext(mainConfigCtx,
                                                       tenant.getDomain(), tenant.getId());
        }
    }    
}
