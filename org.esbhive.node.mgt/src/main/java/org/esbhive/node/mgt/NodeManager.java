package org.esbhive.node.mgt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.esbhive.node.mgt.data.ESBNode;
import org.esbhive.node.mgt.authenticator.proxy.AuthenticationAdminStub;
import org.esbhive.node.mgt.authenticator.proxy.AuthenticationExceptionException;

/**
 * Hello world!
 *
 */

//TODO after getting list tell everyone
public class NodeManager {

	private Map<String, ESBNode> nodes;

	public NodeManager() {

		nodes = new HashMap<String,ESBNode>();

	}

	//this is called by me to add myself to one esb in the hive
	public ESBNode[] addNode(ESBNode me, ESBNode addto) throws RemoteException, AuthenticationExceptionException, UnknownHostException {

		ConfigurationContext ctx =
						ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

		String cookie = getCookie(addto,ctx);

		String serviceEPR4 = "https://" + addto.getIp() + "/services/" + "EsbNodeManager";
		EsbNodeManagerStub stub4 = new EsbNodeManagerStub(ctx, serviceEPR4);
		ServiceClient client4 = stub4._getServiceClient();
		Options option = client4.getOptions();
		option.setManageSession(true);
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);

		String myIp = InetAddress.getLocalHost().getHostAddress();

		org.esbhive.node.mgt.data.xsd.ESBNode meInOtherFormat = new org.esbhive.node.mgt.data.xsd.ESBNode();
		meInOtherFormat.setIp(myIp+":"+me.getIp());
		meInOtherFormat.setUsername(me.getUsername());
		meInOtherFormat.setPassword(me.getPassword());
		org.esbhive.node.mgt.data.xsd.ESBNode[] a = stub4.addNodeAndGetNodes(meInOtherFormat);
		MessageContext currentMessageContext = org.apache.axis2.context.MessageContext.getCurrentMessageContext();

		nodes = new HashMap<String, ESBNode>();
		currentMessageContext.getConfigurationContext().setProperty("ESBNodeList", nodes);
		
		HashMap property = (HashMap<String, ESBNode>) org.apache.axis2.context.MessageContext.getCurrentMessageContext().getConfigurationContext().getProperty("ESBNodeList");

		
		for (int i = 0; i < a.length; i++) {
			ESBNode tempNode = new ESBNode(a[i].getIp(), a[i].getUsername(), a[i].getPassword());
			property.put(tempNode.getIp(), tempNode);

		}


		org.apache.axis2.context.MessageContext.getCurrentMessageContext().getConfigurationContext().setProperty("ESBNodeList", property);
		ESBNode[] nodeArray = new ESBNode[property.size()];
		property.values().toArray(nodeArray);

		logOut(addto, ctx);
		return nodeArray;

	}

	//this is called by another backend to add him to me(this is called in the addnode method)
	public ESBNode[] addNodeAndGetNodes(ESBNode node) throws AxisFault, RemoteException, AuthenticationExceptionException {
		if(!checkCredentials(node)){
			return null;
		}
		
		MessageContext currentMessageContext = org.apache.axis2.context.MessageContext.getCurrentMessageContext();
		if ((HashMap) currentMessageContext.getConfigurationContext().getProperty("ESBNodeList") == null) {
			nodes = new HashMap<String, ESBNode>();
			currentMessageContext.getConfigurationContext().setProperty("ESBNodeList", nodes);
		}


		HashMap<String,ESBNode> property = (HashMap<String,ESBNode>) org.apache.axis2.context.MessageContext.getCurrentMessageContext().getConfigurationContext().getProperty("ESBNodeList");
		if (node != null) {
			
			property.put(node.getIp(),node);
			org.apache.axis2.context.MessageContext.getCurrentMessageContext().getConfigurationContext().setProperty("ESBNodeList", property);
			
		}
		ESBNode[] nodeArray = new ESBNode[property.size()];
		property.values().toArray(nodeArray);
		return nodeArray;
	}

	public ESBNode[] getNodes() {
		MessageContext currentMessageContext = org.apache.axis2.context.MessageContext.getCurrentMessageContext();
		if ((HashMap) currentMessageContext.getConfigurationContext().getProperty("ESBNodeList") == null) {
			nodes = new HashMap<String, ESBNode>();
			currentMessageContext.getConfigurationContext().setProperty("ESBNodeList", nodes);
		}

		HashMap<String, ESBNode> property = (HashMap<String, ESBNode>) org.apache.axis2.context.MessageContext.getCurrentMessageContext().getConfigurationContext().getProperty("ESBNodeList");
		ESBNode[] nodeArray = new ESBNode[property.size()];
		property.values().toArray(nodeArray);
		return nodeArray;
	}

	private String getCookie(ESBNode addto,ConfigurationContext ctx) throws AxisFault, RemoteException, AuthenticationExceptionException{
		String serviceEPR = "https://" + addto.getIp() + "/services/" + "AuthenticationAdmin";
		AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);

		ServiceClient client = stub._getServiceClient();
		Options options = client.getOptions();
		options.setManageSession(true);

		boolean isLogged = stub.login(addto.getUsername(), addto.getPassword(), addto.getIp());

		String cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
						HTTPConstants.COOKIE_STRING);
		return cookie;
	}

	private void logOut (ESBNode addto,ConfigurationContext ctx) throws AxisFault, RemoteException, AuthenticationExceptionException{
		String serviceEPR = "https://" + addto.getIp() + "/services/" + "AuthenticationAdmin";
		AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);

		ServiceClient client = stub._getServiceClient();
		Options options = client.getOptions();
		options.setManageSession(true);

		boolean isLogged = stub.login(addto.getUsername(), addto.getPassword(), addto.getIp());
		if(isLogged){
			stub.logout();
		}
	}

	private boolean checkCredentials(ESBNode newcomer) throws AxisFault, RemoteException, AuthenticationExceptionException{
		ConfigurationContext ctx =
						ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);;
		String serviceEPR = "https://" + newcomer.getIp() + "/services/" + "AuthenticationAdmin";
		AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);

		ServiceClient client = stub._getServiceClient();
		Options options = client.getOptions();
		options.setManageSession(true);

		boolean isLogged = stub.login(newcomer.getUsername(), newcomer.getPassword(), newcomer.getIp());
		stub.logout();
		return isLogged;
	}
}
