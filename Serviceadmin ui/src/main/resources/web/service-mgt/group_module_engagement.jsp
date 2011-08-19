<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.service.mgt.ui.ServiceGroupAdminClient" %>
<%@ page import="org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaData" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ taglib uri="http://ajaxtags.org/tags/ajax" prefix="ajax" %>
<%@page import="org.wso2.carbon.service.mgt.ui.ModuleManagementClient"%>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<jsp:include page="../admin/layout/ajaxheader.jsp"/>

<div id="middle">
<script type="text/javascript" src="global-params.js"></script>
<script type="text/javascript" src="../admin/js/dhtmlHistory.js"></script>
<script type="text/javascript" src="../admin/js/breadcrumbs.js"></script>
<script type="text/javascript" src="../admin/js/cookies.js"></script>
<script type="text/javascript" src="../admin/js/WSRequest.js"></script>
<script type="text/javascript" src="extensions/core/js/services.js"></script>


<%
	String serviceGroupName = null;
	String moduleName = null;

	serviceGroupName = request.getParameter("serviceGroupName");
	moduleName = request.getParameter("modules");

	if (serviceGroupName != null && serviceGroupName.trim().length() > 0) {
		String backendServerURL = null;
		ConfigurationContext configContext = null;
		String cookie = null;
		ServiceGroupAdminClient client = null;
		ModuleManagementClient moduleMgmt = null;

		backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
		configContext = (ConfigurationContext) config.getServletContext().getAttribute(
				CarbonConstants.CONFIGURATION_CONTEXT);

		cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
		
		try {			
			client = new ServiceGroupAdminClient(cookie,backendServerURL, configContext);
			moduleMgmt = new ModuleManagementClient(cookie,backendServerURL, configContext);
		} catch (Exception e) {
            response.setStatus(500);
            CarbonUIMessage uiMsg = new CarbonUIMessage(CarbonUIMessage.ERROR, e.getMessage(), e);
            session.setAttribute(CarbonUIMessage.ID, uiMsg);
%>
            <jsp:include page="../admin/error.jsp"/>
        <%
            return;
		}

		try {					
			client.engageModuleForServiceGroup(moduleName,moduleMgmt.getModuleVersion(moduleName),serviceGroupName);
		} catch (Exception e) {
%>
            <script>
    			location.href = 'list_group_modules.jsp?serviceGroupName=<%= serviceGroupName%>&failed=true';
			</script>
<%
		  return;
		}
	}	
%>

<script>
     location.href = 'list_group_modules.jsp?serviceGroupName=<%= serviceGroupName%>';
</script>