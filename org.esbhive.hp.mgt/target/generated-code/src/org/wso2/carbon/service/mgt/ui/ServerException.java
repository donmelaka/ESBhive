
/**
 * ServerException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

package org.wso2.carbon.service.mgt.ui;

public class ServerException extends java.lang.Exception{

    private static final long serialVersionUID = 1313775696814L;
    
    private org.esbhive.hp.mgt.carbon.axis2.ServerExceptionE faultMessage;

    
        public ServerException() {
            super("ServerException");
        }

        public ServerException(java.lang.String s) {
           super(s);
        }

        public ServerException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ServerException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.esbhive.hp.mgt.carbon.axis2.ServerExceptionE msg){
       faultMessage = msg;
    }
    
    public org.esbhive.hp.mgt.carbon.axis2.ServerExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    