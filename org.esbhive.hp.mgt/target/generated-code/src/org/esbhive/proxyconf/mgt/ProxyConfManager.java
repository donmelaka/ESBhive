

/**
 * ProxyConfManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.proxyconf.mgt;

    /*
     *  ProxyConfManager java interface
     */

    public interface ProxyConfManager {
          

        /**
          * Auto generated method signature
          * 
         */

         
                     public org.esbhive.node.mgt.xsd.ESBNode[] getEsbNodeList(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetEsbNodeList(

            

            final org.esbhive.proxyconf.mgt.ProxyConfManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  addNodeToMap(
         org.wso2.carbon.proxyadmin.xsd.ProxyData proxyData9,org.esbhive.node.mgt.xsd.ESBNode esbNode10

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
         */

         
                     public org.esbhive.proxyconf.mgt.xsd.ProEsb[] getlist(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetlist(

            

            final org.esbhive.proxyconf.mgt.ProxyConfManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteNode(
         java.lang.String proxyname15

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
         */

         
                     public org.wso2.carbon.proxyadmin.xsd.ProxyData[] getProxyDataList(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetProxyDataList(

            

            final org.esbhive.proxyconf.mgt.ProxyConfManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    