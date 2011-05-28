
/**
 * RemoteExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

package org.esbhive.node.mgt;

public class RemoteExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1304822245296L;
    
    private org.esbhive.node.mgt.RemoteExceptionE faultMessage;

    
        public RemoteExceptionException() {
            super("RemoteExceptionException");
        }

        public RemoteExceptionException(java.lang.String s) {
           super(s);
        }

        public RemoteExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public RemoteExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.esbhive.node.mgt.RemoteExceptionE msg){
       faultMessage = msg;
    }
    
    public org.esbhive.node.mgt.RemoteExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    