package org.wso2.carbon.core.test.config;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import org.wso2.carbon.core.CarbonAxisConfigurator;

public class CarbonAxisConfiguratorTest extends TestCase {

    private String basedir;
    private String repoLocation;
    private String weblocation;
    private RegistryServiceCreater grs;

    public void setUp() throws Exception {
        super.setUp();
//        grs = new RegistryServiceCreater();
//        grs.setRegistryService();
//        File file =new File("../org.wso2.carbon.core/src/main");
//        basedir = file.getAbsolutePath();
        repoLocation = "src/test/resources/repository";
        System.setProperty("weblocation","");
        weblocation = System.getProperty("weblocation");
        System.setProperty("axis2.xml",repoLocation+"../axis2.xml");
    }

    public void testTest() throws Exception{
        CarbonAxisConfigurator caconfig = CarbonAxisConfigurator.getInstance();
        caconfig.init(repoLocation,weblocation);
       // caconfig.getAxisConfiguration();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }



    public static Test suite() {
        return new TestSuite(CarbonAxisConfiguratorTest.class);
    }
}
