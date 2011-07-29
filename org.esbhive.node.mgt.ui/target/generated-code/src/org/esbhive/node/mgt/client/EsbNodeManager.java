

/**
 * EsbNodeManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.node.mgt.client;

    /*
     *  EsbNodeManager java interface
     */

    public interface EsbNodeManager {
          
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  process(
         org.apache.zookeeper.xsd.WatchedEvent event2

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
         */

         
                     public org.esbhive.node.mgt.client.ESBNode[] getNodes(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetNodes(

            

            final org.esbhive.node.mgt.client.EsbNodeManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    