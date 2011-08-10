

/**
 * ServiceAdmin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.wso2.carbon.service.mgt.ui;

    /*
     *  ServiceAdmin java interface
     */

    public interface ServiceAdmin {
          
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  startService(
         java.lang.String serviceName81

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        

        /**
          * Auto generated method signature
          * 
                    * @param getExposedTransports82
                
         */

         
                     public java.lang.String[] getExposedTransports(

                        java.lang.String serviceId83)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getExposedTransports82
            
          */
        public void startgetExposedTransports(

            java.lang.String serviceId83,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param listServiceGroup86
                
         */

         
                     public org.esbhive.hp.mgt.carbon.service.ServiceGroupMetaData listServiceGroup(

                        java.lang.String serviceGroupName87)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param listServiceGroup86
            
          */
        public void startlistServiceGroup(

            java.lang.String serviceGroupName87,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param addTransportBinding90
                
             * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */

         
                     public java.lang.String addTransportBinding(

                        java.lang.String serviceId91,java.lang.String transportProtocol92)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.Exception;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param addTransportBinding90
            
          */
        public void startaddTransportBinding(

            java.lang.String serviceId91,java.lang.String transportProtocol92,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
         */

         
                     public int getNumberOfFaultyServices(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetNumberOfFaultyServices(

            

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  configureMTOM(
         java.lang.String flag99,java.lang.String serviceName100

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  setServicePolicy(
         java.lang.String serviceName102,java.lang.String policyString103

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        

        /**
          * Auto generated method signature
          * 
                    * @param getPolicy104
                
         */

         
                     public java.lang.String getPolicy(

                        java.lang.String serviceName105)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getPolicy104
            
          */
        public void startgetPolicy(

            java.lang.String serviceName105,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getFaultyServiceArchives108
                
         */

         
                     public org.esbhive.hp.mgt.carbon.service.FaultyServicesWrapper getFaultyServiceArchives(

                        int pageNumber109)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getFaultyServiceArchives108
            
          */
        public void startgetFaultyServiceArchives(

            int pageNumber109,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  addPoliciesToService(
         java.lang.String serviceName113,org.esbhive.hp.mgt.carbon.neethi.Policy policy114,int policyType115,java.lang.String[] modulePaths116

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        

        /**
          * Auto generated method signature
          * 
                    * @param getServiceParameters117
                
             * @throws org.wso2.carbon.service.mgt.ui.ServerException : 
         */

         
                     public java.lang.String[] getServiceParameters(

                        java.lang.String serviceName118)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.ServerException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getServiceParameters117
            
          */
        public void startgetServiceParameters(

            java.lang.String serviceName118,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  setBindingOperationMessagePolicy(
         java.lang.String serviceName122,java.lang.String bindingName123,java.lang.String operationName124,java.lang.String messageType125,java.lang.String policyString126

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  setBindingPolicy(
         java.lang.String serviceName128,java.lang.String bindingName129,java.lang.String policyString130

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  removeServicePoliciesByNamespace(
         java.lang.String serviceName132,java.lang.String namesapce133

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  setBindingOperationPolicy(
         java.lang.String serviceName135,java.lang.String bindingName136,java.lang.String operationName137,java.lang.String policyString138

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  setModulePolicy(
         java.lang.String moduleName140,java.lang.String moduleVersion141,java.lang.String policyString142

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        

        /**
          * Auto generated method signature
          * 
                    * @param getBindingOperationMessagePolicy143
                
         */

         
                     public java.lang.String getBindingOperationMessagePolicy(

                        java.lang.String serviceName144,java.lang.String bindingName145,java.lang.String operationName146,java.lang.String messageType147)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getBindingOperationMessagePolicy143
            
          */
        public void startgetBindingOperationMessagePolicy(

            java.lang.String serviceName144,java.lang.String bindingName145,java.lang.String operationName146,java.lang.String messageType147,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  changeServiceState(
         java.lang.String serviceName151,boolean isActive152

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  setServiceParameters(
         java.lang.String serviceName154,java.lang.String[] parameters155

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
                    * @param getOperationMessagePolicy156
                
         */

         
                     public java.lang.String getOperationMessagePolicy(

                        java.lang.String serviceName157,java.lang.String operationName158,java.lang.String messageType159)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getOperationMessagePolicy156
            
          */
        public void startgetOperationMessagePolicy(

            java.lang.String serviceName157,java.lang.String operationName158,java.lang.String messageType159,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
             * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */

         
                     public int getNumberOfActiveServices(

                        )
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.Exception;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetNumberOfActiveServices(

            

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getServiceData165
                
         */

         
                     public org.esbhive.hp.mgt.carbon.service.ServiceMetaData getServiceData(

                        java.lang.String serviceName166)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getServiceData165
            
          */
        public void startgetServiceData(

            java.lang.String serviceName166,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param removeTransportBinding169
                
             * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */

         
                     public java.lang.String removeTransportBinding(

                        java.lang.String serviceId170,java.lang.String transportProtocol171)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.Exception;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param removeTransportBinding169
            
          */
        public void startremoveTransportBinding(

            java.lang.String serviceId170,java.lang.String transportProtocol171,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getPolicies174
                
         */

         
                     public org.esbhive.hp.mgt.carbon.service.PolicyMetaData[] getPolicies(

                        java.lang.String serviceName175)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getPolicies174
            
          */
        public void startgetPolicies(

            java.lang.String serviceName175,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  removeServiceParameter(
         java.lang.String serviceName179,java.lang.String parameterName180

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
                    * @param getBindingOperationPolicy181
                
         */

         
                     public java.lang.String getBindingOperationPolicy(

                        java.lang.String serviceName182,java.lang.String bindingName183,java.lang.String operationName184)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getBindingOperationPolicy181
            
          */
        public void startgetBindingOperationPolicy(

            java.lang.String serviceName182,java.lang.String bindingName183,java.lang.String operationName184,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteServiceGroups(
         java.lang.String[] serviceGroups188

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
             * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */

         
                     public int getNumberOfInactiveServices(

                        )
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.Exception;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startgetNumberOfInactiveServices(

            

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  setServiceOperationPolicy(
         java.lang.String serviceName193,java.lang.String operationName194,java.lang.String policyString195

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        

        /**
          * Auto generated method signature
          * 
                    * @param deleteFaultyServiceGroup196
                
         */

         
                     public boolean deleteFaultyServiceGroup(

                        java.lang.String archiveName197)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param deleteFaultyServiceGroup196
            
          */
        public void startdeleteFaultyServiceGroup(

            java.lang.String archiveName197,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  engageModuleToService(
         java.lang.String serviceName201,java.lang.String moduleName202,java.lang.String version203

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteFaultyServiceGroups(
         java.lang.String[] fileNames205

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.ServerException : 
         */
        public void  removeBindingPolicy(
         java.lang.String serviceName207,java.lang.String policyKey208,java.lang.String[] moduleNames209

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.ServerException;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteAllFaultyServiceGroups(
         

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  deleteAllNonAdminServiceGroups(
         

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
                    * @param getModulePolicy212
                
         */

         
                     public java.lang.String getModulePolicy(

                        java.lang.String moduleName213,java.lang.String moduleVersion214)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getModulePolicy212
            
          */
        public void startgetModulePolicy(

            java.lang.String moduleName213,java.lang.String moduleVersion214,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  setPolicy(
         java.lang.String serviceName218,java.lang.String policyString219

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        

        /**
          * Auto generated method signature
          * 
                    * @param listServiceGroups220
                
         */

         
                     public org.esbhive.hp.mgt.carbon.service.ServiceGroupMetaDataWrapper listServiceGroups(

                        java.lang.String serviceTypeFilter221,java.lang.String serviceGroupSearchString222,int pageNumber223)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param listServiceGroups220
            
          */
        public void startlistServiceGroups(

            java.lang.String serviceTypeFilter221,java.lang.String serviceGroupSearchString222,int pageNumber223,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  setServiceOperationMessagePolicy(
         java.lang.String serviceName227,java.lang.String operationName228,java.lang.String messageType229,java.lang.String policyString230

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        

        /**
          * Auto generated method signature
          * 
                    * @param getBindingPolicy231
                
         */

         
                     public java.lang.String getBindingPolicy(

                        java.lang.String serviceName232,java.lang.String bindingName233)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getBindingPolicy231
            
          */
        public void startgetBindingPolicy(

            java.lang.String serviceName232,java.lang.String bindingName233,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getServiceBindings236
                
             * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */

         
                     public java.lang.String[] getServiceBindings(

                        java.lang.String serviceName237)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.Exception;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getServiceBindings236
            
          */
        public void startgetServiceBindings(

            java.lang.String serviceName237,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */
        public void  stopService(
         java.lang.String serviceName241

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.Exception;

        

        /**
          * Auto generated method signature
          * 
                    * @param getOperationPolicy242
                
         */

         
                     public java.lang.String getOperationPolicy(

                        java.lang.String serviceName243,java.lang.String operationName244)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getOperationPolicy242
            
          */
        public void startgetOperationPolicy(

            java.lang.String serviceName243,java.lang.String operationName244,

            final org.wso2.carbon.service.mgt.ui.ServiceAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    