package org.esbhive.node.mgt;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import org.esbhive.login.LoginData;
import org.esbhive.login.RemoteLogin;

import org.esbhive.login.client.AuthenticationExceptionException;
import org.esbhive.node.mgt.client.EsbNodeManagerStub;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.utils.ConfigurationContextService;

//services (objectClass=org.esbhive.*)
/**
 * @scr.component name="node.manager" immediate="true"
 * @scr.service interface="org.esbhive.node.mgt.NodeManagerInterface"
 * @scr.reference name="esbhive.login.service" interface="org.esbhive.login.RemoteLogin"
 * cardinality="1..1" policy="dynamic" bind="setRemoteLogin"  unbind="unSetRemoteLogin"
 * @scr.reference name="configuration.context.service" interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService" unbind="unsetConfigurationContextService"
 * 
 */
public class NodeManager implements NodeManagerInterface
{
  
	private boolean recurse = true;
  private static RemoteLogin remoteLogin;
  private static ConfigurationContextService configurationContextService;
  private static Map<String, org.esbhive.node.mgt.ESBNode> nodes
                = new HashMap<String, ESBNode>();

	public NodeManager() {

	}

  

  

  protected void setRemoteLogin(RemoteLogin rl){
    remoteLogin = rl;
  }

  protected void unSetRemoteLogin(RemoteLogin rl){
    remoteLogin = null;
  }

    protected void setConfigurationContextService(ConfigurationContextService cfgCtxService) {
        configurationContextService = cfgCtxService;
        
    }

    protected void unsetConfigurationContextService(ConfigurationContextService cfgCtxService) {
        configurationContextService = null;
    }


	//this is called by me to add myself to one esb in the hive
	public org.esbhive.node.mgt.ESBNode[] addNode(org.esbhive.node.mgt.ESBNode me,
                           org.esbhive.node.mgt.ESBNode addto) throws AxisFault, RemoteException, AuthenticationExceptionException
                            {
    String port = System.getProperty("carbon.https.port");
    String ipAddress = System.getProperty(ServerConstants.LOCAL_IP_ADDRESS);
    me.setIp(ipAddress);
    me.setHttpsPort(port);
    me.setIpAndPort(ipAddress+":"+port);
    
    me.setSynapsePort((String) configurationContextService.getServerConfigContext()
            .getAxisConfiguration().getTransportIn("http")
            .getParameter("port").getValue());
    LoginData otherNode = new LoginData();
    otherNode.setUserName(addto.getUsername());
    otherNode.setPassWord(addto.getPassword());
    otherNode.setHostNameAndPort(addto.getIpAndPort());
    LoginData loginData = remoteLogin.logIn(otherNode);

		ConfigurationContext ctx =
						ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);



 		String serviceEPR =
            "https://" + loginData.getHostNameAndPort() + "/services/" + "EsbNodeManager";
		EsbNodeManagerStub nodeManagerStub =
            new EsbNodeManagerStub(ctx, serviceEPR);
		ServiceClient nodeManagerClient = nodeManagerStub._getServiceClient();
		Options option = nodeManagerClient.getOptions();
		option.setManageSession(true);
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                       loginData.getCookie());

  	org.esbhive.node.mgt.client.ESBNode meInOtherFormat
              = new org.esbhive.node.mgt.client.ESBNode();
		meInOtherFormat.setIpAndPort(me.getIpAndPort());
		meInOtherFormat.setUsername(me.getUsername());
		meInOtherFormat.setPassword(me.getPassword());
    meInOtherFormat.setIp(me.getIp());
    meInOtherFormat.setHttpsPort(me.getHttpsPort());
    meInOtherFormat.setSynapsePort(me.getSynapsePort());
		org.esbhive.node.mgt.client.ESBNode[] nodeArray =
            nodeManagerStub.addNodeAndGetNodes(meInOtherFormat);

    
    if(recurse){
      recurse = false;
      for(int i=0;i<nodeArray.length;i++){
        org.esbhive.node.mgt.ESBNode tempNode =
                new org.esbhive.node.mgt.ESBNode(nodeArray[i].getIpAndPort(),
                                                 nodeArray[i].getUsername(),
                                                 nodeArray[i].getPassword(),
                                                 nodeArray[i].getIp(),
                                                 nodeArray[i].getHttpsPort(),
                                                 nodeArray[i].getSynapsePort());
        

        nodes.put(tempNode.getIpAndPort(), tempNode);
        addNode(me, tempNode);
      }
    }
    
		return nodes.values().toArray(new org.esbhive.node.mgt.ESBNode[nodes.size()]);
    

	}

	//this is called by another backend to add him to me(this is called in the addnode method)
	public org.esbhive.node.mgt.ESBNode[] addNodeAndGetNodes(org.esbhive.node.mgt.ESBNode node) throws AxisFault, RemoteException, AuthenticationExceptionException   {
    LoginData loginData = new LoginData();
    loginData.setUserName(node.getUsername());
    loginData.setPassWord(node.getPassword());
    loginData.setHostNameAndPort(node.getIpAndPort());
		remoteLogin.logIn(loginData);
    if(loginData.isLoggedIn()){
      nodes.put(node.getIpAndPort(), node);
    }else{
      return null;
    }
    org.esbhive.node.mgt.ESBNode[] nodeArray = new org.esbhive.node.mgt.ESBNode[nodes.size()];
		return nodes.values().toArray(nodeArray);
	}

	public org.esbhive.node.mgt.ESBNode[] getNodes() {
    
    org.esbhive.node.mgt.ESBNode[] nodeArray = new org.esbhive.node.mgt.ESBNode[nodes.size()];
		return nodes.values().toArray(nodeArray);
	}

 

}
