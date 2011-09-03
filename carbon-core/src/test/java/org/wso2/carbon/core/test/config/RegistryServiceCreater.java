package org.wso2.carbon.core.test.config;

import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;

import org.wso2.carbon.registry.core.jdbc.EmbeddedRegistryService;
import org.wso2.carbon.registry.core.jdbc.InMemoryEmbeddedRegistryService;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.RegistryConstants;
import org.wso2.carbon.registry.core.internal.RegistryCoreServiceComponent;
import org.wso2.carbon.registry.core.config.RegistryConfiguration;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.utils.CarbonUtils;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;

import java.io.File;

public class RegistryServiceCreater extends CarbonCoreServiceComponent {

    Log log = LogFactory.getLog(CarbonCoreServiceComponent.class);
    EmbeddedRegistryService embeddedRegistryService = null;
    Registry registry = null;
    AxisConfiguration ac = null;
    BundleContext bundleContext;

    public RegistryServiceCreater() {

        if (System.getProperty("carbon.home") == null) {
            File file = new File("distribution/carbon-home");
            if (file.exists()) {
                System.setProperty("carbon.home", file.getAbsolutePath());
            }
            file = new File("../distribution/carbon-home");
            if (file.exists()) {
                System.setProperty("carbon.home", file.getAbsolutePath());
            }
        }

        try {
            String carbonXMLPath = CarbonUtils.getCarbonXml();
            RegistryConfiguration regConfig = new RegistryConfiguration(carbonXMLPath);
            RegistryCoreServiceComponent.setRegistryConfig(regConfig);
        } catch (RegistryException e) {
            throw new RuntimeException("Error creating RegistryConfig" + e.getMessage(), e);
        }

        if (embeddedRegistryService != null) {
            return;
        }

        try {
            embeddedRegistryService = new InMemoryEmbeddedRegistryService();
            registry = embeddedRegistryService.getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        } catch (RegistryException e) {
            log.error("Failed to initialize the registry. Caused by: " + e.getMessage());
        }

    }

    public void setRegistryService() throws Exception {
        RegistryServiceCreater grs = new RegistryServiceCreater();
        grs.setRegistryService(embeddedRegistryService);

    }
}




