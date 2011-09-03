package org.wso2.carbon.core.init;

import org.osgi.framework.BundleListener;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Bundle;
import org.wso2.carbon.CarbonConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps track of the required Modules and Deployer which should be deployed before initializing Axis2
 */
public class PreAxis2ConfigItemListener implements BundleListener {
    private static Log log = LogFactory.getLog(PreAxis2ConfigItemListener.class);

    private Map<String, Bundle> moduleBundleMap = new HashMap<String, Bundle>();
    private Map<String, Bundle> deployerBundleMap = new HashMap<String, Bundle>();


    private BundleContext bundleContext;
    private CarbonServerManager carbonServerManager;
    private boolean listenerRegistered;

    public PreAxis2ConfigItemListener(BundleContext bundleContext, CarbonServerManager carbonServerManager) {
        this.bundleContext = bundleContext;
        this.carbonServerManager = carbonServerManager;
    }

    /**
     * Registering PreAxis2ConfigItemListener as a BundleListener
     * @return boolean to indicate whether there are pending modules or deployers
     */
    boolean registerBundleListener() {
        //what if there are no pending things, then don't register the listener
        if(moduleBundleMap.isEmpty() && deployerBundleMap.isEmpty()){
            listenerRegistered = false;
        } else {
            bundleContext.addBundleListener(this);
            listenerRegistered = true;
        }
        return listenerRegistered;
    }

    void unregisterBundleListener() {
        if (listenerRegistered) {
            bundleContext.removeBundleListener(this);
        }
    }

    synchronized void start() {
        Bundle bundle;
        //Searching Non ACTIVE Bundles and add them to the pending list.
        for (String moduleName : moduleBundleMap.keySet()) {
            bundle = moduleBundleMap.get(moduleName);
            if (bundle.getState() != Bundle.ACTIVE) {
                carbonServerManager.addPendingItem(moduleName, "Module");
            }
        }

        for (String moduleName : deployerBundleMap.keySet()) {
            bundle = deployerBundleMap.get(moduleName);
            if (bundle.getState() != Bundle.ACTIVE) {
                carbonServerManager.addPendingItem(moduleName, "Deployer");
            }
        }
    }

    void addModuleBundle(String moduleName, Bundle bundle) {
        moduleBundleMap.put(moduleName, bundle);
    }

    void addDeployerBundle(String deployerName, Bundle bundle) {
        deployerBundleMap.put(deployerName, bundle);
    }

    public synchronized void bundleChanged(BundleEvent event) {
        //checking the event type
        if(event.getType() != BundleEvent.STARTED)
            return;
        //TODO: Need to handle the STOPPED event. Bundles should be removed from AxisConfiguration
        //TODO: Need to handle bundle restarts

        String value;
        Dictionary headers = event.getBundle().getHeaders();

        //Searching for a Deployer
        value = (String) headers.get(CarbonConstants.CarbonManifestHeaders.AXIS2_DEPLOYER);
        if (value != null) {
            carbonServerManager.removePendingItem(value);
        }

        //Searching for a Module
        value = (String) headers.get(CarbonConstants.CarbonManifestHeaders.AXIS2_MODULE);
        if (value != null) {
            carbonServerManager.removePendingItem(value);
        }
    }

    public Bundle[] getModuleBundles(){
        return moduleBundleMap.values().toArray(new Bundle[moduleBundleMap.size()]); 
    }

    public Bundle[] getDeployerBundles(){
        return deployerBundleMap.values().toArray(new Bundle[deployerBundleMap.size()]); 
    }
}
