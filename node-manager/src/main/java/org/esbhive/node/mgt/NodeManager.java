package org.esbhive.node.mgt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.wso2.carbon.utils.ConfigurationContextService;
import org.apache.zookeeper.Watcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utils.ServerConfiguration;

//services (objectClass=org.esbhive.*)
/**
 * @scr.component name="node.manager" immediate="true"
 * @scr.service interface="org.esbhive.node.mgt.NodeManagerInterface"
 * @scr.reference name="configuration.context.service" interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService" unbind="unsetConfigurationContextService"
 * @scr.reference name="org.wso2.carbon.serverConfig" interface="org.wso2.carbon.utils.ServerConfiguration"
 * cardinality="1..1" policy="dynamic" bind="setServerConfiguration" unbind="unsetServerConfiguration
 *
 */
//Test the code
//Get the node data and create ESB node and save it
//TODO leader election and fault handling on failure
//User name and password
//other ports jms etc..
//breaking the sequences
//init code in setconfigcontext
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.utils.ServerConstants;

public class NodeManager implements NodeManagerInterface, Watcher {

  private static ConfigurationContextService configurationContextService;
  ZooKeeper zk;
  private final String NODES = "/nodes";
  Map<String, ESBNode> nodeMap = new HashMap<String, ESBNode>();
  private static final Log log = LogFactory.getLog("org.wso2.carbon.HiveNodeManager");
  private ServerConfiguration serverConfig;

  public NodeManager() {
  }

  protected void activate(ComponentContext context) {

    String connectionString = serverConfig.getFirstProperty("ESBhive.ZookeeperConnectionString");
    List<String> children = null;
    try {
      this.zk = new ZooKeeper(connectionString, 3000, this);
      Stat nodesRoot = zk.exists(NODES, false);
      if (nodesRoot == null) {
        zk.create(NODES, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
      }

      ESBNode thisNode = thisNode();
      zk.create(NODES + "/" + thisNode.getIpAndPort(),
              toByteArray(thisNode), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
      children = zk.getChildren(NODES, this);
    } catch (Exception ex) {
      log.error("Error in initializing NodeManager", ex);
    }
    for (Iterator<String> iter = children.iterator(); iter.hasNext();) {
      ESBNode node = null;
      try {
        node = createESBNodeFromZnode(iter.next());
      } catch (IOException ex) {
        log.error("Error in initializing NodeManager " + NODES, ex);
      } catch (KeeperException ex) {
        log.error("Error in initializing NodeManager " + NODES, ex);
      } catch (ClassNotFoundException ex) {
        log.error("Error in initializing NodeManager " + NODES, ex);
      } catch (InterruptedException ex) {
        log.error("Error in initializing NodeManager " + NODES, ex);
      }

      nodeMap.put(node.getIp(), node);
    }
    log.info("Node " + System.getProperty(ServerConstants.LOCAL_IP_ADDRESS)
            + ":" + " I'm added. Current Nodes = " + nodeMap.keySet());
  }

  protected void setConfigurationContextService(ConfigurationContextService cfgCtxService) {
    configurationContextService = cfgCtxService;


  }

  protected void unsetConfigurationContextService(ConfigurationContextService cfgCtxService) {
    configurationContextService = null;
  }

  protected void setServerConfiguration(ServerConfiguration serverConfig) {
    this.serverConfig = serverConfig;
  }

  protected void unsetServerConfiguration(ServerConfiguration serverConfig) {
    this.serverConfig = null;
  }

  public org.esbhive.node.mgt.ESBNode[] getNodes() {
    return (ESBNode[]) nodeMap.values().toArray();
  }

  public void process(WatchedEvent event) {
    EventType et = event.getType();
    if (et == EventType.None) {
      
    } else if (et == EventType.NodeChildrenChanged) {
      try {
        List<String> nodeList = zk.getChildren(NODES, true);
        Set nodeSet = nodeMap.keySet();
        if (nodeSet.size() < nodeList.size()) {
          nodeList.removeAll(nodeSet);
          for (Iterator<String> iter = nodeList.iterator(); iter.hasNext();) {
            String ip = iter.next();
            nodeMap.put(ip, createESBNodeFromZnode(ip));
            log.info("New node added. Ip : " + ip + ". Current Nodes = " + nodeMap.keySet());
//            log.info("New node username : " +createESBNodeFromZnode(ip).getUsername());
//            log.info("New node password : " +createESBNodeFromZnode(ip).getPassword());
          }
        } else if (nodeSet.size() > nodeList.size()) {
          Set nodeSetCopy = new HashSet(nodeSet);
          nodeSetCopy.removeAll(nodeList);
          for (Iterator<String> iter = nodeSetCopy.iterator(); iter.hasNext();) {
            String ip = iter.next();
            nodeMap.remove(ip);
            log.info("Node Failed. Ip : " + ip + ". Current Nodes = " + nodeMap.keySet());
          }
        }
      } catch (IOException ex) {
        log.error("Error when fetching children of znode " + NODES, ex);
      } catch (ClassNotFoundException ex) {
        log.error("Error when fetching children of znode " + NODES, ex);
      } catch (KeeperException ex) {
        log.error("Error when fetching children of znode " + NODES, ex);
      } catch (InterruptedException ex) {
        log.error("Error when fetching children of znode " + NODES, ex);
      }

    }
  }

  private ESBNode createESBNodeFromZnode(String zNode) throws IOException, KeeperException,
          ClassNotFoundException, InterruptedException {
    byte[] bytes = zk.getData(this.NODES+"/"+zNode, false, null);
    ESBNode node = toESBNode(bytes);
    node.setIp(zNode);
    return node;
  }

  private ESBNode thisNode() {
    String synapsePort = (String) configurationContextService.getServerConfigContext().getAxisConfiguration().getTransportIn("http").getParameter("port").getValue();

    String httpsPort = (String) configurationContextService.getServerConfigContext().getAxisConfiguration().getTransportIn("https").
            getParameter("port").getValue();

    String carbonHttpsPort = System.getProperty("carbon.https.port");

    String ip = System.getProperty(ServerConstants.LOCAL_IP_ADDRESS);

    ESBNode thisNode = new ESBNode();
    thisNode.setHttpsPort(carbonHttpsPort);
    thisNode.setIp(ip);
    thisNode.setIpAndPort(ip + ":" + carbonHttpsPort);
    thisNode.setSynapsePort(synapsePort);
    thisNode.setUsername(serverConfig.getFirstProperty("ESBhive.UserName"));
    thisNode.setPassword(serverConfig.getFirstProperty("ESBhive.PassWord"));
    return thisNode;
  }

  //taken from
  //http://scr4tchp4d.blogspot.com/2008/07/object-to-byte-array-and-byte-array-to.html
  private byte[] toByteArray(ESBNode obj) throws IOException {
    byte[] bytes = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(obj);
    oos.flush();
    oos.close();
    bos.close();
    bytes = bos.toByteArray();
    return bytes;
  }

  private ESBNode toESBNode(byte[] bytes) throws IOException, ClassNotFoundException {
    Object obj = null;
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    ObjectInputStream ois = new ObjectInputStream(bis);
    obj = ois.readObject();
    return (ESBNode) obj;
  }
}
