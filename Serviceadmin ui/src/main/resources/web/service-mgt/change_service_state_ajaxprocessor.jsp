<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.wso2.carbon.service.mgt.ui.ServiceAdminClient" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>

<fmt:bundle basename="org.wso2.carbon.service.mgt.ui.i18n.Resources">
    <%
        String serviceName = request.getParameter("serviceName");
        String isActive = request.getParameter("isActive");
        if (serviceName == null || serviceName.trim().length() == 0) {
    %>
    <p><fmt:message key="service.name.cannot.be.null"/></p>

    <%
            return;
        }
        String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
        ConfigurationContext configContext =
                (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);

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

        Boolean isActiveBool = Boolean.valueOf(isActive);
        client.changeServiceState(serviceName, isActiveBool);
        request.setAttribute("serviceName", serviceName);
        request.setAttribute("isActive", isActive);
    %>
    <jsp:include page="service_state_include.jsp"/>
</fmt:bundle>