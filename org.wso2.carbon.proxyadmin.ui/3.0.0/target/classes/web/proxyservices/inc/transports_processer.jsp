<%@ page import="java.util.Enumeration" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData" %>
<%
    Object proxyObj = request.getAttribute("proxyDataObject");
    if (proxyObj != null) {
        ProxyData proxy = (ProxyData) proxyObj;
        Enumeration<String> params = request.getParameterNames();
        String prefix = "trp__";
        boolean transportsFound = false;
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (param.startsWith(prefix)) {
                proxy.addTransports(param.substring(prefix.length()));
                transportsFound = true;
            }
        }

        if (!transportsFound) {
            request.setAttribute("proxyCreationError",
                        "At least one transport must be specified for a proxy service");   
        }
    }
%>