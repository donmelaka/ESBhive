

/**
 * AuthenticationAdmin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 03, 2010 (02:59:42 IST)
 */

    package org.esbhive.hp.mgt.authenticator.proxy;

    /*
     *  AuthenticationAdmin java interface
     */

    public interface AuthenticationAdmin {
          
       /**
         * Auto generated method signature for Asynchronous Invocations
         * 
                 * @throws org.esbhive.hp.mgt.authenticator.proxy.AuthenticationExceptionException : 
         */
        public void  logout(
         

        ) throws java.rmi.RemoteException
        
        
               ,org.esbhive.hp.mgt.authenticator.proxy.AuthenticationExceptionException;

        

        /**
          * Auto generated method signature
          * 
                    * @param login2
                
             * @throws org.esbhive.hp.mgt.authenticator.proxy.AuthenticationExceptionException : 
         */

         
                     public boolean login(

                        java.lang.String username3,java.lang.String password4,java.lang.String remoteAddress5)
                        throws java.rmi.RemoteException
             
          ,org.esbhive.hp.mgt.authenticator.proxy.AuthenticationExceptionException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param login2
            
          */
        public void startlogin(

            java.lang.String username3,java.lang.String password4,java.lang.String remoteAddress5,

            final org.esbhive.hp.mgt.authenticator.proxy.AuthenticationAdminCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    