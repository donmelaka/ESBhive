
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.2  Built on : Sep 06, 2010 (09:42:47 CEST)
 */

            package org.wso2.carbon.proxyadmin.xsd;
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ProxyAdminException".equals(typeName)){
                   
                            return  org.wso2.carbon.proxyadmin.xsd.ProxyAdminException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ProxyData".equals(typeName)){
                   
                            return  org.wso2.carbon.proxyadmin.xsd.ProxyData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ProxyServicePolicyInfo".equals(typeName)){
                   
                            return  org.wso2.carbon.proxyadmin.xsd.ProxyServicePolicyInfo.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.apache.synapse/xsd".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  synapse.apache.org.xsd.Exception.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "Entry".equals(typeName)){
                   
                            return  org.wso2.carbon.proxyadmin.xsd.Entry.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "MetaData".equals(typeName)){
                   
                            return  org.wso2.carbon.proxyadmin.xsd.MetaData.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    