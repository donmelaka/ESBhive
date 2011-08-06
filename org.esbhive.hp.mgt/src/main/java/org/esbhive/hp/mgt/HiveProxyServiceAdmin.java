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
import org.apache.synapse.endpoints.Endpoint;
import org.esbhive.hp.mgt.authenticator.proxy.AuthenticationExceptionException;
import org.esbhive.proxyconf.mgt.xsd.ProEsb;
import org.esbhive.proxyconf.mgt.xsd.ProxyDataList;

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
import java.util.Collections;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.esbhive.hp.mgt.authenticator.proxy.AuthenticationAdminStub;
import org.apache.axis2.transport.http.HTTPConstants;

import org.esbhive.node.mgt.ESBNode;
import org.esbhive.node.mgt.NodeManagerInterface;
import org.wso2.carbon.utils.ServerConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esbhive.login.LoginData;
import org.esbhive.proxyconf.mgt.ProxyConfManagerStub;
import org.esbhive.login.RemoteLogin;
import org.esbhive.statistics.*;

/**
 * @scr.component name="hp.manager" immediate="true"
 * @scr.reference name="esbhive.node.service" interface="org.esbhive.node.mgt.NodeManagerInterface"
 * cardinality="1..1" policy="dynamic" bind="setNodeManager"  unbind="unsetNodeManager"
 * @scr.reference name="esbhive.login.service" interface="org.esbhive.login.RemoteLogin"
 * cardinality="1..1" policy="dynamic" bind="setRemoteLogin"  unbind="unSetRemoteLogin"
 */
@SuppressWarnings({"UnusedDeclaration"})
public class HiveProxyServiceAdmin {

	private static String SUCCESSFUL = "successful";
	private static String FAILED = "failed";
	private static NodeManagerInterface nodeManager;
	private static final Log log2 = LogFactory.getLog("org.wso2.carbon.HiveProxyServiceAdmin");
	private static RemoteLogin remoteLogin;
	
	private static String ipAddress = System.getProperty(ServerConstants.LOCAL_IP_ADDRESS);


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

	

	/**
	 * Enables statistics for the specified proxy service
	 *
	 * @param proxyName name of the proxy service name of which the statistics need to be enabled
	 * @throws ProxyAdminException in case of a failure in enabling statistics
	 * @return <code>successful</code> on success or <code>failed</code> otherwise
	 */
	public String enableStatistics(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		String enableStatics = "";
		ProxyServiceAdminStub proxyServiceAdminStub = CreateProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = CreateProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = CreateProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = CreateProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
	 * @throws ProxyAdminException if the element is not an proxy service or if a proxy service with the
	 *                   same name exists
	 */
	private ProxyServiceAdminStub CreateProxyServiceAdminStub(String username, String password, String ipAndPort) {

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


		String serviceEPR = "https://" + ipAndPort + "/services/" + "AuthenticationAdmin";
		// String serviceEPR = "https://" + "localhost:9443" + "/services/" + "AuthenticationAdmin";
		AuthenticationAdminStub stub = null;
		try {
			stub = new AuthenticationAdminStub(ctx, serviceEPR);
		} catch (AxisFault ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
		}

		ServiceClient client = stub._getServiceClient();
		Options options = client.getOptions();
		options.setManageSession(true);
		try {
			boolean isLogged = stub.login(username, password, ipAndPort);
		} catch (RemoteException ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
		} catch (AuthenticationExceptionException ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
		}

		String cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
			HTTPConstants.COOKIE_STRING);

		String serviceEPR4 = "https://" + ipAndPort + "/services/" + "ProxyServiceAdmin";

		ProxyServiceAdminStub stub4 = null;
		try {
			stub4 = new ProxyServiceAdminStub(ctx, serviceEPR4);
		} catch (AxisFault ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
		}
		ServiceClient client4 = stub4._getServiceClient();
		Options option = client4.getOptions();
		option.setManageSession(true);
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, loginData.getCookie());
		return stub4;
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
		ConfigurationContext ctx = null;
		try {
			ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
		} catch (AxisFault ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
		}
		String serviceEPR = "";
		AuthenticationAdminStub stub = null;
		ServiceClient client = null;
		Options options = null;
		boolean isLogged = false;
		String cookie = null;
		String serviceEPR4 = "";
		ProxyServiceAdminStub stub4 = null;
		ServiceClient client4 = null;
		Options option = null;
		ESBNode[] nodeList = null;
		if (nodeManager != null) {
			nodeList = nodeManager.getNodes();
		} else {
			if (log2.isDebugEnabled()) {
				log2.debug("Error:: NodeManager is not set ");
			}
		}
		for (ESBNode tempNode : nodeList) {

			serviceEPR = "https://" + tempNode.getIpAndPort() + "/services/" + "AuthenticationAdmin";
			try {
				stub = new AuthenticationAdminStub(ctx, serviceEPR);
			} catch (AxisFault ex) {
				Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
			}

			client = stub._getServiceClient();
			options = client.getOptions();
			options.setManageSession(true);
			try {
				isLogged = stub.login(tempNode.getUsername(), tempNode.getPassword(), tempNode.getIpAndPort());
			} catch (RemoteException ex) {
				Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
			} catch (AuthenticationExceptionException ex) {
				Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
			}

			cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
				HTTPConstants.COOKIE_STRING);

			serviceEPR4 = "https://" + tempNode.getIpAndPort() + "/services/" + "ProxyServiceAdmin";
			try {
				stub4 = new ProxyServiceAdminStub(ctx, serviceEPR4);
			} catch (AxisFault ex) {
				Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
			}
			client4 = stub4._getServiceClient();
			option = client4.getOptions();
			option.setManageSession(true);
			option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
			try {
				stub4.deleteProxyService(proxyName);
			} catch (RemoteException ex) {

				Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);

			} catch (org.esbhive.hp.mgt.ProxyAdminException ex) {

				Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
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
		ProxyServiceAdminStub proxyServiceAdminStub = CreateProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = CreateProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		try {
			// todo - at the moment I get the OMElement from the pd and asks the private method to build the proxy service
			// todo - but I could improve this by creating a proxy service from the pd itself. Not for this release :)
			ConfigurationContext ctx =
				ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

			String serviceEPR = "https://" + ipAddress + ":" + port + "/services/" + "AuthenticationAdmin";
			AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);

			ServiceClient client = stub._getServiceClient();
			Options options = client.getOptions();
			options.setManageSession(true);

			boolean isLogged = stub.login("admin", "admin", ipAddress + ":" + port);

			String cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
				HTTPConstants.COOKIE_STRING);

			String serviceEPR4 = "https://" + ipAddress + ":" + port + "/services/" + "ProxyServiceAdmin";

			ProxyServiceAdminStub stub4 = new ProxyServiceAdminStub(ctx, serviceEPR4);
			ServiceClient client4 = stub4._getServiceClient();
			Options option = client4.getOptions();
			option.setManageSession(true);
			option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);

			return stub4.getAvailableEndpoints();

		} catch (Exception ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 * Gets the endpoint object defined under the given name
	 *
	 * @param name the name of the endpoint
	 * @return endpoint configuration related with the name
	 * @throws ProxyAdminException if the endpoint is not found for the given name
	 */
	public String getEndpoint(String name) throws ProxyAdminException {
		String epXML = null;
		try {
			Endpoint ep = ConfigHolder.getInstance().getSynapseConfiguration().getDefinedEndpoints().get(name);
			epXML = EndpointSerializer.getElementFromEndpoint(ep).toString();
		} catch (Exception axisFault) {
			handleException(log2, "No endpoint defined by the name: " + name, axisFault);
		}
		return epXML;
	}

	/**
	 * Encapsulates the available transports, endpoints, and sequences into a single two dimensional array
	 * @return  A two dimensional array containing the set of transports, endpoints, and sequences
	 * under 0,1, and 2 indices.
	 * @throws ProxyAdminException
	 */
	public MetaData getMetaData() throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");




		//   org.wso2.carbon.proxyadmin.service.ProxyServiceAdmin proxyAdmin = new  org.wso2.carbon.proxyadmin.service.ProxyServiceAdmin();
		// MetaData metaData = new MetaData();
		MetaData metaData = new MetaData();
		ProxyServiceAdminStub stub4 = null;
		try {
			// todo - at the moment I get the OMElement from the pd and asks the private method to build the proxy service
			// todo - but I could improve this by creating a proxy service from the pd itself. Not for this release :)
			ConfigurationContext ctx =
				ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

			String serviceEPR = "https://" + ipAddress + ":" + port + "/services/" + "AuthenticationAdmin";
			AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);

			ServiceClient client = stub._getServiceClient();
			Options options = client.getOptions();
			options.setManageSession(true);

			boolean isLogged = stub.login("admin", "admin", ipAddress + ":" + port);

			String cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
				HTTPConstants.COOKIE_STRING);

			String serviceEPR4 = "https://" + ipAddress + ":" + port + "/services/" + "ProxyServiceAdmin";

			stub4 = new ProxyServiceAdminStub(ctx, serviceEPR4);
			ServiceClient client4 = stub4._getServiceClient();
			Options option = client4.getOptions();
			option.setManageSession(true);
			option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
			org.esbhive.hp.mgt.types.carbon.MetaData metaData1 = stub4.getMetaData();
			metaData.setEndpoints(metaData1.getEndpoints());
			metaData.setEndpointsAvailable(metaData1.getEndpointsAvailable());
			metaData.setSequences(metaData1.getSequences());
			metaData.setSequencesAvailable(metaData1.getSequencesAvailable());
			metaData.setTransports(metaData1.getTransports());
			metaData.setTransportsAvailable(metaData1.getTransportsAvailable());

		} catch (Exception ex) {
			log2.error("HiveProxyService admin getMetaData", ex);
		}

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
		ProxyServiceAdminStub proxyServiceAdminStub = CreateProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		ProxyServiceAdminStub proxyServiceAdminStub = CreateProxyServiceAdminStub("admin", "admin", ipAddress + ":" + port);
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
		try {
			ProxyService currentProxy = ConfigHolder.getInstance().getSynapseConfiguration().getProxyService(proxyName);
			if (currentProxy != null) {
				if (log2.isDebugEnabled()) {
					log2.debug("Redeploying proxy service : " + proxyName);
				}
				OMElement proxyElement = ProxyServiceSerializer.serializeProxy(null, currentProxy);
				modifyProxyService(proxyElement);
				if (log2.isDebugEnabled()) {
					log2.debug("Redeployed proxy service : " + proxyName);
				}
				return SUCCESSFUL;
			}
		} catch (Exception af) {
			handleException(log2, "Unable to redeploy proxy service : " + proxyName, af);
		}
		return FAILED;
	}

	public String getSourceView(ProxyData pd) throws ProxyAdminException {
		return pd.retrieveOM().toString();
	}

	public ProxyData getProxy(String proxyName) throws ProxyAdminException {
		String port = System.getProperty("carbon.https.port");
		org.wso2.carbon.proxyadmin.ProxyData cproxy = new org.wso2.carbon.proxyadmin.ProxyData();
		org.esbhive.hp.mgt.types.carbon.ProxyData proxy = null;
		try {
			// todo - at the moment I get the OMElement from the pd and asks the private method to build the proxy service
			// todo - but I could improve this by creating a proxy service from the pd itself. Not for this release :)
			ConfigurationContext ctx =
				ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

			String serviceEPR = "https://" + ipAddress + ":" + port + "/services/" + "AuthenticationAdmin";
			AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);

			ServiceClient client = stub._getServiceClient();
			Options options = client.getOptions();
			options.setManageSession(true);

			boolean isLogged = stub.login("admin", "admin", ipAddress + ":" + port);// this was 9443

			String cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
				HTTPConstants.COOKIE_STRING);

			String serviceEPR4 = "https://" + ipAddress + ":" + port + "/services/" + "ProxyServiceAdmin";

			ProxyServiceAdminStub stub4 = new ProxyServiceAdminStub(ctx, serviceEPR4);
			ServiceClient client4 = stub4._getServiceClient();
			Options option = client4.getOptions();
			option.setManageSession(true);
			option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
			proxy = stub4.getProxy(proxyName);

		} catch (Exception ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
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

	private static int[] selectEsbNodes(int length, double percentage) {
		if (length != 1) {
			double realProxyCount = length * percentage;
			int count = (int) Math.floor(realProxyCount);

			List<Integer> list = new ArrayList<Integer>();
			int[] temArray = new int[count];
			for (int a = 0; a < length; a++) {
				list.add(a);
			}

			for (int j = 0; j < count; j++) {
				Collections.shuffle(list);
				temArray[j] = list.get(0);
				list.remove(new Integer(temArray[j]));

			}


			return temArray;
		} else {
			int [] array={0};
			return array;
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
			int[] selectedNodesIndexes = selectEsbNodes(nodeList.length, 0.5);


			 ESBNode[] selectedNodes = new ESBNode[selectedNodesIndexes.length];
//			ESBNode[] selectedNodes=stat.selectBestNodes();

			//	ESBNode[] notselectedNodes = new ESBNode[notSelectedNodesIndexes.size()];

			ProxyServiceAdminStub proxyServiceAdminStub;
			//ESBNode tempEsbNode = nodeList[selectedNodesIndexes[0]];
			org.esbhive.hp.mgt.types.carbon.ProxyData changedProxyData = changeProxyDataType(pd);
			for (int j = 0; j < selectedNodesIndexes.length; j++) {
				selectedNodes[j] = nodeList[selectedNodesIndexes[j]];
			}



			for (ESBNode esbNode : selectedNodes) {
				proxyServiceAdminStub = this.CreateProxyServiceAdminStub(esbNode.getUsername(), esbNode.getPassword(), esbNode.getIpAndPort());
				proxyServiceAdminStub.addProxy(changedProxyData);
			}

			String serviceEPRAuthentication = "";
			AuthenticationAdminStub authenticationStub2 = null;
			ServiceClient authenticatoinClient = null;
			Options authenticationOptions = null;
			boolean isLogged2 = false;
			String cookie2 = null;
			String serviceEPRProxyConf = "";
			ProxyConfManagerStub proxyConfstub = null;
			ServiceClient proxyConfClient = null;
			Options proxyConfOption = null;
			org.wso2.carbon.proxyadmin.xsd.ProxyData newProxyData = setNewProxyData(pd);
			//	org.esbhive.node.mgt.xsd.ESBNode newEsbNode = setNewEsbNode(tempEsbNode);

			org.esbhive.node.mgt.xsd.ESBNode[] newEsbNodeList = new org.esbhive.node.mgt.xsd.ESBNode[selectedNodes.length];
			for (int k = 0; k < selectedNodes.length; k++) {
				newEsbNodeList[k] = setNewEsbNode(selectedNodes[k]);
			}

			for (ESBNode tempNode : nodeList) {
				serviceEPRAuthentication = "https://" + tempNode.getIpAndPort() + "/services/" + "AuthenticationAdmin";
				authenticationStub2 = new AuthenticationAdminStub(ctx, serviceEPRAuthentication);

				authenticatoinClient = authenticationStub2._getServiceClient();
				authenticationOptions = authenticatoinClient.getOptions();
				authenticationOptions.setManageSession(true);

				isLogged2 = authenticationStub2.login(tempNode.getUsername(), tempNode.getPassword(), tempNode.getIpAndPort());

				cookie2 = (String) authenticationStub2._getServiceClient().getServiceContext().getProperty(
					HTTPConstants.COOKIE_STRING);

				serviceEPRProxyConf = "https://" + tempNode.getIpAndPort() + "/services/" + "ProxyConfManager";

				proxyConfstub = new org.esbhive.proxyconf.mgt.ProxyConfManagerStub(ctx, serviceEPRProxyConf);
				proxyConfClient = proxyConfstub._getServiceClient();
				proxyConfOption = proxyConfClient.getOptions();
				proxyConfOption.setManageSession(true);
				proxyConfOption.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie2);
				//proxyConfstub.addNodeToMap(newProxyData, selectedNodes);


				proxyConfstub.addProxyConf(newProxyData, newEsbNodeList);
				
			}
			
			deployDummyProxies(pd, selectedNodes);

		} catch (Exception ex) {
			log2.error("error while adding a proxy in  HiveProxyServiceAdmin", ex);
		}

		return SUCCESSFUL;
	}

	private org.esbhive.node.mgt.xsd.ESBNode setNewEsbNode(ESBNode node) {
		org.esbhive.node.mgt.xsd.ESBNode newesbnode = new org.esbhive.node.mgt.xsd.ESBNode();
		newesbnode.setIpAndPort(node.getIpAndPort());
		newesbnode.setPassword(node.getPassword());
		newesbnode.setUsername(node.getUsername());
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
		int number;
		int randomNumber = this.getRandomNumber(4, 12);
		return SUCCESSFUL;
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
//        dummyProxy.setOutSeqXML(pd.getOutSeqXML());
		// log.debug(pd.getOutSeqXML());
		// dummyProxy.setOutSeqKey(pd.getOutSeqKey());

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
		boolean isAvailable = false;
		for (int i = 0; i < nodeList.length; i++) {
			isAvailable = false;
			for (int k = 0; k < selectedEsbs.length; k++) {
				if (nodeList[i] == selectedEsbs[k]) {
					isAvailable = true;
				}
				if (isAvailable == false) {
					notSelectedNodes.add(nodeList[i]);
				}
			}
		}

		try {
			// todo - at the moment I get the OMElement from the dummyProxy and asks the private method to build the proxy service
			// todo - but I could improve this by creating a proxy service from the dummyProxy itself. Not for this release :)

			ESBNode[] notSeletedNodesArray = notSelectedNodes.toArray(nodeList);
			if (nodeManager != null) {
				nodeList = nodeManager.getNodes();
			} else {
				if (log2.isDebugEnabled()) {
					log2.debug("Error:: NodeManager is not set ");
				}

			}
			ConfigurationContext ctx =
				ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
			String serviceEPR = "";
			AuthenticationAdminStub stub = null;
			ServiceClient client = null;
			Options options = null;
			boolean isLogged = false;
			String cookie = null;
			String serviceEPR4 = "";
			ProxyServiceAdminStub stub4 = null;
			ServiceClient client4 = null;
			Options option = null;
			int count = 0;
			for (ESBNode tempNode : notSeletedNodesArray) {


				serviceEPR = "https://" + tempNode.getIpAndPort() + "/services/" + "AuthenticationAdmin";
				stub = new AuthenticationAdminStub(ctx, serviceEPR);

				client = stub._getServiceClient();
				options = client.getOptions();
				options.setManageSession(true);

				isLogged = stub.login(tempNode.getUsername(), tempNode.getPassword(), tempNode.getIpAndPort());

				cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
					HTTPConstants.COOKIE_STRING);

				serviceEPR4 = "https://" + tempNode.getIpAndPort() + "/services/" + "ProxyServiceAdmin";

				stub4 = new ProxyServiceAdminStub(ctx, serviceEPR4);
				client4 = stub4._getServiceClient();
				option = client4.getOptions();
				option.setManageSession(true);
				option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);


				stub4.addProxy(this.createDummyProxy(selectedEsbs[count], pd));

				if (count == selectedEsbs.length - 1) {
					count = 0;
				} else {
					count++;
				}
			}

		} catch (Exception ex) {
			Logger.getLogger(HiveProxyServiceAdmin.class.getName()).log(Level.SEVERE, null, ex);
		}


	}
}
