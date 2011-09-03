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

package org.wso2.carbon.core;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.core.multitenancy.MultitenantConstants;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.utils.WSO2Constants;
import org.wso2.carbon.utils.i18n.Messages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * Parent class for all admin services
 */
public abstract class AbstractAdmin {

    protected AxisConfiguration axisConfig;
    protected ConfigurationContext configurationContext;

    protected AbstractAdmin() {
    }

    protected AbstractAdmin(AxisConfiguration axisConfig) throws Exception {
        this.axisConfig = axisConfig;
    }

    public AxisConfiguration getAxisConfig() {
        return (axisConfig != null) ? axisConfig : getConfigContext().getAxisConfiguration();
    }

    public ConfigurationContext getConfigContext() {
        if(configurationContext != null) {
            return configurationContext;
        }
        MessageContext msgContext = MessageContext.getCurrentMessageContext();
        if (msgContext != null) {
            ConfigurationContext mainConfigContext = msgContext.getConfigurationContext();

            // If a tenant has been set, then try to get the ConfigurationContext of that tenant
            String tenant = (String) msgContext.getProperty(CarbonConstants.TENANT_DOMAIN);
            if (tenant != null) {
                Map<String, ConfigurationContext> tenantConfigContexts =
                        (Map<String, ConfigurationContext>) mainConfigContext.getProperty(MultitenantConstants.TENANT_CONFIGURATION_CONTEXTS);
                return tenantConfigContexts.get(tenant);
            } else {
                return mainConfigContext;
            }
        } else {
            try {
                return CarbonConfigurationContextFactory.
                        getConfigurationContext(CarbonAxisConfigurator.getInstance());
            } catch (AxisFault axisFault) {
                throw new RuntimeException(Messages.getMessage("InvalidAxisConfiguration"),
                                           axisFault);
            }
        }
    }

    public String getTenantDomain(){
        String tenant = null;
        MessageContext msgContext = MessageContext.getCurrentMessageContext();
        if (msgContext != null) {
            tenant = (String) msgContext.getProperty(CarbonConstants.TENANT_DOMAIN);
        }
        return tenant;
    }
    
    public void setConfigurationContext(ConfigurationContext configurationContext) {
        this.configurationContext = configurationContext;
        this.axisConfig = configurationContext.getAxisConfiguration();
    }

    /**
     * @deprecated
     * @return config registry
     */
    public Registry getRegistry() {
        return (Registry) getAxisConfig().getParameterValue(WSO2Constants.CONFIG_SYSTEM_REGISTRY_INSTANCE);
    }

    public Registry getConfigSystemRegistry() {
        return (Registry) getAxisConfig().getParameterValue(WSO2Constants.CONFIG_SYSTEM_REGISTRY_INSTANCE);
    }

    public Registry getConfigUserRegistry() {
        if (getHttpSession() != null) {
            Registry registry =
                    (Registry) getHttpSession().getAttribute(
                            WSO2Constants.CONFIG_USER_REGISTRY_INSTANCE);
            if (registry != null) {
                return registry;
            }
        }
        return null;
    }
    
    public UserRealm getUserRealm() {
        UserRealm userRealm = null;
        if (getHttpSession() != null) {
            UserRegistry registry = (UserRegistry) getHttpSession().getAttribute(
                    WSO2Constants.CONFIG_USER_REGISTRY_INSTANCE);
            userRealm = registry.getUserRealm();
        }
        return userRealm;
    }

    public Registry getLocalRepo() {
        return (Registry) getAxisConfig().getParameterValue(WSO2Constants.LOCAL_REPO_INSTANCE);
    }

    public Registry getGovernanceRegistry() {
        if (getHttpSession() != null) {
            Registry registry =
                    (Registry) getHttpSession().getAttribute(
                            WSO2Constants.GOVERNANCE_REGISTRY_INSTANCE);
            if (registry != null) {
                return registry;
            }
        }
        return null;
    }

    protected HttpSession getHttpSession() {
        MessageContext msgCtx = MessageContext.getCurrentMessageContext();
        HttpSession httpSession = null;
        if (msgCtx != null) {
            HttpServletRequest request =
                    (HttpServletRequest) msgCtx.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
            httpSession = request.getSession();
        }
        return httpSession;
    }
}
