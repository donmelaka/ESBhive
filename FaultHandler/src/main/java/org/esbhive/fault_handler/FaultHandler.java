package org.esbhive.fault_handler;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.esbhive.hp.mgt.ProxyAdminException;
import org.esbhive.login.client.AuthenticationExceptionException;
import org.wso2.carbon.utils.ServerConstants;
import org.esbhive.node.mgt.xsd.ESBNode;
import org.esbhive.login.*;
import org.wso2.carbon.core.services.authentication.*;

//import org.esbhive.hp.mgt.*;
import org.esbhive.hp.mgt.HiveProxyServiceAdminStub;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;

/**
 *
 * @author Piumi
 */
//services (objectClass=org.esbhive.*)
/**
 * @scr.component name="Fault_Handler" immediate="true"
 * @scr.service interface="org.esbhive.fault_handler.FaultHandlerInterface"
 * @scr.reference name="esbhive.login.service" interface="org.esbhive.login.RemoteLogin"
 * cardinality="1..1" policy="dynamic" bind="setRemoteLogin"  unbind="unSetRemoteLogin"

 */
public class FaultHandler implements FaultHandlerInterface {

    private String failedIp;
    private String failedPort;
    private boolean isFixed = false;
    private ESBNode node;
    private org.wso2.carbon.proxyadmin.xsd.ProxyData[] pdList;
    static HiveProxyServiceAdminStub hiveStub ;
    static ProxyConfManagerStub confStub;
    String test = null;
    private static RemoteLogin remoteLogin;


        protected void setRemoteLogin(RemoteLogin rl){
            remoteLogin = rl;
          }

        protected void unSetRemoteLogin(RemoteLogin rl){
            remoteLogin = null;
          }

    public void setFailedNode(String ipAddress, String port) {
        this.failedIp = ipAddress;
        this.failedPort = port;
    }

    public String fixNode(String ipAddress, String port) {
        this.createStub();
        this.setFailedNode(ipAddress, port);
        this.getProxyList();
//        this.deleteProxyServices();
//        this.redeployProxyServices();
        String result = this.getResult();
        this.reset();
        return result;
    }

    public void getProxyList() {
//        String port = System.getProperty("carbon.https.port");
//    String ipAddress = System.getProperty(ServerConstants.LOCAL_IP_ADDRESS);
////        HiveProxyServiceAdminStub hiveStub1 ;
////    ProxyConfManagerStub confStub1 = null;
//// try {
////
////            LoginData otherNode = new LoginData();
////                otherNode.setUserName("admin");
////                otherNode.setPassWord("admin");
////                otherNode.setHostNameAndPort(ipAddress+":"+port);
////                LoginData loginData = null;
////            try {
////                 loginData = remoteLogin.logIn(otherNode);
////            } catch (RemoteException ex) {
////                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
////            } catch (AuthenticationExceptionException ex) {
////                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
////            }
////
////
////            } catch (AxisFault ex) {
////                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            String serviceEPR1 = "https://" + ipAddress + ":" + port + "/services/" + "HiveProxyServiceAdmin";
////            String serviceEPR2 = "https://" + ipAddress + ":" + port + "/services/" + "ProxyConfManager";
////
////
////            hiveStub1 = new HiveProxyServiceAdminStub(ctx, serviceEPR1);
////            confStub1 = new ProxyConfManagerStub(ctx, serviceEPR2);
////
////            ServiceClient proConfManagerClient = confStub._getServiceClient();
////		Options option = proConfManagerClient.getOptions();
////		option.setManageSession(true);
////		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
////                       loginData.getCookie());
////
////                ServiceClient hiveProxyAdminClient = hiveStub._getServiceClient();
////		Options option1 = hiveProxyAdminClient.getOptions();
////		option1.setManageSession(true);
////		option1.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
////                       loginData.getCookie());
////
////        } catch (AxisFault ex) {
////            Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
////        }
//
//		String serviceEPR = "https://" + ipAddress+":"+port + "/services/" + "AuthenticationAdmin";
//			// String serviceEPR = "https://" + "localhost:9443" + "/services/" + "AuthenticationAdmin";
//                ConfigurationContext ctx = null;
//
//                ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
//			AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);
//
//			ServiceClient client = stub._getServiceClient();
//			Options options = client.getOptions();
//			options.setManageSession(true);
//			boolean isLogged = stub.login("admin", "admin", ipAddress+":"+port);
//
//			String cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
//				HTTPConstants.COOKIE_STRING);



        try {
             
            int j = 0;
            String ipandport = this.failedIp + ":" + this.failedPort;
            org.esbhive.proxyconf.mgt.xsd.ProEsb[] proesb =new org.esbhive.proxyconf.mgt.xsd.ProEsb[50];
                    proesb= confStub.getlist();
            this.pdList = new org.wso2.carbon.proxyadmin.xsd.ProxyData[proesb.length];
            for (int i=0; i < proesb.length; i++) {
                if (proesb[i].getESBNode().getIpAndPort().equals(ipandport)) {
                    test=test+proesb[i].getESBNode().getIpAndPort();
                    this.pdList[j] = proesb[i].getProxyData();
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.isFixed = true;

    }

    public void deleteProxyServices() {
        try {

            hiveStub.deleteProxyService("StockQuoteProxy");
            this.isFixed = true;
            test="deleted";
//           if (pdList != null) {
//                for (int i = 0; i < pdList.length; i++) {
//                    String proxyName = pdList[i].getName();
//                    // hiveStub._deleteProxyService(proxyName);
//                }
//            }
        } catch (ProxyAdminException ex) {
            Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void redeployProxyServices() {
//        if (pdList != null) {
//            for (int i = 0; i < pdList.length; i++) {
//                // hiveStub._addProxy(pdList[i]);
//            }
//        }
////        this.isFixed = true;
    }

    private void reset() {
        this.failedIp = null;
        this.failedPort = null;

    }

    private void createStub() {
        try {
            String port = System.getProperty("carbon.https.port");
            String ipAddress = System.getProperty(ServerConstants.LOCAL_IP_ADDRESS);
            LoginData otherNode = new LoginData();
                otherNode.setUserName("admin");
                otherNode.setPassWord("admin");
                otherNode.setHostNameAndPort(ipAddress+":"+port);
                LoginData loginData = null;
            try {
                 loginData = remoteLogin.logIn(otherNode);
            } catch (RemoteException ex) {
                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AuthenticationExceptionException ex) {
                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ConfigurationContext ctx = null;
            try {
                ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
            } catch (AxisFault ex) {
                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            String serviceEPR1 = "https://" + ipAddress + ":" + port + "/services/" + "HiveProxyServiceAdmin";
            String serviceEPR2 = "https://" + ipAddress + ":" + port + "/services/" + "ProxyConfManager";


            hiveStub = new HiveProxyServiceAdminStub(ctx, serviceEPR1);
            confStub = new ProxyConfManagerStub(ctx, serviceEPR2);

            ServiceClient proConfManagerClient = confStub._getServiceClient();
		Options option = proConfManagerClient.getOptions();
		option.setManageSession(true);
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                       loginData.getCookie());

                ServiceClient hiveProxyAdminClient = hiveStub._getServiceClient();
		Options option1 = hiveProxyAdminClient.getOptions();
		option1.setManageSession(true);
		option1.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                       loginData.getCookie());

        } catch (AxisFault ex) {
            Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private String getResult() {
        String result = this.failedIp + ":" + this.failedPort;
        if (isFixed) {
            result = result + " was fixed sucessfully";
            result=test;
        } else {
            result = result + " could not be fixed";
        }
        return result;
    }
}
