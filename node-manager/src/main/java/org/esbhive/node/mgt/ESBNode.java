/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.node.mgt;

/**
 *
 * @author pubudu
 */
public class ESBNode {
  private String ipAndPort;

  
  private String ip;
  private String httpsPort;
  private String synapsePort;
  private String username;
  private String password;

  public ESBNode(){

  }

  public String getHttpsPort() {
    return httpsPort;
  }

  public void setHttpsPort(String httpsPort) {
    this.httpsPort = httpsPort;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getSynapsePort() {
    return synapsePort;
  }

  public void setSynapsePort(String synapsePort) {
    this.synapsePort = synapsePort;
  }



  public ESBNode(String ip, String username, String password) {
    this.ipAndPort = ip;
    this.username = username;
    this.password = password;
  }



  public String getIpAndPort() {
    return ipAndPort;
  }

  public void setIpAndPort(String ip) {
    this.ipAndPort = ip;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
