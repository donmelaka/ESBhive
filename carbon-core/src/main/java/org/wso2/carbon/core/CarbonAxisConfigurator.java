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
import org.apache.axis2.util.XMLUtils;
import org.apache.axis2.deployment.*;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisConfigurator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;
import org.osgi.framework.BundleContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.core.transports.TransportPersistenceManager;
import org.wso2.carbon.utils.deployment.service.listeners.Axis2ConfigServiceListener;
import org.wso2.carbon.utils.deployment.Axis2DeployerRegistry;
import org.wso2.carbon.utils.deployment.Axis2ModuleRegistry;
import org.wso2.carbon.utils.*;
import org.wso2.carbon.CarbonConstants;

import javax.xml.namespace.QName;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;

/**
 * WSO2 Carbon implementation of AxisConfigurator to load Axis2
 * configuration for WSO2 WSAS.
 */
public class CarbonAxisConfigurator extends DeploymentEngine implements AxisConfigurator {

    private static Log log = LogFactory.getLog(CarbonAxisConfigurator.class);

    private Collection globallyEngagedModules = new ArrayList();
    private String axis2xml;
    private String repoLocation;
    private String webLocation;
    private static CarbonAxisConfigurator instance;
    private boolean isInitialized;
    private boolean isUrlRepo;
    private boolean isUrlAxis2Xml;
    private Axis2ConfigServiceListener configServiceListener;
    private Axis2ConfigItemHolder configItemHolder;
    private String INSTANCE="instance";

    private BundleContext bundleContext;
    private String carbonContextRoot;

    public boolean isInitialized() {
        return isInitialized;
    }

    public static CarbonAxisConfigurator getInstance() {
        if (instance == null) {
            instance = new CarbonAxisConfigurator();
        }
        return instance;
    }

    private CarbonAxisConfigurator() {
    }

    public Axis2ConfigItemHolder getConfigItemHolder() {
        return configItemHolder;
    }

    public void setBundleContext(BundleContext context) {
        this.bundleContext = context;
    }

    public void setCarbonContextRoot(String carbonContextRoot) {
        this.carbonContextRoot = carbonContextRoot;
    }

    /**
     * Load an AxisConfiguration from the repository directory specified
     *
     * @param repoLocation repoLocation
     * @param weblocation  weblocation
     * @throws org.wso2.carbon.utils.ServerException ServerException
     */
    public void init(String repoLocation, String weblocation) throws
                                                              ServerException {
        if (repoLocation == null) {
            throw new ServerException("Axis2 repository not specified!");
        }
        this.webLocation = weblocation;

        // Check whether this is a URL
        isUrlRepo = CarbonUtils.isURL(repoLocation);

        if (isUrlRepo) { // Is repoLocation a URL Repo?
            try {
                new URL(repoLocation).openConnection().connect();
            } catch (IOException e) {
                throw new ServerException("Cannot connect to URL repository " + repoLocation, e);
            }
            this.repoLocation = repoLocation;
        } else { // Is repoLocation a file repo?
            File repo = new File(repoLocation);
            if (repo.exists()) {
                this.repoLocation = repo.getAbsolutePath();
            } else {
                this.repoLocation =
                        System.getProperty("wso2wsas.home") + File.separator +
                                repoLocation;
                repo = new File(this.repoLocation);
                if (!repo.exists()) {
                    this.repoLocation = null;
                    throw new ServerException("Repository location '" + repoLocation +
                            "' not found!");
                }
            }
        }

        axis2xml = CarbonUtils.getAxis2Xml();

        isUrlAxis2Xml = CarbonUtils.isURL(axis2xml);

        if (!isUrlAxis2Xml) { // Is axis2xml a URL to the axis2.xml file?
            File configFile = new File(axis2xml);
            if (!configFile.exists()) {
                //This will fallback to default axis2.xml
                this.axis2xml = null;
                //Thus default
            }
        } else {
            try {
                URLConnection urlConnection = new URL(axis2xml).openConnection();
                urlConnection.connect();
            } catch (IOException e) {
                throw new ServerException("Cannot connect to axis2.xml URL " + repoLocation, e);
            }
            isInitialized = true;
        }
    }

    /**
     * First create a Deployment engine, use that to create an AxisConfiguration
     *
     * @return Axis Configuration
     * @throws AxisFault
     */
    public synchronized AxisConfiguration getAxisConfiguration() throws AxisFault {
        axisConfig = null;
        try {
            axisConfig = populateAxisConfiguration(getAxis2XmlInputStream());
        } catch (Exception e) {
            throw new AxisFault("Exception occured while loading the Axis configuration " +
                    "from " + axis2xml, e);
        }
        
        axisConfig.setConfigurator(this);
        // Registering Axis2 Configuration services in axis2config
        if (bundleContext != null) {
            configServiceListener = new Axis2ConfigServiceListener(axisConfig, bundleContext);
        }

        //Adding deployers which come inside bundles
        Axis2DeployerRegistry deployerRegistry = new Axis2DeployerRegistry(axisConfig);
        deployerRegistry.register(configItemHolder.getDeployerBundles());

        //Deploying modules which come inside bundles.
        Axis2ModuleRegistry moduleRegistry = new Axis2ModuleRegistry(axisConfig);
        moduleRegistry.register(configItemHolder.getModuleBundles());

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
        if (carbonContextRoot != null) {
            Parameter contextRootParam = new Parameter("contextRoot", carbonContextRoot);
            axisConfig.addParameter(contextRootParam);
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
        //set the registry instance in AxisConfiguration
        try {
            RegistryService rs = CarbonCoreServiceComponent.getRegistryService();
            axisConfig.addParameter(WSO2Constants
                    .CONFIG_SYSTEM_REGISTRY_INSTANCE, rs.getConfigSystemRegistry());
            axisConfig.addParameter(WSO2Constants.LOCAL_REPO_INSTANCE, rs.getLocalRepository());
        } catch (Exception e) {
            String msg = "Error occured while adding RegistryService as a paramter to AxisConfiguration";
            log.error(msg, e);
            throw new DeploymentException(msg, e);
        }
        //TCCL will be based on OSGi
        AxisConfigBuilder builder =
                new AxisConfigBuilder(in, axisConfig, this);
        builder.populateConfig();
        /* if user is starting multiple instances change the default port numbers before starting Transports */
        if(CarbonUtils.isChildNode()){
            try{
                OMElement element = (OMElement) XMLUtils.toOM(getAxis2XmlInputStream());
                Iterator trs_Reivers =
                        element.getChildrenWithName(new QName(TAG_TRANSPORT_RECEIVER));
                while(trs_Reivers.hasNext()){
                    OMElement transport = (OMElement) trs_Reivers.next();
                    String transportType = transport.getAttributeValue(new QName(ATTRIBUTE_NAME));
                    Iterator itr = transport.getChildrenWithName(new QName(TAG_PARAMETER));
                    while (itr.hasNext()) {
                        OMElement parameterElement = (OMElement) itr.next();
                        OMAttribute paramName = parameterElement.getAttribute(new QName(ATTRIBUTE_NAME));
                        if("port".equals(paramName.getAttributeValue())){
                            if("http".equals(transportType)){
                                parameterElement.setText(System.getProperty("niohttpPort"));
                            }else if("https".equals(transportType)){
                                parameterElement.setText(System.getProperty("niohttpsPort"));                                
                            }
                        }
                    }
                }
                builder.processTransportReceivers(element.getChildrenWithName(new QName(TAG_TRANSPORT_RECEIVER)));
            }catch(Exception e){
                log.error("Error Reading axis2.xml",e);
            }
        }
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            String msg = "error in closing input stream";
            log.error(msg);
        }

        try {
            // Load transports from the registry
            loadTransports();
        } catch (Exception e) {
            log.warn("Unable to load transports from the registry. Some transports may not " +
                    "get initialized.", e);
        }

        moduleDeployer = new ModuleDeployer(axisConfig);
        return axisConfig;
    }

    private void loadTransports() throws Exception {

        List<TransportInDescription> transportIns = new ArrayList<TransportInDescription>();
        List<TransportOutDescription> transportOuts = new ArrayList<TransportOutDescription>();

        // process transport senders
        String[] transports = TransportPersistenceManager.getEnabledTransports(false);
        if (transports != null) {
            for (String transportToInit : transports) {
                if (axisConfig.getTransportOut(transportToInit.trim()) == null) {
                    TransportOutDescription transportOutDesc = TransportPersistenceManager.
                            getTransportSender(transportToInit, true);
                    if (transportOutDesc != null) {
                        transportOuts.add(transportOutDesc);
                        // No need to init the sender
                        // ConfigurationContextFactory should take care of that
                    }
                }
            }
        }

        // process transport receivers
        transports = TransportPersistenceManager.getEnabledTransports(true);
        if (transports != null) {
            for (String transportToInit : transports) {
                if (axisConfig.getTransportIn(transportToInit.trim()) == null) {
                    TransportInDescription transportInDesc = TransportPersistenceManager.
                            getTransportListener(transportToInit, true);
                    if (transportInDesc != null) {
                        transportIns.add(transportInDesc);
                        // No need to init the listener
                        // ListenerManager should take care of that
                    }
                }
            }
        }

        // Now add the descriptions to the axis configuration
        // This ensures that either all the transports in the registry are initialized or none at all
        for (TransportOutDescription trpOut : transportOuts) {
            axisConfig.addTransportOut(trpOut);
            if (log.isDebugEnabled()) {
                log.debug(trpOut.getName() + " transport sender added to the configuration");
            }
        }

        for (TransportInDescription trpIn : transportIns) {
            axisConfig.addTransportIn(trpIn);
            if (log.isDebugEnabled()) {
                log.debug(trpIn.getName() + " transport receiver added to the configuration");
            }
        }

        // Save the transport configurations to the registry.
        // We do this here to ensure that necessary transport resources are in the registry
        // before services start getting deployed.
        TransportPersistenceManager.updateEnabledTransports(axisConfig.getTransportsIn().values(),
                axisConfig.getTransportsOut().values());
    }

    public void loadServices() {
        setWebLocationString(webLocation);
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

    public void addAxis2ConfigServiceListener() throws Exception{
        bundleContext.addServiceListener(configServiceListener,
                                             "("+ CarbonConstants.AXIS2_CONFIG_SERVICE +"=*)");
    }

    public void setAxis2ConfigItemHolder(Axis2ConfigItemHolder configItemHolder){
        this.configItemHolder = configItemHolder;
    }

    private InputStream getAxis2XmlInputStream()throws AxisFault{
        InputStream axis2xmlStream = null;
        try{
            if (axis2xml != null && axis2xml.trim().length() != 0) {
                if (isUrlAxis2Xml) { // Is it a URL?
                    try {
                        axis2xmlStream = new URL(axis2xml).openStream();
                    } catch (IOException e) {
                        throw new AxisFault("Cannot load axis2.xml from URL", e);
                    }
                } else { // Is it a File?
                    axis2xmlStream = new FileInputStream(axis2xml);
                }
            } else {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                axis2xmlStream =
                        cl.getResourceAsStream(DeploymentConstants.AXIS2_CONFIGURATION_RESOURCE);
            }
        }
        catch(IOException e){
            log.error("Cannot find Axis2.xml file",e);
        }
        return axis2xmlStream;
    }

    public void clearInstance() {
        instance = null;
        this.configContext = null;
    }

}
