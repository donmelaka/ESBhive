/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.login;

import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.esbhive.login.client.AuthenticationExceptionException;

/**
 *
 * @author pubudu
 */
public interface RemoteLogin {
  public LoginData logIn(LoginData data) throws AxisFault,
          RemoteException, AuthenticationExceptionException;

  public LoginData logOut(LoginData data)throws AxisFault, RemoteException, AuthenticationExceptionException;
}
