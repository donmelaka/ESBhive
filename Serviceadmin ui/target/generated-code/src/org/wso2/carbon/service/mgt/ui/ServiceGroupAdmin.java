

/**
 * ServiceGroupAdmin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.wso2.carbon.service.mgt.ui;

    /*
     *  ServiceGroupAdmin java interface
     */

    public interface ServiceGroupAdmin {
          

        /**
          * Auto generated method signature
          * 
                    * @param listServiceGroup15
                
             * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */

         
                     public org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaData listServiceGroup(

                        java.lang.String serviceGroupName16)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.Exception;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param listServiceGroup15
            
          */
        public void startlistServiceGroup(

            java.lang.String serviceGroupName16,

            final org.wso2.carbon.service.mgt.ui.ServiceGroupAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  setServiceGroupParameters(
         java.lang.String serviceGroupId20,java.lang.String[] parameterElement21

        ) throws java.rmi.RemoteException
        
        ;

        

        /**
          * Auto generated method signature
          * 
                    * @param getServiceGroupParameter22
                
             * @throws org.wso2.carbon.service.mgt.ui.ServerException : 
         */

         
                     public org.wso2.carbon.service.mgt.ui.types.carbon.ParameterMetaData getServiceGroupParameter(

                        java.lang.String serviceGroupName23,java.lang.String paramName24)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.ServerException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getServiceGroupParameter22
            
          */
        public void startgetServiceGroupParameter(

            java.lang.String serviceGroupName23,java.lang.String paramName24,

            final org.wso2.carbon.service.mgt.ui.ServiceGroupAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param configureServiceGroupMTOM27
                
             * @throws org.wso2.carbon.service.mgt.ui.Exception : 
         */

         
                     public org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaData configureServiceGroupMTOM(

                        java.lang.String flag28,java.lang.String serviceGroupName29)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.Exception;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param configureServiceGroupMTOM27
            
          */
        public void startconfigureServiceGroupMTOM(

            java.lang.String flag28,java.lang.String serviceGroupName29,

            final org.wso2.carbon.service.mgt.ui.ServiceGroupAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  removeServiceGroupParameter(
         java.lang.String serviceGroupId33,java.lang.String parameterName34

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.ServerException : 
         */
        public void  updateServiceGroupParamters(
         java.lang.String serviceGroupName36,org.wso2.carbon.service.mgt.ui.types.carbon.ParameterMetaData[] params37

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.ServerException;

        

        /**
          * Auto generated method signature
          * 
                    * @param dumpAAR38
                
         */

         
                     public java.lang.String dumpAAR(

                        java.lang.String serviceGroupName39)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param dumpAAR38
            
          */
        public void startdumpAAR(

            java.lang.String serviceGroupName39,

            final org.wso2.carbon.service.mgt.ui.ServiceGroupAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
         */
        public void  setServiceGroupParameter(
         java.lang.String serviceGroupId43,java.lang.String parameterElement44

        ) throws java.rmi.RemoteException
        
        ;

        
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.wso2.carbon.service.mgt.ui.ServerException : 
         */
        public void  updateServiceGroupParameter(
         java.lang.String serviceGroupName46,org.wso2.carbon.service.mgt.ui.types.carbon.ParameterMetaData paramMetaData47

        ) throws java.rmi.RemoteException
        
        
               ,org.wso2.carbon.service.mgt.ui.ServerException;

        

        /**
          * Auto generated method signature
          * 
         */

         
                     public void listServiceGroups(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
          */
        public void startlistServiceGroups(

            

            final org.wso2.carbon.service.mgt.ui.ServiceGroupAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getServiceGroupParameters50
                
             * @throws org.wso2.carbon.service.mgt.ui.ServerException : 
         */

         
                     public java.lang.String[] getServiceGroupParameters(

                        java.lang.String serviceGroupName51)
                        throws java.rmi.RemoteException
             
          ,org.wso2.carbon.service.mgt.ui.ServerException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getServiceGroupParameters50
            
          */
        public void startgetServiceGroupParameters(

            java.lang.String serviceGroupName51,

            final org.wso2.carbon.service.mgt.ui.ServiceGroupAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    