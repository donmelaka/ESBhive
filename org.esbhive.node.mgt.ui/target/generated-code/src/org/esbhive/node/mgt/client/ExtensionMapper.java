
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : May 03, 2010 (02:59:46 IST)
 */

        
            package org.esbhive.node.mgt.client;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://proto.zookeeper.apache.org/xsd".equals(namespaceURI) &&
                  "WatcherEvent".equals(typeName)){
                   
                            return  org.apache.zookeeper.proto.xsd.WatcherEvent.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://mgt.node.esbhive.org/xsd".equals(namespaceURI) &&
                  "ESBNode".equals(typeName)){
                   
                            return  org.esbhive.node.mgt.client.ESBNode.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://zookeeper.apache.org/xsd".equals(namespaceURI) &&
                  "WatchedEvent".equals(typeName)){
                   
                            return  org.apache.zookeeper.xsd.WatchedEvent.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    