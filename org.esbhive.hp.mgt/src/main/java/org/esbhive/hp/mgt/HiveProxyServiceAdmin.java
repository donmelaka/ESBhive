/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.hp.mgt;

/*
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisService;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.util.PolicyInfo;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.config.xml.ProxyServiceFactory;
import org.apache.synapse.config.xml.ProxyServiceSerializer;
import org.apache.synapse.config.xml.SequenceMediatorSerializer;
import org.apache.synapse.config.xml.XMLConfigConstants;
import org.apache.synapse.config.xml.endpoints.EndpointSerializer;
import org.apache.synapse.core.axis2.ProxyService;

import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.mediation.initializer.persistence.MediationPersistenceManager;
import org.wso2.carbon.mediation.initializer.ServiceBusConstants;
import org.wso2.carbon.proxyadmin.MetaData;
import org.wso2.carbon.proxyadmin.ProxyAdminException;
import org.wso2.carbon.proxyadmin.ProxyData;
import org.wso2.carbon.proxyadmin.ProxyServicePolicyInfo;

import org.wso2.carbon.proxyadmin.Entry;
import org.wso2.carbon.proxyadmin.util.ConfigHolder;
import org.apache.axis2.context.ConfigurationContext;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
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

	//private static HiveStatisticsServiceInterface stat;
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

	/**
	 * Enables statistics for the specified proxy service
	 *
	 * @param proxyName name of the proxy service name of which the statistics need to be enabled
	 * @throws ProxyAdminException in case of a failure in enabling statistics
	 * @return <code>successful</code> on success or <code>failed</code> otherwise
	 */
	public String enableStatistics(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		//(ipAddress + port);
		String enableStatics = "";
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
	 * @throw
	s ProxyAdminException in case of a failure in disabling statistics
	 * @return <code>successful</code> on success or <code>failed</code> if unsuccessful
	 */
	public String disableStatistics(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		String disableStatics = "";
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
	 * @return <code>successful</code> on success or <code>failed</code> otherwise
	 */
	public String enableTracing(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		String enableTracing = "";
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
	 * @return SUCCESSFUL is the operation is successful and FAILED if it is failed
	 */
	public String disableTracing(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		String disableTracing = "";
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
		try {
			disableTracing = proxyServiceAdminStub.disableTracing(proxyName);
		} catch (RemoteException ex) {
			log2.error("HiveProxyServiceAdmin RemoteException while disableTracing", ex);
		} catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
			log2.error("HiveProxyServiceAdmin ProxyAdminException while disableTracing", ex);
		}
		return disableTracing;
	}

	/**
	 * Add a proxy service described by the given OMElement
	 *
	 * @param proxyServiceElement configuraiton of the proxy service which needs to be added
	 * @param fileName Name of the file in which this configuration should be saved or null
	 * @throws ProxyAdminException if the element is   an proxy service or if a proxy service with the
	 *                   same name exists
	 */
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
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, loginData.getCookie());
		return stub4;
	}

	private String[] getCredentials(String ipAndPort) {
		ESBNode[] nodes = null;
		String[] credentials = new String[2];
		if (nodeManager != null) {
			nodes = nodeManager.getNodes();
		}
		for (ESBNode tempNode : nodes) {
			if (tempNode.getIpAndPort().equals(ipAndPort)) {
				credentials[0] = tempNode.getUsername();
				credentials[1] = tempNode.getPassword();
				break;
			}
		}

		return credentials;

	}

	private void addProxyService(OMElement proxyServiceElement,
		String fileName) throws ProxyAdminException {

		if (proxyServiceElement.getQName().getLocalPart().equals(XMLConfigConstants.PROXY_ELT.getLocalPart())) {
			String proxyName = proxyServiceElement.getAttributeValue(new QName("name"));
			boolean b = false;
			try {
				b = ConfigHolder.getInstance().getSynapseConfiguration().getAxisConfiguration().getService(proxyName) != null;
			} catch (Exception ex) {
				Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
			}

			if (b) {
				handleException(log2, "A service named "
					+ proxyName + " already exists", null);
			} else {
				ProxyService proxy = ProxyServiceFactory.createProxy(proxyServiceElement);

				try {
					ConfigHolder.getInstance().getSynapseConfiguration().addProxyService(
						proxy.getName(), proxy);
					proxy.buildAxisService(ConfigHolder.getInstance().getSynapseConfiguration(),
						ConfigHolder.getInstance().getAxisConfiguration());

					if (log2.isDebugEnabled()) {
						log2.debug("Added proxy service : " + proxyName);
					}

					if (!proxy.isStartOnLoad()) {
						proxy.stop(ConfigHolder.getInstance().getSynapseConfiguration());
					}

					if (proxy.getTargetInLineInSequence() != null) {
						proxy.getTargetInLineInSequence().init(
							ConfigHolder.getInstance().getSynapseEnvironment());
					}
					if (proxy.getTargetInLineOutSequence() != null) {
						proxy.getTargetInLineOutSequence().init(
							ConfigHolder.getInstance().getSynapseEnvironment());
					}
					if (proxy.getTargetInLineFaultSequence() != null) {
						proxy.getTargetInLineFaultSequence().init(
							ConfigHolder.getInstance().getSynapseEnvironment());
					}
					if (proxy.getTargetInLineEndpoint() != null) {
						proxy.getTargetInLineEndpoint().init(
							ConfigHolder.getInstance().getSynapseEnvironment());
					}

					if (fileName != null) {
						proxy.setFileName(fileName);
					} else {
						proxy.setFileName(MediationPersistenceManager.generateFileName(proxy.getName()));
					}

					persistProxyService(proxy);

				} catch (Exception e) {
					try {
						ConfigHolder.getInstance().getSynapseConfiguration().removeProxyService(proxyName);
					} catch (Exception ex) {
						Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
					}
					try {
						if (ConfigHolder.getInstance().getAxisConfiguration().getService(proxy.getName()) != null) {
							ConfigHolder.getInstance().getAxisConfiguration().removeService(proxy.getName());
						}
					} catch (Exception ignore) {
					}
					handleException(log2, "Unable to add Proxy service : " + proxy.getName(), e);
				}
			}
		} else {
			handleException(log2, "Invalid proxy service definition", null);
		}

	}

	/**
	 * Alter and saves the proxy service to the SynapseConfiguration as specified by the
	 * given OMElement configuration
	 *
	 * @param proxyServiceElement configuration of the proxy service which needs to be altered
	 * @throws ProxyAdminException if the service not present or the configuration is wrong or
	 *                   in case of a failure in building the axis service
	 */
	private void modifyProxyService(OMElement proxyServiceElement) throws ProxyAdminException {
		try {
			if (proxyServiceElement.getQName().getLocalPart().equals(XMLConfigConstants.PROXY_ELT.getLocalPart())) {

				String proxyName = proxyServiceElement.getAttributeValue(new QName("name"));
				SynapseConfiguration synapseConfig = null;
				try {
					synapseConfig = ConfigHolder.getInstance().getSynapseConfiguration();
				} catch (Exception ex) {
					Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
				}

				ProxyService currentProxy = synapseConfig.getProxyService(proxyName);
				boolean wasRunning;
				if (currentProxy == null) {
					handleException(log2, "A proxy service named "
						+ proxyName + " does not exist", null);

				} else {
					wasRunning = currentProxy.isRunning();
					log2.debug("Deleting existing proxy service : " + proxyName);
					AxisService axisService = synapseConfig.getAxisConfiguration().
						getService(proxyName);
					if (axisService != null) {
						axisService.getParent().addParameter(
							CarbonConstants.KEEP_SERVICE_HISTORY_PARAM, "true");
					}
					deleteProxyService(proxyName);

					try {
						log2.debug("Adding proxy service : " + proxyName);
						addProxyService(proxyServiceElement, currentProxy.getFileName());
						if (log2.isDebugEnabled()) {
							log2.debug("Modified proxy service : " + proxyName);
						}

						if (!wasRunning
							&& synapseConfig.getProxyService(proxyName).isRunning()) {
							synapseConfig.getProxyService(proxyName).stop(synapseConfig);
						} else if (wasRunning
							&& !synapseConfig.getProxyService(proxyName).isRunning()) {
							synapseConfig.getProxyService(proxyName).start(synapseConfig);
						}

						ProxyService proxy = synapseConfig.getProxyService(proxyName);
						if (proxy != null) {
							if (proxy.getTargetInLineInSequence() != null) {
								proxy.getTargetInLineInSequence().init(ConfigHolder.getInstance().getSynapseEnvironment());
							}
							if (proxy.getTargetInLineOutSequence() != null) {
								proxy.getTargetInLineOutSequence().init(ConfigHolder.getInstance().getSynapseEnvironment());
							}
							if (proxy.getTargetInLineFaultSequence() != null) {
								proxy.getTargetInLineFaultSequence().init(ConfigHolder.getInstance().getSynapseEnvironment());
							}
							if (proxy.getTargetInLineEndpoint() != null) {
								proxy.getTargetInLineEndpoint().init(ConfigHolder.getInstance().getSynapseEnvironment());
							}
						}
					} catch (Exception e) {

						log2.error("Unable to save changes made for the proxy service : "
							+ proxyName + ". Restoring the existing proxy..");
						try {
							synapseConfig.addProxyService(proxyName, currentProxy);
							persistProxyService(currentProxy);
							currentProxy.buildAxisService(synapseConfig,
								ConfigHolder.getInstance().getAxisConfiguration());
							if (!wasRunning) {
								currentProxy.stop(synapseConfig);
							} else {
								currentProxy.start(synapseConfig);
							}
						} catch (Exception af) {
							handleException(log2, "Unable to restore the existing proxy", af);
						}

						handleException(log2, "Unable to save changes made for the proxy service : "
							+ proxyName + ". Restored the existing proxy...", e);
					}
				}
			} else {
				handleException(log2, "Invalid proxy service definition", null);
			}

		} catch (AxisFault af) {
			handleException(log2, "Invalid proxy service definition", af);
		}
	}

	/**
	 * Deletes a proxy service from the synapse configuration
	 *
	 * @param proxyName name of the proxy service which needs to be deleted
	 * @throws ProxyAdminException if the proxy service name given is not existent in the
	 *                   synapse configuration
	 * @return <code>successful</code> on success or <code>failed</code> otherwise
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

	private String deleteProxy(String proxyName) throws ProxyAdminException {
		try {
			log2.debug("Deleting proxy service : " + proxyName);
			SynapseConfiguration synapseConfiguration = ConfigHolder.getInstance().getSynapseConfiguration();
			ProxyService proxy = synapseConfiguration.getProxyService(proxyName);
			if (proxy != null) {
				synapseConfiguration.removeProxyService(proxyName);
				MediationPersistenceManager pm = MediationPersistenceManager.getInstance();
				pm.deleteItem(proxyName, proxy.getFileName(),
					ServiceBusConstants.ITEM_TYPE_PROXY_SERVICE);
				if (log2.isDebugEnabled()) {
					log2.debug("Proxy service : " + proxyName + " deleted");
				}
				return SUCCESSFUL;

			} else {
				log2.warn("No proxy service exists by the name : " + proxyName);
				return FAILED;
			}
		} catch (Exception e) {
			handleException(log2, "Unable to delete proxy service : " + proxyName, e);
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
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		//String[] credentials = getCredentials(ipAddress + port);
		org.esbhive.hp.mgt.types.carbon.MetaData metaData1 = null;
		MetaData metaData = new MetaData();
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
	 * @return <code>successful</code> on success or <code>failed</code> otherwise
	 */
	public String startProxyService(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		String startProxyService = null;
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
	 * @return <code>successful</code> on success or <code>failed</code> otherwise
	 */
	public String stopProxyService(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		String stopProxyService = null;
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
	 * @return <code>successful</code> on success or <code>failed</code> otherwise
	 */
	public String redeployProxyService(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		String redeployProxyService = null;
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
		try {
			getSourceView = proxyServiceAdminStub.getSourceView(changeProxyDataType(pd));
		} catch (RemoteException ex) {
			log2.error("HiveProxyServiceAdmin RemoteException while stopProxyService", ex);
		} catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
			log2.error("HiveProxyServiceAdmin ProxyAdminException while stopProxyService", ex);
		}
		return getSourceView;
	}

	public ProxyData getProxy(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		org.wso2.carbon.proxyadmin.ProxyData cproxy = new org.wso2.carbon.proxyadmin.ProxyData();
		org.esbhive.hp.mgt.types.carbon.ProxyData proxy = null;


		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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

//	private  int[] selectEsbNodes(int length, double percentage) {
//		if (length != 1) {
//			double realProxyCount = length * percentage;
//			int count = (int) Math.floor(realProxyCount);
//
//			List<Integer> list = new ArrayList<Integer>();
//			int[] temArray = new int[count];
//			for (int a = 0; a < length; a++) {
//				list.add(a);
//			}
//
//			for (int j = 0; j < count; j++) {
//				Collections.shuffle(list);
//				temArray[j] = list.get(0);
//				list.remove(new Integer(temArray[j]));
//
//			}
//
//
//			return temArray;
//		} else {
//			int[] array = {0};
//			return array;
//		}
//
//	}
	private ESBNode[] selectEsbNodes(double percentage) {
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
		count = (int) Math.floor(sortedNodes.length * percentage);
		ESBNode[] selectedNodes = new ESBNode[count];
		for (int i = 0; i < count; i++) {
			selectedNodes[i] = sortedNodes[i];
		}
		return selectedNodes;
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
			/////////////////////////////////


			//////////////////////////
			//int[] selectedNodesIndexes = selectEsbNodes( 0.5);


			//	ESBNode[] selectedNodes = new ESBNode[selectedNodesIndexes.length];
			ESBNode[] selectedNodes = selectEsbNodes(0.5);
			ProxyServiceAdminStub proxyServiceAdminStub;

			org.esbhive.hp.mgt.types.carbon.ProxyData changedProxyData = changeProxyDataType(pd);
//			for (int j = 0; j < selectedNodesIndexes.length; j++) {
//				selectedNodes[j] = nodeList[selectedNodesIndexes[j]];
//			}

			for (ESBNode esbNode : selectedNodes) {
				proxyServiceAdminStub = createProxyServiceAdminStub(esbNode.getUsername(), esbNode.getPassword(), esbNode.getIpAndPort());
				proxyServiceAdminStub.addProxy(changedProxyData);
			}

			ProxyConfManagerStub proxyConfstub = null;

			org.wso2.carbon.proxyadmin.xsd.ProxyData newProxyData = setNewProxyData(pd);


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

	private ESBNode setNewEsbNode(org.esbhive.node.mgt.ESBNode node) {
		ESBNode newesbnode = new ESBNode();
		newesbnode.setIpAndPort(node.getIpAndPort());
		newesbnode.setPassword(node.getPassword());
		newesbnode.setUsername(node.getUsername());
		newesbnode.setHttpsPort(node.getHttpsPort());
		newesbnode.setSynapsePort(node.getSynapsePort());
		newesbnode.setIp(node.getIp());
		return newesbnode;
	}

	private org.wso2.carbon.proxyadmin.xsd.ProxyData setNewProxyData(ProxyData pd1) {
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

	private org.esbhive.hp.mgt.types.carbon.ProxyData changeProxyDataType(ProxyData pd) {
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

	private int getRandomNumber(int min, int max) {
		int randomNumber;
		randomNumber = (int) (Math.random() * (max - min)) + min;
		return randomNumber;
	}

	public String modifyProxy(ProxyData pd) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		String modifyProxy = null;
		ProxyServiceAdminStub proxyServiceAdminStub = createProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
		try {
			modifyProxy = proxyServiceAdminStub.modifyProxy(changeProxyDataType(pd));
		} catch (RemoteException ex) {
			log2.error("HiveProxyServiceAdmin RemoteException while modifyProxy", ex);
		} catch (org.esbhive.hp.mgt.ProxyAdminException ex) {
			log2.error("HiveProxyServiceAdmin ProxyAdminException while modifyProxy", ex);
		}
		return modifyProxy;
	}

	private ProxyService proxyForName(String proxyName) throws ProxyAdminException {
		try {
			ProxyService ps = ConfigHolder.getInstance().getSynapseConfiguration().getProxyService(proxyName);
			if (ps != null) {
				return ps;
			} else {
				handleException(log2, "A proxy service named : "
					+ proxyName + " does not exist", null);
			}
		} catch (Exception af) {
			handleException(log2, "Unable to get the proxy service definition for : "
				+ proxyName, af);
		}
		return null;
	}

	private ProxyData generateProxyDataFor(ProxyService ps) throws ProxyAdminException {
		ProxyData pd = new ProxyData();
		pd.setName(ps.getName());

		// sets status, i.e. whether running/stop, statistics on/off, tracing on/off,
		// wsdl available/unavilable, startOnLoad true/false
		pd.setRunning(ps.isRunning());
		if (ps.getAspectConfiguration() != null
			&& ps.getAspectConfiguration().isStatisticsEnable()) {
			pd.setEnableStatistics(true);
		} else {
			pd.setEnableStatistics(false);
		}
		if (ps.getTraceState() == SynapseConstants.TRACING_ON) {
			pd.setEnableTracing(true);
		} else if (ps.getTraceState() == SynapseConstants.TRACING_OFF) {
			pd.setEnableTracing(false);
		}
		if (ps.getWsdlURI() != null
			|| ps.getWSDLKey() != null || ps.getInLineWSDL() != null) {
			pd.setWsdlAvailable(true);
		} else {
			pd.setWsdlAvailable(false);
		}
		if (ps.isStartOnLoad()) {
			pd.setStartOnLoad(true);
		} else {
			pd.setStartOnLoad(false);
		}

		// sets transports
		List list;
		if ((list = ps.getTransports()) != null && !list.isEmpty()) {
			String[] arr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				arr[i] = (String) list.get(i);
			}
			pd.setTransports(arr);
		}

		// sets pinned servers (if any)
		if ((list = ps.getPinnedServers()) != null && !list.isEmpty()) {
			String[] arr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				arr[i] = (String) list.get(i);
			}
			pd.setPinnedServers(arr);
		}

		SequenceMediatorSerializer seqMedSerializer = new SequenceMediatorSerializer();
		// sets target in sequence
		if (ps.getTargetInSequence() != null) {
			pd.setInSeqKey(ps.getTargetInSequence());
		} else if (ps.getTargetInLineInSequence() != null) {
			OMElement inSeq = seqMedSerializer.serializeAnonymousSequence(
				null, ps.getTargetInLineInSequence());
			inSeq.setLocalName("inSequence");
			pd.setInSeqXML(inSeq.toString());
		}

		// sets target out sequence
		if (ps.getTargetOutSequence() != null) {
			pd.setOutSeqKey(ps.getTargetOutSequence());
		} else if (ps.getTargetInLineOutSequence() != null) {
			OMElement outSeq = seqMedSerializer.serializeAnonymousSequence(
				null, ps.getTargetInLineOutSequence());
			outSeq.setLocalName("outSequence");
			pd.setOutSeqXML(outSeq.toString());
		}

		// sets fault sequence
		if (ps.getTargetFaultSequence() != null) {
			pd.setFaultSeqKey(ps.getTargetFaultSequence());
		} else if (ps.getTargetInLineFaultSequence() != null) {
			OMElement faultSeq = seqMedSerializer.serializeAnonymousSequence(
				null, ps.getTargetInLineFaultSequence());
			faultSeq.setLocalName("faultSequence");
			pd.setFaultSeqXML(faultSeq.toString());
		}

		// sets endpoint
		if (ps.getTargetEndpoint() != null) {
			pd.setEndpointKey(ps.getTargetEndpoint());
		} else if (ps.getTargetInLineEndpoint() != null) {
			pd.setEndpointXML(EndpointSerializer.getElementFromEndpoint(
				ps.getTargetInLineEndpoint()).toString());
		}

		// sets publish WSDL
		if (pd.isWsdlAvailable()) {
			if (ps.getWSDLKey() != null) {
				pd.setWsdlKey(ps.getWSDLKey());
			} else if (ps.getWsdlURI() != null) {
				pd.setWsdlURI(ps.getWsdlURI().toString());
			} else if (ps.getInLineWSDL() != null) {
				pd.setWsdlDef(ps.getInLineWSDL().toString());
			}
			Map<String, String> map;
			if (ps.getResourceMap() != null
				&& (map = ps.getResourceMap().getResources()) != null && !map.isEmpty()) {
				Entry[] entries = new Entry[map.size()];
				int i = 0;
				for (String key : map.keySet()) {
					entries[i] = new Entry(key, map.get(key));
					i++;
				}
				pd.setWsdlResources(entries);
			}
		}

		// sets additional service parameters
		Map<String, Object> map;
		if ((map = ps.getParameterMap()) != null && !map.isEmpty()) {
			Entry[] entries = new Entry[map.size()];
			int i = 0;
			Object o;
			for (String key : map.keySet()) {
				o = map.get(key);
				if (o instanceof String) {
					entries[i] = new Entry(key, (String) o);
				} else if (o instanceof OMElement) {
					entries[i] = new Entry(key, o.toString());
				}
				i++;
			}
			pd.setServiceParams(entries);
		}

		if (ps.isWsSecEnabled()) {
			pd.setEnableSecurity(true);
		}

		if (ps.getPolicies() != null && ps.getPolicies().size() > 0) {
			List<ProxyServicePolicyInfo> policies = new ArrayList<ProxyServicePolicyInfo>();
			for (PolicyInfo policyInfo : ps.getPolicies()) {
				if (policyInfo.getPolicyKey() != null) {
					ProxyServicePolicyInfo policy = new ProxyServicePolicyInfo();
					policy.setKey(policyInfo.getPolicyKey());
					if (policyInfo.getType() != 0) {
						policy.setType(policyInfo.getMessageLable());
					}
					if (policyInfo.getOperation() != null) {
						policy.setOperationName(policyInfo.getOperation().getLocalPart());
						if (policyInfo.getOperation().getNamespaceURI() != null) {
							policy.setOperationNS(policyInfo.getOperation().getNamespaceURI());
						}
					}
					policies.add(policy);

				} else {
					throw new ProxyAdminException("A policy without a key was found on the "
						+ "proxy service : " + ps.getName());
				}
			}
			pd.setPolicies(policies.toArray(new ProxyServicePolicyInfo[policies.size()]));
		}
		return pd;
	}

	private void persistProxyService(ProxyService proxy) throws ProxyAdminException {
		MediationPersistenceManager pm = MediationPersistenceManager.getInstance();
		pm.saveItem(proxy.getName(), ServiceBusConstants.ITEM_TYPE_PROXY_SERVICE);
	}

	private void handleException(Log log, String message, Exception e) throws ProxyAdminException {
		if (e == null) {
			ProxyAdminException paf = new ProxyAdminException(message);
			log.error(message, paf);
			throw paf;
		} else {
			message = message + " :: " + e.getMessage();
			log.error(message, e);
			throw new ProxyAdminException(message, e);
		}
	}

	private org.esbhive.hp.mgt.types.carbon.ProxyData createDummyProxy(ESBNode esbNode, ProxyData pd) {

		ProxyData dummyProxy = new ProxyData();

		dummyProxy.setStartOnLoad(true);


		dummyProxy.setName(pd.getName());
		dummyProxy.setOutSeqXML("<outSequence xmlns=\"http://ws.apache.org/ns/synapse\"><send/></outSequence>");
		URL url = null;
		try {
			String targetURL = "http://" + esbNode.getIpAndPort().substring(0, esbNode.getIpAndPort().indexOf(':')) + ":" + esbNode.getSynapsePort() + "/services/" + pd.getName();
			url = new URL(targetURL);
		} catch (MalformedURLException ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
		}
		dummyProxy.setEndpointXML("<endpoint xmlns=\"http://ws.apache.org/ns/synapse\"><address uri=\"" + url.toString() + "\"/></endpoint>");

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
//always no of dummy proxies should be higher that the no of  real proxy services
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
		//Iterator iterator = notSelectedNodes.iterator();
//		ESBNode next = null;
//		for (ESBNode tempNode : selectedEsbs) {
////			while (iterator.hasNext()) {
////				next = (ESBNode) iterator.next();
////				if (tempNode.getIpAndPort().equals(next.getIpAndPort())) {
////					notSelectedNodes.remove(next);
////				}
////			}
//			for (int k=0;k<nodeList.length;k++)  {
//				next = nodeList[k];
//				if (tempNode.getIpAndPort().equals(next.getIpAndPort())) {
//					notSelectedNodes.remove(k);
//				}
//			}
//
//		}



//		boolean isAvailable = false;
//		for (int i = 0; i < nodeList.length; i++) {
//			isAvailable = false;
//			for (int k = 0; k < selectedEsbs.length; k++) {
//				if (nodeList[i]==selectedEsbs[k]) {
//					isAvailable = true;
//				}
//				if (isAvailable == false) {
//					notSelectedNodes.add(nodeList[i]);
//				}
//			}
//		}

		try {
			// todo - at the moment I get the OMElement from the dummyProxy and asks the private method to build the proxy service
			// todo - but I could improve this by creating a proxy service from the dummyProxy itself. Not for this release :)
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

				stub4 = createProxyServiceAdminStub(tempNode.getUsername(), tempNode.getPassword(), tempNode.getIpAndPort());

				stub4.addProxy(createDummyProxy(selectedEsbs[count], pd));

				if (count == selectedEsbs.length - 1) {
					count = 0;
				} else {
					count++;
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
