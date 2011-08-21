
/**
 * HiveProxyServiceAdminCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.esbhive.hp.mgt;

    /**
     *  HiveProxyServiceAdminCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class HiveProxyServiceAdminCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public HiveProxyServiceAdminCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public HiveProxyServiceAdminCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for stopProxyService method
            * override this method for handling normal response from stopProxyService operation
            */
           public void receiveResultstopProxyService(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stopProxyService operation
           */
            public void receiveErrorstopProxyService(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyProxy method
            * override this method for handling normal response from modifyProxy operation
            */
           public void receiveResultmodifyProxy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyProxy operation
           */
            public void receiveErrormodifyProxy(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAvailableEndpoints method
            * override this method for handling normal response from getAvailableEndpoints operation
            */
           public void receiveResultgetAvailableEndpoints(
                    java.lang.String[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAvailableEndpoints operation
           */
            public void receiveErrorgetAvailableEndpoints(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for redeployProxyService method
            * override this method for handling normal response from redeployProxyService operation
            */
           public void receiveResultredeployProxyService(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from redeployProxyService operation
           */
            public void receiveErrorredeployProxyService(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteProxyService method
            * override this method for handling normal response from deleteProxyService operation
            */
           public void receiveResultdeleteProxyService(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteProxyService operation
           */
            public void receiveErrordeleteProxyService(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSourceView method
            * override this method for handling normal response from getSourceView operation
            */
           public void receiveResultgetSourceView(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSourceView operation
           */
            public void receiveErrorgetSourceView(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getProxy method
            * override this method for handling normal response from getProxy operation
            */
           public void receiveResultgetProxy(
                    org.wso2.carbon.proxyadmin.xsd.ProxyData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getProxy operation
           */
            public void receiveErrorgetProxy(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getAvailableSequences method
            * override this method for handling normal response from getAvailableSequences operation
            */
           public void receiveResultgetAvailableSequences(
                    java.lang.String[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAvailableSequences operation
           */
            public void receiveErrorgetAvailableSequences(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAvailableTransports method
            * override this method for handling normal response from getAvailableTransports operation
            */
           public void receiveResultgetAvailableTransports(
                    java.lang.String[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAvailableTransports operation
           */
            public void receiveErrorgetAvailableTransports(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for addProxyInNewNode method
            * override this method for handling normal response from addProxyInNewNode operation
            */
           public void receiveResultaddProxyInNewNode(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addProxyInNewNode operation
           */
            public void receiveErroraddProxyInNewNode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getEndpoint method
            * override this method for handling normal response from getEndpoint operation
            */
           public void receiveResultgetEndpoint(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getEndpoint operation
           */
            public void receiveErrorgetEndpoint(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for disableStatistics method
            * override this method for handling normal response from disableStatistics operation
            */
           public void receiveResultdisableStatistics(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from disableStatistics operation
           */
            public void receiveErrordisableStatistics(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addProxy method
            * override this method for handling normal response from addProxy operation
            */
           public void receiveResultaddProxy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addProxy operation
           */
            public void receiveErroraddProxy(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMetaData method
            * override this method for handling normal response from getMetaData operation
            */
           public void receiveResultgetMetaData(
                    org.wso2.carbon.proxyadmin.xsd.MetaData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMetaData operation
           */
            public void receiveErrorgetMetaData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startProxyService method
            * override this method for handling normal response from startProxyService operation
            */
           public void receiveResultstartProxyService(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startProxyService operation
           */
            public void receiveErrorstartProxyService(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enableTracing method
            * override this method for handling normal response from enableTracing operation
            */
           public void receiveResultenableTracing(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enableTracing operation
           */
            public void receiveErrorenableTracing(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for disableTracing method
            * override this method for handling normal response from disableTracing operation
            */
           public void receiveResultdisableTracing(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from disableTracing operation
           */
            public void receiveErrordisableTracing(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enableStatistics method
            * override this method for handling normal response from enableStatistics operation
            */
           public void receiveResultenableStatistics(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enableStatistics operation
           */
            public void receiveErrorenableStatistics(java.lang.Exception e) {
            }
                


    }
    