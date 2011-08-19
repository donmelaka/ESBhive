<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="org.wso2.carbon.service.mgt.ui.i18n.Resources">
<%
    if (Boolean.valueOf((String) request.getAttribute("isActive"))) {
%>
<span class="icon-text" style="background-image:url(images/activate.gif);"><fmt:message key="active"/>&nbsp; &nbsp;[</span>
<a href="#" class="icon-link" style="background-image:none !important; margin-left: 2px !important; padding-left: 2px !important;" title="<fmt:message key="deactivate.service"/>" onclick="changeServiceState(false); return false;"><fmt:message key="deactivate"/></a>
<span class="icon-text" style="background-image:none !important; margin-left: 2px !important; padding-left: 2px !important;">]</span>
<%} else {%>
<span class="icon-text" style="background-image:url(images/deactivate.gif);"><fmt:message key="inactive"/>&nbsp; &nbsp;[</span><a href="#" class="icon-link" style="background-image:none !important; margin-left: 2px !important; padding-left: 2px !important;" title="<fmt:message key="activate.service"/>" onclick="changeServiceState(true); return false;"><fmt:message key="activate"/></a>
<span class="icon-text" style="background-image:none !important; margin-left: 2px !important; padding-left: 2px !important;">]</span>
<%}%>
</fmt:bundle>
