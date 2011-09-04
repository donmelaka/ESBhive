package org.esbhive.node.addition;

import junit.framework.TestCase;
import org.esbhive.login.RemoteLogin;
import org.esbhive.node.mgt.ESBNode;

/**
 *
 * @author Piumi
 */
public class AdditionHandlerTest extends TestCase {
    
    public AdditionHandlerTest(String testName) {
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
     * Test of setRemoteLogin method, of class AdditionHandler.
     */
    public void testSetRemoteLogin() {
        System.out.println("setRemoteLogin");
        RemoteLogin rl = null;
        AdditionHandler instance = new AdditionHandler();
        instance.setRemoteLogin(rl);

    }

    /**
     * Test of unSetRemoteLogin method, of class AdditionHandler.
     */
    public void testUnSetRemoteLogin() {
        System.out.println("unSetRemoteLogin");
        RemoteLogin rl = null;
        AdditionHandler instance = new AdditionHandler();
        instance.unSetRemoteLogin(rl);

    }

    /**
     * Test of setNode method, of class AdditionHandler.
     */
    public void testSetNode() {
//        System.out.println("setNode");
//        ESBNode newNode = null;
//        ESBNode[] nodeList = null;
//        AdditionHandler instance = new AdditionHandler();
//        String expResult = "";
//        String result = instance.setNode(newNode, nodeList);
//        assertEquals(expResult, result);
 
    }

    /**
     * Test of DeployProxies method, of class AdditionHandler.
     */
    public void testDeployProxies() {
//        System.out.println("DeployProxies");
//        AdditionHandler instance = new AdditionHandler();
//        String expResult = "";
//        instance.setNodeCount(7);
//        String result = instance.DeployProxies();
//        assertEquals(expResult, result);

    }

}
