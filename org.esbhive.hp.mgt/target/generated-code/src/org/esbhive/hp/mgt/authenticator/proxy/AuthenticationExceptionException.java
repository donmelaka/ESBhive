
/**
 * AuthenticationExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

package org.esbhive.hp.mgt.authenticator.proxy;

public class AuthenticationExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1307851912448L;
    
    private org.esbhive.hp.mgt.authenticator.proxy.AuthenticationExceptionE faultMessage;

    
        public AuthenticationExceptionException() {
            super("AuthenticationExceptionException");
        }

        public AuthenticationExceptionException(java.lang.String s) {
           super(s);
        }

        public AuthenticationExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public AuthenticationExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.esbhive.hp.mgt.authenticator.proxy.AuthenticationExceptionE msg){
       faultMessage = msg;
    }
    
    public org.esbhive.hp.mgt.authenticator.proxy.AuthenticationExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    