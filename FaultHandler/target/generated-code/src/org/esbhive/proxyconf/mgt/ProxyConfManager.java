

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
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  addProxyConf(
         org.wso2.carbon.proxyadmin.xsd.ProxyData prodata4,org.esbhive.node.mgt.xsd.ESBNode[] esbarray5

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteESB(
         java.lang.String ipandport7

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
                    * @param getProEsb8
                
         */

         
                     public org.esbhive.proxyconf.mgt.xsd.ProEsb getProEsb(

                        java.lang.String proxyname9)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getProEsb8
            
          */
        public void startgetProEsb(

            java.lang.String proxyname9,

            final org.esbhive.proxyconf.mgt.ProxyConfManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteProxy(
         java.lang.String proxyname13

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
                    * @param getProxyDataList14
                
         */

         
                     public org.esbhive.proxyconf.mgt.xsd.ProxyDataList getProxyDataList(

                        java.lang.String ipandport15)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getProxyDataList14
            
          */
        public void startgetProxyDataList(

            java.lang.String ipandport15,

            final org.esbhive.proxyconf.mgt.ProxyConfManagerCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    