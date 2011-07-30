package org.esbhive.fault_handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.esbhive.node.mgt.ESBNode;
import org.wso2.carbon.proxyadmin.ProxyData;
import org.wso2.carbon.utils.ServerConstants;
import org.esbhive.hp.mgt.*;
import org.esbhive.proxyconf.mgt.*;
//import org.esbhive.hp.mgt.HiveProxyServiceAdminStub;
//import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;
//import org.esbhive.proxyconf.mgt.ProEsb;

/**
 *
 * @author Piumi
 */
//services (objectClass=org.esbhive.*)
/**
 * @scr.component name="Fault_Handler" immediate="true"
 * @scr.service interface="org.esbhive.fault_handler.FaultHandlerInterface"
 */
public class FaultHandler implements FaultHandlerInterface {

    private String failedIp;
    private String failedPort;
    private boolean isFixed = false;
    private ESBNode node;
    private ProxyData[] pdList;
//    HiveProxyServiceAdminStub hiveStub ;
//    ProxyConfManagerStub confStub;

    public void setFailedNode(String ipAddress, String port) {
        this.failedIp = ipAddress;
        this.failedPort = port;
    }

    public String fixNode(String ipAddress, String port) {
        this.createStub();
        this.setFailedNode(ipAddress, port);
        this.getProxyList();
        this.deleteProxyServices();
        this.redeployProxyServices();
        String result = this.getResult();
        this.reset();
        return result;
    }

    public void getProxyList() {

        int j = 0;
        String ipandport = this.failedIp + ":" + this.failedPort;
//         ProEsb[] proesb = confStub._getList();
//         this.pdList= new ProxyData[proesb.size];
//         for (i=0;i<proesb.size;i++){
//          if(proesb[i].getESBNode.getIpAndPort==ipandport)
//          {this.pdList[j]=proesb[i].getProxyData();}
//        }

    }

    public void deleteProxyServices() {
        if (pdList != null) {
            for (int i = 0; i < pdList.length; i++) {
                String proxyName = pdList[i].getName();
                // hiveStub._deleteProxyService(proxyName);
            }
        }
    }

    public void redeployProxyServices() {
        if (pdList != null) {
            for (int i = 0; i < pdList.length; i++) {
                // hiveStub._addProxy(pdList[i]);
            }
        }
        this.isFixed = true;
    }

    private void reset() {
        this.failedIp = null;
        this.failedPort = null;

    }

    private void createStub() {
//        try {
        String port = System.getProperty("carbon.https.port");
        String ipAddress = System.getProperty(ServerConstants.LOCAL_IP_ADDRESS);
        ConfigurationContext ctx = null;
        try {
            ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        } catch (AxisFault ex) {
            Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String serviceEPR1 = "https://" + ipAddress + ":" + port + "/services/" + "HiveProxyServiceAdmin";
        String serviceEPR2 = "https://" + ipAddress + ":" + port + "/services/" + "ProxyConfManager";
//            hiveStub = new HiveProxyServiceAdminStub(ctx, serviceEPR1);
//            confStub = new ProxyConfManagerStub(ctx, serviceEPR2);
//        } catch (AxisFault ex) {
//            Logger.getLogger(FaultHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private String getResult() {
        String result = this.failedIp + ":" + this.failedPort;
        if (isFixed) {
            result = result + " was fixed sucessfully";
        } else {
            result = result + " could not be fixed";
        }
        return result;
    }
}
