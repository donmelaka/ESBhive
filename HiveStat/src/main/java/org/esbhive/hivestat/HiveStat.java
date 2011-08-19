package org.esbhive.hivestat;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import java.util.ArrayList;
import org.esbhive.login.*;
import org.esbhive.node.mgt.ESBNode;

import org.esbhive.node.mgt.NodeManagerInterface;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;
import org.esbhive.proxyconf.mgt.xsd.ProxyDataList;
import org.wso2.carbon.proxyadmin.xsd.ProxyData;

//services (objectClass=org.esbhive.*)
/**
 * @scr.component name="HiveStat" immediate="true"
 * @scr.service interface="org.esbhive.hivestat.HiveStatInterface"
 * @scr.reference name="esbhive.login.service" interface="org.esbhive.login.RemoteLogin"
 * cardinality="1..1" policy="dynamic" bind="setRemoteLogin"  unbind="unSetRemoteLogin"
 * @scr.reference name="esbhive.node.service" interface="org.esbhive.node.mgt.NodeManagerInterface"
 * cardinality="1..1" policy="dynamic" bind="setNodeManager"  unbind="unsetNodeManager"
 */
public class HiveStat implements HiveStatInterface {

	private static final Log log = LogFactory.getLog("org.esbhive.HiveStat");
	private static RemoteLogin remoteLogin;
	private NodeManagerInterface nodeManager;

	protected void setRemoteLogin(RemoteLogin rl) {
		remoteLogin = rl;
	}

	protected void unSetRemoteLogin(RemoteLogin rl) {
		remoteLogin = null;
	}

	public synchronized void setNodeManager(NodeManagerInterface r) {
		nodeManager = r;
	}

	public synchronized void unsetNodeManager(NodeManagerInterface r) {
		nodeManager = null;
	}

	public String giveStat(String name) {
		return "melaka";
	}

	private ProxyConfManagerStub CreateProxyServiceAdminStub(String username, String password, String ipAndPort) {

		LoginData otherNode = new LoginData();
		otherNode.setUserName(username);
		otherNode.setPassWord(password);
		otherNode.setHostNameAndPort(ipAndPort);
		LoginData loginData = null;
		try {
			loginData = remoteLogin.logIn(otherNode);
		} catch (AxisFault ex) {
			log.error("AxisFault in HiveStat when login ", ex);
		} catch (RemoteException ex) {
			log.error("Remote exception in HiveStat when login ", ex);
		} catch (org.esbhive.login.client.AuthenticationExceptionException ex) {
			log.error("AuthenticationExceptionException in HiveStat when login ", ex);
		}


		ConfigurationContext ctx = null;
		try {
			ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
		} catch (AxisFault ex) {
			log.error("Axis fault in HiveStat ", ex);
		}


		String serviceEPR4 = "https://" + ipAndPort + "/services/" + "ProxyConfManager";

		ProxyConfManagerStub stub4 = null;
		try {
			stub4 = new ProxyConfManagerStub(ctx, serviceEPR4);
		} catch (AxisFault ex) {
			log.error("error while creating a new ProxyConfManager stub ", ex);
		}
		ServiceClient client4 = stub4._getServiceClient();
		Options option = client4.getOptions();
		option.setManageSession(true);
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, loginData.getCookie());
		return stub4;
	}

	private int getCurrentLength(ESBNode array[], int j, ProxyConfManagerStub proxyConf) {
		ProxyDataList proxyDataListObj = null;
		ProxyData[] proxyDataList = null;
		int k = 0;
		try {
			proxyDataListObj = proxyConf.getProxyDataList(array[j - 1].getIpAndPort());
		} catch (RemoteException ex) {
			log.error("RemoteException in HiveStat", ex);
		}
		if (proxyDataListObj != null) {
			proxyDataList = proxyDataListObj.getProxyDataList();
			k = proxyDataList.length;
		}
		return k;
	}

	private ESBNode[] insertionSort(ESBNode array[], ProxyConfManagerStub proxyConf) {
		try {
			ProxyDataList proxyDataListObj = null;
			ProxyData[] proxyDataList = null;
			ProxyDataList proxyDataListObj2 = null;
			ProxyData[] proxyDataList1 = null;
			//int k = 0;
			int k2 = 0;
			for (int i = 1; i < array.length; i++) {
				int j = i;
				ESBNode B = array[i];

				proxyDataListObj2 = proxyConf.getProxyDataList(B.getIpAndPort());
				if (proxyDataListObj2 != null) {
					proxyDataList1 = proxyDataListObj2.getProxyDataList();
					k2 = proxyDataList1.length;
				} else {
				k2=0;
				}
				while ((j > 0) && (getCurrentLength(array, j, proxyConf) > k2)) {
					array[j] = array[j - 1];
					j--;
				}
				array[j] = B;
			}

		} catch (Exception ex) {
			log.error("RemoteException in HiveStat", ex);
		}

		return array;


	}

	public ESBNode[] selectBestNodes() {
		ESBNode[] nodeList = new ESBNode[0];
		ESBNode[] sortedNodeList = null;
		ArrayList<ESBNode> list = new ArrayList<ESBNode>();
		if (nodeManager != null) {
			nodeList = nodeManager.getNodes();
//			for (ESBNode node : nodeList) {
//				list.add(this.setNewEsbNode(node));
//			}

			sortedNodeList = insertionSort(nodeList, CreateProxyServiceAdminStub(nodeList[0].getUsername(), nodeList[0].getPassword(), nodeList[0].getIpAndPort()));
		} else {
			log.error("Error:: NodeManager is not set ");
		}
		if (remoteLogin != null) {
			//log2.error("Error:: RemoteLogin is not set");
		} else {
			log.error("Error:: RemoteLogin is not set");
		}
//		sortedNodeList = list.toArray(nodeList);
		return sortedNodeList;

	}

	private ESBNode setNewEsbNode(org.esbhive.node.mgt.ESBNode node) {
		org.esbhive.node.mgt.ESBNode newesbnode = new org.esbhive.node.mgt.ESBNode();
		newesbnode.setIpAndPort(node.getIpAndPort());
		newesbnode.setPassword(node.getPassword());
		newesbnode.setUsername(node.getUsername());
		newesbnode.setHttpsPort(node.getHttpsPort());
		newesbnode.setSynapsePort(node.getSynapsePort());
		newesbnode.setIp(node.getIp());
		return newesbnode;
	}
}
