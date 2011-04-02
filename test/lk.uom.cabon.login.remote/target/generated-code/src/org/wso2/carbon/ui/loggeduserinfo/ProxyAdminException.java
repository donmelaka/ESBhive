
/**
 * ProxyAdminException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.2  Built on : Sep 06, 2010 (09:42:01 CEST)
 */

package org.wso2.carbon.ui.loggeduserinfo;

public class ProxyAdminException extends java.lang.Exception{
    
    private synapse.apache.org.xsd.ProxyAdminExceptionE faultMessage;

    
        public ProxyAdminException() {
            super("ProxyAdminException");
        }

        public ProxyAdminException(java.lang.String s) {
           super(s);
        }

        public ProxyAdminException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ProxyAdminException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(synapse.apache.org.xsd.ProxyAdminExceptionE msg){
       faultMessage = msg;
    }
    
    public synapse.apache.org.xsd.ProxyAdminExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    