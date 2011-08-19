<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.service.mgt.ui.ServiceAdminClient" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="java.util.ResourceBundle" %>
<%
    String[] serviceGroups = request.getParameterValues("serviceGroups");
    String pageNumber = request.getParameter("pageNumber");
    String deleteAllServiceGroups = request.getParameter("deleteAllServiceGroups");
    int pageNumberInt = 0;
    if (pageNumber != null) {
        pageNumberInt = Integer.parseInt(pageNumber);
    }
%>

<%
    String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);

    ResourceBundle bundle = ResourceBundle
            .getBundle("org.wso2.carbon.service.mgt.ui.i18n.Resources", request.getLocale());

    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
    ServiceAdminClient client;
    try {
        client = new ServiceAdminClient(cookie, backendServerURL, configContext, request.getLocale());
    } catch (Exception e) {
        CarbonUIMessage uiMsg = new CarbonUIMessage(CarbonUIMessage.ERROR, e.getMessage(), e);
        session.setAttribute(CarbonUIMessage.ID, uiMsg);
%>
<jsp:include page="../admin/error.jsp"/>
<%
        return;
    }

    try {
        if (deleteAllServiceGroups != null) {
            client.deleteAllNonAdminServiceGroups();
            CarbonUIMessage.sendCarbonUIMessage(bundle.getString("successfully.deleted.all.non.admin.service.groups"),
                    CarbonUIMessage.INFO, request);
        } else {
            client.deleteServiceGroups(serviceGroups);  //TODO handle the returned boolean value
            CarbonUIMessage.sendCarbonUIMessage(bundle.getString("successfully.deleted.service.groups"),
                    CarbonUIMessage.INFO, request);
        }
                %>
                <script>
                    location.href = 'index.jsp?pageNumber=<%=pageNumberInt%>'
                </script>

                <%
    } catch (Exception e) {
                CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request);
                %>
                        <script type="text/javascript">
                               location.href = "index.jsp?pageNumber=<%=pageNumberInt%>";
                        </script>
                <%
                return;
    }
%>