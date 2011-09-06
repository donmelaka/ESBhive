/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.login;

/**
 * This class represents the data used to login to another ESB.
 * @author pubudu
 */
public class LoginData {

  private String userName;
  private String passWord;
  private String hostNameAndPort;
  private boolean loggedIn = false;
  private String cookie;

  public String getCookie() {
    return cookie;
  }

  public void setCookie(String cookie) {
    this.cookie = cookie;
  }

  

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setIsLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }  

  public String getHostNameAndPort() {
    return hostNameAndPort;
  }

  public void setHostNameAndPort(String hostNameAndPort) {
    this.hostNameAndPort = hostNameAndPort;
  }

  
  
  public String getPassWord() {
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
  

  
  
}
