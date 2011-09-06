/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.login.internal;

import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esbhive.login.LoginData;
import org.esbhive.login.RemoteLogin;
import org.esbhive.login.client.AuthenticationAdminStub;
import org.esbhive.login.client.AuthenticationExceptionException;



/**
 * This class implements the remote login interface and allows one ESB Node
 * to login to another ESB node
 * @scr.component name="esbhive.login.service" immediate="true"
 * @scr.service interface="org.esbhive.login.RemoteLogin"
 *
 */

public class RemoteLoginImpl implements RemoteLogin{
  //The string passed to getLog should be of the form org.wso2.carbon.NAME
  private static final Log log = LogFactory.getLog("org.wso2.carbon.HiveRemoteLogin");

  /**
   * This method will attempt to login to another ESB node
   * @param data - the Loign data object that contains username/password
   * @return - a LoginData object with the cookie set if the login was successful
   * @throws AxisFault
   * @throws RemoteException
   * @throws AuthenticationExceptionException
   */
  public LoginData logIn(LoginData data) throws AxisFault,
          RemoteException, AuthenticationExceptionException {
    ConfigurationContext ctx =
            ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(null, null);
    String serviceEPR = "https://" + data.getHostNameAndPort() +
                                                "/services/AuthenticationAdmin";
    AuthenticationAdminStub authAdminStub = new AuthenticationAdminStub(ctx,
                                                                        serviceEPR);
    ServiceClient client = authAdminStub._getServiceClient();
		Options options = client.getOptions();
    options.setManageSession(true);
		boolean isLogged = authAdminStub.login(data.getUserName(),
                                            data.getPassWord(),
                                            data.getHostNameAndPort());

    data.setIsLoggedIn(isLogged);
		String cookie = (String) authAdminStub
                            ._getServiceClient()
                            .getServiceContext()
                            .getProperty(HTTPConstants.COOKIE_STRING);
    data.setCookie(cookie);
    log.info("Login attempt to "+data.getHostNameAndPort()+" was successful.");
    return data;
    
  }

  /**
   * This method is used to logOut from another ESB node backend
   * @param data - Data informing from which ESB node to logout
   * @return
   * @throws AxisFault
   * @throws RemoteException
   * @throws AuthenticationExceptionException
   */
  public LoginData logOut(LoginData data) throws AxisFault, RemoteException, AuthenticationExceptionException {
     ConfigurationContext ctx =
            ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(null, null);
    String serviceEPR = "https://" + data.getHostNameAndPort() +
                                                "/services/AuthenticationAdmin";
    AuthenticationAdminStub authAdminStub = new AuthenticationAdminStub(ctx,
                                                                        serviceEPR);
    ServiceClient client = authAdminStub._getServiceClient();
		Options options = client.getOptions();
    options.setManageSession(true);    
    options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                            data.getCookie());
		    
    authAdminStub.logout();
    data.setIsLoggedIn(false);
    data.setCookie(null);
    return data;   
  }  
}
