/**
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.proxyadmin;

import org.apache.axis2.deployment.DeploymentEngine;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.config.xml.MultiXMLConfigurationBuilder;
import org.apache.synapse.core.axis2.ProxyService;
import org.apache.synapse.deployers.SynapseArtifactDeploymentStore;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.mediation.initializer.ServiceBusConstants;
import org.wso2.carbon.mediation.initializer.ServiceBusUtils;
import org.wso2.carbon.mediation.initializer.services.SynapseConfigurationService;
import org.wso2.carbon.mediation.initializer.services.SynapseEnvironmentService;
import org.wso2.carbon.proxyadmin.observer.ProxyObserver;
import org.wso2.carbon.proxyadmin.util.ConfigHolder;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.service.mgt.ServiceAdmin;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.io.File;

/**
 * @scr.component name="org.wso2.carbon.proxyadmin" immediate="true"
 * @scr.reference name="configuration.context.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService" unbind="unsetConfigurationContextService"
 * @scr.reference name="synapse.config.service"
 * interface="org.wso2.carbon.mediation.initializer.services.SynapseConfigurationService"
 * cardinality="1..1" policy="dynamic"
 * bind="setSynapseConfigurationService" unbind="unsetSynapseConfigurationService"
 * @scr.reference name="synapse.env.service"
 * interface="org.wso2.carbon.mediation.initializer.services.SynapseEnvironmentService"
 * cardinality="1..1" policy="dynamic"
 * bind="setSynapseEnvironmentService" unbind="unsetSynapseEnvironmentService"
 * @scr.reference name="service.admin.service" interface="org.wso2.carbon.service.mgt.ServiceAdmin"
 *  cardinality="1..1" policy="dynamic"
 *  bind="setServiceadminService" unbind="unsetServiceAdminService"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"
 * bind="setRegistryService" unbind="unsetRegistryService"
 */
@SuppressWarnings({"UnusedDeclaration"})
public class ProxyAdminServiceComponent {

    private static final Log log = LogFactory.getLog(ProxyAdminServiceComponent.class);

    private ConfigurationContextService cfgCtxSvc;

    private ProxyObserver proxyObserver;

    protected void activate(ComponentContext context) {
        if (cfgCtxSvc != null) {
            AxisConfiguration axisConf = cfgCtxSvc.getServerConfigContext().getAxisConfiguration();
            try {
                proxyObserver = new ProxyObserver(ConfigHolder.getInstance().
                        getSynapseConfiguration());
                axisConf.addObservers(proxyObserver);
                registerDeployer();
            } catch (ProxyAdminException e) {
                log.error("Error while initializing the proxy service observer. " +
                        "Proxy admin component may be unstable.", e);
            }
        } else {
            log.warn("Cannot register the proxy service observer. The axis service " +
                    "representing the proxy service might not be in sync with the proxy");
        }
    }

    private void registerDeployer() throws ProxyAdminException {

        AxisConfiguration axisConfig = ConfigHolder.getInstance().getAxisConfiguration();
        SynapseConfiguration synCfg = ConfigHolder.getInstance().getSynapseConfiguration();
        DeploymentEngine deploymentEngine = (DeploymentEngine) axisConfig.getConfigurator();
        SynapseArtifactDeploymentStore deploymentStore = SynapseArtifactDeploymentStore.getInstance();
        String synapseConfigPath = ServiceBusUtils.getSynapseConfigAbsPath();
        String proxyDirPath = synapseConfigPath
                + File.separator + MultiXMLConfigurationBuilder.PROXY_SERVICES_DIR;

        for (ProxyService proxyService : synCfg.getProxyServices()) {
            if (proxyService.getFileName() != null) {
                deploymentStore.addRestoredArtifact(
                        proxyDirPath + File.separator + proxyService.getFileName());
            }
        }
        deploymentEngine.addDeployer(
                new ProxyServiceDeployer(), proxyDirPath, ServiceBusConstants.ARTIFACT_EXTENSION);
    }

    protected void deactivate(ComponentContext context) {
        try {
            AxisConfiguration axisConfig = ConfigHolder.getInstance().getAxisConfiguration();
            if (axisConfig != null) {
                DeploymentEngine deploymentEngine = (DeploymentEngine) axisConfig.getConfigurator();
                String synapseConfigPath = ServiceBusUtils.getSynapseConfigAbsPath();
                String proxyDirPath = synapseConfigPath
                        + File.separator + MultiXMLConfigurationBuilder.PROXY_SERVICES_DIR;
                deploymentEngine.removeDeployer(
                        proxyDirPath, ServiceBusConstants.ARTIFACT_EXTENSION);
            }
        } catch (ProxyAdminException e) {
            log.warn("Couldn't remove the ProxyServiceDeployer");
        }
    }

    protected void setConfigurationContextService(ConfigurationContextService cfgCtxService) {
        this.cfgCtxSvc = cfgCtxService;
        ConfigHolder.getInstance().setAxisConfiguration(
                cfgCtxService.getServerConfigContext().getAxisConfiguration());
    }

    protected void unsetConfigurationContextService(ConfigurationContextService cfgCtxService) {
        this.cfgCtxSvc = null;
        ConfigHolder.getInstance().setAxisConfiguration(null);
    }

    protected void setSynapseConfigurationService(
            SynapseConfigurationService synapseConfigurationService) {

        ConfigHolder.getInstance().setSynapseConfiguration(
                synapseConfigurationService.getSynapseConfiguration());
        if (proxyObserver != null) {
            proxyObserver.setSynapseConfig(synapseConfigurationService.getSynapseConfiguration());
        }
    }

    protected void unsetSynapseConfigurationService(
            SynapseConfigurationService synapseConfigurationService) {

        ConfigHolder.getInstance().setSynapseConfiguration(null);
        if (proxyObserver != null) {
            proxyObserver.setSynapseConfig(null);
        }
    }

    protected void setSynapseEnvironmentService(
            SynapseEnvironmentService synapseEnvironmentService) {

        ConfigHolder.getInstance().setSynapseEnvironment(
                synapseEnvironmentService.getSynapseEnvironment());
    }

    protected void unsetSynapseEnvironmentService(
            SynapseEnvironmentService synapseEnvironmentService) {

        ConfigHolder.getInstance().setSynapseEnvironment(null);
    }

    protected void setServiceadminService(ServiceAdmin serviceAdmin) {}
    protected void unsetServiceAdminService(ServiceAdmin serviceAdmin) {}

    protected void setRegistryService(RegistryService regService) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService bound to the ESB initialization process");
        }
        try {
            ConfigHolder.getInstance().setRegistry(regService.getConfigSystemRegistry());
        } catch (RegistryException e) {
            log.error("Couldn't retrieve the registry from the registry service");
        }
    }

    protected void unsetRegistryService(RegistryService regService) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService unbound from the ESB environment");
        }
        ConfigHolder.getInstance().setRegistry(null);
    }
}
