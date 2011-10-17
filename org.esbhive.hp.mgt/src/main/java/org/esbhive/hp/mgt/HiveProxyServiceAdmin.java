/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.hp.mgt;


import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import org.esbhive.proxyconf.mgt.xsd.ProEsb;
import org.wso2.carbon.utils.ServerConfiguration;

import org.wso2.carbon.proxyadmin.MetaData;
import org.wso2.carbon.proxyadmin.ProxyAdminException;
import org.wso2.carbon.proxyadmin.ProxyData;
import org.wso2.carbon.proxyadmin.ProxyServicePolicyInfo;

import org.wso2.carbon.proxyadmin.Entry;
import org.apache.axis2.context.ConfigurationContext;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContextFactory;

import org.esbhive.node.mgt.ESBNode;
import org.esbhive.node.mgt.NodeManagerInterface;
import org.wso2.carbon.utils.ServerConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esbhive.login.LoginData;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;
import org.esbhive.login.RemoteLogin;
import org.esbhive.hivestat.HiveStatInterface;

import org.wso2.carbon.service.mgt.ui.ServiceAdminStub;

/**
 * @scr.component name="hp.manager" immediate="true"
 * @scr.reference name="esbhive.node.service" interface="org.esbhive.node.mgt.NodeManagerInterface"
 * cardinality="1..1" policy="dynamic" bind="setNodeManager"  unbind="unsetNodeManager"
 * @scr.reference name="esbhive.login.service" interface="org.esbhive.login.RemoteLogin"
 * cardinality="1..1" policy="dynamic" bind="setRemoteLogin"  unbind="unSetRemoteLogin"
 * @scr.reference name="HiveStat" interface="org.esbhive.hivestat.HiveStatInterface"
 * cardinality="1..1" policy="dynamic" bind="setHiveStat"  unbind="unSetHiveStat"
 * @scr.reference name="org.wso2.carbon.serverConfig" interface="org.wso2.carbon.utils.ServerConfiguration"
 * cardinality="1..1" policy="dynamic" bind="setServerConfiguration" unbind="unsetServerConfiguration"
 */
@SuppressWarnings({"UnusedDeclaration"})
public class HiveProxyServiceAdmin {

    private static String SUCCESSFUL = "successful";
    private static String FAILED = "failed";
    private static NodeManagerInterface nodeManager;
    private static HiveStatInterface stat;
    private static final Log log2 = LogFactory.getLog("org.wso2.carbon.HiveProxyServiceAdmin");
    private static RemoteLogin remoteLogin;
    private static String ipAddress = System.getProperty(ServerConstants.LOCAL_IP_ADDRESS);
    private static ServerConfiguration serverConfig;

    
    public synchronized void setNodeManager(NodeManagerInterface r) {
        nodeManager = r;

    }

    public synchronized void unsetNodeManager(NodeManagerInterface r) {
        nodeManager = null;
    }

    protected void setRemoteLogin(RemoteLogin rl) {
        remoteLogin = rl;
    }

    protected void unSetRemoteLogin(RemoteLogin rl) {
        remoteLogin = null;
    }

    protected void setHiveStat(HiveStatInterface hl) {
        stat = hl;
    }

    protected void unSetHiveStat(HiveStatInterface hl) {
        stat = null;
    }

    protected void setServerConfiguration(ServerConfiguration sc) {
        serverConfig = sc;
    }

    protected void unsetServerConfiguration(ServerConfiguration sc) {
        serverConfig = null;
    }

    /**
     * Enables statistics for the specified proxy service
     *
     * @param proxyName name of the proxy service name of which the statistics need to be enabled
     * @throws ProxyAdminException in case of a failure in enabling statistics
     * @return SUCCESSFUL on success or FAILED otherwise
     */
    public String enableStatistics(String proxyName) throws ProxyAdminException { 
        String port = System.getProperty("carbon.https.port");
        String[] credentials = getCredentials();
        String enableStatics = "";
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            enableStatics = proxyServiceAdminStub.enableStatistics(proxyName);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while enableStatistics", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while enableStatistics", ex);
        }
        return enableStatics;
    }

    /**
     * Disables statistics for the specified proxy servivce
     *
     * @param proxyName name of the proxy service of which statistics need to be disabled
     * @throws ProxyAdminException in case of a failure in disabling statistics
     * @return SUCCESSFUL on success or FAILED otherwise
     */
    public String disableStatistics(String proxyName) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String disableStatics = "";
        String[] credentials = getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            disableStatics = proxyServiceAdminStub.disableStatistics(proxyName);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while disableStatistics", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while disableStatistics", ex);
        }
        return disableStatics;

    }

    /**
     * Enables tracing for the specified proxy service
     *
     * @param proxyName name of the the proxy service of which tracing needs to be enabled
     * @throws ProxyAdminException in case of a failure in enabling tracing
     * @return SUCCESSFUL on success or FAILED otherwise
     */
    public String enableTracing(String proxyName) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String enableTracing = "";
        String[] credentials = getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            enableTracing = proxyServiceAdminStub.enableTracing(proxyName);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while enableTracing", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while enableTracing", ex);
        }
        return enableTracing;
    }

    /**
     * Disables tracing for the specified proxy service
     *
     * @param proxyName name of the proxy service of which tracing needs to be disabled
     * @throws ProxyAdminException in case of a failure in disabling tracing
     * @return SUCCESSFUL if the operation is successful and FAILED if it is failed
     */
    public String disableTracing(String proxyName) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String disableTracing = "";
        String[] credentials = getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            disableTracing = proxyServiceAdminStub.disableTracing(proxyName);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while disableTracing", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while disableTracing", ex);
        }
        return disableTracing;
    }

   
    private ProxyServiceAdminStub createProxyServiceAdminStub(String username, String password, String ipAndPort) {

        LoginData otherNode = new LoginData();
        otherNode.setUserName(username);
        otherNode.setPassWord(password);
        otherNode.setHostNameAndPort(ipAndPort);
        LoginData loginData = null;
        try {
            loginData = remoteLogin.logIn(otherNode);
        } catch (AxisFault ex) {
            log2.error("AxisFault in HiveProxyServiceAdmin when login ", ex);
        } catch (RemoteException ex) {
            log2.error("Remote exception in HiveProxyServiceAdmin when login ", ex);
        } catch (org.esbhive.login.client.AuthenticationExceptionException ex) {
            log2.error("AuthenticationExceptionException in HiveProxyServiceAdmin when login ", ex);
        }


        ConfigurationContext ctx = null;
        try {
            ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        } catch (AxisFault ex) {
            Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }

        String serviceEPR4 = "https://" + ipAndPort + "/services/" + "ProxyServiceAdmin";

        ProxyServiceAdminStub stub4 = null;
        try {
            stub4 = new ProxyServiceAdminStub(ctx, serviceEPR4);
        } catch (AxisFault ex) {
            log2.error("AxisFault in HiveProxyServiceAdmin", ex);
        }
        ServiceClient client4 = stub4._getServiceClient();
        Options option = client4.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, loginData.getCookie());
        return stub4;
    }

    private ProxyConfManagerStub createProxyConfManagerStub(String username, String password, String ipAndPort) {

        LoginData otherNode = new LoginData();
        otherNode.setUserName(username);
        otherNode.setPassWord(password);
        otherNode.setHostNameAndPort(ipAndPort);
        LoginData loginData = null;
        try {
            loginData = remoteLogin.logIn(otherNode);
        } catch (AxisFault ex) {
            log2.error("AxisFault in HiveProxyServiceAdmin when login ", ex);
        } catch (RemoteException ex) {
            log2.error("Remote exception in HiveProxyServiceAdmin when login ", ex);
        } catch (org.esbhive.login.client.AuthenticationExceptionException ex) {
            log2.error("AuthenticationExceptionException in HiveProxyServiceAdmin when login ", ex);
        }


        ConfigurationContext ctx = null;
        try {
            ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        } catch (AxisFault ex) {
            Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }

        String serviceEPR4 = "https://" + ipAndPort + "/services/" + "ProxyConfManager";

        ProxyConfManagerStub stub4 = null;
        try {
            stub4 = new ProxyConfManagerStub(ctx, serviceEPR4);
        } catch (AxisFault ex) {
            log2.error("AxisFault in HiveProxyServiceAdmin", ex);
        }
        ServiceClient client4 = stub4._getServiceClient();
        Options option = client4.getOptions();
        option.setManageSession(true);
        option.secarbon-parenttProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, loginData.getCookie());
        return stub4;
    }

    private String[] getCredentials() {
        String[] credintials = new String[2];
        String userName = serverConfig.getFirstProperty("ESBhive.UserName");
        String pswd = serverConfig.getFirstProperty("ESBhive.PassWord");
        credintials[0] = userName;
        credintials[1] = pswd;
        return credintials;

    }

    

    /**
     * Deletes a proxy service from the synapse configuration
     *
     * @param proxyName name of the proxy service which needs to be deleted
     * @throws ProxyAdminException if the proxy service name given is not existent in the
     *                   synapse configuration
     * @return SUCCESSFUL on success or FAILED otherwise
     */
    public String deleteProxyService(String proxyName) throws ProxyAdminException {

        String[] serviceNames = new String[1];
        serviceNames[0] = proxyName;
        ESBNode[] nodes = null;
        if (nodeManager != null) {
            nodes = nodeManager.getNodes();
        }

        for (ESBNode esbNode : nodes) {
            ServiceAdminStub serviceAdminStub = this.createServiceAdminStub(esbNode.getUsername(), esbNode.getPassword(), esbNode.getIpAndPort());
            try {
                serviceAdminStub.deleteServiceGroups(serviceNames);
            } catch (RemoteException ex) {
                log2.error("HiveProxyServiceAdmin RemoteException while deleting service groups", ex);
            }
        }
        /////////////////////

        for (ESBNode esbNode : nodes) {
            ProxyConfManagerStub proxyConftub = this.createProxyConfManagerStub(esbNode.getUsername(), esbNode.getPassword(), esbNode.getIpAndPort());
            try {
                proxyConftub.deleteProxy(proxyName);
            } catch (RemoteException ex) {
                log2.error("HiveProxyServiceAdmin RemoteException while deleting service groups", ex);
            }
        }





        return FAILED;
    }

  

    /**
     * Get the available transport names from the AxisConfiguration
     *
     * @return String array of available transport names
     */
    public String[] getAvailableTransports() throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String[] availableTransports = null;
        String[] credentials = this.getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            availableTransports = proxyServiceAdminStub.getAvailableTransports();
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while getAvailableTransports", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while getAvailableTransports", ex);
        }
        return availableTransports;


    }

    /**
     * Get the available sequences from the SynapseConfiguration
     *
     * @return String array of available sequence names
     * @throws ProxyAdminException if there is an error
     */
    public String[] getAvailableSequences() throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String[] availableSequences = null;
        String[] credentials = this.getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            availableSequences = proxyServiceAdminStub.getAvailableSequences();
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while getAvailableSequences", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while getAvailableSequences", ex);
        }
        return availableSequences;
    }

    /**
     * Get the available endpoints from the SynapseConfiguration
     *
     * @return String array of available endpoint names
     * @throws ProxyAdminException if there is an error
     */
    public String[] getAvailableEndpoints() throws ProxyAdminException {

        String port = System.getProperty("carbon.https.port");
        String[] getAvailableEndPoints = null;
        String[] credentials = this.getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            getAvailableEndPoints = proxyServiceAdminStub.getAvailableEndpoints();
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while getAvailableEndpoints", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while getAvailableEndpoints", ex);
        }
        return getAvailableEndPoints;

    }

    /**
     * Gets the endpoint object defined under the given name
     *
     * @param name the name of the endpoint
     * @return endpoint configuration related with the name
     * @throws ProxyAdminException if the endpoint is not found for the given name
     */
    public String getEndpoint(String name) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String getEndpoint = null;
        String[] credentials = this.getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            getEndpoint = proxyServiceAdminStub.getEndpoint(name);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while getEndpoint", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while getEndpoint", ex);
        }
        return getEndpoint;
    }

    /**
     * Encapsulates the available transports, endpoints, and sequences into a single two dimensional array
     * @return  A two dimensional array containing the set of transports, endpoints, and sequences
     * under 0,1, and 2 indices.
     * @throws ProxyAdminException
     */
    public MetaData getMetaData() throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String[] credentials = getCredentials();
        //String[] credentials = getCredentials(ipAddress + port);
        org.esbhive.hp.mgt.types.carbon.MetaData metaData1 = null;
        MetaData metaData = new MetaData();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            metaData1 = proxyServiceAdminStub.getMetaData();
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while getEndpoint", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while getEndpoint", ex);
        }
        metaData.setEndpoints(metaData1.getEndpoints());
        metaData.setEndpointsAvailable(metaData1.getEndpointsAvailable());
        metaData.setSequences(metaData1.getSequences());
        metaData.setSequencesAvailable(metaData1.getSequencesAvailable());
        metaData.setTransports(metaData1.getTransports());
        metaData.setTransportsAvailable(metaData1.getTransportsAvailable());
        return metaData;

    }

    /**
     * Starts the service specified by the name
     *
     * @param proxyName name of the proxy service which needs to be started
     * @throws ProxyAdminException incase of a failure in starting the service
     * @return SUCCESSFUL on success or FAILED otherwise
     */
    public String startProxyService(String proxyName) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String startProxyService = null;
        String[] credentials = this.getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            startProxyService = proxyServiceAdminStub.startProxyService(proxyName);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while startProxyService", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while startProxyService", ex);
        }
        return startProxyService;

    }

    /**
     * Stops the service specified by the name
     *
     * @param proxyName name of the proxy service which needs to be stoped
     * @throws ProxyAdminException in case of a failure in stopping the service
     * @return SUCCESSFUL on success or FAILED otherwise
     */
    public String stopProxyService(String proxyName) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String stopProxyService = null;
        String[] credentials = this.getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            stopProxyService = proxyServiceAdminStub.stopProxyService(proxyName);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while stopProxyService", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while stopProxyService", ex);
        }
        return stopProxyService;

    }

    /**
     * Redeploying service
     * Removes an existing one,Adds a new one
     *
     * @param proxyName name of the proxy service which needs to be redeployed
     * @throws ProxyAdminException in case of a failure in redeploying the service
     * @return SUCCESSFUL on success or FAILED otherwise
     */
    public String redeployProxyService(String proxyName) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String redeployProxyService = null;
        String[] credentials = this.getCredentials();
        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            redeployProxyService = proxyServiceAdminStub.redeployProxyService(proxyName);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while redeployProxyService", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while redeployProxyService", ex);
        }
        return redeployProxyService;
    }

    public String getSourceView(ProxyData pd) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String getSourceView = null;
        String[] credentials = this.getCredentials();
        ProEsb proEsb = null;
        ProxyServiceAdminStub proxyServiceAdminStub;
        ProxyConfManagerStub stub1 = this.createProxyConfManagerStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            proEsb = stub1.getProEsb(pd.getName());
        } catch (RemoteException ex) {
            this.log2.error("RemoteException in HiveProxyServiceAdmin", ex);
        }
        if (proEsb != null) {
            org.esbhive.node.mgt.xsd.ESBNode[] eSBNodes = proEsb.getESBNodes();
            proxyServiceAdminStub = createProxyServiceAdminStub(eSBNodes[0].getUsername(), eSBNodes[0].getPassword(), eSBNodes[0].getIpAndPort());
        } else {

            proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        }
        try {
            getSourceView = proxyServiceAdminStub.getSourceView(setNewProxyData(pd));
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while stopProxyService", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while stopProxyService", ex);
        }
        return getSourceView;
    }

    public ProxyData getProxy(String proxyName) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String[] credentials = this.getCredentials();
        ProEsb proEsb = null;
        org.wso2.carbon.proxyadmin.ProxyData cproxy = new org.wso2.carbon.proxyadmin.ProxyData();
        org.esbhive.hp.mgt.types.carbon.ProxyData proxy = null;
        ProxyConfManagerStub stub1 = this.createProxyConfManagerStub(credentials[0], credentials[1], ipAddress + ":" + port);

        try {
            proEsb = stub1.getProEsb(proxyName);
        } catch (RemoteException ex) {
            log2.error("RemoteException in HiveProxyServiceAdmin", ex);
        }
        org.esbhive.node.mgt.xsd.ESBNode[] eSBNodes = null;
        ProxyServiceAdminStub proxyServiceAdminStub = null;


        if (proEsb != null) {
            eSBNodes = proEsb.getESBNodes();
            proxyServiceAdminStub = createProxyServiceAdminStub(eSBNodes[0].getUsername(), eSBNodes[0].getPassword(), eSBNodes[0].getIpAndPort());
        } else {

            proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
        }

        try {
            proxy = proxyServiceAdminStub.getProxy(proxyName);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException while stopProxyService", ex);
        } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
            log2.error("HiveProxyServiceAdmin ProxyAdminException while stopProxyService", ex);
        }
        
        
        cproxy.setEnableSecurity(proxy.getEnableSecurity());
        cproxy.setEnableStatistics(proxy.getEnableStatistics());
        cproxy.setEnableTracing(proxy.getEnableTracing());
        cproxy.setEndpointKey(proxy.getEndpointKey());
        cproxy.setEndpointXML(proxy.getEndpointXML());
        cproxy.setFaultSeqKey(proxy.getFaultSeqKey());

        cproxy.setFaultSeqXML(proxy.getFaultSeqXML());

        cproxy.setInSeqKey(proxy.getInSeqKey());
        cproxy.setInSeqXML(proxy.getInSeqXML());
        cproxy.setName(proxy.getName());
        cproxy.setOutSeqKey(proxy.getOutSeqKey());
        cproxy.setOutSeqXML(proxy.getOutSeqXML());
        cproxy.setPinnedServers(proxy.getPinnedServers());



        org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo[] policies = proxy.getPolicies();

        org.wso2.carbon.proxyadmin.ProxyServicePolicyInfo[] temp = new org.wso2.carbon.proxyadmin.ProxyServicePolicyInfo[policies.length];
        org.wso2.carbon.proxyadmin.ProxyServicePolicyInfo obj = new org.wso2.carbon.proxyadmin.ProxyServicePolicyInfo();

        int i = 0;

        for (org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo pspi : policies) {
            if (pspi != null) {
                obj.setKey(pspi.getKey());
                obj.setOperationNS(pspi.getOperationNS());
                obj.setOperationName(pspi.getOperationName());
                obj.setType(pspi.getType());
                temp[i] = obj;
                i++;
            }

        }

        cproxy.setPolicies(temp);
        cproxy.setRunning(proxy.getRunning());
        cproxy.setServiceGroup(proxy.getServiceGroup());

        org.esbhive.hp.mgt.types.carbon.Entry[] serviceParams = proxy.getServiceParams();
        org.wso2.carbon.proxyadmin.Entry[] temp2 = new org.wso2.carbon.proxyadmin.Entry[serviceParams.length];
        org.wso2.carbon.proxyadmin.Entry obj2 = new org.wso2.carbon.proxyadmin.Entry();
        int j = 0;
        for (org.esbhive.hp.mgt.types.carbon.Entry entry : serviceParams) {
            if (entry != null) {
                obj2.setKey(entry.getKey());
                obj2.setValue(entry.getValue());
                temp2[j] = obj2;
                j++;
            }
        }
        cproxy.setServiceParams(temp2);
        cproxy.setStartOnLoad(proxy.getStartOnLoad());
        cproxy.setTransports(proxy.getTransports());
        cproxy.setWsdlAvailable(proxy.getWsdlAvailable());
        cproxy.setWsdlDef(proxy.getWsdlDef());
        cproxy.setWsdlKey(proxy.getWsdlKey());

        org.esbhive.hp.mgt.types.carbon.Entry[] serviceParams2 = proxy.getWsdlResources();
        org.wso2.carbon.proxyadmin.Entry[] temp4 = new org.wso2.carbon.proxyadmin.Entry[serviceParams2.length];
        org.wso2.carbon.proxyadmin.Entry obj4 = new org.wso2.carbon.proxyadmin.Entry();
        int l = 0;
        for (org.esbhive.hp.mgt.types.carbon.Entry entry : serviceParams) {
            if (entry != null) {
                obj4.setKey(entry.getKey());
                obj4.setValue(entry.getValue());
                temp4[j] = obj4;
                l++;
            }
        }
        cproxy.setWsdlResources(temp4);
        cproxy.setWsdlURI(proxy.getWsdlURI());
        return cproxy;
    }


    private ESBNode[] selectBestEsbNodes(double percentage) {
        org.esbhive.node.mgt.ESBNode[] sortedNodes = null;
        int count = 0;
        if (stat != null) {
            sortedNodes = stat.selectBestNodes();
            for (org.esbhive.node.mgt.ESBNode node : sortedNodes) {
                System.out.println(node.toString());
            }
        } else {
            log2.error("HiveStat not set ");
        }

        if (sortedNodes.length == 1) {
            return sortedNodes;
        } else {
            count = (int) Math.floor(sortedNodes.length * percentage);
            ESBNode[] selectedNodes = new ESBNode[count];
            for (int i = 0; i < count; i++) {
                selectedNodes[i] = sortedNodes[i];
            }

            return selectedNodes;
        }
    }

    public String addProxy(ProxyData pd) throws ProxyAdminException {

        try {

            ConfigurationContext ctx =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
            ESBNode[] nodeList = null;

            if (nodeManager != null) {
                nodeList = nodeManager.getNodes();
            } else {
                if (log2.isDebugEnabled()) {
                    log2.debug("Error:: NodeManager is not set ");
                }
            }
           
            String proxyPercentageString = serverConfig.getFirstProperty("ESBhive.ProxyPercentage");
            double proxyPercentageVaule=Double.parseDouble(proxyPercentageString);
            
            ESBNode[] selectedNodes = selectBestEsbNodes(proxyPercentageVaule);
            ProxyServiceAdminStub proxyServiceAdminStub;

            org.esbhive.hp.mgt.types.carbon.ProxyData changedProxyData = setNewProxyData(pd);

            for (ESBNode esbNode : selectedNodes) {
                proxyServiceAdminStub = createProxyServiceAdminStub(esbNode.getUsername(), esbNode.getPassword(), esbNode.getIpAndPort());
                proxyServiceAdminStub.addProxy(changedProxyData);
            }

            ProxyConfManagerStub proxyConfstub = null;

            org.wso2.carbon.proxyadmin.xsd.ProxyData newProxyData = setNewXsdProxyData(pd);


            org.esbhive.node.mgt.xsd.ESBNode[] newEsbNodeList = new org.esbhive.node.mgt.xsd.ESBNode[selectedNodes.length];
            for (int k = 0; k < selectedNodes.length; k++) {
                newEsbNodeList[k] = setNewXsdEsbNode(selectedNodes[k]);
            }

            for (ESBNode tempNode : nodeList) {
                proxyConfstub = this.createProxyConfManagerStub(tempNode.getUsername(), tempNode.getPassword(), tempNode.getIpAndPort());
                proxyConfstub.addProxyConf(newProxyData, newEsbNodeList);
            }

            deployDummyProxies(pd, selectedNodes);
            

        } catch (Exception ex) {
            log2.error("error while adding a proxy in  HiveProxyServiceAdmin", ex);
        }

        return SUCCESSFUL;
    }
    
    public void deleteAllProxiesInNewNode(String newIp){
         ESBNode[] nodeList = null;
        ESBNode newNode = null;
         if (nodeManager != null) {
            nodeList = nodeManager.getNodes();
        } else {
            if (log2.isDebugEnabled()) {
                log2.debug("Error:: NodeManager is not set ");
            }
        }
        for (ESBNode node : nodeList) {
            if (node.getIpAndPort().equals(newIp)) {
                newNode = node;
                break;
            }
        }
        ServiceAdminStub serviceAdminStub = createServiceAdminStub(newNode.getUsername(), newNode.getPassword(), newNode.getIpAndPort());
        try {
            serviceAdminStub.deleteAllNonAdminServiceGroups();
        } catch (RemoteException ex) {
            log2.error("RemoteException in HiveProxyServiceAdmin", ex);
        }
    }

    public String addProxyInNewNode(boolean isDummy, ProxyData pd, String newIp, String oldIp) {
        ESBNode[] nodeList = null;
        ESBNode newNode = null;
        ESBNode oldNode = null;
        org.esbhive.node.mgt.xsd.ESBNode[] newList = new org.esbhive.node.mgt.xsd.ESBNode[1];
        String result = "";
        if (nodeManager != null) {
            nodeList = nodeManager.getNodes();
        } else {
            if (log2.isDebugEnabled()) {
                log2.debug("Error:: NodeManager is not set ");
            }
        }
        for (ESBNode node : nodeList) {
            if (node.getIpAndPort().equals(newIp)) {
                newNode = node;
                break;
            }
        }
        newList[0] = this.setNewXsdEsbNode(newNode);

        for (ESBNode node : nodeList) {
            if (node.getIpAndPort().equals(oldIp)) {
                oldNode = node;
                break;
            }
        }

        ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub(newNode.getUsername(), newNode.getPassword(), newNode.getIpAndPort());
       


        if (isDummy) {
            
            ProxyConfManagerStub poxyConfManagerStub = createProxyConfManagerStub(oldNode.getUsername(), oldNode.getPassword(), oldNode.getIpAndPort());
            org.esbhive.node.mgt.xsd.ESBNode[] eSBNodes = null;

            try {
                eSBNodes = poxyConfManagerStub.getProEsb(pd.getName()).getESBNodes();
            } catch (RemoteException ex) {
                log2.error("RemoteException in HiveProxyServiceAdmin", ex);
            }
            ESBNode[] esbNodes = new ESBNode[eSBNodes.length];

            for (int i = 0; i < eSBNodes.length; i++) {
                esbNodes[i] = this.setNewEsbNode(eSBNodes[i]);

            }
            org.esbhive.hp.mgt.types.carbon.ProxyData dummyProxy = this.createDummyProxy(esbNodes, pd);
            try {
                result = proxyServiceAdminStub.addProxy(dummyProxy);
            } catch (RemoteException ex) {
                log2.error("RemoteException in HiveProxyServiceAdmin", ex);
            } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
                log2.error("ProxyAdminException in HiveProxyServiceAdmin", ex);
            }
        } else {
            try {
                result = proxyServiceAdminStub.addProxy(setNewProxyData(pd));
                ProxyConfManagerStub stub = null;
                for (ESBNode node : nodeList) {
                    stub = this.createProxyConfManagerStub(node.getUsername(), node.getPassword(), node.getIpAndPort());
                    stub.addProxyConf(this.setNewXsdProxyData(pd), newList);
                }

            } catch (RemoteException ex) {
                log2.error("RemoteException in HiveProxyServiceAdmin", ex);
            } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
                log2.error("ProxyAdminException in HiveProxyServiceAdmin", ex);
            }
        }

        return result;
    }

    private org.esbhive.node.mgt.xsd.ESBNode setNewXsdEsbNode(ESBNode node) {
        org.esbhive.node.mgt.xsd.ESBNode newesbnode = new org.esbhive.node.mgt.xsd.ESBNode();
        newesbnode.setIpAndPort(node.getIpAndPort());
        newesbnode.setPassword(node.getPassword());
        newesbnode.setUsername(node.getUsername());
        newesbnode.setHttpsPort(node.getHttpsPort());
        newesbnode.setSynapsePort(node.getSynapsePort());
        newesbnode.setIp(node.getIp());
        return newesbnode;
    }

    private ESBNode setNewEsbNode(org.esbhive.node.mgt.xsd.ESBNode node) {
        ESBNode newesbnode = new ESBNode();
        newesbnode.setIpAndPort(node.getIpAndPort());
        newesbnode.setPassword(node.getPassword());
        newesbnode.setUsername(node.getUsername());
        newesbnode.setHttpsPort(node.getHttpsPort());
        newesbnode.setSynapsePort(node.getSynapsePort());
        newesbnode.setIp(node.getIp());
        return newesbnode;
    }

    private org.wso2.carbon.proxyadmin.xsd.ProxyData setNewXsdProxyData(ProxyData pd1) {
        org.wso2.carbon.proxyadmin.xsd.ProxyData aa = new org.wso2.carbon.proxyadmin.xsd.ProxyData();
        aa.setEndpointKey(pd1.getEndpointKey());
        aa.setEndpointXML(pd1.getEndpointXML());
        aa.setFaultSeqKey(pd1.getFaultSeqKey());
        aa.setFaultSeqXML(pd1.getFaultSeqXML());
        aa.setInSeqKey(pd1.getInSeqKey());
        aa.setInSeqXML(pd1.getInSeqXML());
        aa.setName(pd1.getName());
        aa.setOutSeqKey(pd1.getOutSeqKey());

        aa.setOutSeqXML(pd1.getOutSeqXML());
        aa.setPinnedServers(pd1.getPinnedServers());

        ProxyServicePolicyInfo[] policies = pd1.getPolicies();
        org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo[] temp;
        org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo obj;

        if (policies != null) {
            temp = new org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo[policies.length];
            obj = new org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo();

            int i = 0;
            for (ProxyServicePolicyInfo pspi : policies) {
                obj.setKey(pspi.getKey());
                obj.setOperationNS(pspi.getOperationNS());
                obj.setOperationName(pspi.getOperationName());
                obj.setType(pspi.getType());
                temp[i] = obj;
                i++;
            }
        } else {
            temp = null;
        }


        aa.setPolicies(temp);
        aa.setServiceGroup(pd1.getServiceGroup());

        Entry[] serviceParams = pd1.getServiceParams();

        org.wso2.carbon.proxyadmin.xsd.Entry[] temp2;
        org.wso2.carbon.proxyadmin.xsd.Entry obj2;
        if (serviceParams != null) {
            temp2 = new org.wso2.carbon.proxyadmin.xsd.Entry[serviceParams.length];
            obj2 = new org.wso2.carbon.proxyadmin.xsd.Entry();

            int j = 0;
            for (Entry entry : serviceParams) {
                obj2.setKey(entry.getKey());
                obj2.setValue(entry.getValue());
                temp2[j] = obj2;
                j++;
            }
        } else {
            temp2 = null;
        }

        aa.setServiceParams(temp2);

        aa.setTransports(pd1.getTransports());
        aa.setWsdlDef(pd1.getWsdlDef());
        aa.setWsdlKey(pd1.getWsdlKey());

        Entry[] wsdlResources = pd1.getWsdlResources();
        org.wso2.carbon.proxyadmin.xsd.Entry[] temp3;
        org.wso2.carbon.proxyadmin.xsd.Entry obj3;
        if (wsdlResources != null) {
            temp3 = new org.wso2.carbon.proxyadmin.xsd.Entry[wsdlResources.length];
            obj3 = new org.wso2.carbon.proxyadmin.xsd.Entry();
            int k = 0;
            for (Entry entry : wsdlResources) {
                obj3.setKey(entry.getKey());
                obj3.setValue(entry.getValue());
                temp3[k] = obj3;
                k++;
            }
        } else {
            temp3 = null;
        }

        aa.setWsdlResources(temp3);
        aa.setWsdlURI(pd1.getWsdlURI());

        return aa;

    }

    private org.esbhive.hp.mgt.types.carbon.ProxyData setNewProxyData(ProxyData pd) {
        org.esbhive.hp.mgt.types.carbon.ProxyData a = new org.esbhive.hp.mgt.types.carbon.ProxyData();

        a.setEndpointKey(pd.getEndpointKey());
        a.setEndpointXML(pd.getEndpointXML());
        a.setFaultSeqKey(pd.getFaultSeqKey());
        a.setFaultSeqXML(pd.getFaultSeqXML());
        a.setInSeqKey(pd.getInSeqKey());
        a.setInSeqXML(pd.getInSeqXML());
        a.setName(pd.getName());
        a.setOutSeqKey(pd.getOutSeqKey());

        a.setOutSeqXML(pd.getOutSeqXML());
        a.setPinnedServers(pd.getPinnedServers());

        ProxyServicePolicyInfo[] policies = pd.getPolicies();
        org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo[] temp;
        org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo obj;

        if (policies != null) {
            temp = new org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo[policies.length];
            obj = new org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo();

            int i = 0;
            for (ProxyServicePolicyInfo pspi : policies) {
                obj.setKey(pspi.getKey());
                obj.setOperationNS(pspi.getOperationNS());
                obj.setOperationName(pspi.getOperationName());
                obj.setType(pspi.getType());
                temp[i] = obj;
                i++;
            }
        } else {
            temp = null;
        }


        a.setPolicies(temp);
        a.setServiceGroup(pd.getServiceGroup());

        Entry[] serviceParams = pd.getServiceParams();

        org.esbhive.hp.mgt.types.carbon.Entry[] temp2;
        org.esbhive.hp.mgt.types.carbon.Entry obj2;
        if (serviceParams != null) {
            temp2 = new org.esbhive.hp.mgt.types.carbon.Entry[serviceParams.length];
            obj2 = new org.esbhive.hp.mgt.types.carbon.Entry();

            int j = 0;
            for (Entry entry : serviceParams) {
                obj2.setKey(entry.getKey());
                obj2.setValue(entry.getValue());
                temp2[j] = obj2;
                j++;
            }
        } else {
            temp2 = null;
        }

        a.setServiceParams(temp2);

        a.setTransports(pd.getTransports());
        a.setWsdlDef(pd.getWsdlDef());
        a.setWsdlKey(pd.getWsdlKey());

        Entry[] wsdlResources = pd.getWsdlResources();
        org.esbhive.hp.mgt.types.carbon.Entry[] temp3;
        org.esbhive.hp.mgt.types.carbon.Entry obj3;
        if (wsdlResources != null) {
            temp3 = new org.esbhive.hp.mgt.types.carbon.Entry[wsdlResources.length];
            obj3 = new org.esbhive.hp.mgt.types.carbon.Entry();
            int k = 0;
            for (Entry entry : wsdlResources) {
                obj3.setKey(entry.getKey());
                obj3.setValue(entry.getValue());
                temp3[k] = obj3;
                k++;
            }
        } else {
            temp3 = null;
        }

        a.setWsdlResources(temp3);
        a.setWsdlURI(pd.getWsdlURI());
        return a;

    }

   

    public String modifyProxy(ProxyData pd) throws ProxyAdminException {
        String port = System.getProperty("carbon.https.port");
        String modifyProxy = "";
        String[] credentials = this.getCredentials();
        ProEsb proEsb = null;
        ProxyServiceAdminStub proxyServiceAdminStub = null;
        ProxyConfManagerStub stub1 = this.createProxyConfManagerStub(credentials[0], credentials[1], ipAddress + ":" + port);
        try {
            proEsb = stub1.getProEsb(pd.getName());
        } catch (RemoteException ex) {
            this.log2.error("RemoteException in HiveProxyServiceAdmin", ex);
        }
        if (proEsb != null) {
            org.esbhive.node.mgt.xsd.ESBNode[] eSBNodes = proEsb.getESBNodes();
            for (org.esbhive.node.mgt.xsd.ESBNode node : eSBNodes) {
                proxyServiceAdminStub = this.createProxyServiceAdminStub(node.getUsername(), node.getPassword(), node.getIpAndPort());
                try {
                    modifyProxy = proxyServiceAdminStub.modifyProxy(setNewProxyData(pd));
                } catch (RemoteException ex) {
                    log2.error("RemoteException in HiveProxyServiceAdmin", ex);
                } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
                    log2.error("ProxyAdminException in HiveProxyServiceAdmin", ex);
                }
            }


            ESBNode[] nodeList = null;
            

            String result = "";
            if (nodeManager != null) {
                nodeList = nodeManager.getNodes();
                ProxyConfManagerStub stub2 = null;
                for (ESBNode node : nodeList) {
                    stub2 = this.createProxyConfManagerStub(node.getUsername(), node.getPassword(), node.getIpAndPort());
                    try {
                        stub2.updataProxy(this.setNewXsdProxyData(pd));
                    } catch (RemoteException ex) {
                        log2.error("RemoteException in HiveProxyServiceAdmin", ex);
                    }
                }
            } else {
                if (log2.isDebugEnabled()) {
                    log2.debug("Error:: NodeManager is not set ");
                }
            }




        } else {
            proxyServiceAdminStub = createProxyServiceAdminStub(credentials[0], credentials[1], ipAddress + ":" + port);
            try {
                proxyServiceAdminStub.modifyProxy(setNewProxyData(pd));
            } catch (RemoteException ex) {
                log2.error("RemoteException in HiveProxyServiceAdmin", ex);
            } catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
                log2.error("ProxyAdminException in HiveProxyServiceAdmin", ex);
            }
        }


        return modifyProxy;
    }



    
    private org.esbhive.hp.mgt.types.carbon.ProxyData createDummyProxy(ESBNode[] esbNodes, ProxyData pd) {

        ProxyData dummyProxy = new ProxyData();
        dummyProxy.setStartOnLoad(true);
        dummyProxy.setName(pd.getName());
        String endPoints = "";
        for (ESBNode esbNode : esbNodes) {
            URL url = null;
            try {
                String targetURL = "http://" + esbNode.getIpAndPort().substring(0, esbNode.getIpAndPort().indexOf(':')) + ":" + esbNode.getSynapsePort() + "/services/" + pd.getName();
                url = new URL(targetURL);
            } catch (MalformedURLException ex) {
                Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
            endPoints += "<endpoint>"
                    + "<address uri=\"" + url.toString() + "\">"
                    + "<enableAddressing/>"
                    + "</address>"
                    + "</endpoint>";
        }

        String as = "<inSequence xmlns=\"http://ws.apache.org/ns/synapse\">"
                + "<send>"
                + "<endpoint>"
                + "<session type=\"simpleClientSession\"/>"
                + "<loadbalance>"
                
                + endPoints
                + "</loadbalance>"
                + "</endpoint>"
                + "</send>"
                + "</inSequence>";

        dummyProxy.setInSeqXML(as);
        dummyProxy.setOutSeqXML("<outSequence xmlns=\"http://ws.apache.org/ns/synapse\"><send/></outSequence>");



        dummyProxy.setEndpointXML(dummyProxy.getEndpointXML());

        org.esbhive.hp.mgt.types.carbon.ProxyData a = new org.esbhive.hp.mgt.types.carbon.ProxyData();

        a.setEndpointKey(dummyProxy.getEndpointKey());
        a.setEndpointXML(dummyProxy.getEndpointXML());
        a.setFaultSeqKey(dummyProxy.getFaultSeqKey());
        a.setFaultSeqXML(dummyProxy.getFaultSeqXML());
        a.setInSeqKey(dummyProxy.getInSeqKey());
        a.setInSeqXML(dummyProxy.getInSeqXML());
        a.setName(dummyProxy.getName());
        a.setOutSeqKey(dummyProxy.getOutSeqKey());

        a.setOutSeqXML(dummyProxy.getOutSeqXML());
        a.setPinnedServers(dummyProxy.getPinnedServers());

        ProxyServicePolicyInfo[] policies = dummyProxy.getPolicies();
        org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo[] temp;
        org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo obj;

        if (policies != null) {
            temp = new org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo[policies.length];
            obj = new org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo();

            int i = 0;
            for (ProxyServicePolicyInfo pspi : policies) {
                obj.setKey(pspi.getKey());
                obj.setOperationNS(pspi.getOperationNS());
                obj.setOperationName(pspi.getOperationName());
                obj.setType(pspi.getType());
                temp[i] = obj;
                i++;
            }
        } else {
            temp = null;
        }


        a.setPolicies(temp);
        a.setServiceGroup(dummyProxy.getServiceGroup());

        Entry[] serviceParams = dummyProxy.getServiceParams();

        org.esbhive.hp.mgt.types.carbon.Entry[] temp2;
        org.esbhive.hp.mgt.types.carbon.Entry obj2;
        if (serviceParams != null) {
            temp2 = new org.esbhive.hp.mgt.types.carbon.Entry[serviceParams.length];
            obj2 = new org.esbhive.hp.mgt.types.carbon.Entry();

            int j = 0;
            for (Entry entry : serviceParams) {
                obj2.setKey(entry.getKey());
                obj2.setValue(entry.getValue());
                temp2[j] = obj2;
                j++;
            }
        } else {
            temp2 = null;
        }

        a.setServiceParams(temp2);

        a.setTransports(dummyProxy.getTransports());
        a.setWsdlDef(dummyProxy.getWsdlDef());
        a.setWsdlKey(dummyProxy.getWsdlKey());

        Entry[] wsdlResources = dummyProxy.getWsdlResources();
        org.esbhive.hp.mgt.types.carbon.Entry[] temp3;
        org.esbhive.hp.mgt.types.carbon.Entry obj3;
        if (wsdlResources != null) {
            temp3 = new org.esbhive.hp.mgt.types.carbon.Entry[wsdlResources.length];
            obj3 = new org.esbhive.hp.mgt.types.carbon.Entry();
            int k = 0;
            for (Entry entry : wsdlResources) {
                obj3.setKey(entry.getKey());
                obj3.setValue(entry.getValue());
                temp3[k] = obj3;
                k++;
            }
        } else {
            temp3 = null;
        }

        a.setWsdlResources(temp3);
        a.setWsdlURI(dummyProxy.getWsdlURI());

        return a;
    }


    private void deployDummyProxies(ProxyData pd, ESBNode[] selectedEsbs) {

        ESBNode[] nodeList = null;

        if (nodeManager != null) {
            nodeList = nodeManager.getNodes();
        } else {
            if (log2.isDebugEnabled()) {
                log2.debug("Error:: NodeManager is not set ");
            }
        }
        ArrayList<ESBNode> notSelectedNodes = new ArrayList<ESBNode>();
        notSelectedNodes.addAll(Arrays.asList(nodeList));
        ArrayList<ESBNode> selectedNodes = new ArrayList<ESBNode>();
        selectedNodes.addAll(Arrays.asList(selectedEsbs));
        boolean removeAll = notSelectedNodes.removeAll(selectedNodes);
       

        try {
       
            ESBNode[] tempNodes = new ESBNode[0];
            ESBNode[] notSeletedNodesArray = notSelectedNodes.toArray(tempNodes);
            if (nodeManager != null) {
                nodeList = nodeManager.getNodes();
            } else {
                if (log2.isDebugEnabled()) {
                    log2.debug("Error:: NodeManager is not set ");
                }

            }

            ProxyServiceAdminStub stub4 = null;

            int count = 0;
            for (ESBNode tempNode : notSeletedNodesArray) {
                if (tempNode != null) {
                    stub4 = createProxyServiceAdminStub(tempNode.getUsername(), tempNode.getPassword(), tempNode.getIpAndPort());

                    stub4.addProxy(createDummyProxy(selectedEsbs, pd));

                    if (count == selectedEsbs.length - 1) {
                        count = 0;
                    } else {
                        count++;
                    }
                }
            }
        } catch (Exception ex) {
            log2.error("Error in HiveProxyServiceAdmin", ex);
        }


    }

    private ServiceAdminStub createServiceAdminStub(String username, String password, String ipAndPort) {

        LoginData otherNode = new LoginData();
        otherNode.setUserName(username);
        otherNode.setPassWord(password);
        otherNode.setHostNameAndPort(ipAndPort);
        LoginData loginData = null;
        try {
            loginData = remoteLogin.logIn(otherNode);
        } catch (AxisFault ex) {
            log2.error("HiveProxyServiceAdmin AxisFault when trying to login ", ex);
        } catch (RemoteException ex) {
            log2.error("HiveProxyServiceAdmin RemoteException when trying to login ", ex);
        } catch (org.esbhive.login.client.AuthenticationExceptionException ex) {
            log2.error("HiveProxyServiceAdmin AuthenticationExceptionException when trying to login ", ex);
        }
        ConfigurationContext ctx = null;
        try {
            ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        } catch (AxisFault ex) {
            log2.error("AxisFault in ServiceAdminClient when login ", ex);
        }
        String serviceEPR4 = "https://" + ipAndPort + "/services/" + "ServiceAdmin";

        ServiceAdminStub stub4 = null;
        try {
            stub4 = new ServiceAdminStub(ctx, serviceEPR4);
        } catch (AxisFault ex2) {
            log2.error("AxisFault in ServiceAdminClient", ex2);
        }
        ServiceClient client4 = stub4._getServiceClient();
        Options option = client4.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, loginData.getCookie());
        return stub4;
    }
}
