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
import org.apache.axis2.Constants;
import org.apache.axis2.deployment.AxisConfigBuilder;
import org.apache.axis2.deployment.DeploymentConstants;
import org.apache.axis2.deployment.DeploymentEngine;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.ModuleDeployer;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisConfigurator;
import org.apache.axis2.transport.TransportListener;
import org.apache.axis2.transport.local.LocalTransportSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.wso2.carbon.core.deployment.DeploymentInterceptor;
import org.wso2.carbon.core.multitenancy.transports.DummyTransportListener;
import org.wso2.carbon.core.util.ParameterUtil;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.carbon.utils.ServerConfiguration;
import org.wso2.carbon.utils.WSO2Constants;
import org.wso2.carbon.utils.deployment.Axis2ModuleRegistry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static org.apache.axis2.transport.TransportListener.HOST_ADDRESS;

/**
 * The tenant specific AxisConfigurator
 */
public class TenantAxisConfigurator extends DeploymentEngine implements AxisConfigurator {


    private static Log log = LogFactory.getLog(TenantAxisConfigurator.class);

    private Collection globallyEngagedModules = new ArrayList();
    private boolean isUrlRepo;

    private final AxisConfiguration mainAxisConfig;
    private final String tenant;
    private final String repoLocation;
    private final UserRegistry registry;
    private final Bundle[] moduleBundles;

    public TenantAxisConfigurator(AxisConfiguration mainAxisConfig,
                                  String tenant,
                                  String repoLocation,
                                  UserRegistry registry,
                                  Bundle[] moduleBundles) throws AxisFault {
        this.tenant = tenant;
        this.mainAxisConfig = mainAxisConfig;
        this.registry = registry;
        this.moduleBundles = moduleBundles;

        if (repoLocation == null) {
            throw new AxisFault(tenant + " tenant: Axis2 repository not specified!");
        }

        // Check whether this is a URL
        isUrlRepo = CarbonUtils.isURL(repoLocation);

        if (isUrlRepo) { // Is repoLocation a URL Repo?
            try {
                new URL(repoLocation).openConnection().connect();
            } catch (IOException e) {
                throw new AxisFault(tenant + " tenant: Cannot connect to URL repository " + repoLocation, e);
            }
            this.repoLocation = repoLocation;
        } else { // Is repoLocation a file repo?
            File repo = new File(repoLocation);
            if (repo.exists()) {
                this.repoLocation = repo.getAbsolutePath();
            } else {
                throw new AxisFault(tenant + " tenant: repository location '" + repoLocation +
                                    "' not found!");
            }
        }
    }

    /**
     * First create a Deployment engine, use that to create an AxisConfiguration
     *
     * @return Axis Configuration
     * @throws AxisFault
     */
    public synchronized AxisConfiguration getAxisConfiguration() throws AxisFault {
        log.info("Creating tenant AxisConfiguration for tenant: " + tenant);
        if (log.isDebugEnabled()) {
//            log.debug("axis2.xml : " + axis2xml);
            log.debug("Axis2 repo: " + repoLocation);
        }
        axisConfig = null;
        try {
            InputStream axis2xmlStream;
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            axis2xmlStream =
                    cl.getResourceAsStream(DeploymentConstants.AXIS2_CONFIGURATION_RESOURCE);
            axisConfig = populateAxisConfiguration(axis2xmlStream);
        } catch (Exception e) {
            throw AxisFault.makeFault(e);
        }

        axisConfig.setConfigurator(this);

        //TODO: May need to set certain parameters into the AxisConfig
        
        globallyEngagedModules = axisConfig.getEngagedModules();
        if (repoLocation != null && repoLocation.trim().length() != 0) {
            try {
                if (isUrlRepo) {
                    URL axis2Repository = new URL(repoLocation);
                    axisConfig.setRepository(axis2Repository);
                    loadRepositoryFromURL(axis2Repository);
                } else {
                    axisConfig.setRepository(new URL("file://" + repoLocation));
                    loadRepository(repoLocation);
                }
            } catch (MalformedURLException e) {
                throw new AxisFault("Invalid URL", e);
            }
        } else {
            loadFromClassPath();
        }

        for (Object globallyEngagedModule : globallyEngagedModules) {
            AxisModule module = (AxisModule) globallyEngagedModule;
            if (log.isDebugEnabled()) {
                log.debug("Globally engaging module: " + module.getName());
            }
        }

        // Remove all the transports made available in the tenant's axis2.xml
        axisConfig.getTransportsOut().clear();

        // Add the local transport
        TransportOutDescription tOut = new TransportOutDescription(Constants.TRANSPORT_LOCAL);
        tOut.setSender(new LocalTransportSender());
        axisConfig.addTransportOut(tOut);

        // Remove all in-transports made available in the tenant's axis2.xml
        axisConfig.getTransportsIn().clear();

        // Add the transports that are made available in the main axis2.xml file
        for (String transport : mainAxisConfig.getTransportsIn().keySet()) {
            TransportInDescription tenantTransportIn = new TransportInDescription(transport);
            TransportListener mainTransportListener =
                    mainAxisConfig.getTransportIn(transport).getReceiver();
            tenantTransportIn.setReceiver(new DummyTransportListener(mainTransportListener, tenant));
            axisConfig.addTransportIn(tenantTransportIn);
        }
        return axisConfig;
    }

    public boolean isGlobalyEngaged(AxisModule axisModule) {
        String modName = axisModule.getName();
        for (Object globallyEngagedModule : globallyEngagedModules) {
            AxisModule module = (AxisModule) globallyEngagedModule;
            if (modName.startsWith(module.getName())) {
                return true;
            }
        }
        return false;
    }

    public void engageGlobalModules() throws AxisFault {
        engageModules();
    }

    public AxisConfiguration populateAxisConfiguration(InputStream in) throws DeploymentException {
        axisConfig = new AxisConfiguration();
        try {
            // Set services dir
            axisConfig.addParameter(new Parameter(DeploymentConstants.SERVICE_DIR_PATH,
                                                  "axis2services"));
            // Set modules dir
            axisConfig.addParameter(new Parameter(DeploymentConstants.MODULE_DRI_PATH,
                                                  "axis2modules"));
        } catch (AxisFault e) {
            String msg =
                    "Cannot add DeploymentConstants.SERVICE_DIR_PATH or " +
                    "DeploymentConstants.MODULE_DIR_PATH parameters";
            log.error(msg, e);
            throw new DeploymentException(msg, e);
        }

        setRegistry();
        setUserRealm();

        // Add the DeploymentInterceptor for the tenant AxisConfigurations
        DeploymentInterceptor interceptor = new DeploymentInterceptor();
        interceptor.setRegistry(registry);
        interceptor.init(axisConfig);
        axisConfig.addObservers(interceptor);

        setHostName(axisConfig);

        //TCCL will be based on OSGi
        AxisConfigBuilder builder = new AxisConfigBuilder(in, axisConfig, this);
        builder.populateConfig();
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            String msg = "error in closing input stream";
            log.error(msg);
        }
        moduleDeployer = new ModuleDeployer(axisConfig);
        new Axis2ModuleRegistry(axisConfig).register(moduleBundles);
        return axisConfig;
    }

    private void setRegistry() throws DeploymentException {
        Parameter param = new Parameter(WSO2Constants.CONFIG_SYSTEM_REGISTRY_INSTANCE, registry);   
        try {
            axisConfig.addParameter(param);
        } catch (AxisFault axisFault) {
            throw new DeploymentException(axisFault.getMessage(), axisFault);
        }
    }
    
    private void setUserRealm() throws DeploymentException {
        Parameter param = new Parameter(WSO2Constants.USER_REALM_INSTANCE, registry.getUserRealm());   
        try {
            axisConfig.addParameter(param);
        } catch (AxisFault axisFault) {
            throw new DeploymentException(axisFault.getMessage(), axisFault);
        }
    }

    private static void setHostName(AxisConfiguration axisConfig) throws DeploymentException {
        try {
            ServerConfiguration serverConfig = ServerConfiguration.getInstance();
            String hostName = serverConfig.getFirstProperty("HostName");
            if (hostName != null) {
                Parameter param = ParameterUtil.createParameter(HOST_ADDRESS, hostName);
                axisConfig.addParameter(param);
            }
        } catch (AxisFault axisFault) {
            throw new DeploymentException(axisFault.getMessage(), axisFault);
        }
    }

    public void loadServices() {
        if (repoLocation != null && repoLocation.trim().length() != 0) {
            if (isUrlRepo) {
                try {
                    loadServicesFromUrl(new URL(repoLocation));
                } catch (MalformedURLException e) {
                    log.error("Services repository URL " + repoLocation + " is invalid");
                }
            } else {
                super.loadServices();
            }
        }
    }
}
