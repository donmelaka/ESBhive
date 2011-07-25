<%@ page import="java.net.URL" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>

<jsp:include page="../resources/resources-i18n-ajaxprocessor.jsp"/>

<fmt:bundle basename="org.wso2.carbon.proxyadmin.ui.i18n.Resources">
<script type="text/javascript" src="../sequences/js/registry-browser.js"></script>
    <carbon:breadcrumb
            label="service.proxy.menu.text"
            resourceBundle="org.wso2.carbon.proxyadmin.ui.i18n.Resources"
            topPage="true"
            request="<%=request%>"/>

    <div id="middle">
        <h2><fmt:message key="templates.title"/></h2>
        <div id="workArea">
            <p>
                <fmt:message key="templates.desc"/>
            </p>
            &nbsp;
            <table cellspacing="0" class="styledLeft" id="templatesTable">
                <thead>
                <tr>
                    <th colspan="2"><fmt:message key="select.template"/></th>
                </tr>
                </thead>
                <tbody>
                    <%
                        Set<String> templates = application.getResourcePaths("/proxyservices");
                        Iterator<String> iter = templates.iterator();
                        while (iter.hasNext()) {
                            String template = iter.next();
                            template = template.substring(template.lastIndexOf("/") + 1);
                            String url = template;
                            if (template.startsWith("template_") && template.endsWith(".jsp")) {
                                template = template.substring(9, template.length() - 4);
                    %>
                                <tr>
                                    <td width="20%">
                                        <a href="<%=url%>"><fmt:message key="<%=template%>"/></a>
                                    </td>
                                    <td><fmt:message key="<%=template+".desc"%>"/></td>
                                </tr>
                    <%
                            }
                        }
                    %>
                    <tr>
                        <td width="20%">
                            <a href="./index.jsp?startwiz=true"><fmt:message key="custom.proxy"/></a>
                        </td>
                        <td><fmt:message key="custom.proxy.desc"/></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <script type="text/javascript">
        alternateTableRows('templatesTable', 'tableEvenRow', 'tableOddRow');
    </script>

</fmt:bundle>