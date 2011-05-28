
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 05, 2010 (06:40:53 UTC)
 */

        
            package org.esbhive.node.mgt.authenticator.proxy.xsd;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://io.java/xsd".equals(namespaceURI) &&
                  "IOException".equals(typeName)){
                   
                            return  java.io.xsd.IOException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxy.authenticator.mgt.node.esbhive.org/xsd".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  org.esbhive.node.mgt.authenticator.proxy.xsd.ExceptionE.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://rmi.java/xsd".equals(namespaceURI) &&
                  "RemoteException".equals(typeName)){
                   
                            return  java.rmi.xsd.RemoteException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxy.authenticator.mgt.node.esbhive.org/xsd".equals(namespaceURI) &&
                  "AuthenticationExceptionException".equals(typeName)){
                   
                            return  org.esbhive.node.mgt.authenticator.proxy.xsd.AuthenticationExceptionException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://authentication.mgt.node.esbhive.org/xsd".equals(namespaceURI) &&
                  "AuthenticationException".equals(typeName)){
                   
                            return  org.esbhive.node.mgt.authentication.xsd.AuthenticationException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://data.mgt.node.esbhive.org/xsd".equals(namespaceURI) &&
                  "ESBNode".equals(typeName)){
                   
                            return  org.esbhive.node.mgt.data.xsd.ESBNode.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://proxy.authenticator.mgt.node.esbhive.org/xsd".equals(namespaceURI) &&
                  "AuthenticationExceptionE".equals(typeName)){
                   
                            return  org.esbhive.node.mgt.authenticator.proxy.xsd.AuthenticationExceptionE.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.node.esbhive.org".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  org.esbhive.node.mgt.Exception.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    