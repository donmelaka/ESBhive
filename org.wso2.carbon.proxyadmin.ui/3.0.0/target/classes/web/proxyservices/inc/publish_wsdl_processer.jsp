<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.Entry" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.MalformedURLException" %>
<%
    Object proxyObj = request.getAttribute("proxyDataObject");
    if (proxyObj != null) {
        ProxyData proxy = (ProxyData) proxyObj;
        String wsdlMode = request.getParameter("publishWsdlCombo");
        Object resourcesOnly = request.getAttribute("processResourcesOnly");

        if (!"true".equals(resourcesOnly)) {
            
            if ("inline".equals(wsdlMode)) {
                String inlineTxt = request.getParameter("wsdlInlineText");
                if (inlineTxt != null && !"".equals(inlineTxt)) {
                    inlineTxt = inlineTxt.replaceAll("\n|\\r|\\f|\\t", "");
                    inlineTxt = inlineTxt.replaceAll("> +<", "><");
                    proxy.setWsdlAvailable(true);
                    proxy.setWsdlDef(inlineTxt);
                } else {
                    request.setAttribute("proxyCreationError",
                            "The Inline WSDL content has not been provided");
                }
            } else if ("uri".equals(wsdlMode)) {
                String uri = request.getParameter("wsdlUriText");
                if (uri != null && !"".equals(uri)) {
                    try {
                        URL url = new URL(uri);
                        proxy.setWsdlAvailable(true);
                        proxy.setWsdlURI(url.toString());
                    } catch (MalformedURLException e) {
                        request.setAttribute("proxyCreationError",
                            "The WSDL URI provided is malformed");
                    }

                } else {
                    request.setAttribute("proxyCreationError",
                            "The WSDL URI has not been provided");
                }
            } else if ("reg".equals(wsdlMode)) {
                String registryKey = request.getParameter("wsdlRegText");
                if (registryKey != null && !"".equals(registryKey)) {
                    proxy.setWsdlAvailable(true);
                    proxy.setWsdlKey(registryKey);
                } else {
                    request.setAttribute("proxyCreationError",
                            "The registry key for the WSDL has not been provided");
                }
            }
        }

        if (proxy.getWsdlAvailable() && request.getAttribute("proxyCreationError") == null) {
            String resourceList = request.getParameter("wsdlResourceList");
            if (resourceList != null && !"".equals(resourceList)) {
                String[] resourceValues = resourceList.split("::");
                for (String resourceValue : resourceValues) {
                    Entry resourceEntry = new Entry();
                    int index = resourceValue.indexOf(',');
                    resourceEntry.setKey(resourceValue.substring(0, index));
                    resourceEntry.setValue(resourceValue.substring(index + 1));
                    proxy.addWsdlResources(resourceEntry);
                }
            }
        }
    }
%>