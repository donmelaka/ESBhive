

/**
 * HiveProxyServiceAdmin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.hp.mgt;

    /*
     *  HiveProxyServiceAdmin java interface
     */

    public interface HiveProxyServiceAdmin {
          

        /**
          * Auto generated method signature
          * 
                    * @param stopProxyService33
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String stopProxyService(

                        java.lang.String proxyName34)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param stopProxyService33
            
          */
        public void startstopProxyService(

            java.lang.String proxyName34,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param modifyProxy37
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String modifyProxy(

                        org.wso2.carbon.proxyadmin.xsd.ProxyData pd38)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param modifyProxy37
            
          */
        public void startmodifyProxy(

            org.wso2.carbon.proxyadmin.xsd.ProxyData pd38,

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
                    * @param redeployProxyService44
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String redeployProxyService(

                        java.lang.String proxyName45)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param redeployProxyService44
            
          */
        public void startredeployProxyService(

            java.lang.String proxyName45,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param deleteProxyService48
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String deleteProxyService(

                        java.lang.String proxyName49)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param deleteProxyService48
            
          */
        public void startdeleteProxyService(

            java.lang.String proxyName49,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getSourceView52
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String getSourceView(

                        org.wso2.carbon.proxyadmin.xsd.ProxyData pd53)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getSourceView52
            
          */
        public void startgetSourceView(

            org.wso2.carbon.proxyadmin.xsd.ProxyData pd53,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getProxy56
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public org.wso2.carbon.proxyadmin.xsd.ProxyData getProxy(

                        java.lang.String proxyName57)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getProxy56
            
          */
        public void startgetProxy(

            java.lang.String proxyName57,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  unsetNodeManager(
         org.esbhive.node.mgt.xsd.NodeManagerInterface r61

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
         org.esbhive.node.mgt.xsd.NodeManagerInterface r69

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
                    * @param addProxyInNewNode70
                
         */

         
                     public java.lang.String addProxyInNewNode(

                        boolean isDummy71,org.wso2.carbon.proxyadmin.xsd.ProxyData pd72,java.lang.String ipAndPort273)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addProxyInNewNode70
            
          */
        public void startaddProxyInNewNode(

            boolean isDummy71,org.wso2.carbon.proxyadmin.xsd.ProxyData pd72,java.lang.String ipAndPort273,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getEndpoint76
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String getEndpoint(

                        java.lang.String name77)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getEndpoint76
            
          */
        public void startgetEndpoint(

            java.lang.String name77,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param disableStatistics80
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String disableStatistics(

                        java.lang.String proxyName81)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param disableStatistics80
            
          */
        public void startdisableStatistics(

            java.lang.String proxyName81,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param addProxy84
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String addProxy(

                        org.wso2.carbon.proxyadmin.xsd.ProxyData pd85)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addProxy84
            
          */
        public void startaddProxy(

            org.wso2.carbon.proxyadmin.xsd.ProxyData pd85,

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
                    * @param startProxyService91
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String startProxyService(

                        java.lang.String proxyName92)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param startProxyService91
            
          */
        public void startstartProxyService(

            java.lang.String proxyName92,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param enableTracing95
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String enableTracing(

                        java.lang.String proxyName96)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param enableTracing95
            
          */
        public void startenableTracing(

            java.lang.String proxyName96,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param disableTracing99
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String disableTracing(

                        java.lang.String proxyName100)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param disableTracing99
            
          */
        public void startdisableTracing(

            java.lang.String proxyName100,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param enableStatistics103
                
             * @throws org.esbhive.hp.mgt.ProxyAdminException : 
         */

         
                     public java.lang.String enableStatistics(

                        java.lang.String proxyName104)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.ProxyAdminException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param enableStatistics103
            
          */
        public void startenableStatistics(

            java.lang.String proxyName104,

            final org.esbhive.hp.mgt.HiveProxyServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    