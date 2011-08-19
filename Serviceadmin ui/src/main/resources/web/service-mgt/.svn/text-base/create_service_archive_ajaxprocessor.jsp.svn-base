<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.service.mgt.ui.ServiceGroupAdminClient" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%
    String serviceGroupName = request.getParameter("serviceGroupName");
    String responseTextValue;
    String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);

    try {
    	responseTextValue = new ServiceGroupAdminClient(cookie, backendServerURL, configContext).dumpAAR(serviceGroupName);
%>
        <script>
          location.href = '../..<%=responseTextValue%>';
        </script>
<%
    } catch (Exception e) {
        CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request ,e);
%>
        <script>
          location.href = 'list_service_group.jsp?serviceGroupName=<%= serviceGroupName%>';
        </script>
<%
        return;
    }
%>