
/**
 * ProxyAdminException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

package org.esbhive.hp.mgt;

public class ProxyAdminException extends java.lang.Exception{

    private static final long serialVersionUID = 1306921638137L;
    
    private org.esbhive.hp.mgt.types.synapse.ProxyAdminExceptionE faultMessage;

    
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
    

    public void setFaultMessage(org.esbhive.hp.mgt.types.synapse.ProxyAdminExceptionE msg){
       faultMessage = msg;
    }
    
    public org.esbhive.hp.mgt.types.synapse.ProxyAdminExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    