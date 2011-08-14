
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:53 UTC)
 */

        
            package org.wso2.carbon.service.mgt.ui.types.carbon;
        
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
                   
                            return  org.wso2.carbon.service.mgt.ui.types.neethi.PolicyComponent.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://utils.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServerException".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.ServerException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://neethi.apache.org/xsd".equals(namespaceURI) &&
                  "Policy".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.neethi.Policy.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "FaultyService".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.FaultyService.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://neethi.apache.org/xsd".equals(namespaceURI) &&
                  "AbstractPolicyOperator".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.neethi.AbstractPolicyOperator.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://util.java/xsd".equals(namespaceURI) &&
                  "Map".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.Map.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.apache.axis2/xsd".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.axis2.Exception.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "FaultyServicesWrapper".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.FaultyServicesWrapper.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://util.java/xsd".equals(namespaceURI) &&
                  "Iterator".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.Iterator.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServiceGroupMetaData".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServiceGroupMetaDataWrapper".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaDataWrapper.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServiceMetaData".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.ServiceMetaData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "PolicyMetaData".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.PolicyMetaData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://neethi.apache.org/xsd".equals(namespaceURI) &&
                  "All".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.neethi.All.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    