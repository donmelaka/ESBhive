
/**
 * ServiceGroupAdminCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:41 UTC)
 */

    package org.wso2.carbon.service.mgt.ui;

    /**
     *  ServiceGroupAdminCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ServiceGroupAdminCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ServiceGroupAdminCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ServiceGroupAdminCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for listServiceGroup method
            * override this method for handling normal response from listServiceGroup operation
            */
           public void receiveResultlistServiceGroup(
                    org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listServiceGroup operation
           */
            public void receiveErrorlistServiceGroup(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getServiceGroupParameter method
            * override this method for handling normal response from getServiceGroupParameter operation
            */
           public void receiveResultgetServiceGroupParameter(
                    org.wso2.carbon.service.mgt.ui.types.carbon.ParameterMetaData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getServiceGroupParameter operation
           */
            public void receiveErrorgetServiceGroupParameter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for configureServiceGroupMTOM method
            * override this method for handling normal response from configureServiceGroupMTOM operation
            */
           public void receiveResultconfigureServiceGroupMTOM(
                    org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from configureServiceGroupMTOM operation
           */
            public void receiveErrorconfigureServiceGroupMTOM(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for dumpAAR method
            * override this method for handling normal response from dumpAAR operation
            */
           public void receiveResultdumpAAR(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dumpAAR operation
           */
            public void receiveErrordumpAAR(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for listServiceGroups method
            * override this method for handling normal response from listServiceGroups operation
            */
           public void receiveResultlistServiceGroups(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listServiceGroups operation
           */
            public void receiveErrorlistServiceGroups(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getServiceGroupParameters method
            * override this method for handling normal response from getServiceGroupParameters operation
            */
           public void receiveResultgetServiceGroupParameters(
                    java.lang.String[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getServiceGroupParameters operation
           */
            public void receiveErrorgetServiceGroupParameters(java.lang.Exception e) {
            }
                


    }
    