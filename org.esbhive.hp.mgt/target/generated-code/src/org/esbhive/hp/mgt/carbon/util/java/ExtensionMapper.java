
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:53 UTC)
 */

        
            package org.esbhive.hp.mgt.carbon.util.java;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://neethi.apache.org/xsd".equals(namespaceURI) &&
                  "PolicyComponent".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.neethi.PolicyComponent.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://utils.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServerException".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.utils.ServerException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://neethi.apache.org/xsd".equals(namespaceURI) &&
                  "Policy".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.neethi.Policy.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "FaultyService".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.service.FaultyService.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://neethi.apache.org/xsd".equals(namespaceURI) &&
                  "AbstractPolicyOperator".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.neethi.AbstractPolicyOperator.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://util.java/xsd".equals(namespaceURI) &&
                  "Map".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.util.java.Map.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.apache.axis2/xsd".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.axis2.Exception.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "FaultyServicesWrapper".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.service.FaultyServicesWrapper.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://util.java/xsd".equals(namespaceURI) &&
                  "Iterator".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.util.java.Iterator.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServiceGroupMetaData".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.service.ServiceGroupMetaData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServiceGroupMetaDataWrapper".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.service.ServiceGroupMetaDataWrapper.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServiceMetaData".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.service.ServiceMetaData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "PolicyMetaData".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.service.PolicyMetaData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://neethi.apache.org/xsd".equals(namespaceURI) &&
                  "All".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.carbon.neethi.All.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    