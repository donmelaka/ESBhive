package org.esbhive.node.addition;

import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esbhive.hp.mgt.HiveProxyServiceAdminStub;
import org.esbhive.login.LoginData;
import org.esbhive.login.RemoteLogin;
import org.esbhive.login.client.AuthenticationExceptionException;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;
import org.esbhive.proxyconf.mgt.xsd.ProEsb;
import org.wso2.carbon.proxyadmin.xsd.ProxyData;
import org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo;

/**
 *
 * @author Piumi
 */
public class Deployer extends Thread {

    private static final Log log = LogFactory.getLog("org.wso2.carbon.ESBhive.AdditionHandler");
    private static RemoteLogin remoteLogin;
    private static String newNodeAddress;
    private static ProxyConfManagerStub myConfStub;
    private static HiveProxyServiceAdminStub myHiveStub;
    private static ProxyConfManagerStub otherConfStub;
    private int nodeCount;
    private String otherAddress;
    private double percentage = 0.5;

    public void setRemoteLogin(RemoteLogin remoteLogin1) {
        remoteLogin = remoteLogin1;
    }

    public RemoteLogin getRemoteLogin() {
        return remoteLogin;
    }

    public void setOtherAddress(String otherAddress1) {
        otherAddress = otherAddress1;
    }

    public String getOtherAddress() {
        return otherAddress;
    }

    public void setNodeCount(int nodeCount1) {
        nodeCount = nodeCount1;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNewNodeAddress(String newNodeAddress1) {
        newNodeAddress = newNodeAddress1;
    }

    public String getNewNodeAddress() {
        return newNodeAddress;
    }

    public void setOtherConfStub(ProxyConfManagerStub otherConfStub1) {
        otherConfStub = otherConfStub1;
    }

    public ProxyConfManagerStub getOtherConfStub() {
        return otherConfStub;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            log.error("Error while sleeping the deploying thread", ex);
        }
        createThisNodeStubs(newNodeAddress);
        getProxies();
    }

    private void createThisNodeStubs(String newAddress) {
        try {
            LoginData myNode = new LoginData();
            myNode.setUserName("admin");
            myNode.setPassWord("admin");
            myNode.setHostNameAndPort(newAddress);
            LoginData loginData = null;
            try {
                loginData = remoteLogin.logIn(myNode);
            } catch (RemoteException ex) {
                log.info("Error while login to own node", ex);
            } catch (AuthenticationExceptionException ex) {
                log.info("Error while login to own node", ex);
            }

            ConfigurationContext ctx = null;
            try {
                ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
            } catch (AxisFault ex) {
                log.info("Can not create context", ex);
            }

            String myHiveServiceEPR = "https://" + newAddress + "/services/" + "HiveProxyServiceAdmin";
            String myConfServiceEPR = "https://" + newAddress + "/services/" + "ProxyConfManager";

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
            log.info("Error while setting cookie to own node", ex);
        }

    }

    private void getProxies() {

        String[] keySet = null;
        int realProxyCount;
        try {
            keySet = otherConfStub.getKeySet();
        } catch (RemoteException ex) {
            log.info("Error while getting the deployed proxies", ex);
        }
        ProEsb proEsb = null;
        org.esbhive.node.mgt.xsd.ESBNode[] eSBNodes = null;
        for (int i = 0; i < keySet.length; i++) {
            try {
                proEsb = otherConfStub.getProEsb(keySet[i]);
                eSBNodes = proEsb.getESBNodes();
                realProxyCount = eSBNodes.length;
                boolean isDummy = checkStatus(realProxyCount);
                myHiveStub.addProxyInNewNode(isDummy, proEsb.getProxyData(), newNodeAddress, otherAddress);
            } catch (RemoteException ex) {
                log.info("Error while deploying the proxies", ex);
            }
        }
        updateProxyConf(keySet);
    }

    private void updateProxyConf(String[] keySet) {
        ProEsb proEsb = null;
        org.esbhive.node.mgt.xsd.ESBNode[] eSBNodes = null;
        for (int i = 0; i < keySet.length; i++) {
            try {
                proEsb = otherConfStub.getProEsb(keySet[i]);
                eSBNodes = proEsb.getESBNodes();
                myConfStub.addProxyConf(this.setNewProxyData(proEsb.getProxyData()), eSBNodes);
            } catch (RemoteException ex) {
                log.info("Error while updating Proxy Conf Manager", ex);
            }
        }

    }

    private boolean checkStatus(int realCount) {
        int totalNodeCount = nodeCount + 1;
        int requiredRealProxyCount = (int) Math.floor(totalNodeCount * percentage);
        if (realCount < requiredRealProxyCount) {
            return false;
        } else {
            return true;
        }
    }

    private org.wso2.carbon.proxyadmin.xsd.ProxyData setNewProxyData(ProxyData pd1) {
        org.wso2.carbon.proxyadmin.xsd.ProxyData xsdProxyData = new org.wso2.carbon.proxyadmin.xsd.ProxyData();
        xsdProxyData.setEndpointKey(pd1.getEndpointKey());
        xsdProxyData.setEndpointXML(pd1.getEndpointXML());
        xsdProxyData.setFaultSeqKey(pd1.getFaultSeqKey());
        xsdProxyData.setFaultSeqXML(pd1.getFaultSeqXML());
        xsdProxyData.setInSeqKey(pd1.getInSeqKey());
        xsdProxyData.setInSeqXML(pd1.getInSeqXML());
        xsdProxyData.setName(pd1.getName());
        xsdProxyData.setOutSeqKey(pd1.getOutSeqKey());

        xsdProxyData.setOutSeqXML(pd1.getOutSeqXML());
        xsdProxyData.setPinnedServers(pd1.getPinnedServers());

        ProxyServicePolicyInfo[] policies = pd1.getPolicies();
        org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo[] temp;
        org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo obj;

        if (policies != null) {
            temp = new org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo[policies.length];
            obj = new org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo();

            int i = 0;
            if (obj != null) {
                for (ProxyServicePolicyInfo pspi : policies) {
                    if (pspi != null) {
                        obj.setKey(pspi.getKey());
                        obj.setOperationNS(pspi.getOperationNS());
                        obj.setOperationName(pspi.getOperationName());
                        obj.setType(pspi.getType());
                        temp[i] = obj;
                        i++;
                    }
                }
            }

        } else {
            temp = null;
        }


        xsdProxyData.setPolicies(temp);
        xsdProxyData.setServiceGroup(pd1.getServiceGroup());

        org.wso2.carbon.proxyadmin.xsd.Entry[] serviceParams = pd1.getServiceParams();

        org.wso2.carbon.proxyadmin.xsd.Entry[] temp2;
        org.wso2.carbon.proxyadmin.xsd.Entry obj2;
        if (serviceParams != null) {
            temp2 = new org.wso2.carbon.proxyadmin.xsd.Entry[serviceParams.length];
            obj2 = new org.wso2.carbon.proxyadmin.xsd.Entry();

            int j = 0;
            for (org.wso2.carbon.proxyadmin.xsd.Entry entry : serviceParams) {
                if(entry!=null){
                obj2.setKey(entry.getKey());
                obj2.setValue(entry.getValue());
                temp2[j] = obj2;
                j++;
                }
            }
        } else {
            temp2 = null;
        }

        xsdProxyData.setServiceParams(temp2);

        xsdProxyData.setTransports(pd1.getTransports());
        xsdProxyData.setWsdlDef(pd1.getWsdlDef());
        xsdProxyData.setWsdlKey(pd1.getWsdlKey());

        org.wso2.carbon.proxyadmin.xsd.Entry[] wsdlResources = pd1.getWsdlResources();
        org.wso2.carbon.proxyadmin.xsd.Entry[] temp3;
        org.wso2.carbon.proxyadmin.xsd.Entry obj3;
        if (wsdlResources != null) {
            temp3 = new org.wso2.carbon.proxyadmin.xsd.Entry[wsdlResources.length];
            obj3 = new org.wso2.carbon.proxyadmin.xsd.Entry();
            int k = 0;
            for (org.wso2.carbon.proxyadmin.xsd.Entry entry : wsdlResources) {
                if(entry!=null){
                obj3.setKey(entry.getKey());
                obj3.setValue(entry.getValue());
                temp3[k] = obj3;
                k++;
                }
            }
        } else {
            temp3 = null;
        }

        xsdProxyData.setWsdlResources(temp3);
        xsdProxyData.setWsdlURI(pd1.getWsdlURI());

        return xsdProxyData;

    }
}
