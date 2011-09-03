/**
 *
 * @author Piumi
 */
package org.esbhive.node.addition;

import org.esbhive.node.mgt.ESBNode;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.esbhive.hp.mgt.HiveProxyServiceAdminStub;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;
import org.esbhive.login.client.AuthenticationExceptionException;
import org.esbhive.login.*;

/**
 * @scr.component name="Node_Addition_Handler" immediate="true"
 * @scr.service interface="org.esbhive.node.addition.NodeAdditionInterface"
 * @scr.reference name="esbhive.login.service" interface="org.esbhive.login.RemoteLogin"
 * cardinality="1..1" policy="dynamic" bind="setRemoteLogin"  unbind="unSetRemoteLogin"
 */
public class AdditionHandler implements NodeAdditionInterface {

    private static ProxyConfManagerStub otherConfStub;
    private static ProxyConfManagerStub myConfStub;
    private static RemoteLogin remoteLogin;
    private static HiveProxyServiceAdminStub myHiveStub;
    private int nodeCount;
    private static String newNodeAddress;
    private static ESBNode[] currentNodeList;
    private String otherAddress;

    protected void setRemoteLogin(RemoteLogin rl) {
        remoteLogin = rl;
    }

    protected void unSetRemoteLogin(RemoteLogin rl) {
        remoteLogin = null;
    }

    public synchronized String setNode(ESBNode newNode, ESBNode[] nodeList) {
        currentNodeList = nodeList;
        newNodeAddress = newNode.getIpAndPort();
        return "New node Parameters were set";
    }

    public synchronized String DeployProxies() {
        nodeCount = currentNodeList.length;
        if (nodeCount != 0) {
            otherAddress = currentNodeList[0].getIpAndPort();
            createOtherNodeStub(otherAddress);
            Deployer dep = new Deployer();
            setParameters(dep);
            dep.start();
        }
        return "New node Set";
    }

    private void setParameters(Deployer dep) {
        dep.setRemoteLogin(remoteLogin);
        dep.setNewNodeAddress(newNodeAddress);
        dep.setOtherConfStub(otherConfStub);
        dep.setNodeCount(nodeCount);
        dep.setOtherAddress(otherAddress);
    }

    private void createOtherNodeStub(String otherAddress) {
        try {

            LoginData otherNode = new LoginData();
            otherNode.setUserName("admin");
            otherNode.setPassWord("admin");
            otherNode.setHostNameAndPort(otherAddress);
            LoginData loginData = null;
            try {
                loginData = remoteLogin.logIn(otherNode);
            } catch (RemoteException ex) {
                Logger.getLogger(AdditionHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AuthenticationExceptionException ex) {
                Logger.getLogger(AdditionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            ConfigurationContext ctx = null;
            try {
                ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
            } catch (AxisFault ex) {
                Logger.getLogger(AdditionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            String otherConfServiceEPR = "https://" + otherAddress + "/services/" + "ProxyConfManager";
            otherConfStub = new ProxyConfManagerStub(ctx, otherConfServiceEPR);
            ServiceClient proConfManagerClient = otherConfStub._getServiceClient();
            Options option = proConfManagerClient.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                    loginData.getCookie());

        } catch (AxisFault ex) {
            Logger.getLogger(AdditionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
