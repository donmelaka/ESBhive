package org.esbhive.node.addition;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.esbhive.login.RemoteLogin;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;

/**
 *
 * @author Piumi
 */
public class DeployerTest extends TestCase {

    public DeployerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of setRemoteLogin method, of class Deployer.
     */
    public void testSetRemoteLogin() {
        System.out.println("setRemoteLogin");
        RemoteLogin remoteLogin1 = null;
        Deployer instance = new Deployer();
        instance.setRemoteLogin(remoteLogin1);
    }

    /**
     * Test of getRemoteLogin method, of class Deployer.
     */
    public void testGetRemoteLogin() {
        System.out.println("getRemoteLogin");
        Deployer instance = new Deployer();
        RemoteLogin expResult = null;
        instance.setRemoteLogin(expResult);
        RemoteLogin result = instance.getRemoteLogin();
        assertEquals(expResult, instance.getRemoteLogin());
    }

    /**
     * Test of setOtherAddress method, of class Deployer.
     */
    public void testSetOtherAddress() {
        System.out.println("setOtherAddress");
        String otherAddress1 = "";
        Deployer instance = new Deployer();
        instance.setOtherAddress(otherAddress1);
    }

    /**
     * Test of getOtherAddress method, of class Deployer.
     */
    public void testGetOtherAddress() {
        System.out.println("getOtherAddress");
        Deployer instance = new Deployer();
        instance.setOtherAddress("Other Address");
        String expResult = "Other Address";
        String result = instance.getOtherAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNodeCount method, of class Deployer.
     */
    public void testSetNodeCount() {
        System.out.println("setNodeCount");
        int nodeCount1 = 0;
        Deployer instance = new Deployer();
        instance.setNodeCount(nodeCount1);
    }

    /**
     * Test of getNodeCount method, of class Deployer.
     */
    public void testGetNodeCount() {
        System.out.println("getNodeCount");
        Deployer instance = new Deployer();
        int expResult = 0;
        int result = instance.getNodeCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNewNodeAddress method, of class Deployer.
     */
    public void testSetNewNodeAddress() {
        System.out.println("setNewNodeAddress");
        String newNodeAddress1 = "";
        Deployer instance = new Deployer();
        instance.setNewNodeAddress(newNodeAddress1);
    }

    /**
     * Test of getNewNodeAddress method, of class Deployer.
     */
    public void testGetNewNodeAddress() {
        System.out.println("getNewNodeAddress");
        Deployer instance = new Deployer();
        String expResult = "";
        String result = instance.getNewNodeAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOtherConfStub method, of class Deployer.
     */
    public void testSetOtherConfStub() {
        System.out.println("setOtherConfStub");
        ProxyConfManagerStub otherConfStub1 = null;
        Deployer instance = new Deployer();
        instance.setOtherConfStub(otherConfStub1);
    }

    /**
     * Test of getOtherConfStub method, of class Deployer.
     */
    public void testGetOtherConfStub() {
        System.out.println("getOtherConfStub");
        Deployer instance = new Deployer();
        ProxyConfManagerStub expResult = null;
        ProxyConfManagerStub result = instance.getOtherConfStub();
        assertEquals(expResult, result);
    }

    /**
     * Test of run method, of class Deployer.
     */
    public void testRun() {
        System.out.println("run");
        Deployer instance = new Deployer();
        instance.start();
    }

    /**
     * Test of checkStatus method, of class Deployer.
     */
    public void testCheckStatus() {
        Deployer instance = new Deployer();
        Assert.assertTrue(instance.checkStatus(2, 5));
        Assert.assertFalse(instance.checkStatus(2, 7));
    }
}
