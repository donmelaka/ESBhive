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
import org.esbhive.login.*;
import org.esbhive.hp.mgt.HiveProxyServiceAdminStub;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;
import org.esbhive.proxyconf.mgt.xsd.ProxyDataList;
import org.wso2.carbon.proxyadmin.xsd.ProxyData;

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
    private static HiveProxyServiceAdminStub hiveStub;
    private static ProxyConfManagerStub confStub;
    private static RemoteLogin remoteLogin;

    protected void setRemoteLogin(RemoteLogin rl) {
        remoteLogin = rl;
    }

    protected void unSetRemoteLogin(RemoteLogin rl) {
        remoteLogin = null;
    }

    public void setFailedNode(String ipAddress, String port) {
        this.failedIp = ipAddress;
        this.failedPort = port;
    }

    public String fixNode(String ipAddress, String port) {
        long startTime = System.currentTimeMillis();
        this.createStub();
        this.setFailedNode(ipAddress, port);
        ProxyData[] proxyDataList = this.getProxyList();
        if (proxyDataList != null && proxyDataList.length != 0) {
            this.deleteProxyServices(proxyDataList);
            this.redeployProxyServices(proxyDataList);
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = (stopTime - startTime);
        String result = this.getResult(elapsedTime);
        this.reset();

        return result;
    }

    public ProxyData[] getProxyList() {
        ProxyData[] pdList = null;
        ProxyDataList pdList0;
        try {
            String ipandport = this.failedIp + ":" + this.failedPort;
            pdList0 = confStub.getProxyDataList(ipandport);
            if (pdList0 != null) {
                pdList = pdList0.getProxyDataList();
            }

        } catch (RemoteException ex) {
            Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pdList;

    }

    public void deleteProxyServices(ProxyData[] pdList) {
        for (int i = 0; i < pdList.length; i++) {
            try {
                hiveStub.deleteProxyService(pdList[i].getName());
            } catch (RemoteException ex) {
                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProxyAdminException ex) {
                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void redeployProxyServices(ProxyData[] pdList) {

        for (int i = 0; i < pdList.length; i++) {
            try {
                hiveStub.addProxy(pdList[i]);
                this.isFixed = true;
            } catch (RemoteException ex) {
                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProxyAdminException ex) {
                Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
            otherNode.setHostNameAndPort(ipAddress + ":" + port);
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

    private String getResult(long elapsedTime ) {
        String result = this.failedIp + ":" + this.failedPort;
        if (isFixed) {
            result = result + " was fixed sucessfully in "+elapsedTime+" miliseconds" ;
        } else {
            result = result + " could not be fixed";
        }

        return result;

    }
}
