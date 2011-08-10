
/**
 * ServiceAdminCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.wso2.carbon.service.mgt.ui;

    /**
     *  ServiceAdminCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ServiceAdminCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ServiceAdminCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ServiceAdminCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getExposedTransports method
            * override this method for handling normal response from getExposedTransports operation
            */
           public void receiveResultgetExposedTransports(
                    java.lang.String[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getExposedTransports operation
           */
            public void receiveErrorgetExposedTransports(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for listServiceGroup method
            * override this method for handling normal response from listServiceGroup operation
            */
           public void receiveResultlistServiceGroup(
                    org.esbhive.hp.mgt.carbon.service.ServiceGroupMetaData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listServiceGroup operation
           */
            public void receiveErrorlistServiceGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addTransportBinding method
            * override this method for handling normal response from addTransportBinding operation
            */
           public void receiveResultaddTransportBinding(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addTransportBinding operation
           */
            public void receiveErroraddTransportBinding(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumberOfFaultyServices method
            * override this method for handling normal response from getNumberOfFaultyServices operation
            */
           public void receiveResultgetNumberOfFaultyServices(
                    int result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumberOfFaultyServices operation
           */
            public void receiveErrorgetNumberOfFaultyServices(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getPolicy method
            * override this method for handling normal response from getPolicy operation
            */
           public void receiveResultgetPolicy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPolicy operation
           */
            public void receiveErrorgetPolicy(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFaultyServiceArchives method
            * override this method for handling normal response from getFaultyServiceArchives operation
            */
           public void receiveResultgetFaultyServiceArchives(
                    org.esbhive.hp.mgt.carbon.service.FaultyServicesWrapper result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFaultyServiceArchives operation
           */
            public void receiveErrorgetFaultyServiceArchives(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getServiceParameters method
            * override this method for handling normal response from getServiceParameters operation
            */
           public void receiveResultgetServiceParameters(
                    java.lang.String[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getServiceParameters operation
           */
            public void receiveErrorgetServiceParameters(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getBindingOperationMessagePolicy method
            * override this method for handling normal response from getBindingOperationMessagePolicy operation
            */
           public void receiveResultgetBindingOperationMessagePolicy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getBindingOperationMessagePolicy operation
           */
            public void receiveErrorgetBindingOperationMessagePolicy(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getOperationMessagePolicy method
            * override this method for handling normal response from getOperationMessagePolicy operation
            */
           public void receiveResultgetOperationMessagePolicy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOperationMessagePolicy operation
           */
            public void receiveErrorgetOperationMessagePolicy(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumberOfActiveServices method
            * override this method for handling normal response from getNumberOfActiveServices operation
            */
           public void receiveResultgetNumberOfActiveServices(
                    int result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumberOfActiveServices operation
           */
            public void receiveErrorgetNumberOfActiveServices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getServiceData method
            * override this method for handling normal response from getServiceData operation
            */
           public void receiveResultgetServiceData(
                    org.esbhive.hp.mgt.carbon.service.ServiceMetaData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getServiceData operation
           */
            public void receiveErrorgetServiceData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for removeTransportBinding method
            * override this method for handling normal response from removeTransportBinding operation
            */
           public void receiveResultremoveTransportBinding(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removeTransportBinding operation
           */
            public void receiveErrorremoveTransportBinding(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPolicies method
            * override this method for handling normal response from getPolicies operation
            */
           public void receiveResultgetPolicies(
                    org.esbhive.hp.mgt.carbon.service.PolicyMetaData[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPolicies operation
           */
            public void receiveErrorgetPolicies(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getBindingOperationPolicy method
            * override this method for handling normal response from getBindingOperationPolicy operation
            */
           public void receiveResultgetBindingOperationPolicy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getBindingOperationPolicy operation
           */
            public void receiveErrorgetBindingOperationPolicy(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getNumberOfInactiveServices method
            * override this method for handling normal response from getNumberOfInactiveServices operation
            */
           public void receiveResultgetNumberOfInactiveServices(
                    int result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumberOfInactiveServices operation
           */
            public void receiveErrorgetNumberOfInactiveServices(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for deleteFaultyServiceGroup method
            * override this method for handling normal response from deleteFaultyServiceGroup operation
            */
           public void receiveResultdeleteFaultyServiceGroup(
                    boolean result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteFaultyServiceGroup operation
           */
            public void receiveErrordeleteFaultyServiceGroup(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getModulePolicy method
            * override this method for handling normal response from getModulePolicy operation
            */
           public void receiveResultgetModulePolicy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getModulePolicy operation
           */
            public void receiveErrorgetModulePolicy(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for listServiceGroups method
            * override this method for handling normal response from listServiceGroups operation
            */
           public void receiveResultlistServiceGroups(
                    org.esbhive.hp.mgt.carbon.service.ServiceGroupMetaDataWrapper result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listServiceGroups operation
           */
            public void receiveErrorlistServiceGroups(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getBindingPolicy method
            * override this method for handling normal response from getBindingPolicy operation
            */
           public void receiveResultgetBindingPolicy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getBindingPolicy operation
           */
            public void receiveErrorgetBindingPolicy(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getServiceBindings method
            * override this method for handling normal response from getServiceBindings operation
            */
           public void receiveResultgetServiceBindings(
                    java.lang.String[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getServiceBindings operation
           */
            public void receiveErrorgetServiceBindings(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getOperationPolicy method
            * override this method for handling normal response from getOperationPolicy operation
            */
           public void receiveResultgetOperationPolicy(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOperationPolicy operation
           */
            public void receiveErrorgetOperationPolicy(java.lang.Exception e) {
            }
                


    }
    