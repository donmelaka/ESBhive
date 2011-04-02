
/**
 * AuthenticationExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.2  Built on : Sep 06, 2010 (09:42:01 CEST)
 */

package org.wso2.carbon.authenticator.proxy;

public class AuthenticationExceptionException extends java.lang.Exception{
    
    private org.wso2.carbon.authenticator.proxy.AuthenticationExceptionE faultMessage;

    
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
    

    public void setFaultMessage(org.wso2.carbon.authenticator.proxy.AuthenticationExceptionE msg){
       faultMessage = msg;
    }
    
    public org.wso2.carbon.authenticator.proxy.AuthenticationExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    