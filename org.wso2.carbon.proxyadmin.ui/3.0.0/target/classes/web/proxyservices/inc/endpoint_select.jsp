<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.MetaData" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>

<jsp:include page="../resources/resources-i18n-ajaxprocessor.jsp"/>
<script type="text/javascript" src="../sequences/js/registry-browser.js"></script>
<script type="text/javascript" src="../js/proxyservices.js"></script>

<script type="text/javascript">
    function showEndpointOptions(select) {
        var i = select.value;
        if (i == 'url') {
            showElem('targetURL');
            hideElem('predefEndpoints');
            hideElem('endpointReg');
        } else if (i == 'predef') {
            hideElem('targetURL');
            showElem('predefEndpoints');
            hideElem('endpointReg');
        } else if (i == 'reg') {
            hideElem('targetURL');
            hideElem('predefEndpoints');
            showElem('endpointReg');
        }
    }
</script>

<fmt:bundle basename="org.wso2.carbon.proxyadmin.ui.i18n.Resources">
    <carbon:jsi18n
            resourceBundle="org.wso2.carbon.proxyadmin.ui.i18n.JSResources"
            request="<%=request%>"
            i18nObjectName="proxyi18n"/>

    <%
        MetaData md = (MetaData) request.getAttribute("proxyMetadata");
    %>

    <tr>
        <td><fmt:message key="target.endpoint"/><span class="required">*</span></td>
        <td>
            <select id="targetEndpointMode" name="targetEndpointMode" onchange="showEndpointOptions(this);">
                <option value="url"><fmt:message key="endpoint.op.enter.url"/></option>
                <%
                    if (md.getEndpointsAvailable()) {
                %>
                <option value="predef"><fmt:message key="endpoint.op.predef"/></option>
                <%
                    }
                %>
                <option value="reg"><fmt:message key="pick.from.registry"/></option>
            </select>
        </td>
    </tr>
    <tr id="targetURL">
        <td><fmt:message key="target.url"/><span class="required">*</span></td>
        <td><input type="text" name="targetURL" id="targetURLTxt" size="60"/></td>
    </tr>
    <%
        if (md.getEndpointsAvailable()) {
    %>
    <tr id="predefEndpoints" style="display:none;">
        <td><fmt:message key="endpoint.op.predef"/><span class="required">*</span></td>
        <td>
            <select name="predefEndpoint" id="predefEndpointsCombo">
                <%
                    for (int i = 0; i < md.getEndpoints().length; i++) {
                %>
                <option value="<%=md.getEndpoints()[i]%>"><%=md.getEndpoints()[i]%></option>
                <%
                    }
                %>
            </select>
        </td>
    </tr>
    <%
        }
    %>
    <tr id="endpointReg" style="display:none;">
        <td><fmt:message key="endpoint.op.regkey"/><span class="required">*</span></td>
        <td >
            <table class="normal">
                <tr>
                    <td style="padding-left:0px !important">
                        <input type="text" id="endpointRegKey"
                               name="endpointRegKey" size="40"
                               readonly="true"/>
                    </td>
                    <td>
                        <a href="#"
                           class="registry-picker-icon-link"
                           style="padding-left:20px"
                           onclick="showRegistryBrowser('endpointRegKey', '/_system/config');"><fmt:message key="conf.registry"/></a>
                    </td>
                    <td>
                        <a href="#"
                           class="registry-picker-icon-link"
                           style="padding-left:20px"
                           onclick="showRegistryBrowser('endpointRegKey', '/_system/governance');"><fmt:message key="gov.registry"/></a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <%
        if ("true".equals(request.getParameter("formSubmitted"))) {
            String endpointMode = request.getParameter("targetEndpointMode");
            int selectedIndex = 0;
            if ("url".equals(endpointMode)) {
                if (request.getParameter("targetURL") != null) {
    %>
        <script type="text/javascript">
            document.getElementById('targetURLTxt').value = '<%=request.getParameter("targetURL").trim()%>';
        </script>
    <%
                }
            } else if ("predef".equals(endpointMode)) {
                selectedIndex = 1;
    %>
        <script type="text/javascript">
            document.getElementById('predefEndpointsCombo').value = '<%=request.getParameter("predefEndpoint")%>';
        </script>
    <%
            } else if ("reg".equals(endpointMode)) {
                selectedIndex = 2;
            }
    %>
        <script type="text/javascript">
            var select = document.getElementById('targetEndpointMode');
            select.selectedIndex = <%=selectedIndex%>;
            showEndpointOptions(select);
        </script>
    <%
        }
    %>    

</fmt:bundle>