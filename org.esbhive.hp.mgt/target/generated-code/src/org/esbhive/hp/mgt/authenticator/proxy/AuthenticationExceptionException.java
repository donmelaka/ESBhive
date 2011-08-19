
/**
 * AuthenticationExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 03, 2010 (02:59:42 IST)
 */

package org.esbhive.hp.mgt.authenticator.proxy;

public class AuthenticationExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1312948095109L;
    
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
    