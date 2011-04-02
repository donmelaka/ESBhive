

/**
 * EsbNodeManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.2  Built on : Sep 06, 2010 (09:42:01 CEST)
 */

    package org.wso2.carbon.esbnode.mgt;

    /*
     *  EsbNodeManager java interface
     */

    public interface EsbNodeManager {
          

        /**
          * Auto generated method signature
          * 
         */

         
                     public org.wso2.carbon.esbnode.mgt.data.xsd.EsbNode[] getEsbNodes(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetEsbNodes(

            

            final org.wso2.carbon.esbnode.mgt.EsbNodeManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  addEsbNode(
         org.wso2.carbon.esbnode.mgt.data.xsd.EsbNode esbNode5

        ) throws java.rmi.RemoteException
        
        ;

        

        
       //
       }
    