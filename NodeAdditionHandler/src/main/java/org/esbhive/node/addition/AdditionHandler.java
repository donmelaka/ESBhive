/**
 *
 * @author Piumi
 */
package org.esbhive.node.addition;

import org.esbhive.node.mgt.ESBNode;
import org.esbhive.node.mgt.NodeManagerInterface;
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
import org.wso2.carbon.utils.ServerConstants;
import org.esbhive.proxyconf.mgt.xsd.ProxyDataList;
import org.wso2.carbon.proxyadmin.xsd.ProxyData;

/**
 * @scr.component name="Node_Addition_Handler" immediate="true"
 * @scr.service interface="org.esbhive.node.addition.NodeAdditionInterface"
 */
public class AdditionHandler implements NodeAdditionInterface {

    private NodeManagerInterface nodeManager;
    private static ProxyConfManagerStub otherConfStub;
    private static ProxyConfManagerStub myConfStub;
    private static RemoteLogin remoteLogin;
    private static HiveProxyServiceAdminStub myHiveStub;
    private boolean isFixed = false;

    public synchronized void setNodeManager(NodeManagerInterface r) {
        nodeManager = r;
    }

    public synchronized void unsetNodeManager(NodeManagerInterface r) {
        nodeManager = null;
    }

    public synchronized String setNode(String ipAddress, String port, ESBNode[] nodeList) {

        if (nodeManager != null) {
            createOtherNodeStub();
            createThisNodeStubs();
            boolean isOdd = checkSize(nodeList.length);
            String newNode = ipAddress + ":" + port;
            getProxies(nodeList, isOdd, newNode);
        }
        String result = getResult();
        return result;
    }

    private ProxyData[] getProxies(ESBNode[] nodeList, boolean isOdd, String newNode) {
        ProxyData[] pdList = null;
        ProxyDataList pdList0 = null;
        for (int i = 0; i < nodeList.length; i++) {
            try {
                pdList0 = otherConfStub.getProxyDataList(nodeList[i].getIpAndPort());

            } catch (RemoteException ex) {
                Logger.getLogger(AdditionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (pdList0 != null) {
                pdList = pdList0.getProxyDataList();
                redeployProxies(pdList, isOdd, newNode);
            }
        }
        return pdList;
    }

    private void createOtherNodeStub() {
        try {
            if (nodeManager != null) {
                ESBNode[] nodeList = nodeManager.getNodes();
                String otherPort = nodeList[0].getHttpsPort();
                String otherIpAddress = nodeList[0].getIp();
                LoginData otherNode = new LoginData();
                otherNode.setUserName("admin");
                otherNode.setPassWord("admin");
                otherNode.setHostNameAndPort(otherIpAddress + ":" + otherPort);
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

                String otherConfServiceEPR = "https://" + otherIpAddress + ":" + otherPort + "/services/" + "ProxyConfManager";
                otherConfStub = new ProxyConfManagerStub(ctx, otherConfServiceEPR);
                ServiceClient proConfManagerClient = otherConfStub._getServiceClient();
                Options option = proConfManagerClient.getOptions();
                option.setManageSession(true);
                option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                        loginData.getCookie());

            }
        } catch (AxisFault ex) {
            Logger.getLogger(AdditionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void createThisNodeStubs() {
        try {

            String myPort = System.getProperty("carbon.https.port");
            String myIpAddress = System.getProperty(ServerConstants.LOCAL_IP_ADDRESS);
            LoginData myNode = new LoginData();
            myNode.setUserName("admin");
            myNode.setPassWord("admin");
            myNode.setHostNameAndPort(myIpAddress + ":" + myPort);
            LoginData loginData = null;
            try {
                loginData = remoteLogin.logIn(myNode);
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

            String myHiveServiceEPR = "https://" + myIpAddress + ":" + myPort + "/services/" + "HiveProxyServiceAdmin";
            String myConfServiceEPR = "https://" + myIpAddress + ":" + myPort + "/services/" + "ProxyConfManager";



            myHiveStub = new HiveProxyServiceAdminStub(ctx, myHiveServiceEPR);
            myConfStub = new ProxyConfManagerStub(ctx, myConfServiceEPR);

            ServiceClient myHiveProxyAdminClient = myHiveStub._getServiceClient();
            Options option = myHiveProxyAdminClient.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                    loginData.getCookie());

            ServiceClient otherProConfManagerClient = myConfStub._getServiceClient();
            Options option1 = otherProConfManagerClient.getOptions();
            option1.setManageSession(true);
            option1.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                    loginData.getCookie());


        } catch (AxisFault ex) {
            Logger.getLogger(AdditionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void redeployProxies(ProxyData[] pdList, boolean isOdd, String newNode) {

        for (int i = 0; i < pdList.length; i++) {
            try {
                myHiveStub.addProxyInNewNode(isOdd, pdList[i], newNode);
            } catch (RemoteException ex) {
                Logger.getLogger(AdditionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        isFixed = true;
    }

    private boolean checkSize(int length) {
        int result = length % 2;
        return (result != 0);
    }

    private String getResult() {
        if (isFixed = true) {
            return "New node was set sucessfully";
        } else {
            return "New node could not be set.";
        }
    }
}
