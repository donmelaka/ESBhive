package org.wso2.carbon.core.test.persistence;

import org.wso2.carbon.registry.core.jdbc.EmbeddedRegistryService;
import org.wso2.carbon.registry.core.jdbc.InMemoryEmbeddedRegistryService;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.core.persistence.PersistenceFactory;
import org.wso2.carbon.core.persistence.PersistenceUtils;
import org.wso2.carbon.core.RegistryResources;

import org.wso2.carbon.utils.WSO2Constants;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Association;
import org.apache.axis2.description.Version;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class PersistenceManagerTest extends BaseTestCase {

    protected static EmbeddedRegistryService embeddedRegistryService = null;
    protected static Registry configRegistry = null;
    protected static Registry governanceRegistry = null;

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
            configRegistry = embeddedRegistryService.getConfigSystemRegistry();
        } catch (RegistryException e) {
            fail("Failed to initialize the registry. Caused by: " + e.getMessage());
        }

        try {
            ac = new AxisConfiguration();
            ac.addParameter(WSO2Constants.CONFIG_SYSTEM_REGISTRY_INSTANCE, configRegistry);
            pf = new PersistenceFactory(ac);
        } catch (Exception e) {
            fail("Fail to add Parameter to registry. Caused by:" + e.getMessage());
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNewServiceGroupAddition() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("test");
        AxisService asv = new AxisService("testService");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        Resource re = pf.getServiceGroupPM().getServiceGroup("test");
        assertNotNull(re);
    }

    public void testSetServiceGroupProperty() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testGP");
        AxisService asv = new AxisService("testServiceGP");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);

        pf.getServiceGroupPM().setServiceGroupProperty(asvGroup, "name", "test");
        Resource re = pf.getServiceGroupPM().getServiceGroup("testGP");
        String value = re.getProperty("name");
        assertEquals("test", value);
    }

    public void testNewServiceAddition() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testServiceGroup");
        AxisService asv = new AxisService("testServiceAdd");
        asvGroup.addService(asv);
        String policyXML = "<wsp:Policy\n" +
                "   xmlns:sp=\"http://schemas.xmlsoap.org/ws/2005/07/securitypolicy\"\n" +
                "   xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\"\n" +
                "   xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n" +
                "   wsu:Id=\"SecureMessagePolicy\" >\n" +
                "  <sp:SignedParts>\n" +
                "    <sp:Body />\n" +
                "  </sp:SignedParts>\n" +
                "  <sp:EncryptedParts>\n" +
                "    <sp:Body />\n" +
                "  </sp:EncryptedParts>\n" +
                "</wsp:Policy>";
        ByteArrayInputStream steram = new ByteArrayInputStream(policyXML.getBytes());
        Policy policy = PolicyEngine.getPolicy(steram);
        asv.getPolicySubject().attachPolicy(policy);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        pf.getServicePM().handleNewServiceAddition(asv);

        String serviceResourcePath = RegistryResources.SERVICE_GROUPS
                + asv.getAxisServiceGroup().getServiceGroupName()
                + RegistryResources.SERVICES + asv.getName();
        Resource re = configRegistry.get(serviceResourcePath);
        String s = re.getProperty("policy.uuid");
        assertTrue(s.equals("SecureMessagePolicy"));
    }

    public void testServiceProperty() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testP");
        AxisService asv = new AxisService("testServiceP");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        pf.getServicePM().handleNewServiceAddition(asv);

        pf.getServicePM().setServiceProperty(asv, "key", "value");
        Resource re = pf.getServicePM().getService(asv);
        String prop = re.getProperty("key");
        assertEquals("value", prop);
    }

    public void testUpdateServiceGroupParameter() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testGUp");
        AxisService asv = new AxisService("testServiceGUp");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);

        Resource reOld = pf.getServiceGroupPM().getServiceGroup("testGUp");

        Parameter para = new Parameter();
        para.setName("testGParam");
        para.setValue("testGValue");
        pf.getServiceGroupPM().updateServiceGroupParameter(asvGroup, para);
        Resource reNew = pf.getServiceGroupPM().getServiceGroup("testGUp");

        assertNotSame(reOld, reNew);
    }

    public void testUpdateServiceParameter() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testUp");
        AxisService asv = new AxisService("testServiceUp");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        pf.getServicePM().handleNewServiceAddition(asv);

        Parameter parm = new Parameter();
        parm.setName("testParam");
        parm.setValue("testValue");
        parm.setParameterType(5);
        pf.getServicePM().updateServiceParameter(asv, parm);

        String serviceResourcePath = RegistryResources.SERVICE_GROUPS
                + asv.getAxisServiceGroup().getServiceGroupName()
                + RegistryResources.SERVICES + asv.getName();

        String serviceParamResourcePath = serviceResourcePath + RegistryResources.PARAMETERS
                + parm.getName();

        Resource re = configRegistry.get(serviceParamResourcePath);
        String s1 = re.getProperty("param.name");
        String s2 = re.getProperty("param.type");

        assertTrue(s1.equals("testParam"));
        assertTrue(s2.equals("5"));
    }

    public void testNewModuleAddition() throws Exception {
        AxisModule am = new AxisModule();
        am.setName("newModule");
        am.setVersion(new Version("1.0"));
        pf.getModulePM().handleNewModuleAddition(am, "newModule", "1.0");
        Resource re = pf.getModulePM().getModule("newModule", "1.0");
        assertNotNull(re);

        String modulePath = RegistryResources.MODULES + "newModule" + File.separator
                + "1.0" + File.separator;
        Resource resource = configRegistry.get(modulePath);
        String str1 = resource.getProperty("module.name");
        String str2 = resource.getProperty("module.version");

        assertTrue(str1.equals("newModule"));
        assertTrue(str2.equals("1.0"));
    }

    public void testModuleParameterUpdate() throws Exception {
        AxisModule am = new AxisModule();
        am.setName("Module1");
        Version v = new Version("1.0");
        am.setVersion(v);
        pf.getModulePM().handleNewModuleAddition(am, "Module1", "1.0");

        Parameter parm = new Parameter();
        parm.setName("TestParam");
        parm.setValue("TestValue");

        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace ns = factory.createOMNamespace("http://test.org", "test");
        OMElement Ome = factory.createOMElement("parameter", ns);
        Ome.addAttribute("name", "TestParam", ns);
        Ome.setText("TestValue");
        parm.setParameterElement(Ome);

        pf.getModulePM().updateModuleParameter(am, parm);

        String moduleParamPath = PersistenceUtils.getResourcePath(am)
                + RegistryResources.PARAMETERS + parm.getName();
        Resource modparam = configRegistry.get(moduleParamPath);
        String s1 = modparam.getProperty(RegistryResources.ParameterProperties.NAME);

        assertTrue(s1.equals("TestParam"));
    }

    public void testDeleteService() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("ExistingSvGroup1");
        AxisService asv = new AxisService("ExistingSv1");
        asvGroup.addService(asv);

        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        pf.getServicePM().handleNewServiceAddition(asv);

        pf.getServicePM().deleteService(asv);
        Resource re = pf.getServicePM().getService(asv);
        assertNull(re);
    }

    public void testRemoveServiceParam() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testGD");
        AxisService asv = new AxisService("testServiceD");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        pf.getServicePM().handleNewServiceAddition(asv);

        Parameter parm = new Parameter();
        parm.setName("testParam1");
        parm.setValue("testValue1");
        pf.getServicePM().updateServiceParameter(asv, parm);

        String serviceResourcePath = RegistryResources.SERVICE_GROUPS
                + asv.getAxisServiceGroup().getServiceGroupName()
                + RegistryResources.SERVICES + asv.getName();

        String serviceParamResourcePath = serviceResourcePath + RegistryResources.PARAMETERS
                + parm.getName();

        pf.getServicePM().removeServiceParameter(asv, parm);
        assertFalse(configRegistry.resourceExists(serviceParamResourcePath));
    }

    public void testDeleteServiceGroup() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testSVG");
        AxisService asv = new AxisService("testSvgService");
        asvGroup.addService(asv);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);

        pf.getServiceGroupPM().deleteServiceGroup(asvGroup);
        String sgName = asvGroup.getServiceGroupName();
        String sgResourcePath = RegistryResources.SERVICE_GROUPS + sgName;
        assertFalse(configRegistry.resourceExists(sgResourcePath));
    }

    public void testRemoveModule() throws Exception {
        AxisModule am = new AxisModule();
        am.setName("DModule");
        am.setVersion(new Version("1.0"));
        pf.getModulePM().handleNewModuleAddition(am, "DModule", "1.0");

        pf.getModulePM().removeModule(am);
        String modulePath = RegistryResources.MODULES + "DModule" + File.separator + "1.0";
        assertFalse(configRegistry.resourceExists(modulePath));
    }


    public void testengageModuleForService() throws Exception {
        AxisServiceGroup asvGroup = new AxisServiceGroup(ac);
        asvGroup.setServiceGroupName("testAsvG");
        AxisService asv = new AxisService("testAsv");
        asvGroup.addService(asv);
        AxisModule am = new AxisModule();
        am.setName("Module2");
        Version v = new Version("1.0");
        am.setVersion(v);
        pf.getServiceGroupPM().handleNewServiceGroupAddition(asvGroup);
        pf.getServicePM().handleNewServiceAddition(asv);
        pf.getModulePM().handleNewModuleAddition(am, "Module2", "1.0");
        asv.engageModule(am);
        pf.getServicePM().engageModuleForService(am, asv);

        String servicePath = RegistryResources.SERVICE_GROUPS + asv.getAxisServiceGroup()
                .getServiceGroupName()
                + RegistryResources.SERVICES + asv.getName();
        String s1 = "";
        Association[] as = configRegistry.getAssociations(servicePath,
                RegistryResources.Associations.ENGAGED_MODULES);
        for (Association a : as) {
            s1 = a.getDestinationPath();
        }
        assertTrue(s1.equals(RegistryResources.MODULES + "Module2/1.0"));
    }

}