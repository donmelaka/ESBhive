
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:53 UTC)
 */

        
            package org.esbhive.proxyconf.mgt.xsd;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
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
                  "http://mgt.proxyconf.esbhive.org/xsd".equals(namespaceURI) &&
                  "ProxyDataList".equals(typeName)){
                   
                            return  org.esbhive.proxyconf.mgt.xsd.ProxyDataList.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxyadmin.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "Entry".equals(typeName)){
                   
                            return  org.wso2.carbon.proxyadmin.xsd.Entry.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.node.esbhive.org/xsd".equals(namespaceURI) &&
                  "ESBNode".equals(typeName)){
                   
                            return  org.esbhive.node.mgt.xsd.ESBNode.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.proxyconf.esbhive.org/xsd".equals(namespaceURI) &&
                  "ProEsb".equals(typeName)){
                   
                            return  org.esbhive.proxyconf.mgt.xsd.ProEsb.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    