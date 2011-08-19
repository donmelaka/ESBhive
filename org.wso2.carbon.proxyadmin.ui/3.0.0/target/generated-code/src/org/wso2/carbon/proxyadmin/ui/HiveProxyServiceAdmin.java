

/**
 * HiveProxyServiceAdmin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 03, 2010 (02:59:42 IST)
 */

    package org.wso2.carbon.proxyadmin.ui;

    /*
     *  HiveProxyServiceAdmin java interface
     */

    public interface HiveProxyServiceAdmin {
          

        /**
          * Auto generated method signature
          * 
                    * @param stopProxyService30
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String stopProxyService(

                        java.lang.String proxyName31)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param stopProxyService30
            
          */
        public void startstopProxyService(

            java.lang.String proxyName31,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param modifyProxy34
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String modifyProxy(

                        org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData pd35)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param modifyProxy34
            
          */
        public void startmodifyProxy(

            org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData pd35,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String[] getAvailableEndpoints(

                        )
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetAvailableEndpoints(

            

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param redeployProxyService41
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String redeployProxyService(

                        java.lang.String proxyName42)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param redeployProxyService41
            
          */
        public void startredeployProxyService(

            java.lang.String proxyName42,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param deleteProxyService45
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String deleteProxyService(

                        java.lang.String proxyName46)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param deleteProxyService45
            
          */
        public void startdeleteProxyService(

            java.lang.String proxyName46,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getSourceView49
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String getSourceView(

                        org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData pd50)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getSourceView49
            
          */
        public void startgetSourceView(

            org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData pd50,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getProxy53
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData getProxy(

                        java.lang.String proxyName54)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getProxy53
            
          */
        public void startgetProxy(

            java.lang.String proxyName54,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String[] getAvailableSequences(

                        )
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetAvailableSequences(

            

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String[] getAvailableTransports(

                        )
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetAvailableTransports(

            

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param disableStatistics63
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String disableStatistics(

                        java.lang.String proxyName64)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param disableStatistics63
            
          */
        public void startdisableStatistics(

            java.lang.String proxyName64,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getEndpoint67
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String getEndpoint(

                        java.lang.String name68)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getEndpoint67
            
          */
        public void startgetEndpoint(

            java.lang.String name68,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param addProxy71
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String addProxy(

                        org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData pd72)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addProxy71
            
          */
        public void startaddProxy(

            org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData pd72,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public org.wso2.carbon.proxyadmin.ui.types.carbon.MetaData getMetaData(

                        )
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetMetaData(

            

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param startProxyService78
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String startProxyService(

                        java.lang.String proxyName79)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param startProxyService78
            
          */
        public void startstartProxyService(

            java.lang.String proxyName79,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param enableTracing82
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String enableTracing(

                        java.lang.String proxyName83)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param enableTracing82
            
          */
        public void startenableTracing(

            java.lang.String proxyName83,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param disableTracing86
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String disableTracing(

                        java.lang.String proxyName87)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param disableTracing86
            
          */
        public void startdisableTracing(

            java.lang.String proxyName87,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param enableStatistics90
                
             * @throws org.wso2.carbon.proxyadmin.ui.ProxyAdminException : 
         */

         
                     public java.lang.String enableStatistics(

                        java.lang.String proxyName91)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.proxyadmin.ui.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param enableStatistics90
            
          */
        public void startenableStatistics(

            java.lang.String proxyName91,

            final org.wso2.carbon.proxyadmin.ui.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    