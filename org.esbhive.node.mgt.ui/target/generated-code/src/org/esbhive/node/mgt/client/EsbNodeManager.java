

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
          * Auto generated method signature
          * 
                    * @param addNodeAndGetNodes3
                
         */

         
                     public org.esbhive.node.mgt.client.ESBNode[] addNodeAndGetNodes(

                        org.esbhive.node.mgt.client.ESBNode node4)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addNodeAndGetNodes3
            
          */
        public void startaddNodeAndGetNodes(

            org.esbhive.node.mgt.client.ESBNode node4,

            final org.esbhive.node.mgt.client.EsbNodeManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param addNode7
                
         */

         
                     public org.esbhive.node.mgt.client.ESBNode[] addNode(

                        org.esbhive.node.mgt.client.ESBNode me8,org.esbhive.node.mgt.client.ESBNode addto9)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addNode7
            
          */
        public void startaddNode(

            org.esbhive.node.mgt.client.ESBNode me8,org.esbhive.node.mgt.client.ESBNode addto9,

            final org.esbhive.node.mgt.client.EsbNodeManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     

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
    