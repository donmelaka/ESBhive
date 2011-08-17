
/**
 * Exception.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

package org.wso2.carbon.service.mgt.ui;

public class Exception extends java.lang.Exception{

    private static final long serialVersionUID = 1312981539854L;
    
    private org.esbhive.hp.mgt.carbon.axis2.ExceptionE faultMessage;

    
        public Exception() {
            super("Exception");
        }

        public Exception(java.lang.String s) {
           super(s);
        }

        public Exception(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public Exception(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.esbhive.hp.mgt.carbon.axis2.ExceptionE msg){
       faultMessage = msg;
    }
    
    public org.esbhive.hp.mgt.carbon.axis2.ExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    