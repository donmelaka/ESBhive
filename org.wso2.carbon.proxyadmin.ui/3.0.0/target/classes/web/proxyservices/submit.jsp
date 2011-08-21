<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.client.ProxyServiceAdminClient" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page import="org.apache.axis2.AxisFault" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("org.wso2.carbon.proxyadmin.ui.i18n.Resources",
            request.getLocale());
    String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
    ProxyServiceAdminClient client = new ProxyServiceAdminClient(
            configContext, backendServerURL, cookie, request.getLocale());
    // error object
    String forwardTo=request.getParameter("forwardTo");
    String header = request.getParameter("header");
    try {
        ProxyData pd = (ProxyData)session.getAttribute("proxy");
        if ("add".equals(request.getParameter("submit"))) {
            client.addProxy(pd);
        } else if ("modify".equals(request.getParameter("submit"))) {
            client.modifyProxy(pd);
        }
        session.removeAttribute("proxy");
    } catch (AxisFault af) {
        String cause = null;
        String afMsg = null;

        if (af.getMessage() != null) {
            afMsg = af.getMessage();
        }
        
        if (af.getCause() != null) {
            cause = af.getCause().getMessage();
            cause = cause.replaceAll("\n|\\r|\\t|\\f", "");            
        }
        String msg = MessageFormat.format(
                bundle.getString("unable.to.0.proxy.service"), request.getParameter("submit")) + " :: " +
                afMsg + "-" + ((cause != null) ? cause : "");
        CarbonUIMessage.sendCarbonUIMessage(msg, CarbonUIMessage.ERROR, request);
        if ("designToData.jsp".equals(request.getParameter("originator"))) {
            forwardTo = "index.jsp";
        } else if ("sourceToData.jsp".equals(request.getParameter("originator"))) {
            forwardTo = "source.jsp";
        }
    }
%>
<script type="text/javascript">
    if (window.location.href.indexOf('originator') != -1) {
        if ('<%=forwardTo%>' == '../service-mgt/index.jsp') {
            window.location.href = '../service-mgt/index.jsp?region=region1&item=services_list_menu';
        } else {
            window.location.href='<%=forwardTo%>?header=<%=header%>';
        }
    } else {
        window.location.href = '..service-mgt/index.jsp?region=region1&item=services_list_menu/service-mgt/index.jsp';
    }
</script>


