<%@ page import="org.wso2.carbon.proxyadmin.ui.client.ProxyServiceAdminClient" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.MetaData" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>

<%
    ResourceBundle bundle = ResourceBundle.getBundle("org.wso2.carbon.proxyadmin.ui.i18n.Resources",
            request.getLocale());

    try {
        String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
        ConfigurationContext configContext =
                (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
        String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
        ProxyServiceAdminClient client = new ProxyServiceAdminClient(
                configContext, backendServerURL, cookie, request.getLocale());
        MetaData metaData = client.getMetaData();
        request.setAttribute("proxyMetadata", metaData);

    } catch (Exception e) {
        CarbonUIMessage.sendCarbonUIMessage(bundle.getString("unable.to.get.metadata"),
                CarbonUIMessage.ERROR, request);
%>
<script type="text/javascript">
    window.location.href = '../service-mgt/index.jsp';
</script>
<%
        return;
    }
%>