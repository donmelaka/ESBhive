

/**
 * AuthenticationAdmin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.login.client;

    /*
     *  AuthenticationAdmin java interface
     */

    public interface AuthenticationAdmin {
          
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.esbhive.login.client.AuthenticationExceptionException : 
         */
        public void  logout(
         

        ) throws java.rmi.RemoteException
        
        
               ,org.esbhive.login.client.AuthenticationExceptionException;

        

        /**
          * Auto generated method signature
          * 
                    * @param login2
                
             * @throws org.esbhive.login.client.AuthenticationExceptionException : 
         */

         
                     public boolean login(

                        java.lang.String username3,java.lang.String password4,java.lang.String remoteAddress5)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.login.client.AuthenticationExceptionException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param login2
            
          */
        public void startlogin(

            java.lang.String username3,java.lang.String password4,java.lang.String remoteAddress5,

            final org.esbhive.login.client.AuthenticationAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    