<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.client.ProxyServiceAdminClient" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>

<jsp:include page="../resources/resources-i18n-ajaxprocessor.jsp"/>
<!-- Dependencies -->
<script type="text/javascript" src="../yui/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="../yui/build/container/container_core-min.js"></script>

<!-- Connection handling lib -->
<script type="text/javascript" src="../yui/build/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="../yui/build/event/event-min.js"></script>
<script type="text/javascript" src="../yui/build/connection/connection-min.js"></script>
<script type="text/javascript" src="../resources/js/resource_util.js"></script>
<script type="text/javascript" src="../yui/build/utilities/utilities.js"></script>
<script type="text/javascript" src="../ajax/js/prototype.js"></script>

<!-- Source File -->
<script type="text/javascript" src="../sequences/js/registry-browser.js"></script>
<script type="text/javascript" src="../yui/build/menu/menu-min.js"></script>
<fmt:bundle basename="org.wso2.carbon.proxyadmin.ui.i18n.Resources">

    <carbon:breadcrumb
            label="service.proxy.menu.text"
            resourceBundle="org.wso2.carbon.proxyadmin.ui.i18n.Resources"
            topPage="true"
            request="<%=request%>"/>

    <carbon:jsi18n
            resourceBundle="org.wso2.carbon.proxyadmin.ui.i18n.JSResources"
            request="<%=request%>"
            i18nObjectName="proxyi18n"/>

    <script type="text/javascript" src="inc/utils.js"></script>

    <script type="text/javascript">
        function createProxy() {
            if (validateProxyName() && validateTargetEndpoint() &&
                    validateWSDLOptions() && validateTransports()) {
                proxy_form.action = "template_pass-through.jsp";
                proxy_form.submit();
                return true;
            }
            return false;
        }

        function enableLevelSelect() {
            if (document.getElementById('log_responses').checked) {
                document.getElementById('response_log_level').removeAttribute('disabled');
            } else {
                document.getElementById('response_log_level').disabled = 'true';                                    
            }
        }
    </script>

    <%
        String proxyName = null;
        String logLevel = null;
        boolean logResponses = false;
        String responseLogLevel = null;

        boolean submitted = "true".equals(request.getParameter("formSubmitted"));
        if (submitted) {
            try {
                proxyName = request.getParameter("proxyName");
                if (proxyName == null || "".equals(proxyName)) {
                    throw new Exception("The proxy service name has not been specified");
                }

                logLevel = request.getParameter("logLevel");
                logResponses = request.getParameter("logResponses") != null;
                responseLogLevel = request.getParameter("responseLogLevel");

                String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
                ConfigurationContext configContext =
                        (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
                String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
                ProxyServiceAdminClient client = new ProxyServiceAdminClient(
                        configContext, backendServerURL, cookie, request.getLocale());

                String logMediator;
                if ("full".equals(logLevel)) {
                    logMediator = "<log level=\"full\"/>";
                } else {
                    logMediator = "<log/>";
                }

                ProxyData proxy = new ProxyData();
                proxy.setStartOnLoad(true);
                proxy.setName(proxyName);
                proxy.setInSeqXML("<inSequence xmlns=\"http://ws.apache.org/ns/synapse\">" + logMediator + "</inSequence>");
                if (!logResponses) {
                    proxy.setOutSeqXML("<outSequence xmlns=\"http://ws.apache.org/ns/synapse\"><send/></outSequence>");
                } else {
                    String responseLogMediator;
                    if ("full".equals(responseLogLevel)) {
                        responseLogMediator = "<log level=\"full\"/>";
                    } else {
                        responseLogMediator = "<log/>";
                    }
                    proxy.setOutSeqXML("<outSequence xmlns=\"http://ws.apache.org/ns/synapse\">" + responseLogMediator + "<send/></outSequence>");
                }

                request.setAttribute("proxyDataObject", proxy);
    %>
    <jsp:include page="inc/endpoint_processer.jsp"/>
    <%
                if (request.getAttribute("proxyCreationError") != null) {
                    throw new Exception((String) request.getAttribute("proxyCreationError"));
                }
    %>
    <jsp:include page="inc/publish_wsdl_processer.jsp"/>
    <%
                if (request.getAttribute("proxyCreationError") != null) {
                    throw new Exception((String) request.getAttribute("proxyCreationError"));
                }
    %>
    <jsp:include page="inc/transports_processer.jsp"/>
    <%
                if (request.getAttribute("proxyCreationError") != null) {
                    throw new Exception((String) request.getAttribute("proxyCreationError"));
                }
                client.addProxy(proxy);
                ResourceBundle bundle = ResourceBundle.getBundle("org.wso2.carbon.proxyadmin.ui.i18n.Resources");
                CarbonUIMessage.sendCarbonUIMessage(
                        bundle.getString("proxy.add.success") + " " + proxy.getName(),
                        CarbonUIMessage.INFO, request);
    %>
    <script type="text/javascript">
        proxyCreated('<%=proxyName%>');
    </script>
    <%
            } catch (Exception e) {
                String cause;
                if (e.getCause() != null) {
                    cause = e.getCause().getMessage();
                    cause = cause.replaceAll("\n|\\r|\\t|\\f", "");
                } else {
                    cause = e.getMessage();
                }
    %>
    <script type="text/javascript">
        CARBON.showErrorDialog('<%=cause%>');
    </script>
    <%
            } finally {
                request.removeAttribute("proxyDataObject");
                request.removeAttribute("proxyCreationError");
            }
        }
    %>

    <jsp:include page="inc/metadata.jsp"/>

    <div id="middle">
        <h2><fmt:message key="log-n-forward"/></h2>
        <div id="workArea">
            <p>
                <fmt:message key="log-n-forward.desc"/>
            </p>
            &nbsp;
            <form id="proxy_form" method="POST" action="">
                <input type="hidden" name="formSubmitted" value="true"/>
                <table cellpadding="0" cellspacing="0" border="0" class="styledLeft noBorders">
                    <thead>
                    <tr>
                        <th colspan="2"><fmt:message key="proxy.settings"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td colspan="2" style="padding-bottom:10px;">
                            <table>
                                <tr>
                                    <td style="width:130px;"><fmt:message key="proxy.service.name"/><span class="required">*</span></td>
                                    <td><input id="proxy_name" type="text" name="proxyName" size="40"/></td>
                                </tr>
                                <jsp:include page="inc/endpoint_select.jsp"/>
                                <tr>
                                    <td><fmt:message key="log.level"/></td>
                                    <td>
                                        <select id="log_level" name="logLevel">
                                            <option value="full"><fmt:message key="full"/></option>
                                            <option value="simple"><fmt:message key="simple"/></option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="log.response"/></td>
                                    <td><input id="log_responses" type="checkbox" name="logResponses" onchange="enableLevelSelect();"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="log.response.level"/></td>
                                    <td>
                                        <select id="response_log_level" name="responseLogLevel" disabled="true">
                                            <option value="full"><fmt:message key="full"/></option>
                                            <option value="simple"><fmt:message key="simple"/></option>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <jsp:include page="inc/publish_wsdl.jsp"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <jsp:include page="inc/transports.jsp"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="buttonRow">
                            <button class="button" onclick="createProxy(); return false;"><fmt:message key="create"/></button>
                            <button class="button" onclick="templatesHome(); return false;"><fmt:message key="back"/></button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>

    <%
        if (submitted) {
            if (proxyName != null) {
    %>
        <script type="text/javascript">
            document.getElementById('proxy_name').value = '<%=proxyName%>';
        </script>
    <%
            }
    %>
        <script type="text/javascript">
            document.getElementById('log_level').value = '<%=logLevel%>';
            if ('<%=logResponses%>' == 'true') {
                document.getElementById('log_responses').checked = 'true';
                enableLevelSelect();
                document.getElementById('response_log_level').value = '<%=responseLogLevel%>';
            }
        </script>
    <%
        }
    %>

</fmt:bundle>