
/**
 * ProxyData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 03, 2010 (02:59:46 IST)
 */

            
                package org.wso2.carbon.proxyadmin.ui.types.carbon;
            

            /**
            *  ProxyData bean class
            */
        
        public  class ProxyData
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = ProxyData
                Namespace URI = http://proxyadmin.carbon.wso2.org/xsd
                Namespace Prefix = ns1
                */
            

                        /**
                        * field for EnableSecurity
                        */

                        
                                    protected boolean localEnableSecurity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEnableSecurityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getEnableSecurity(){
                               return localEnableSecurity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EnableSecurity
                               */
                               public void setEnableSecurity(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localEnableSecurityTracker = false;
                                              
                                       } else {
                                          localEnableSecurityTracker = true;
                                       }
                                   
                                            this.localEnableSecurity=param;
                                    

                               }
                            

                        /**
                        * field for EnableStatistics
                        */

                        
                                    protected boolean localEnableStatistics ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEnableStatisticsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getEnableStatistics(){
                               return localEnableStatistics;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EnableStatistics
                               */
                               public void setEnableStatistics(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localEnableStatisticsTracker = false;
                                              
                                       } else {
                                          localEnableStatisticsTracker = true;
                                       }
                                   
                                            this.localEnableStatistics=param;
                                    

                               }
                            

                        /**
                        * field for EnableTracing
                        */

                        
                                    protected boolean localEnableTracing ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEnableTracingTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getEnableTracing(){
                               return localEnableTracing;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EnableTracing
                               */
                               public void setEnableTracing(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localEnableTracingTracker = false;
                                              
                                       } else {
                                          localEnableTracingTracker = true;
                                       }
                                   
                                            this.localEnableTracing=param;
                                    

                               }
                            

                        /**
                        * field for EndpointKey
                        */

                        
                                    protected java.lang.String localEndpointKey ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEndpointKeyTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getEndpointKey(){
                               return localEndpointKey;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EndpointKey
                               */
                               public void setEndpointKey(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localEndpointKeyTracker = true;
                                       } else {
                                          localEndpointKeyTracker = true;
                                              
                                       }
                                   
                                            this.localEndpointKey=param;
                                    

                               }
                            

                        /**
                        * field for EndpointXML
                        */

                        
                                    protected java.lang.String localEndpointXML ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEndpointXMLTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getEndpointXML(){
                               return localEndpointXML;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EndpointXML
                               */
                               public void setEndpointXML(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localEndpointXMLTracker = true;
                                       } else {
                                          localEndpointXMLTracker = true;
                                              
                                       }
                                   
                                            this.localEndpointXML=param;
                                    

                               }
                            

                        /**
                        * field for FaultSeqKey
                        */

                        
                                    protected java.lang.String localFaultSeqKey ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localFaultSeqKeyTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getFaultSeqKey(){
                               return localFaultSeqKey;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FaultSeqKey
                               */
                               public void setFaultSeqKey(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localFaultSeqKeyTracker = true;
                                       } else {
                                          localFaultSeqKeyTracker = true;
                                              
                                       }
                                   
                                            this.localFaultSeqKey=param;
                                    

                               }
                            

                        /**
                        * field for FaultSeqXML
                        */

                        
                                    protected java.lang.String localFaultSeqXML ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localFaultSeqXMLTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getFaultSeqXML(){
                               return localFaultSeqXML;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FaultSeqXML
                               */
                               public void setFaultSeqXML(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localFaultSeqXMLTracker = true;
                                       } else {
                                          localFaultSeqXMLTracker = true;
                                              
                                       }
                                   
                                            this.localFaultSeqXML=param;
                                    

                               }
                            

                        /**
                        * field for InSeqKey
                        */

                        
                                    protected java.lang.String localInSeqKey ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localInSeqKeyTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getInSeqKey(){
                               return localInSeqKey;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param InSeqKey
                               */
                               public void setInSeqKey(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localInSeqKeyTracker = true;
                                       } else {
                                          localInSeqKeyTracker = true;
                                              
                                       }
                                   
                                            this.localInSeqKey=param;
                                    

                               }
                            

                        /**
                        * field for InSeqXML
                        */

                        
                                    protected java.lang.String localInSeqXML ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localInSeqXMLTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getInSeqXML(){
                               return localInSeqXML;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param InSeqXML
                               */
                               public void setInSeqXML(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localInSeqXMLTracker = true;
                                       } else {
                                          localInSeqXMLTracker = true;
                                              
                                       }
                                   
                                            this.localInSeqXML=param;
                                    

                               }
                            

                        /**
                        * field for Name
                        */

                        
                                    protected java.lang.String localName ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localNameTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getName(){
                               return localName;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Name
                               */
                               public void setName(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localNameTracker = true;
                                       } else {
                                          localNameTracker = true;
                                              
                                       }
                                   
                                            this.localName=param;
                                    

                               }
                            

                        /**
                        * field for OutSeqKey
                        */

                        
                                    protected java.lang.String localOutSeqKey ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOutSeqKeyTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOutSeqKey(){
                               return localOutSeqKey;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OutSeqKey
                               */
                               public void setOutSeqKey(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOutSeqKeyTracker = true;
                                       } else {
                                          localOutSeqKeyTracker = true;
                                              
                                       }
                                   
                                            this.localOutSeqKey=param;
                                    

                               }
                            

                        /**
                        * field for OutSeqXML
                        */

                        
                                    protected java.lang.String localOutSeqXML ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOutSeqXMLTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOutSeqXML(){
                               return localOutSeqXML;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OutSeqXML
                               */
                               public void setOutSeqXML(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOutSeqXMLTracker = true;
                                       } else {
                                          localOutSeqXMLTracker = true;
                                              
                                       }
                                   
                                            this.localOutSeqXML=param;
                                    

                               }
                            

                        /**
                        * field for PinnedServers
                        * This was an Array!
                        */

                        
                                    protected java.lang.String[] localPinnedServers ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPinnedServersTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String[]
                           */
                           public  java.lang.String[] getPinnedServers(){
                               return localPinnedServers;
                           }

                           
                        


                               
                              /**
                               * validate the array for PinnedServers
                               */
                              protected void validatePinnedServers(java.lang.String[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param PinnedServers
                              */
                              public void setPinnedServers(java.lang.String[] param){
                              
                                   validatePinnedServers(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localPinnedServersTracker = true;
                                          } else {
                                             localPinnedServersTracker = true;
                                                 
                                          }
                                      
                                      this.localPinnedServers=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param java.lang.String
                             */
                             public void addPinnedServers(java.lang.String param){
                                   if (localPinnedServers == null){
                                   localPinnedServers = new java.lang.String[]{};
                                   }

                            
                                 //update the setting tracker
                                localPinnedServersTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localPinnedServers);
                               list.add(param);
                               this.localPinnedServers =
                             (java.lang.String[])list.toArray(
                            new java.lang.String[list.size()]);

                             }
                             

                        /**
                        * field for Policies
                        * This was an Array!
                        */

                        
                                    protected org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[] localPolicies ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPoliciesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[]
                           */
                           public  org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[] getPolicies(){
                               return localPolicies;
                           }

                           
                        


                               
                              /**
                               * validate the array for Policies
                               */
                              protected void validatePolicies(org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Policies
                              */
                              public void setPolicies(org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[] param){
                              
                                   validatePolicies(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localPoliciesTracker = true;
                                          } else {
                                             localPoliciesTracker = true;
                                                 
                                          }
                                      
                                      this.localPolicies=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo
                             */
                             public void addPolicies(org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo param){
                                   if (localPolicies == null){
                                   localPolicies = new org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[]{};
                                   }

                            
                                 //update the setting tracker
                                localPoliciesTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localPolicies);
                               list.add(param);
                               this.localPolicies =
                             (org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[])list.toArray(
                            new org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[list.size()]);

                             }
                             

                        /**
                        * field for Running
                        */

                        
                                    protected boolean localRunning ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRunningTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getRunning(){
                               return localRunning;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Running
                               */
                               public void setRunning(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localRunningTracker = false;
                                              
                                       } else {
                                          localRunningTracker = true;
                                       }
                                   
                                            this.localRunning=param;
                                    

                               }
                            

                        /**
                        * field for ServiceGroup
                        */

                        
                                    protected java.lang.String localServiceGroup ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localServiceGroupTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getServiceGroup(){
                               return localServiceGroup;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ServiceGroup
                               */
                               public void setServiceGroup(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localServiceGroupTracker = true;
                                       } else {
                                          localServiceGroupTracker = true;
                                              
                                       }
                                   
                                            this.localServiceGroup=param;
                                    

                               }
                            

                        /**
                        * field for ServiceParams
                        * This was an Array!
                        */

                        
                                    protected org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[] localServiceParams ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localServiceParamsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[]
                           */
                           public  org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[] getServiceParams(){
                               return localServiceParams;
                           }

                           
                        


                               
                              /**
                               * validate the array for ServiceParams
                               */
                              protected void validateServiceParams(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param ServiceParams
                              */
                              public void setServiceParams(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[] param){
                              
                                   validateServiceParams(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localServiceParamsTracker = true;
                                          } else {
                                             localServiceParamsTracker = true;
                                                 
                                          }
                                      
                                      this.localServiceParams=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param org.wso2.carbon.proxyadmin.ui.types.carbon.Entry
                             */
                             public void addServiceParams(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry param){
                                   if (localServiceParams == null){
                                   localServiceParams = new org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[]{};
                                   }

                            
                                 //update the setting tracker
                                localServiceParamsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localServiceParams);
                               list.add(param);
                               this.localServiceParams =
                             (org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[])list.toArray(
                            new org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[list.size()]);

                             }
                             

                        /**
                        * field for StartOnLoad
                        */

                        
                                    protected boolean localStartOnLoad ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStartOnLoadTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getStartOnLoad(){
                               return localStartOnLoad;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StartOnLoad
                               */
                               public void setStartOnLoad(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localStartOnLoadTracker = false;
                                              
                                       } else {
                                          localStartOnLoadTracker = true;
                                       }
                                   
                                            this.localStartOnLoad=param;
                                    

                               }
                            

                        /**
                        * field for Transports
                        * This was an Array!
                        */

                        
                                    protected java.lang.String[] localTransports ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTransportsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String[]
                           */
                           public  java.lang.String[] getTransports(){
                               return localTransports;
                           }

                           
                        


                               
                              /**
                               * validate the array for Transports
                               */
                              protected void validateTransports(java.lang.String[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Transports
                              */
                              public void setTransports(java.lang.String[] param){
                              
                                   validateTransports(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localTransportsTracker = true;
                                          } else {
                                             localTransportsTracker = true;
                                                 
                                          }
                                      
                                      this.localTransports=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param java.lang.String
                             */
                             public void addTransports(java.lang.String param){
                                   if (localTransports == null){
                                   localTransports = new java.lang.String[]{};
                                   }

                            
                                 //update the setting tracker
                                localTransportsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localTransports);
                               list.add(param);
                               this.localTransports =
                             (java.lang.String[])list.toArray(
                            new java.lang.String[list.size()]);

                             }
                             

                        /**
                        * field for WsdlAvailable
                        */

                        
                                    protected boolean localWsdlAvailable ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localWsdlAvailableTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getWsdlAvailable(){
                               return localWsdlAvailable;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param WsdlAvailable
                               */
                               public void setWsdlAvailable(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localWsdlAvailableTracker = false;
                                              
                                       } else {
                                          localWsdlAvailableTracker = true;
                                       }
                                   
                                            this.localWsdlAvailable=param;
                                    

                               }
                            

                        /**
                        * field for WsdlDef
                        */

                        
                                    protected java.lang.String localWsdlDef ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localWsdlDefTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getWsdlDef(){
                               return localWsdlDef;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param WsdlDef
                               */
                               public void setWsdlDef(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localWsdlDefTracker = true;
                                       } else {
                                          localWsdlDefTracker = true;
                                              
                                       }
                                   
                                            this.localWsdlDef=param;
                                    

                               }
                            

                        /**
                        * field for WsdlKey
                        */

                        
                                    protected java.lang.String localWsdlKey ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localWsdlKeyTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getWsdlKey(){
                               return localWsdlKey;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param WsdlKey
                               */
                               public void setWsdlKey(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localWsdlKeyTracker = true;
                                       } else {
                                          localWsdlKeyTracker = true;
                                              
                                       }
                                   
                                            this.localWsdlKey=param;
                                    

                               }
                            

                        /**
                        * field for WsdlResources
                        * This was an Array!
                        */

                        
                                    protected org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[] localWsdlResources ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localWsdlResourcesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[]
                           */
                           public  org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[] getWsdlResources(){
                               return localWsdlResources;
                           }

                           
                        


                               
                              /**
                               * validate the array for WsdlResources
                               */
                              protected void validateWsdlResources(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param WsdlResources
                              */
                              public void setWsdlResources(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[] param){
                              
                                   validateWsdlResources(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localWsdlResourcesTracker = true;
                                          } else {
                                             localWsdlResourcesTracker = true;
                                                 
                                          }
                                      
                                      this.localWsdlResources=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param org.wso2.carbon.proxyadmin.ui.types.carbon.Entry
                             */
                             public void addWsdlResources(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry param){
                                   if (localWsdlResources == null){
                                   localWsdlResources = new org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[]{};
                                   }

                            
                                 //update the setting tracker
                                localWsdlResourcesTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localWsdlResources);
                               list.add(param);
                               this.localWsdlResources =
                             (org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[])list.toArray(
                            new org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[list.size()]);

                             }
                             

                        /**
                        * field for WsdlURI
                        */

                        
                                    protected java.lang.String localWsdlURI ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localWsdlURITracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getWsdlURI(){
                               return localWsdlURI;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param WsdlURI
                               */
                               public void setWsdlURI(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localWsdlURITracker = true;
                                       } else {
                                          localWsdlURITracker = true;
                                              
                                       }
                                   
                                            this.localWsdlURI=param;
                                    

                               }
                            

     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
               parentQName,factory,dataSource);
            
       }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://proxyadmin.carbon.wso2.org/xsd");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":ProxyData",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "ProxyData",
                           xmlWriter);
                   }

               
                   }
                if (localEnableSecurityTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "enableSecurity", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("enableSecurity cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnableSecurity));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localEnableStatisticsTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "enableStatistics", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("enableStatistics cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnableStatistics));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localEnableTracingTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "enableTracing", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("enableTracing cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnableTracing));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localEndpointKeyTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "endpointKey", xmlWriter);
                             

                                          if (localEndpointKey==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localEndpointKey);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localEndpointXMLTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "endpointXML", xmlWriter);
                             

                                          if (localEndpointXML==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localEndpointXML);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localFaultSeqKeyTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "faultSeqKey", xmlWriter);
                             

                                          if (localFaultSeqKey==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localFaultSeqKey);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localFaultSeqXMLTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "faultSeqXML", xmlWriter);
                             

                                          if (localFaultSeqXML==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localFaultSeqXML);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localInSeqKeyTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "inSeqKey", xmlWriter);
                             

                                          if (localInSeqKey==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localInSeqKey);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localInSeqXMLTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "inSeqXML", xmlWriter);
                             

                                          if (localInSeqXML==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localInSeqXML);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localNameTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "name", xmlWriter);
                             

                                          if (localName==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localName);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOutSeqKeyTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "outSeqKey", xmlWriter);
                             

                                          if (localOutSeqKey==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOutSeqKey);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOutSeqXMLTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "outSeqXML", xmlWriter);
                             

                                          if (localOutSeqXML==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOutSeqXML);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPinnedServersTracker){
                             if (localPinnedServers!=null) {
                                   namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                   for (int i = 0;i < localPinnedServers.length;i++){
                                        
                                            if (localPinnedServers[i] != null){
                                        
                                                writeStartElement(null, namespace, "pinnedServers", xmlWriter);

                                            
                                                        xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPinnedServers[i]));
                                                    
                                                xmlWriter.writeEndElement();
                                              
                                                } else {
                                                   
                                                           // write null attribute
                                                            namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                                            writeStartElement(null, namespace, "pinnedServers", xmlWriter);
                                                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                            xmlWriter.writeEndElement();
                                                       
                                                }

                                   }
                             } else {
                                 
                                         // write the null attribute
                                        // write null attribute
                                           writeStartElement(null, "http://proxyadmin.carbon.wso2.org/xsd", "pinnedServers", xmlWriter);

                                           // write the nil attribute
                                           writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                           xmlWriter.writeEndElement();
                                    
                             }

                        } if (localPoliciesTracker){
                                       if (localPolicies!=null){
                                            for (int i = 0;i < localPolicies.length;i++){
                                                if (localPolicies[i] != null){
                                                 localPolicies[i].serialize(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","policies"),
                                                           xmlWriter);
                                                } else {
                                                   
                                                            writeStartElement(null, "http://proxyadmin.carbon.wso2.org/xsd", "policies", xmlWriter);

                                                           // write the nil attribute
                                                           writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                           xmlWriter.writeEndElement();
                                                    
                                                }

                                            }
                                     } else {
                                        
                                                writeStartElement(null, "http://proxyadmin.carbon.wso2.org/xsd", "policies", xmlWriter);

                                               // write the nil attribute
                                               writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                               xmlWriter.writeEndElement();
                                        
                                    }
                                 } if (localRunningTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "running", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("running cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRunning));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localServiceGroupTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "serviceGroup", xmlWriter);
                             

                                          if (localServiceGroup==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localServiceGroup);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localServiceParamsTracker){
                                       if (localServiceParams!=null){
                                            for (int i = 0;i < localServiceParams.length;i++){
                                                if (localServiceParams[i] != null){
                                                 localServiceParams[i].serialize(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","serviceParams"),
                                                           xmlWriter);
                                                } else {
                                                   
                                                            writeStartElement(null, "http://proxyadmin.carbon.wso2.org/xsd", "serviceParams", xmlWriter);

                                                           // write the nil attribute
                                                           writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                           xmlWriter.writeEndElement();
                                                    
                                                }

                                            }
                                     } else {
                                        
                                                writeStartElement(null, "http://proxyadmin.carbon.wso2.org/xsd", "serviceParams", xmlWriter);

                                               // write the nil attribute
                                               writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                               xmlWriter.writeEndElement();
                                        
                                    }
                                 } if (localStartOnLoadTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "startOnLoad", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("startOnLoad cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStartOnLoad));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTransportsTracker){
                             if (localTransports!=null) {
                                   namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                   for (int i = 0;i < localTransports.length;i++){
                                        
                                            if (localTransports[i] != null){
                                        
                                                writeStartElement(null, namespace, "transports", xmlWriter);

                                            
                                                        xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTransports[i]));
                                                    
                                                xmlWriter.writeEndElement();
                                              
                                                } else {
                                                   
                                                           // write null attribute
                                                            namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                                            writeStartElement(null, namespace, "transports", xmlWriter);
                                                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                            xmlWriter.writeEndElement();
                                                       
                                                }

                                   }
                             } else {
                                 
                                         // write the null attribute
                                        // write null attribute
                                           writeStartElement(null, "http://proxyadmin.carbon.wso2.org/xsd", "transports", xmlWriter);

                                           // write the nil attribute
                                           writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                           xmlWriter.writeEndElement();
                                    
                             }

                        } if (localWsdlAvailableTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "wsdlAvailable", xmlWriter);
                             
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("wsdlAvailable cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWsdlAvailable));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localWsdlDefTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "wsdlDef", xmlWriter);
                             

                                          if (localWsdlDef==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localWsdlDef);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localWsdlKeyTracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "wsdlKey", xmlWriter);
                             

                                          if (localWsdlKey==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localWsdlKey);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localWsdlResourcesTracker){
                                       if (localWsdlResources!=null){
                                            for (int i = 0;i < localWsdlResources.length;i++){
                                                if (localWsdlResources[i] != null){
                                                 localWsdlResources[i].serialize(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","wsdlResources"),
                                                           xmlWriter);
                                                } else {
                                                   
                                                            writeStartElement(null, "http://proxyadmin.carbon.wso2.org/xsd", "wsdlResources", xmlWriter);

                                                           // write the nil attribute
                                                           writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                           xmlWriter.writeEndElement();
                                                    
                                                }

                                            }
                                     } else {
                                        
                                                writeStartElement(null, "http://proxyadmin.carbon.wso2.org/xsd", "wsdlResources", xmlWriter);

                                               // write the nil attribute
                                               writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                               xmlWriter.writeEndElement();
                                        
                                    }
                                 } if (localWsdlURITracker){
                                    namespace = "http://proxyadmin.carbon.wso2.org/xsd";
                                    writeStartElement(null, namespace, "wsdlURI", xmlWriter);
                             

                                          if (localWsdlURI==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localWsdlURI);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();
               

        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://proxyadmin.carbon.wso2.org/xsd")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        
        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localEnableSecurityTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "enableSecurity"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnableSecurity));
                            } if (localEnableStatisticsTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "enableStatistics"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnableStatistics));
                            } if (localEnableTracingTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "enableTracing"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnableTracing));
                            } if (localEndpointKeyTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "endpointKey"));
                                 
                                         elementList.add(localEndpointKey==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEndpointKey));
                                    } if (localEndpointXMLTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "endpointXML"));
                                 
                                         elementList.add(localEndpointXML==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEndpointXML));
                                    } if (localFaultSeqKeyTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "faultSeqKey"));
                                 
                                         elementList.add(localFaultSeqKey==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFaultSeqKey));
                                    } if (localFaultSeqXMLTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "faultSeqXML"));
                                 
                                         elementList.add(localFaultSeqXML==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFaultSeqXML));
                                    } if (localInSeqKeyTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "inSeqKey"));
                                 
                                         elementList.add(localInSeqKey==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInSeqKey));
                                    } if (localInSeqXMLTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "inSeqXML"));
                                 
                                         elementList.add(localInSeqXML==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInSeqXML));
                                    } if (localNameTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "name"));
                                 
                                         elementList.add(localName==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
                                    } if (localOutSeqKeyTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "outSeqKey"));
                                 
                                         elementList.add(localOutSeqKey==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOutSeqKey));
                                    } if (localOutSeqXMLTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "outSeqXML"));
                                 
                                         elementList.add(localOutSeqXML==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOutSeqXML));
                                    } if (localPinnedServersTracker){
                            if (localPinnedServers!=null){
                                  for (int i = 0;i < localPinnedServers.length;i++){
                                      
                                         if (localPinnedServers[i] != null){
                                          elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                              "pinnedServers"));
                                          elementList.add(
                                          org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPinnedServers[i]));
                                          } else {
                                             
                                                    elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                              "pinnedServers"));
                                                    elementList.add(null);
                                                
                                          }
                                      

                                  }
                            } else {
                              
                                    elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                              "pinnedServers"));
                                    elementList.add(null);
                                
                            }

                        } if (localPoliciesTracker){
                             if (localPolicies!=null) {
                                 for (int i = 0;i < localPolicies.length;i++){

                                    if (localPolicies[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "policies"));
                                         elementList.add(localPolicies[i]);
                                    } else {
                                        
                                                elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "policies"));
                                                elementList.add(null);
                                            
                                    }

                                 }
                             } else {
                                 
                                        elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "policies"));
                                        elementList.add(localPolicies);
                                    
                             }

                        } if (localRunningTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "running"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRunning));
                            } if (localServiceGroupTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "serviceGroup"));
                                 
                                         elementList.add(localServiceGroup==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServiceGroup));
                                    } if (localServiceParamsTracker){
                             if (localServiceParams!=null) {
                                 for (int i = 0;i < localServiceParams.length;i++){

                                    if (localServiceParams[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "serviceParams"));
                                         elementList.add(localServiceParams[i]);
                                    } else {
                                        
                                                elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "serviceParams"));
                                                elementList.add(null);
                                            
                                    }

                                 }
                             } else {
                                 
                                        elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "serviceParams"));
                                        elementList.add(localServiceParams);
                                    
                             }

                        } if (localStartOnLoadTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "startOnLoad"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStartOnLoad));
                            } if (localTransportsTracker){
                            if (localTransports!=null){
                                  for (int i = 0;i < localTransports.length;i++){
                                      
                                         if (localTransports[i] != null){
                                          elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                              "transports"));
                                          elementList.add(
                                          org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTransports[i]));
                                          } else {
                                             
                                                    elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                              "transports"));
                                                    elementList.add(null);
                                                
                                          }
                                      

                                  }
                            } else {
                              
                                    elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                              "transports"));
                                    elementList.add(null);
                                
                            }

                        } if (localWsdlAvailableTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "wsdlAvailable"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWsdlAvailable));
                            } if (localWsdlDefTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "wsdlDef"));
                                 
                                         elementList.add(localWsdlDef==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWsdlDef));
                                    } if (localWsdlKeyTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "wsdlKey"));
                                 
                                         elementList.add(localWsdlKey==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWsdlKey));
                                    } if (localWsdlResourcesTracker){
                             if (localWsdlResources!=null) {
                                 for (int i = 0;i < localWsdlResources.length;i++){

                                    if (localWsdlResources[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "wsdlResources"));
                                         elementList.add(localWsdlResources[i]);
                                    } else {
                                        
                                                elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "wsdlResources"));
                                                elementList.add(null);
                                            
                                    }

                                 }
                             } else {
                                 
                                        elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                          "wsdlResources"));
                                        elementList.add(localWsdlResources);
                                    
                             }

                        } if (localWsdlURITracker){
                                      elementList.add(new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd",
                                                                      "wsdlURI"));
                                 
                                         elementList.add(localWsdlURI==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWsdlURI));
                                    }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static ProxyData parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            ProxyData object =
                new ProxyData();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"ProxyData".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ProxyData)org.wso2.carbon.proxyadmin.ui.types.carbon.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                        java.util.ArrayList list13 = new java.util.ArrayList();
                    
                        java.util.ArrayList list14 = new java.util.ArrayList();
                    
                        java.util.ArrayList list17 = new java.util.ArrayList();
                    
                        java.util.ArrayList list19 = new java.util.ArrayList();
                    
                        java.util.ArrayList list23 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","enableSecurity").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEnableSecurity(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","enableStatistics").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEnableStatistics(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","enableTracing").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEnableTracing(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","endpointKey").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEndpointKey(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","endpointXML").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEndpointXML(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","faultSeqKey").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setFaultSeqKey(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","faultSeqXML").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setFaultSeqXML(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","inSeqKey").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInSeqKey(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","inSeqXML").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInSeqXML(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","name").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setName(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","outSeqKey").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOutSeqKey(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","outSeqXML").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOutSeqXML(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","pinnedServers").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    
                                              nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                              if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                  list13.add(null);
                                                       
                                                  reader.next();
                                              } else {
                                            list13.add(reader.getElementText());
                                            }
                                            //loop until we find a start element that is not part of this array
                                            boolean loopDone13 = false;
                                            while(!loopDone13){
                                                // Ensure we are at the EndElement
                                                while (!reader.isEndElement()){
                                                    reader.next();
                                                }
                                                // Step out of this element
                                                reader.next();
                                                // Step to next element event.
                                                while (!reader.isStartElement() && !reader.isEndElement())
                                                    reader.next();
                                                if (reader.isEndElement()){
                                                    //two continuous end elements means we are exiting the xml structure
                                                    loopDone13 = true;
                                                } else {
                                                    if (new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","pinnedServers").equals(reader.getName())){
                                                         
                                                          nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                          if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                              list13.add(null);
                                                                   
                                                              reader.next();
                                                          } else {
                                                        list13.add(reader.getElementText());
                                                        }
                                                    }else{
                                                        loopDone13 = true;
                                                    }
                                                }
                                            }
                                            // call the converter utility  to convert and set the array
                                            
                                                    object.setPinnedServers((java.lang.String[])
                                                        list13.toArray(new java.lang.String[list13.size()]));
                                                
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","policies").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    
                                                          nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                          if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                              list14.add(null);
                                                              reader.next();
                                                          } else {
                                                        list14.add(org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo.Factory.parse(reader));
                                                                }
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone14 = false;
                                                        while(!loopDone14){
                                                            // We should be at the end element, but make sure
                                                            while (!reader.isEndElement())
                                                                reader.next();
                                                            // Step out of this element
                                                            reader.next();
                                                            // Step to next element event.
                                                            while (!reader.isStartElement() && !reader.isEndElement())
                                                                reader.next();
                                                            if (reader.isEndElement()){
                                                                //two continuous end elements means we are exiting the xml structure
                                                                loopDone14 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","policies").equals(reader.getName())){
                                                                    
                                                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                                          list14.add(null);
                                                                          reader.next();
                                                                      } else {
                                                                    list14.add(org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo.Factory.parse(reader));
                                                                        }
                                                                }else{
                                                                    loopDone14 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setPolicies((org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo.class,
                                                                list14));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","running").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRunning(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","serviceGroup").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setServiceGroup(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","serviceParams").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    
                                                          nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                          if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                              list17.add(null);
                                                              reader.next();
                                                          } else {
                                                        list17.add(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry.Factory.parse(reader));
                                                                }
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone17 = false;
                                                        while(!loopDone17){
                                                            // We should be at the end element, but make sure
                                                            while (!reader.isEndElement())
                                                                reader.next();
                                                            // Step out of this element
                                                            reader.next();
                                                            // Step to next element event.
                                                            while (!reader.isStartElement() && !reader.isEndElement())
                                                                reader.next();
                                                            if (reader.isEndElement()){
                                                                //two continuous end elements means we are exiting the xml structure
                                                                loopDone17 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","serviceParams").equals(reader.getName())){
                                                                    
                                                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                                          list17.add(null);
                                                                          reader.next();
                                                                      } else {
                                                                    list17.add(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry.Factory.parse(reader));
                                                                        }
                                                                }else{
                                                                    loopDone17 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setServiceParams((org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                org.wso2.carbon.proxyadmin.ui.types.carbon.Entry.class,
                                                                list17));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","startOnLoad").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStartOnLoad(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","transports").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    
                                              nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                              if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                  list19.add(null);
                                                       
                                                  reader.next();
                                              } else {
                                            list19.add(reader.getElementText());
                                            }
                                            //loop until we find a start element that is not part of this array
                                            boolean loopDone19 = false;
                                            while(!loopDone19){
                                                // Ensure we are at the EndElement
                                                while (!reader.isEndElement()){
                                                    reader.next();
                                                }
                                                // Step out of this element
                                                reader.next();
                                                // Step to next element event.
                                                while (!reader.isStartElement() && !reader.isEndElement())
                                                    reader.next();
                                                if (reader.isEndElement()){
                                                    //two continuous end elements means we are exiting the xml structure
                                                    loopDone19 = true;
                                                } else {
                                                    if (new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","transports").equals(reader.getName())){
                                                         
                                                          nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                          if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                              list19.add(null);
                                                                   
                                                              reader.next();
                                                          } else {
                                                        list19.add(reader.getElementText());
                                                        }
                                                    }else{
                                                        loopDone19 = true;
                                                    }
                                                }
                                            }
                                            // call the converter utility  to convert and set the array
                                            
                                                    object.setTransports((java.lang.String[])
                                                        list19.toArray(new java.lang.String[list19.size()]));
                                                
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","wsdlAvailable").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWsdlAvailable(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","wsdlDef").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWsdlDef(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","wsdlKey").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWsdlKey(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","wsdlResources").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    
                                                          nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                          if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                              list23.add(null);
                                                              reader.next();
                                                          } else {
                                                        list23.add(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry.Factory.parse(reader));
                                                                }
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone23 = false;
                                                        while(!loopDone23){
                                                            // We should be at the end element, but make sure
                                                            while (!reader.isEndElement())
                                                                reader.next();
                                                            // Step out of this element
                                                            reader.next();
                                                            // Step to next element event.
                                                            while (!reader.isStartElement() && !reader.isEndElement())
                                                                reader.next();
                                                            if (reader.isEndElement()){
                                                                //two continuous end elements means we are exiting the xml structure
                                                                loopDone23 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","wsdlResources").equals(reader.getName())){
                                                                    
                                                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                                          list23.add(null);
                                                                          reader.next();
                                                                      } else {
                                                                    list23.add(org.wso2.carbon.proxyadmin.ui.types.carbon.Entry.Factory.parse(reader));
                                                                        }
                                                                }else{
                                                                    loopDone23 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setWsdlResources((org.wso2.carbon.proxyadmin.ui.types.carbon.Entry[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                org.wso2.carbon.proxyadmin.ui.types.carbon.Entry.class,
                                                                list23));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://proxyadmin.carbon.wso2.org/xsd","wsdlURI").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWsdlURI(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                  
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
    