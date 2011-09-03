package org.wso2.carbon.core.test.persistence;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.registry.core.jdbc.EmbeddedRegistryService;
import org.wso2.carbon.registry.core.jdbc.InMemoryEmbeddedRegistryService;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.core.persistence.*;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.utils.WSO2Constants;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.description.*;
import org.apache.axis2.wsdl.WSDLConstants;

import java.io.InputStream;

public class PersistenceUtilsTest extends BaseTestCase {

    protected static EmbeddedRegistryService embeddedRegistryService = null;
    protected static Registry registry = null;
    private static PersistenceFactory pf;
    private static AxisConfiguration ac;

    public void setUp() {
        super.setUp();

        if (embeddedRegistryService != null) {
            return;
        }

        try {
            InputStream regConfigStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("registry.xml");
            embeddedRegistryService = new InMemoryEmbeddedRegistryService(regConfigStream);
            registry = embeddedRegistryService.getConfigSystemRegistry();
        } catch (RegistryException e) {
            fail("Failed to initialize the registry. Caused by: " + e.getMessage());
        }

        try {
            ac = new AxisConfiguration();
            ac.addParameter(WSO2Constants.CONFIG_SYSTEM_REGISTRY_INSTANCE, registry);
            pf = new PersistenceFactory(ac);
        } catch (Exception e) {
            fail("Fail to add Parameter to registry. Caused by:" + e.getMessage());
        }
    }


    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetResourcePath() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testServiceGroup1");
        AxisService asv = new AxisService("testService1");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        String path = PersistenceUtils.getResourcePath(asvGroup);
        assertTrue(path.equals(RegistryResources.SERVICE_GROUPS + "testServiceGroup1"));
    }

    public void testGetResourcePath1() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testServiceGroup2");
        AxisService asv = new AxisService("testService2");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        pf.getServicePM().handleNewServiceAddition(asv);
        String path = PersistenceUtils.getResourcePath(asv);
        assertTrue(path.equals(RegistryResources.SERVICE_GROUPS +
                "testServiceGroup2/services/testService2"));
    }

    public void testGetResourcePath2() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testServiceGroup3");
        AxisService asv = new AxisService("testService3");
        AxisOperation operation = AxisOperationFactory
                .getAxisOperation(WSDLConstants.MEP_CONSTANT_IN_OUT);
        asvGroup.addService(asv);
        asv.addOperation(operation);
        String path = PersistenceUtils.getResourcePath(operation);
        assertNotNull(path);
    }

    public void testGetResourcePath3() throws Exception {
        AxisModule am = new AxisModule();
        Version v = new Version("1.0");
        am.setVersion(v);
        am.setName("Module1");
        pf.getModulePM().handleNewModuleAddition(am, "Module1", "1.0");
        String modulePath = RegistryResources.MODULES + "Module1" + "/" + "1.0" + "/";
        String path = PersistenceUtils.getResourcePath(am) + "/";
        assertTrue(path.equals(modulePath));
    }

    public void testGetResourcePath4() throws Exception {
        //This is already checked in PersistenceManagerTest.
    }

    public static Test suite() {
        return new TestSuite(PersistenceUtilsTest.class);
    }
}