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
import org.esbhive.login.LoginData;
import org.esbhive.login.RemoteLogin;
import org.esbhive.login.client.AuthenticationAdminStub;
import org.esbhive.login.client.AuthenticationExceptionException;



/**
 * @scr.component name="esbhive.login.service" immediate="true"
 * @scr.service interface="org.esbhive.login.RemoteLogin"
 *
 */

public class RemoteLoginImpl implements RemoteLogin{

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
    return data;
    
  }

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
