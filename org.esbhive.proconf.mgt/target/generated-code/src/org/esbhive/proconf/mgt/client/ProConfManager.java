

/**
 * ProConfManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.proconf.mgt.client;

    /*
     *  ProConfManager java interface
     */

    public interface ProConfManager {
          
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  unsetNodeManager(
         org.esbhive.node.mgt.xsd.NodeManagerInterface r8

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
         */

         
                     public java.lang.String getHelp(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetHelp(

            

            final org.esbhive.proconf.mgt.client.ProConfManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  addToOthers(
         org.wso2.carbon.proxyadmin.xsd.ProxyData data13,org.esbhive.node.mgt.xsd.ESBNode esbnode14

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  setNodeManager(
         org.esbhive.node.mgt.xsd.NodeManagerInterface r16

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
         */

         
                     public org.esbhive.proconf.mgt.client.ProEsb[] getlist(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetlist(

            

            final org.esbhive.proconf.mgt.client.ProConfManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteNode(
         java.lang.String proxyname21

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  addNodeToMap(
         org.wso2.carbon.proxyadmin.xsd.ProxyData data23,org.esbhive.node.mgt.xsd.ESBNode esbnode24

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteOthers(
         java.lang.String proxyname26

        ) throws java.rmi.RemoteException
        
        ;

        

        
       //
       }
    