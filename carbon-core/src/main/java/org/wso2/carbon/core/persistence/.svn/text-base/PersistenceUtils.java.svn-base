package org.wso2.carbon.core.persistence;

import org.apache.axis2.AxisFault;
import org.apache.axis2.description.*;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.util.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.Axis2ModuleNotFound;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.registry.core.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PersistenceUtils {

    private static Log log = LogFactory.getLog(PersistenceUtils.class);

    private static final String PROXY_SERVICE = "proxy";
    private static final String GLOBALLY_ENGAGED_PARAM_NAME = "globallyEngaged";
    private static final String GLOBALLY_ENGAGED_CUSTOM = "globallyEngagedCustom";

    /**
     * Returns the resource path for the specified Service group
     *
     * @param serviceGroup - AxisServiceGroup instance
     * @return - registry resource path
     */
    public static String getResourcePath(AxisServiceGroup serviceGroup) {
        return RegistryResources.SERVICE_GROUPS + serviceGroup.getServiceGroupName();
    }

    /**
     * Returns the resource path for the specified AxisService
     *
     * @param service - AxisService instance
     * @return - registry resource path
     */
    public static String getResourcePath(AxisService service) {
        return RegistryResources.SERVICE_GROUPS + service.getAxisServiceGroup()
                .getServiceGroupName() + RegistryResources.SERVICES + service.getName();
    }

    /**
     * Returns the resource path for the specified AxisOperation
     *
     * @param operation - AxisService instance
     * @return - registry resource path
     */
    public static String getResourcePath(AxisOperation operation) {
        // Get AxisService
        AxisService service = operation.getAxisService();
        return RegistryResources.SERVICE_GROUPS
                + service.getAxisServiceGroup().getServiceGroupName() + RegistryResources.SERVICES
                + service.getName() + RegistryResources.ServiceProperties.OPERATIONS
                + operation.getName().getLocalPart();
    }

    /**
     * Returns the resource path for the specified AxisBindingOperation
     *
     * @param servicePath - service resource path
     * @param abo         - AxisBindingOperation instance
     * @return - registry resource path
     */
    public static String getBindingOperationPath(String servicePath, AxisBindingOperation abo) {
        AxisBinding binding = abo.getAxisBinding();
        return servicePath + RegistryResources.ServiceProperties.BINDINGS
                + binding.getName().getLocalPart() + RegistryResources.ServiceProperties.OPERATIONS
                + abo.getName().getLocalPart();
    }

    /**
     * Returns the resource path for the given module
     *
     * @param module - AxisModule instance
     * @return module resource path
     */
    public static String getResourcePath(AxisModule module) {
        String version = RegistryResources.ModuleProperties.UNDEFINED;
        if (module.getVersion() != null) {
            version = module.getVersion().toString();
        }
        return RegistryResources.MODULES + module.getName() + "/" + version;
    }

    /**
     * Finds a policy with the given uuid in the provided list of policies
     *
     * @param policyId           - uuid to find
     * @param policyResourceList - resource list
     * @return - policy resource if found, else null
     */
    public static Resource getPolicyResourceFromList(String policyId, List policyResourceList) {
        for (Object resource : policyResourceList) {
            Resource currentResource = (Resource) resource;
            if (currentResource.getProperty(RegistryResources.ServiceProperties.POLICY_UUID)
                    .equalsIgnoreCase(policyId)) {
                return currentResource;
            }
        }
        return null;
    }

    /**
     * Finds module name and version from the given resource and retrieves the AxisModule instance
     * from the config context
     *
     * @param moduleResource - Registry resource for the module
     * @param axisConfig     - AxisConfiguration instance
     * @return - the AxisModule instance if found
     * @throws Axis2ModuleNotFound - if the module is not found
     */
    public static AxisModule getAxisModule(Resource moduleResource, AxisConfiguration
            axisConfig) throws Axis2ModuleNotFound {
        String modName = moduleResource.getProperty(RegistryResources.ModuleProperties.NAME);
        String modVersion = moduleResource
                .getProperty(RegistryResources.ModuleProperties.VERSION);
        AxisModule axisModule = axisConfig.getModule(modName, modVersion);

        if (axisModule == null)
            throw new Axis2ModuleNotFound("Module: " + modName + " not found", modName);

        return axisModule;
    }

    /**
     * Checks whether the given service is a proxy service
     *
     * @param service - AxisService instance
     * @return true if "proxy" param is found. else false
     */
    public static boolean isProxyService(AxisService service) {
        ArrayList axisServiceParameters = service.getParameters();
        ListIterator iter = axisServiceParameters.listIterator();
        boolean isProxyService = false;

        while (iter.hasNext()) {
            Parameter elem = (Parameter) (iter.next());
            Object value = elem.getValue();
            if (value != null && PROXY_SERVICE.equals(value.toString())) {
                isProxyService = true;
            }
        }
        return isProxyService;
    }

    /**
     * Mark this module as faulty
     *
     * @param axisModule - AxisModule instance to be marked as faulty
     */
    public static void markFaultyModule(AxisModule axisModule) {
        axisModule.getParent().getFaultyModules().put(Utils.getModuleName(axisModule.getName(),
                axisModule.getVersion().toString()), axisModule.getName());
    }

    /**
     * Set the needed global params for the module by reading from registry resource
     *
     * @param axisModule     - AxisModule instance
     * @param moduleResource - module resource
     * @throws AxisFault - on axis level errors
     */
    public static void handleGlobalParams(AxisModule axisModule,
                                          Resource moduleResource) throws AxisFault {
        if (Boolean.parseBoolean(moduleResource
                .getProperty(RegistryResources.ModuleProperties.GLOBALLY_ENGAGED))) {
            axisModule.addParameter(new Parameter(GLOBALLY_ENGAGED_PARAM_NAME,
                    Boolean.TRUE.toString()));
            axisModule.getParent().engageModule(axisModule);
        }

        if (Boolean.parseBoolean(moduleResource.getProperty(GLOBALLY_ENGAGED_CUSTOM))) {
            axisModule.addParameter(new Parameter(GLOBALLY_ENGAGED_PARAM_NAME,
                    Boolean.TRUE.toString()));
        }
    }
}
