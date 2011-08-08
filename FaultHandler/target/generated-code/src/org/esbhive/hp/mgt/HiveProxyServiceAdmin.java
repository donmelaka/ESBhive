

/**
 * HiveProxyServiceAdmin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 03, 2010 (02:59:42 IST)
 */

    package org.esbhive.hp.mgt;

    /*
     *  HiveProxyServiceAdmin java interface
     */

    public interface HiveProxyServiceAdmin {
          

        /**
          * Auto generated method signature
          * 
                    * @param stopProxyService31
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String stopProxyService(

                        java.lang.String proxyName32)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param stopProxyService31
            
          */
        public void startstopProxyService(

            java.lang.String proxyName32,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param modifyProxy35
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String modifyProxy(

                        org.wso2.carbon.proxyadmin.xsd.ProxyData pd36)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param modifyProxy35
            
          */
        public void startmodifyProxy(

            org.wso2.carbon.proxyadmin.xsd.ProxyData pd36,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String[] getAvailableEndpoints(

                        )
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetAvailableEndpoints(

            

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param redeployProxyService42
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String redeployProxyService(

                        java.lang.String proxyName43)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param redeployProxyService42
            
          */
        public void startredeployProxyService(

            java.lang.String proxyName43,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param deleteProxyService46
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String deleteProxyService(

                        java.lang.String proxyName47)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param deleteProxyService46
            
          */
        public void startdeleteProxyService(

            java.lang.String proxyName47,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getSourceView50
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String getSourceView(

                        org.wso2.carbon.proxyadmin.xsd.ProxyData pd51)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getSourceView50
            
          */
        public void startgetSourceView(

            org.wso2.carbon.proxyadmin.xsd.ProxyData pd51,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getProxy54
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public org.wso2.carbon.proxyadmin.xsd.ProxyData getProxy(

                        java.lang.String proxyName55)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getProxy54
            
          */
        public void startgetProxy(

            java.lang.String proxyName55,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  unsetNodeManager(
         org.esbhive.node.mgt.xsd.NodeManagerInterface r59

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String[] getAvailableSequences(

                        )
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetAvailableSequences(

            

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String[] getAvailableTransports(

                        )
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetAvailableTransports(

            

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  setNodeManager(
         org.esbhive.node.mgt.xsd.NodeManagerInterface r67

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
                    * @param getEndpoint68
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String getEndpoint(

                        java.lang.String name69)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getEndpoint68
            
          */
        public void startgetEndpoint(

            java.lang.String name69,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param disableStatistics72
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String disableStatistics(

                        java.lang.String proxyName73)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param disableStatistics72
            
          */
        public void startdisableStatistics(

            java.lang.String proxyName73,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param addProxy76
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String addProxy(

                        org.wso2.carbon.proxyadmin.xsd.ProxyData pd77)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addProxy76
            
          */
        public void startaddProxy(

            org.wso2.carbon.proxyadmin.xsd.ProxyData pd77,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public org.wso2.carbon.proxyadmin.xsd.MetaData getMetaData(

                        )
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetMetaData(

            

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param startProxyService83
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String startProxyService(

                        java.lang.String proxyName84)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param startProxyService83
            
          */
        public void startstartProxyService(

            java.lang.String proxyName84,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param enableTracing87
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String enableTracing(

                        java.lang.String proxyName88)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param enableTracing87
            
          */
        public void startenableTracing(

            java.lang.String proxyName88,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param disableTracing91
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String disableTracing(

                        java.lang.String proxyName92)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param disableTracing91
            
          */
        public void startdisableTracing(

            java.lang.String proxyName92,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param enableStatistics95
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String enableStatistics(

                        java.lang.String proxyName96)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param enableStatistics95
            
          */
        public void startenableStatistics(

            java.lang.String proxyName96,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    