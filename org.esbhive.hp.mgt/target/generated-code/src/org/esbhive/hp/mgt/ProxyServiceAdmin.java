

/**
 * ProxyServiceAdmin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.hp.mgt;

    /*
     *  ProxyServiceAdmin java interface
     */

    public interface ProxyServiceAdmin {
          

        /**
          * Auto generated method signature
          * 
                    * @param disableStatistics30
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String disableStatistics(

                        java.lang.String proxyName31)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param disableStatistics30
            
          */
        public void startdisableStatistics(

            java.lang.String proxyName31,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

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

            

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param redeployProxyService37
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String redeployProxyService(

                        java.lang.String proxyName38)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param redeployProxyService37
            
          */
        public void startredeployProxyService(

            java.lang.String proxyName38,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param addProxy41
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String addProxy(

                        org.esbhive.hp.mgt.types.carbon.ProxyData pd42)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addProxy41
            
          */
        public void startaddProxy(

            org.esbhive.hp.mgt.types.carbon.ProxyData pd42,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param modifyProxy45
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String modifyProxy(

                        org.esbhive.hp.mgt.types.carbon.ProxyData pd46)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param modifyProxy45
            
          */
        public void startmodifyProxy(

            org.esbhive.hp.mgt.types.carbon.ProxyData pd46,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param disableTracing49
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String disableTracing(

                        java.lang.String proxyName50)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param disableTracing49
            
          */
        public void startdisableTracing(

            java.lang.String proxyName50,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getProxy53
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public org.esbhive.hp.mgt.types.carbon.ProxyData getProxy(

                        java.lang.String proxyName54)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getProxy53
            
          */
        public void startgetProxy(

            java.lang.String proxyName54,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

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

            

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getEndpoint60
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String getEndpoint(

                        java.lang.String name61)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getEndpoint60
            
          */
        public void startgetEndpoint(

            java.lang.String name61,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param startProxyService64
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String startProxyService(

                        java.lang.String proxyName65)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param startProxyService64
            
          */
        public void startstartProxyService(

            java.lang.String proxyName65,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public org.esbhive.hp.mgt.types.carbon.MetaData getMetaData(

                        )
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetMetaData(

            

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param enableTracing71
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String enableTracing(

                        java.lang.String proxyName72)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param enableTracing71
            
          */
        public void startenableTracing(

            java.lang.String proxyName72,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getSourceView75
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String getSourceView(

                        org.esbhive.hp.mgt.types.carbon.ProxyData pd76)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getSourceView75
            
          */
        public void startgetSourceView(

            org.esbhive.hp.mgt.types.carbon.ProxyData pd76,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param stopProxyService79
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String stopProxyService(

                        java.lang.String proxyName80)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param stopProxyService79
            
          */
        public void startstopProxyService(

            java.lang.String proxyName80,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param enableStatistics83
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String enableStatistics(

                        java.lang.String proxyName84)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param enableStatistics83
            
          */
        public void startenableStatistics(

            java.lang.String proxyName84,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

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

            

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param deleteProxyService90
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String deleteProxyService(

                        java.lang.String proxyName91)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param deleteProxyService90
            
          */
        public void startdeleteProxyService(

            java.lang.String proxyName91,

            final org.esbhive.hp.mgt.ProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    