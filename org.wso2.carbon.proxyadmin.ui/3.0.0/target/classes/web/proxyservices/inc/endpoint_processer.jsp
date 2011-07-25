<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.MalformedURLException" %>

<%
    Object proxyObj = request.getAttribute("proxyDataObject");
    if (proxyObj != null) {
        ProxyData proxy = (ProxyData) proxyObj;
        String mode = request.getParameter("targetEndpointMode");
        if ("url".equals(mode)) {
            String targetURL = request.getParameter("targetURL");
            if (targetURL != null && !"".equals(targetURL)) {
                try {
                    URL url = new URL(targetURL);
                    proxy.setEndpointXML("<endpoint xmlns=\"http://ws.apache.org/ns/synapse\"><address uri=\"" + url.toString() + "\"/></endpoint>");
                } catch (MalformedURLException e) {
                    request.setAttribute("proxyCreationError", "The target URL provided is malformed");
                }
            } else {
                request.setAttribute("proxyCreationError", "The target URL has not been provided");
            }
            
        } else if ("predef".equals(mode)) {
            String endpointName = request.getParameter("predefEndpoint");
            if (endpointName != null && !"".equals(endpointName)) {
                proxy.setEndpointKey(endpointName);
            } else {
                request.setAttribute("proxyCreationError", "The target endpoint name is not provided");
            }

        } else if ("reg".equals(mode)) {
            String regKey = request.getParameter("endpointRegKey");
            if (regKey != null && !"".equals(regKey)) {
                proxy.setEndpointKey(regKey);
            } else {
                request.setAttribute("proxyCreationError", "The registry key for the dynamic endpoint is not provided");
            }

        }
    }
%>