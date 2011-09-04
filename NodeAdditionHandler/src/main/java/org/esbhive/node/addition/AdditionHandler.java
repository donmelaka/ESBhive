/**
 *
 * @author Piumi
 */
package org.esbhive.node.addition;

import org.esbhive.node.mgt.ESBNode;
import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;
import org.esbhive.login.client.AuthenticationExceptionException;
import org.esbhive.login.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @scr.component name="Node_Addition_Handler" immediate="true"
 * @scr.service interface="org.esbhive.node.addition.NodeAdditionInterface"
 * @scr.reference name="esbhive.login.service" interface="org.esbhive.login.RemoteLogin"
 * cardinality="1..1" policy="dynamic" bind="setRemoteLogin"  unbind="unSetRemoteLogin"
 */
public class AdditionHandler implements NodeAdditionInterface {

    private static final Log log = LogFactory.getLog("org.wso2.carbon.ESBhive.AdditionHandler");
    private static ProxyConfManagerStub otherConfStub;
    private static RemoteLogin remoteLogin;
    private int nodeCount;
    private static String newNodeAddress;
    private static ESBNode[] currentNodeList;
    private String otherAddress;

    /**
     * @return the otherConfStub
     */
    public static ProxyConfManagerStub getOtherConfStub() {
        return otherConfStub;
    }

    /**
     * @param aOtherConfStub the otherConfStub to set
     */
    public static void setOtherConfStub(ProxyConfManagerStub aOtherConfStub) {
        otherConfStub = aOtherConfStub;
    }

    /**
     * @return the newNodeAddress
     */
    public static String getNewNodeAddress() {
        return newNodeAddress;
    }

    /**
     * @param aNewNodeAddress the newNodeAddress to set
     */
    public static void setNewNodeAddress(String aNewNodeAddress) {
        newNodeAddress = aNewNodeAddress;
    }

    /**
     * @return the currentNodeList
     */
    public static ESBNode[] getCurrentNodeList() {
        return currentNodeList;
    }

    /**
     * @param aCurrentNodeList the currentNodeList to set
     */
    public static void setCurrentNodeList(ESBNode[] aCurrentNodeList) {
        currentNodeList = aCurrentNodeList;
    }

    /**
     * @return the nodeCount
     */
    public int getNodeCount() {
        return nodeCount;
    }

    /**
     * @param nodeCount the nodeCount to set
     */
    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    /**
     * @return the otherAddress
     */
    public String getOtherAddress() {
        return otherAddress;
    }

    /**
     * @param otherAddress the otherAddress to set
     */
    public void setOtherAddress(String otherAddress) {
        this.otherAddress = otherAddress;
    }

    protected void setRemoteLogin(RemoteLogin rl) {
        remoteLogin = rl;
    }

    protected void unSetRemoteLogin(RemoteLogin rl) {
        remoteLogin = null;
    }

    public synchronized String setNode(ESBNode newNode, ESBNode[] nodeList) {
        setCurrentNodeList(nodeList);
        setNewNodeAddress(newNode.getIpAndPort());
        return "New node Parameters were set";
    }

    public synchronized String DeployProxies() {
        if (getCurrentNodeList()!= null && getNewNodeAddress()!=null){
            setNodeCount(getCurrentNodeList().length);
        if (getNodeCount() != 0) {
            setOtherAddress(getCurrentNodeList()[0].getIpAndPort());
            createOtherNodeStub(getOtherAddress());
            Deployer dep = new Deployer();
            setParameters(dep);
            dep.start();
        }}
        return "New node Set";
    }



    private void setParameters(Deployer dep) {
        dep.setRemoteLogin(remoteLogin);
        dep.setNewNodeAddress(getNewNodeAddress());
        dep.setOtherConfStub(getOtherConfStub());
        dep.setNodeCount(getNodeCount());
        dep.setOtherAddress(getOtherAddress());
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
                log.info("Error while login to  "+otherAddress, ex);
            } catch (AuthenticationExceptionException ex) {
                log.info("Error while login to  "+otherAddress, ex);
            }

            ConfigurationContext ctx = null;
            try {
                ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
            } catch (AxisFault ex) {
                log.info("Can not create configuration context", ex);
            }

            String otherConfServiceEPR = "https://" + otherAddress + "/services/" + "ProxyConfManager";
            setOtherConfStub(new ProxyConfManagerStub(ctx, otherConfServiceEPR));
            ServiceClient proConfManagerClient = getOtherConfStub()._getServiceClient();
            Options option = proConfManagerClient.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                    loginData.getCookie());

        } catch (AxisFault ex) {
           log.info("Error while setting cookie to "+otherAddress, ex);
        }

    }

    
}
