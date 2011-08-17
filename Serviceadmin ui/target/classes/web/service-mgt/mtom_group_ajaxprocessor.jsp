<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.service.mgt.ui.ServiceGroupAdminClient" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%
    String serviceGroupName = null;
    String mtomState = null;
    String cookie = null;
    String backendServerURL = null;
    ConfigurationContext configContext = null;
    
	serviceGroupName = request.getParameter("serviceGroupName");	
	mtomState = request.getParameter("mtomState");
	cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
	backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
	configContext = (ConfigurationContext) config.getServletContext().getAttribute(
												CarbonConstants.CONFIGURATION_CONTEXT);
	try {
		new ServiceGroupAdminClient(cookie, backendServerURL,configContext).configureServiceGroupMTOM(mtomState,
																							serviceGroupName);
	} catch (Exception e) {
        response.setStatus(500);
        CarbonUIMessage uiMsg = new CarbonUIMessage(CarbonUIMessage.ERROR, e.getMessage(), e);
        session.setAttribute(CarbonUIMessage.ID, uiMsg);
%>
<jsp:include page="../admin/error.jsp"/>
<%
        return;
	}
%>