package org.esbhive.proconf.mgt;

/**
 *
 * @author jeewantha
 */

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.esbhive.node.mgt.ESBNode;
import org.wso2.carbon.proxyadmin.ProxyData;
import org.esbhive.login.LoginData;
import org.esbhive.login.RemoteLogin;
import  org.esbhive.login.client.AuthenticationExceptionException;
import org.esbhive.proconf.mgt.client.ProConfManagerStub;
import org.eclipse.osgi.framework.console.CommandProvider;
//import org.esbhive.node.mgt.ESBNode;
import org.esbhive.node.mgt.NodeManagerInterface;

//services (objectClass=org.esbhive.*)
/**
 * @scr.component name="node.manager" immediate="true"
 * @scr.reference name="esbhive.node.service" interface="org.esbhive.node.mgt.NodeManagerInterface"
 * cardinality="1..1" policy="dynamic" bind="setNodeManager"  unbind="unsetNodeManager"
 * @scr.reference name="esbhive.login.service" interface="org.esbhive.login.RemoteLogin"
 * cardinality="1..1" policy="dynamic" bind="setRemoteLogin"  unbind="unSetRemoteLogin"

 */



//TODO after getting list tell everyone
public class ProConfManager implements CommandProvider{

	private Map<ProxyData, ESBNode> nodes;
        private NodeManagerInterface nodeManager;
	//private boolean recurse = true;
        private static RemoteLogin remoteLogin;
	public ProConfManager() {

		nodes = new HashMap<ProxyData,ESBNode>();

	}




        protected void setRemoteLogin(RemoteLogin rl){
            remoteLogin = rl;
          }

        protected void unSetRemoteLogin(RemoteLogin rl){
            remoteLogin = null;
          }

        public synchronized void setNodeManager(NodeManagerInterface r) {
		nodeManager = r;
	}
	public synchronized void unsetNodeManager(NodeManagerInterface r) {
		nodeManager = null;
	}

	public void addNodeToMap(ProxyData data, ESBNode esbnode){
            nodes.put(data,esbnode);

        }

        public void deleteNode(String proxyname){
            ProxyData proxydata = null;
            ProxyData [] proxyarray= new ProxyData[nodes.size()];
                    nodes.keySet().toArray(proxyarray);
            for(int i=0;i<nodes.size();i++){
                if(proxyarray[i].getName().equals(proxyname)){
                    proxydata=proxyarray[i];
                    break;
                }
            }
                    nodes.remove(proxydata);
        }

        public ProEsb[] getlist(){
                ProEsb[] proesb = new ProEsb[nodes.size()];
                ProxyData[] prodata = new ProxyData[nodes.size()];
                ESBNode[] esbnodes = new ESBNode[nodes.size()];

		nodes.keySet().toArray(prodata);
                nodes.values().toArray(esbnodes);

                for(int i=0;i<proesb.length;i++){
                    proesb[i].setProxyData(prodata[i]);
                    proesb[i].setESBNode(esbnodes[i]);
                }

            return proesb;
        }
        
        private org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo[] 
        proxydataSetPolicies(org.wso2.carbon.proxyadmin.ProxyServicePolicyInfo[] array)
        {
            org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo[] policyInfo=null;
            for(int i=0;i<array.length;i++){
                policyInfo[i].setKey(array[i].getKey());
                policyInfo[i].setOperationNS(array[i].getOperationNS());
                policyInfo[i].setOperationName(array[i].getOperationName());
                policyInfo[i].setType(array[i].getType());
            }

            return policyInfo;

        }

        private org.wso2.carbon.proxyadmin.xsd.Entry[]
        proxydataSetEntry(org.wso2.carbon.proxyadmin.Entry[] array)
        {
            org.wso2.carbon.proxyadmin.xsd.Entry[] entry=null;
            for(int i=0;i<array.length;i++){
                entry[i].setKey(array[i].getKey());
                entry[i].setValue(array[i].getValue());
            }
            return entry;

        }
        
        private  org.wso2.carbon.proxyadmin.xsd.ProxyData setNewProxyData(ProxyData proxydata){
            org.wso2.carbon.proxyadmin.xsd.ProxyData newproxydata= new org.wso2.carbon.proxyadmin.xsd.ProxyData();
            newproxydata.setEndpointKey(proxydata.getEndpointKey());
            newproxydata.setEndpointXML(proxydata.getEndpointXML());
            newproxydata.setFaultSeqKey(proxydata.getFaultSeqKey());
            newproxydata.setFaultSeqXML(proxydata.getFaultSeqXML());
            newproxydata.setInSeqKey(proxydata.getInSeqKey());
            newproxydata.setInSeqXML(proxydata.getInSeqXML());
            newproxydata.setName(proxydata.getName());
            newproxydata.setOutSeqKey(proxydata.getOutSeqKey());
            newproxydata.setOutSeqXML(proxydata.getOutSeqXML());
            newproxydata.setPinnedServers(proxydata.getPinnedServers());
            newproxydata.setPolicies(proxydataSetPolicies(proxydata.getPolicies()));
            newproxydata.setServiceGroup(proxydata.getServiceGroup());
            newproxydata.setServiceParams(proxydataSetEntry(proxydata.getServiceParams()));
            newproxydata.setTransports(proxydata.getTransports());
            newproxydata.setWsdlDef(proxydata.getWsdlDef());
            newproxydata.setWsdlKey(proxydata.getWsdlKey());
            newproxydata.setWsdlResources(proxydataSetEntry(proxydata.getWsdlResources()));
            newproxydata.setWsdlURI(proxydata.getWsdlURI());
            return newproxydata;

        }
        private org.esbhive.node.mgt.xsd.ESBNode setNewEsbNode(ESBNode node){
        org.esbhive.node.mgt.xsd.ESBNode newesbnode = new org.esbhive.node.mgt.xsd.ESBNode();
        newesbnode.setIpAndPort(node.getIpAndPort());
        newesbnode.setPassword(node.getPassword());
        newesbnode.setUsername(node.getUsername());
        return newesbnode;
        }



        public void addToOthers(ProxyData data, ESBNode esbnode) throws AxisFault{
            org.esbhive.node.mgt.ESBNode[] nodeList = nodeManager.getNodes();
            for(int i=0;i<nodeList.length;i++){
                LoginData otherNode = new LoginData();
                otherNode.setUserName(nodeList[i].getUsername());
                otherNode.setPassWord(nodeList[i].getPassword());
                otherNode.setHostNameAndPort(nodeList[i].getIpAndPort());
                try{
                LoginData loginData = remoteLogin.logIn(otherNode);

                		ConfigurationContext ctx =
						ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);



 		String serviceEPR =
            "https://" + loginData.getHostNameAndPort() + "/services/" + "ProConfManager";
		ProConfManagerStub proConfManagerStub =
            new ProConfManagerStub(ctx, serviceEPR);
		ServiceClient proConfManagerClient = proConfManagerStub._getServiceClient();
		Options option = proConfManagerClient.getOptions();
		option.setManageSession(true);
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                       loginData.getCookie());

               proConfManagerStub.addNodeToMap(setNewProxyData(data), setNewEsbNode(esbnode));
                }
                catch(RemoteException r){


                }
                catch(AuthenticationExceptionException a){}
            }

        }

        public void deleteOthers(String proxyname){
            org.esbhive.node.mgt.ESBNode[] nodeList = nodeManager.getNodes();
            for(int i=0;i<nodeList.length;i++){
                LoginData otherNode = new LoginData();
                otherNode.setUserName(nodeList[i].getUsername());
                otherNode.setPassWord(nodeList[i].getPassword());
                otherNode.setHostNameAndPort(nodeList[i].getIpAndPort());
                try{
                LoginData loginData = remoteLogin.logIn(otherNode);

                		ConfigurationContext ctx =
						ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);



 		String serviceEPR =
            "https://" + loginData.getHostNameAndPort() + "/services/" + "ProConfManager";
		ProConfManagerStub proConfManagerStub =
            new ProConfManagerStub(ctx, serviceEPR);
		ServiceClient proConfManagerClient = proConfManagerStub._getServiceClient();
		Options option = proConfManagerClient.getOptions();
		option.setManageSession(true);
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                       loginData.getCookie());

                proConfManagerStub.deleteNode(proxyname);
                }
                catch(RemoteException r){


                }
                catch(AuthenticationExceptionException a){}
            }

        }

   public String getHelp() {
    return "\tlistnodes - list the currently available nodes";
  }
}
