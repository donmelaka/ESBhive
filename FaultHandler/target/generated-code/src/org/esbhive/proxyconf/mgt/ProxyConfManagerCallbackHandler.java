
/**
 * ProxyConfManagerCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.proxyconf.mgt;

    /**
     *  ProxyConfManagerCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ProxyConfManagerCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ProxyConfManagerCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ProxyConfManagerCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getProEsb method
            * override this method for handling normal response from getProEsb operation
            */
           public void receiveResultgetProEsb(
                    org.esbhive.proxyconf.mgt.xsd.ProEsb result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getProEsb operation
           */
            public void receiveErrorgetProEsb(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getProxyDataList method
            * override this method for handling normal response from getProxyDataList operation
            */
           public void receiveResultgetProxyDataList(
                    org.esbhive.proxyconf.mgt.xsd.ProxyDataList result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getProxyDataList operation
           */
            public void receiveErrorgetProxyDataList(java.lang.Exception e) {
            }
                


    }
    