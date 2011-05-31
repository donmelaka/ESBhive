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
  private String username;
  private String password;

  public ESBNode(){

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
