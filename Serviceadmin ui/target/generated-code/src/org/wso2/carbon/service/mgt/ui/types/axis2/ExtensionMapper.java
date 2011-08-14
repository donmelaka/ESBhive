
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:53 UTC)
 */

        
            package org.wso2.carbon.service.mgt.ui.types.axis2;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://utils.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServerException".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.ServerException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServiceGroupMetaData".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ParameterMetaData".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.ParameterMetaData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.apache.axis2/xsd".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.axis2.Exception.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.service.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ServiceMetaData".equals(typeName)){
                   
                            return  org.wso2.carbon.service.mgt.ui.types.carbon.ServiceMetaData.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    