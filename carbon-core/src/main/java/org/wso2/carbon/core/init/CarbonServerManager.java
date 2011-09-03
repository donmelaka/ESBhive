package org.wso2.carbon.core.init;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.clustering.ClusteringConstants;
import org.apache.axis2.clustering.ClusteringAgent;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.ListenerManager;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.equinox.http.helper.FilterServletAdaptor;
import org.osgi.framework.*;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.bridge.FrameworkLauncherFactory;
import org.wso2.carbon.bridge.FrameworkRestarter;
import org.wso2.carbon.core.*;
import org.wso2.carbon.core.deployment.OSGiAxis2ServiceDeployer;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.core.internal.HTTPGetProcessorListener;
import org.wso2.carbon.core.internal.ListenerManagerRetrieverServiceComponent;
import org.wso2.carbon.core.multitenancy.MultitenantServerManager;
import org.wso2.carbon.core.security.CarbonJMXAuthenticator;
import org.wso2.carbon.core.transports.CarbonServlet;
import org.wso2.carbon.core.transports.TransportPersistenceManager;
import org.wso2.carbon.core.util.HouseKeepingTask;
import org.wso2.carbon.core.util.ParameterUtil;
import org.wso2.carbon.core.util.SystemRestarter;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.*;
import org.wso2.carbon.utils.deployment.Axis2ServiceRegistry;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.axis2.transport.TransportListener.HOST_ADDRESS;


/**
 * This class is responsible for managing the WSO2 Carbon server core. Handles server starting,
 * restarting & shutting down.
 */
public final class CarbonServerManager implements Controllable {
    private static Log log = LogFactory.getLog(CarbonServerManager.class);

    private final Map<String, String> pendingItemMap = new ConcurrentHashMap<String, String>();

    private BundleContext bundleContext;

    private PreAxis2ConfigItemListener configItemListener;
    private PreAxis2RequiredServiceListener requiredServiceListener;
    private OSGiAxis2ServicesListener osgiAxis2ServicesListener;

    private Timer timer = new Timer();

    private final String CLIENT_REPOSITORY_LOCATION = "Axis2Config.ClientRepositoryLocation";
    private final String CLIENT_AXIS2_XML_LOCATION = "Axis2Config.clientAxis2XmlLocation";

    protected String serverName;

    private String carbonHome;
    private ServerConfiguration serverConfig;
    private Thread shutdownHook;
    public boolean isEmbedEnv = false;

    private java.rmi.registry.Registry rmiRegistry;
    private JMXConnectorServer jmxConnectorServer;
    public String serverWorkDir;

    public String axis2RepoLocation;

    private ConfigurationContext serverConfigContext;
    private ConfigurationContext clientConfigContext;

    /**
     * Indicates whether the shutdown of the server was triggered by the Carbon shutdown hook
     */
    private boolean isShutdownTriggeredByShutdownHook = false;


    public CarbonServerManager(BundleContext context) {
        if (System.getProperty(CarbonConstants.START_TIME) == null) {
            System.setProperty(CarbonConstants.START_TIME, System.currentTimeMillis() + "");
        }

        this.bundleContext = context;

        //Initializing ConfigItem Listener - Modules and Deployers
        configItemListener = new PreAxis2ConfigItemListener(bundleContext, this);

        //Initializing Required OSGi service listener
        requiredServiceListener = new PreAxis2RequiredServiceListener(bundleContext, this);

        osgiAxis2ServicesListener = new OSGiAxis2ServicesListener(bundleContext, this);

        populateListeners();

        if (configItemListener.registerBundleListener()) {
            configItemListener.start();
        }

        if (requiredServiceListener.registerServiceListener()) {
            requiredServiceListener.start();
        }

        if(osgiAxis2ServicesListener.registerBundleListener()){
            osgiAxis2ServicesListener.start();
        }

        //check whether pending list is empty, If so initialize Carbon
        if (pendingItemMap.isEmpty()) {
            initializeCarbon();
        } else {
            //Scheduling timer to run if the required items are being delayed.
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    try {
                        if (!pendingItemMap.isEmpty()) {
                            log.warn("Carbon initialization is delayed due to the following unsatisfied items..");
                            for (String configItem : pendingItemMap.keySet()) {
                                log.warn("Waiting for required " + pendingItemMap.get(configItem) + ": " + configItem);
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            }, 5000, 10000);
        }
    }

    /**
     * Populate both listeners with the relavent bundles
     */
    public void populateListeners() {
        Dictionary headers;
        String value;
        for (Bundle bundle : bundleContext.getBundles()) {
            headers = bundle.getHeaders();

            //Searching for a Deployer
            value = (String) headers.get(CarbonConstants.CarbonManifestHeaders.AXIS2_DEPLOYER);
            if (value != null) {
                configItemListener.addDeployerBundle(value, bundle);
            }

            //Searching for a Module
            value = (String) headers.get(CarbonConstants.CarbonManifestHeaders.AXIS2_MODULE);
            if (value != null) {
                configItemListener.addModuleBundle(value, bundle);
            }

            // Searching for Axis2 services defined as OSGi bundles
            Enumeration entries = bundle.findEntries("META-INF", "*services.xml", true);
            if (entries !=null && entries.hasMoreElements()) {
                osgiAxis2ServicesListener.addOSGiAxis2Service(bundle);
            }

            //Searching for a pre Axis2 required OSGi service before 
            value = (String) headers.get(CarbonConstants.CarbonManifestHeaders.AXIS2_INIT_REQUIRED_SERVICE);
            if (value != null) {
                requiredServiceListener.addRequiredServiceBundle(bundle, value);
            }
        }
    }

    void addPendingItem(String requiredItemName, String itemType) {
        synchronized (pendingItemMap) {
            if (log.isDebugEnabled()) {
                log.debug("Pending Item added : " + requiredItemName);
            }
            pendingItemMap.put(requiredItemName, itemType);
        }
    }

    void removePendingItem(String requiredItemName) {
        synchronized (pendingItemMap) {
            if (pendingItemMap.containsKey(requiredItemName)) {
                if (log.isDebugEnabled()) {
                    log.debug("Pending Item removed : " + requiredItemName);
                }
                pendingItemMap.remove(requiredItemName);
                if (pendingItemMap.isEmpty()) {
                    initializeCarbon();
                }
            }
        }
    }

    private void initializeCarbon() {

        // Reset the SAAJ Interfaces
        System.getProperties().remove("javax.xml.soap.MessageFactory");
        System.getProperties().remove("javax.xml.soap.SOAPConnectionFactory");
        
        //remove bundlelistener and service listener
        configItemListener.unregisterBundleListener();
        requiredServiceListener.unregisterServiceListener();
        osgiAxis2ServicesListener.unregisterBundleListener();

        //Cancelling the timer task
        timer.cancel();

        try {
            log.info("Starting Carbon initialization...");

            // Location for expanding web content within AAR files
            String webLocation = System.getProperty(CarbonConstants.WEB_RESOURCE_LOCATION);
            if (webLocation == null){
                webLocation =  CarbonUtils.getTomcatWebappDirPath() +  File.separator + "wservices";
            }

            String temp = System.getProperty(ServerConstants.STANDALONE_MODE);
            if (temp != null) {
                temp = temp.trim();
                isEmbedEnv = temp.equals("true");
            } else {
                isEmbedEnv = false;
            }

            serverConfig = ServerConfiguration.getInstance();

            //Checking Carbon home
            carbonHome = System.getProperty(ServerConstants.CARBON_HOME);
            if (carbonHome == null) {
                String msg = ServerConstants.CARBON_HOME +
                             "System property has not been set.";
                log.fatal(msg);
                log.fatal(serverName + " startup failed.");
                throw new ServletException(msg);
            }

            try {
                System.setProperty(ServerConstants.LOCAL_IP_ADDRESS, NetworkUtils.getLocalHostname());
            } catch (SocketException ignored) {
            }

            initServerConfiguration();
            serverName = serverConfig.getFirstProperty("Name");


            String hostName = serverConfig.getFirstProperty("ClusteringHostName");
            if (System.getProperty(ClusteringConstants.LOCAL_IP_ADDRESS) == null &&
                hostName != null && hostName.trim().length() != 0) {
                System.setProperty(ClusteringConstants.LOCAL_IP_ADDRESS, hostName);
            }

            serverWorkDir =
                    new File(serverConfig.getFirstProperty("WorkDirectory")).getAbsolutePath();
            System.setProperty("axis2.work.dir", serverWorkDir);

            setAxis2RepoLocation();

            Axis2ConfigItemHolder configItemHolder = new Axis2ConfigItemHolder();
            configItemHolder.setDeployerBundles(configItemListener.getDeployerBundles());
            configItemHolder.setModuleBundles(configItemListener.getModuleBundles());

            String carbonContextRoot = serverConfig.getFirstProperty("WebContextRoot");

            CarbonAxisConfigurator carbonAxisConfigurator = CarbonAxisConfigurator.getInstance();
            carbonAxisConfigurator.setAxis2ConfigItemHolder(configItemHolder);
            carbonAxisConfigurator.setBundleContext(bundleContext);
            carbonAxisConfigurator.setCarbonContextRoot(carbonContextRoot);
            if (!carbonAxisConfigurator.isInitialized()) {
                carbonAxisConfigurator.init(axis2RepoLocation, webLocation);
            }

            //This is the point where we initialize Axis2.
            serverConfigContext =
                    CarbonConfigurationContextFactory.
                            createNewConfigurationContext(carbonAxisConfigurator, bundleContext);
            serverConfigContext.setContextRoot(carbonContextRoot);

            // At this point all the services and modules are deployed
            // Therefore it is time to allows other deployers to be registered.
            carbonAxisConfigurator.addAxis2ConfigServiceListener();

            initNetworkUtils(serverConfigContext.getAxisConfiguration());


            // Enabling http binding generation
            Parameter enableHttp = new Parameter("enableHTTP", "true");
            AxisConfiguration axisConfig = serverConfigContext.getAxisConfiguration();
            axisConfig.addParameter(enableHttp);

            TransportPersistenceManager.updateEnabledTransports(
                    axisConfig.getTransportsIn().values(),
                    axisConfig.getTransportsOut().values());

            runInitializers();

            populateConnectionProperties();

            startJMXService();
            
            MultitenantServerManager.start(serverConfigContext);

            //TODO As a tempory solution this part is added here. But when ui bundle are seperated from the core bundles
            //TODO this should be fixed.
            ServerConfiguration config = ServerConfiguration.getInstance();
            String type = config.getFirstProperty("Security.KeyStore.Type");
            String password = config.getFirstProperty("Security.KeyStore.Password");
            String storeFile = new File(config.getFirstProperty("Security.KeyStore.Location")).getAbsolutePath();

            System.setProperty("javax.net.ssl.trustStore", storeFile);
            System.setProperty("javax.net.ssl.trustStoreType", type);
            System.setProperty("javax.net.ssl.trustStorePassword", password);

            try {
                ServerStatus.setServerRunning();
            } catch (AxisFault e) {
                String msg = "Cannot set server to running mode";
                log.error(msg, e);
            }

            addShutdownHook();
            registerHouseKeepingTask(serverConfigContext);

            serverConfigContext.setProperty(Constants.CONTAINER_MANAGED, "true");
            serverConfigContext.setProperty(ServerConstants.WORK_DIR, serverWorkDir);
            // This is used inside the sever-admin component.
            serverConfigContext.setProperty(ServerConstants.CARBON_INSTANCE, this);

            // Creating the Client side configuration context
            clientConfigContext = getClientConfigurationContext();

            //TOa house keeping taskDO add this map to a house keeping task
            //Adding FILE_RESOURCE_MAP
            Object property = new TreeBidiMap();
            clientConfigContext.setProperty(ServerConstants.FILE_RESOURCE_MAP, property);
            clientConfigContext.setContextRoot(carbonContextRoot);
            
            //Deploying Web service which resides in bundles
            Axis2ServiceRegistry serviceRegistry = new Axis2ServiceRegistry(serverConfigContext);
            serviceRegistry.register(bundleContext.getBundles());
            new OSGiAxis2ServiceDeployer(serverConfigContext, bundleContext).registerBundleListener(); // This will register the OSGi bundle listener

            HttpService httpService = CarbonCoreServiceComponent.getHttpService();
            HttpContext defaultHttpContext = httpService.createDefaultHttpContext();

            registerCarbonServlet(httpService, defaultHttpContext);

            RealmService realmService = CarbonCoreServiceComponent.getRealmService();
            UserRealm teannt0Realm = realmService.getBootstrapRealm();
            CarbonJMXAuthenticator.setUserRealm(teannt0Realm);

            log.info("Repository       : " + axis2RepoLocation);
            
            //Registering the configuration contexts as an OSGi service.
            bundleContext.registerService(ConfigurationContextService.class.getName(),
                                          new ConfigurationContextService(serverConfigContext,
                                                                          clientConfigContext),
                                          null);

            //Registering ServerConfiguration as an OSGi service
            bundleContext.registerService(ServerConfiguration.class.getName(), serverConfig, null);

//            log.info("Successfully initialized WSO2 Carbon");
        } catch (Throwable e) {
            log.fatal("WSO2 Carbon initialization Failed", e);
        }
    }

    private void registerCarbonServlet(HttpService httpService, HttpContext defaultHttpContext)
            throws ServletException, NamespaceException, InvalidSyntaxException {
        if (!"false".equals(serverConfig.getFirstProperty("RequireCarbonServlet"))) {
            CarbonServlet carbonServlet = new CarbonServlet(serverConfigContext);
            String servicePath = "/services";
            String path = serverConfigContext.getServicePath();
            if (path != null) {
                servicePath = path.trim();
            }
            if (!servicePath.startsWith("/")) {
                servicePath = "/" + servicePath;
            }
            ServiceReference filterServiceReference = bundleContext.getServiceReference(Filter.class.getName());
            if (filterServiceReference != null) {
                Filter filter = (Filter) bundleContext.getService(filterServiceReference);
                httpService.registerServlet(servicePath, new FilterServletAdaptor(filter , null, carbonServlet), null, defaultHttpContext);
            } else {
                httpService.registerServlet(servicePath, carbonServlet, null, defaultHttpContext);
            }
            HTTPGetProcessorListener getProcessorListener =
                    new HTTPGetProcessorListener(carbonServlet, bundleContext);
            // Check whether there are any services that expose HTTPGetRequestProcessors
            ServiceReference[] getRequestProcessors =
                    bundleContext.getServiceReferences(null,
                                                       "(" + CarbonConstants.HTTP_GET_REQUEST_PROCESSOR_SERVICE + "=*)");

            // If there are any we need to register them explicitly
            if (getRequestProcessors != null) {
                for (ServiceReference getRequestProcessor : getRequestProcessors) {
                    getProcessorListener.addHTTPGetRequestProcessor(getRequestProcessor,
                                                                    ServiceEvent.REGISTERED);
                }
            }

            // We also add a service listener to make sure we react to changes in the bundles that
            // expose HTTPGetRequestProcessors
            bundleContext.addServiceListener(getProcessorListener,
                                             "(" + CarbonConstants.HTTP_GET_REQUEST_PROCESSOR_SERVICE + "=*)");
        }
    }

    private ConfigurationContext getClientConfigurationContext() throws AxisFault {
        String clientRepositoryLocation =
                serverConfig.getFirstProperty(CLIENT_REPOSITORY_LOCATION);
        String clientAxis2XmlLocationn = serverConfig.getFirstProperty(CLIENT_AXIS2_XML_LOCATION);
        ConfigurationContext clientConfigContext =
                ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                        clientRepositoryLocation, clientAxis2XmlLocationn);
        registerHouseKeepingTask(clientConfigContext);
        clientConfigContext.setProperty(ServerConstants.WORK_DIR, serverWorkDir);
        return clientConfigContext;
    }

    private void setAxis2RepoLocation() {
        //First check whether repository should be read from the registry
        String regRepoPath = CarbonUtils.getRegistryRepoPath();

        if (regRepoPath != null) {
            axis2RepoLocation = RegistryRepoHandler.prepareRepository(regRepoPath);
        } else if (System.getProperty("axis2.repo") != null) {
            axis2RepoLocation = System.getProperty("axis2.repo");
            /* First see whether this is the -n scenario */
            if(CarbonUtils.isMultipleInstanceCase()){
                /* Now check whether this is a ChildNode or not, if this is the
		           a ChildNode we do not deploy services to this */
                if(!CarbonUtils.isChildNode()){
                    axis2RepoLocation = CarbonUtils.getCarbonHome();
                }
            }
            serverConfig.setConfigurationProperty(ServerConfiguration.AXIS2_CONFIG_REPO_LOCATION,
                    axis2RepoLocation);
        } else {
            axis2RepoLocation = serverConfig
                    .getFirstProperty(ServerConfiguration.AXIS2_CONFIG_REPO_LOCATION);
        }

        if (!axis2RepoLocation.endsWith("/")) {
            serverConfig.setConfigurationProperty(ServerConfiguration.AXIS2_CONFIG_REPO_LOCATION,
                                                  axis2RepoLocation + "/");
            axis2RepoLocation = axis2RepoLocation + "/";
        }
    }

    private void initServerConfiguration() throws ServletException {
        try {
            File carbonXML = new File(CarbonUtils.getCarbonXml());
            InputStream inSXml = new FileInputStream(carbonXML);
            serverConfig.forceInit(inSXml);
        } catch (ServerConfigurationException e) {
            String msg = "Could not initialize server configuration";
            log.fatal(msg);
            throw new ServletException(msg);
        } catch (FileNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void populateConnectionProperties() throws Exception {
        RegistryService registryService = CarbonCoreServiceComponent.getRegistryService();
        Registry registry = registryService.getConfigSystemRegistry();
        Resource resource = registry.newResource();
        String contextRoot = serverConfigContext.getContextRoot();
        String servicePath = serverConfigContext.getServicePath();
        resource.setProperty("service-path", servicePath);
        resource.setProperty("bundleContext-root", contextRoot);
        String requestIP =
                org.apache.axis2.util.Utils.getIpAddress(serverConfigContext.getAxisConfiguration());
        resource.setProperty("host-name", requestIP);
        registry.put(RegistryResources.CONNECTION_PROPS, resource);
        resource.discard();
    }

    public void stopListenerManager() throws AxisFault {
        try {
            ListenerManager listenerManager =
                    ListenerManagerRetrieverServiceComponent.getListenerManager();
            if (listenerManager != null) {
                listenerManager.destroy();
            }

            //we need to call this method to clean the temp files we created.
            if (serverConfigContext != null) {
                serverConfigContext.terminate();
            }
        } catch (Exception e) {
            log.error("Exception occurred while shutting down listeners", e);
        }
    }

    private void registerHouseKeepingTask(ConfigurationContext configurationContext) {
        if (Boolean.valueOf(serverConfig.getFirstProperty("HouseKeeping.AutoStart"))) {
            Timer houseKeepingTimer = new Timer();
            long houseKeepingInterval =
                    Long.parseLong(serverConfig.
                            getFirstProperty("HouseKeeping.Interval")) * 60 * 1000;
            Object property =
                    configurationContext.getProperty(ServerConstants.FILE_RESOURCE_MAP);
            if (property == null) {
                property = new TreeBidiMap();
                configurationContext.setProperty(ServerConstants.FILE_RESOURCE_MAP, property);
            }
            houseKeepingTimer
                    .scheduleAtFixedRate(new HouseKeepingTask(serverWorkDir, (BidiMap) property),
                                         houseKeepingInterval,
                                         houseKeepingInterval);
        }
    }

    private void runInitializers() throws ServerException {

        String[] initializers =
                serverConfig.getProperties("ServerInitializers.Initializer");
        for (String clazzName : initializers) {
            try {
                Class clazz = bundleContext.getBundle().loadClass(clazzName);
                ServerInitializer intializer = (ServerInitializer) clazz.newInstance();
                if (log.isDebugEnabled()) {
                    log.debug("Using ServerInitializer " + intializer.getClass().getName());
                }
                intializer.init(serverConfigContext);
            } catch (Exception e) {
                throw new ServerException(e);
            }
        }
    }

    private void startJMXService() throws ServerException {
        String rmiRegistryPortString = ServerConfiguration.getInstance().
                getFirstProperty(CarbonInitConstant.JMX_RMI_REGISTRY_CONF);
        // override the config value with the command line argument
        if(getProperty(CarbonInitConstant.JMX_REMOTE_PORT_PROPERTY) != null){
            rmiRegistryPortString = getProperty(CarbonInitConstant.JMX_REMOTE_PORT_PROPERTY);
        }
        String rmiServerPortString = ServerConfiguration.getInstance().
                getFirstProperty(CarbonInitConstant.JMX_RMI_SERVER_CONF);
        // override the config value with the command line argument
        if(getProperty(CarbonInitConstant.RMI_SERVER_PORT_PROPERTY) != null){
            rmiServerPortString = getProperty(CarbonInitConstant.RMI_SERVER_PORT_PROPERTY);
        }

        if (rmiRegistryPortString != null) {
            int rmiRegistryPort = Integer.parseInt(rmiRegistryPortString);
            MBeanServer mbs = ManagementFactory.getMBeanServer();
            String jmxURL;
            try {
                try {
                    rmiRegistry = LocateRegistry.createRegistry(rmiRegistryPort);
                } catch (Throwable ignored) {
                    ignored.printStackTrace();
                }

                // Create an RMI connector and start it
                if (rmiServerPortString != null) {
                    int rmiServerPort = Integer.parseInt(rmiServerPortString);
                    jmxURL = "service:jmx:rmi://" + NetworkUtils.getLocalHostname() + ":" +
                            rmiServerPort + "/jndi/rmi://" + NetworkUtils.getLocalHostname() + ":" +
                            rmiRegistryPort + "/jmxrmi";
                } else {
                    jmxURL = "service:jmx:rmi:///jndi/rmi://" +
                            NetworkUtils.getLocalHostname() + ":" + rmiRegistryPort + "/jmxrmi";
                }
                JMXServiceURL url = new JMXServiceURL(jmxURL);

                // Security credentials are included in the env Map
                HashMap<String, CarbonJMXAuthenticator> env = new HashMap<String, CarbonJMXAuthenticator>();
                env.put(JMXConnectorServer.AUTHENTICATOR, new CarbonJMXAuthenticator());
                jmxConnectorServer =
                        JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
                jmxConnectorServer.start();
                log.info("JMX Service URL  : " + jmxURL);
            } catch (Exception e) {
                String msg = "Could not initialize MBean server";
                log.error(msg, e);
            }
        }
    }

    private static String getProperty(String name){
        return System.getProperty(name);
    }

    public void stopJmxService() {
        try {
            if (jmxConnectorServer != null) {
                jmxConnectorServer.stop();
                try {
                    UnicastRemoteObject.unexportObject(rmiRegistry, true); // Stop the RMI registry
                } catch (java.rmi.NoSuchObjectException ignored) {
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while stopping JMXConnectorServer", e);
        }
    }

    private void initNetworkUtils(AxisConfiguration axisConfiguration)
            throws AxisFault, SocketException {
        String hostName = serverConfig.getFirstProperty("HostName");
        if (hostName != null) {
            Parameter param = axisConfiguration.getParameter(HOST_ADDRESS);
            if (param != null) {
                param.setEditable(true);
                param.setValue(hostName);
            } else {
                param = ParameterUtil.createParameter(HOST_ADDRESS, hostName);
                axisConfiguration.addParameter(param);
            }
        } else {
            Parameter param = axisConfiguration.getParameter(HOST_ADDRESS);
            if (param != null) {
                hostName = (String) param.getValue();
                log.info(HOST_ADDRESS + " has been selected from Axis2.xml.");
            }
        }
        NetworkUtils.init(hostName);
    }

    public void restart() {
        restart(false);
    }

    public void restartGracefully() {
        restart(true);
    }

    /**
     * Restart the Carbon server
     *
     * @param isGraceful True, if the server should be gracefully restarted, false, if a
     *                   restart should be forced
     */
    private void restart(boolean isGraceful) {
        /*try {
            ServerStatus.setServerRestarting();
        } catch (AxisFault e) {
            String msg = "Cannot set server to restarting mode";
            log.error(msg, e);
        } */
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        stopJmxService();

        try {
            ServerStatus.setServerRestarting();

            Map inTransports = serverConfigContext.getAxisConfiguration().getTransportsIn();
            String serverName = ServerConfiguration.getInstance().getFirstProperty("Name");
            if (isGraceful) {
                log.info("Gracefully restarting " + serverName + "...");
                new ServerManagement(inTransports).startMaintenance();
            } else {
                log.info("Restarting " + serverName + "...");
            }
            new Thread(new FrameworkRestarter()).start();
        } catch (Exception e) {
            String msg = "Cannot set server to restarting mode";
            log.error(msg, e);
        }
//        cleanupSystem();
//        new Thread(new SystemRestarter(isGraceful, serverConfigContext)).start();
    }

    /**
     * Forced shutdown
     */
    public void shutdown() {
        log.info("Shutting down " + serverName + "...");
        if (!isShutdownTriggeredByShutdownHook) {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
        }
        try {
            try {
                ServerStatus.setServerShuttingDown();
            } catch (AxisFault e) {
                String msg = "Cannot set server to shutdown mode";
                log.error(msg, e);
            }
            CarbonCoreServiceComponent.shutdown();
            stopListenerManager();
//            cleanupSystem();
            stopJmxService();
            log.info("Shutting down OSGi framework...");
            FrameworkLauncherFactory.getFrameworkLauncher().stop();
            log.info("Shutdown complete");
            log.info("Halting JVM");
            if (!isShutdownTriggeredByShutdownHook) {
                System.exit(0);
            }
        } catch (Exception e) {
            log.error("Error occurred while shutting down " + serverName, e);
            if (!isShutdownTriggeredByShutdownHook) {
                System.exit(1);
            }
        }
    }

    /**
     * Graceful shutdown
     */
    public void shutdownGracefully() {
        try {
            ServerStatus.setServerShuttingDown();
        } catch (Exception e) {
            String msg = "Cannot set server to shutdown mode";
            log.error(msg, e);
        }
        try {
            log.info("Gracefully shutting down " + serverName + "...");
            Map<String, TransportInDescription> inTransports =
                    serverConfigContext.getAxisConfiguration().getTransportsIn();
            new ServerManagement(inTransports).startMaintenance();
        } catch (Exception e) {
            String msg = "Cannot put transports into maintenance mode";
            log.error(msg, e);
        }
        shutdown();
    }

    private void addShutdownHook() {
        if (shutdownHook != null) {
            return;
        }
        shutdownHook = new Thread() {
            public void run() {
                isShutdownTriggeredByShutdownHook = true;
                shutdownGracefully();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

//    private void cleanupSystem() {
//        log.info("Cleaning up system... ");
//        new FileManipulator().deleteDir(new File(carbonHome + File.separator +
//                                                 serverConfig.getFirstProperty("WorkDirectory")));
//        new FileManipulator().deleteDir(new File(carbonHome + File.separator + "normalportlist.txt"));
//        new FileManipulator().deleteDir(new File(carbonHome + File.separator + "nioportlist.txt"));
//
//        if (serverConfigContext != null) {
//            Object property = serverConfigContext.getProperty(ServerConstants.FILE_RESOURCE_MAP);
//            if (property != null) {
//                ((Map) property).clear();
//            }
//        }
//    }

        public void cleanupSystem() throws Exception {
        log.info("Cleaning up system...");
        new FileManipulator().deleteDir(new File(carbonHome + File.separator +
                serverConfig.getFirstProperty("WorkDirectory")));
        if (serverConfigContext != null) {
            Object property = serverConfigContext.getProperty(ServerConstants.FILE_RESOURCE_MAP);
            if (property != null) {
                ((Map) property).clear();
            }
        }

        MBeanRegistrar.unregisterAllMBeans();
        ClusteringAgent clusteringAgent =
                serverConfigContext.getAxisConfiguration().getClusteringAgent();
        if (clusteringAgent != null) {
            clusteringAgent.finalize();
        }

        this.configItemListener = null;
        this.osgiAxis2ServicesListener = null;
        this.requiredServiceListener =null;
        this.shutdownHook = null;
        CarbonConfigurationContextFactory.clear();
        CarbonAxisConfigurator.getInstance().cleanup();
        CarbonAxisConfigurator.getInstance().clearInstance();
        serverConfigContext.removeProperty(ServerConstants.CARBON_INSTANCE);
        serverConfigContext.removeProperty(WSO2Constants.PRIMARY_BUNDLE_CONTEXT);

        serverConfigContext.terminate();
        clientConfigContext.terminate();
        serverConfigContext = null;
        clientConfigContext = null;
    }
}
