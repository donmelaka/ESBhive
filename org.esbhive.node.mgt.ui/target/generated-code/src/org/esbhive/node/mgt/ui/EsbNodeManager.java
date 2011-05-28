

/**
 * EsbNodeManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.node.mgt.ui;

    /*
     *  EsbNodeManager java interface
     */

    public interface EsbNodeManager {
          

        /**
          * Auto generated method signature
          * 
                    * @param addNodeAndGetNodes3
                
         */

         
                     public org.esbhive.node.mgt.data.xsd.ESBNode[] addNodeAndGetNodes(

                        org.esbhive.node.mgt.data.xsd.ESBNode node4)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addNodeAndGetNodes3
            
          */
        public void startaddNodeAndGetNodes(

            org.esbhive.node.mgt.data.xsd.ESBNode node4,

            final org.esbhive.node.mgt.ui.EsbNodeManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param addNode7
                
             * @throws org.esbhive.node.mgt.ui.AuthenticationExceptionExceptionException : 
             * @throws org.esbhive.node.mgt.ui.RemoteExceptionException : 
         */

         
                     public org.esbhive.node.mgt.data.xsd.ESBNode[] addNode(

                        org.esbhive.node.mgt.data.xsd.ESBNode me8,org.esbhive.node.mgt.data.xsd.ESBNode addto9)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.node.mgt.ui.AuthenticationExceptionExceptionException
          ,org.esbhive.node.mgt.ui.RemoteExceptionException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addNode7
            
          */
        public void startaddNode(

            org.esbhive.node.mgt.data.xsd.ESBNode me8,org.esbhive.node.mgt.data.xsd.ESBNode addto9,

            final org.esbhive.node.mgt.ui.EsbNodeManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
         */

         
                     public org.esbhive.node.mgt.data.xsd.ESBNode[] getNodes(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetNodes(

            

            final org.esbhive.node.mgt.ui.EsbNodeManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    