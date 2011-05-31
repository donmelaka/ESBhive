<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.client.ProxyServiceAdminClient" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.ByteArrayInputStream" %>
<%@ page import="org.wso2.carbon.utils.xml.XMLPrettyPrinter" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>
<jsp:include page="../dialog/display_messages.jsp"/>
<script src="../editarea/edit_area_full.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="../proxyservices/css/proxyservices.css"/>
<!-- Dependencies -->
<script type="text/javascript" src="../proxyservices/js/proxyservices.js"></script>
    <%
        ResourceBundle bundle = ResourceBundle.getBundle("org.wso2.carbon.proxyadmin.ui.i18n.Resources",
                request.getLocale());
        String header = request.getParameter("header");
        ProxyData pd = (ProxyData)session.getAttribute("proxy");

        String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
        ConfigurationContext configContext =
                (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
        String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
        ProxyServiceAdminClient client = new ProxyServiceAdminClient(
                configContext, backendServerURL, cookie, request.getLocale());

        String forwardTo;
        String source;
        if (pd != null) {
            try {
                source = prettyPrint(client.getSourceView(pd).trim());
                // removes the proxy attribute after use
                session.removeAttribute("proxy");
            } catch (RemoteException e) {
                forwardTo = "index.jsp?header=" + header;
                CarbonUIMessage.sendCarbonUIMessage(bundle.getString("unable.to.get.source.from.the.design"),
                        CarbonUIMessage.ERROR, request);
                %>
                <script type="text/javascript">
                    window.location.href = '<%=forwardTo%>';
                </script>
                <%
                return;
            }
        } else if ((source = (String)session.getAttribute("proxyXML")) != null && !"".equals(source)){
            // this means that we came here from sourceToData causing an exception
            source = prettyPrint(source);
            // removes the session attribute of proxyXML
            session.removeAttribute("proxyXML");
        }

        String saveOrModify = "add";
        if (bundle.getString("header.add").equals(header)) {
            saveOrModify = "add";
        } else if (bundle.getString("header.modify").equals(header)) {
            saveOrModify = "modify";
        }
    %>
    <%!
        String prettyPrint(String source) {
            InputStream xmlIn = new ByteArrayInputStream(source.getBytes());
            XMLPrettyPrinter xmlPrettyPrinter = new XMLPrettyPrinter(xmlIn);
            return xmlPrettyPrinter.xmlFormat();
        }
    %>

<fmt:bundle basename="org.wso2.carbon.proxyadmin.ui.i18n.Resources">
<carbon:breadcrumb
		label="proxy.service.source"
		resourceBundle="org.wso2.carbon.proxyadmin.ui.i18n.Resources"
		topPage="false"
		request="<%=request%>" />

    <script type="text/javascript">
        function designView(){
            document.getElementById("srcTextArea").value = editAreaLoader.getValue("srcTextArea");
            document.sourceForm.action = "sourceToData.jsp?return=index.jsp&header=<%=header%>&originator=source.jsp";
            document.sourceForm.submit();
        }

        function saveData() {
            document.getElementById("srcTextArea").value = editAreaLoader.getValue("srcTextArea");
            document.sourceForm.action = "sourceToData.jsp?submit=<%=saveOrModify%>&header=<%=header%>&forwardTo=../service-mgt/index.jsp&originator=source.jsp";
            document.sourceForm.submit();
        }

        function cancelData() {
            window.location.href="../service-mgt/index.jsp";
        }
    </script>

    <div id="middle">
        <h2><%=header%> Proxy Service</h2>
        <div id="workArea">
            <form id="form1" name="sourceForm" method="post" action="">
                <table cellspacing="0" cellpadding="0" border="0" class="styledLeft noBorders">
                    <thead>
                        <tr>
                            <th>
                                <span style="float: left; position: relative; margin-top: 2px;"><fmt:message key="proxy.service.source"/></span><a
                                    style="background-image: url(images/design-view.gif);" class="icon-link"
                                    onclick="designView();" href="#"><fmt:message key="proxy.source.switch"/></a>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <textarea
                                        style="border: 0px solid rgb(204, 204, 204); width: 99%; height: 275px; margin-top: 5px;"
                                        id="srcTextArea" name="srcTextArea" rows="30"><%=source%>
                                </textarea>
                            </td>
                        </tr>
                        <tr>
                            <td class="buttonRow">
                                <input id="saveBtn" type="submit" value="<fmt:message key="proxy.source.save"/>" class="button"
                                       onclick="saveData();return false;"/>
                                <input id="cancelBtn" type="button" value="<fmt:message key="proxy.source.cancel"/>" class="button"
                                       onclick="cancelData();return false;"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <script type="text/javascript">
        editAreaLoader.init({
            id : "srcTextArea"		// textarea id
            ,syntax: "xml"			// syntax to be uses for highgliting
            ,start_highlight: true		// to display with highlight mode on start-up
        });
    </script>
</fmt:bundle>
