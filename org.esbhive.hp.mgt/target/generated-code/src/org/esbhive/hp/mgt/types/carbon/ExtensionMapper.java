
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:53 UTC)
 */

        
            package org.esbhive.hp.mgt.types.carbon;
        
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
                   
                            return  org.esbhive.hp.mgt.types.carbon.ProxyAdminException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ProxyData".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.types.carbon.ProxyData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "ProxyServicePolicyInfo".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.types.carbon.ProxyServicePolicyInfo.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.apache.synapse/xsd".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.types.synapse.Exception.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "Entry".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.types.carbon.Entry.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "MetaData".equals(typeName)){
                   
                            return  org.esbhive.hp.mgt.types.carbon.MetaData.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    