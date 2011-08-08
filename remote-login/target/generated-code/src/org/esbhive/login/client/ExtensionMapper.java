
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 03, 2010 (02:59:46 IST)
 */

        
            package org.esbhive.login.client;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://authentication.services.core.carbon.wso2.org".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  org.esbhive.login.client.Exception.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://common.core.carbon.wso2.org/xsd".equals(namespaceURI) &&
                  "AuthenticationException".equals(typeName)){
                   
                            return  org.esbhive.login.client.AuthenticationException.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    