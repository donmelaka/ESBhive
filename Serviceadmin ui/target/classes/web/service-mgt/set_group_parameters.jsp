<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.service.mgt.ui.ServiceGroupAdminClient" %>
<%@ page import="org.wso2.carbon.ui.deployment.beans.BreadCrumbItem" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>

<%
    String serviceGroupName;
    String backendServerURL;
    ConfigurationContext configContext;
    String cookie;
    Map<String, String[]> map;
    List<String> params;

	serviceGroupName = request.getParameter("serviceGroupName");

	backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
	configContext = (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
	cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
	map = request.getParameterMap();
	params = new ArrayList<String>();
	
	for (String key : map.keySet()) {
		String paramValue = map.get(key)[0].trim();
		if (paramValue.startsWith("<parameter")) {
			params.add(paramValue);
		}
	}
	try {
		new ServiceGroupAdminClient(cookie, backendServerURL,configContext).setServiceGroupParamters(serviceGroupName, params);
        session.setAttribute("serviceGroupName", serviceGroupName);

        // A hack to stop breadcrumbs repeating
        HashMap<String, List<BreadCrumbItem>> links =
                (HashMap<String, List<BreadCrumbItem>>) request
                        .getSession().getAttribute("page-breadcrumbs");
        List<BreadCrumbItem> items = links.get("../service-mgt");
        items.remove(items.size() - 1);
%>
       <jsp:include page="edit_group_param.jsp"/>
<%
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