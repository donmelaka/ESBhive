<!--
 ~ Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 ~
 ~ WSO2 Inc. licenses this file to you under the Apache License,
 ~ Version 2.0 (the "License"); you may not use this file except
 ~ in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~    http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing,
 ~ software distributed under the License is distributed on an
 ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ~ KIND, either express or implied.  See the License for the
 ~ specific language governing permissions and limitations
 ~ under the License.
-->

<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.esbhive.node.mgt.ui.NodeManagerClient" %>
<%@ page import="org.esbhive.node.mgt.client.ESBNode" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>


<%
        String serverURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
        ConfigurationContext configContext =
                (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
        String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);

        NodeManagerClient client;
        ESBNode[] nodes = new ESBNode[0];

       // if(request.getMethod().equalsIgnoreCase("POST")){


        try {
            client = new NodeManagerClient(configContext, serverURL, cookie);

           // nodes=client.getNodes();
           if(request.getMethod().equalsIgnoreCase("POST")){
            ESBNode me = new ESBNode();           
            me.setIpAndPort(request.getParameter("ip1"));
            me.setUsername(request.getParameter("pswd1"));
            me.setPassword(request.getParameter("uname1"));

            ESBNode him = new ESBNode();
            him.setIpAndPort(request.getParameter("ip2"));
            him.setUsername(request.getParameter("pswd2"));
            him.setPassword(request.getParameter("uname2"));
            nodes = client.addNodeToHive(me,him);
%>

<div id="middle">
    <h2>Node Management</h2>

    <div id="workArea">
        <table class="styledLeft" id="moduleTable">
            <thead>
                <tr>
                    <th width="40%">Ip Address</th>
                    <th width="40%">User Name</th>
                    <th width="20%">Password</th>
                </tr>
            </thead>
            <tbody>
                <%
                             for(ESBNode node:nodes){

                %>
                <tr>
                    <td><%=node.getIpAndPort()%></td>
                    <td><%=node.getUsername()%></td>
                    <td><%=node.getPassword()%></td>

                </tr>

                <%
                             }
                %>
            </tbody>
        </table>
    </div>
</div>
<%
}
            else {
            if(client.getNodes()!=null){
            nodes=client.getNodes();
%>
<div id="middle">
    <h2>Node Management</h2>

    <div id="workArea">
        <table class="styledLeft" id="moduleTable">
            <thead>
                <tr>
                    <th width="40%">Ip Address</th>
                    <th width="40%">User Name</th>
                    <th width="20%">Password</th>
                </tr>
            </thead>
            <tbody>
                <%
                             for(ESBNode node:nodes){

                %>
                <tr>
                    <td><%=node.getIpAndPort()%></td>
                    <td><%=node.getUsername()%></td>
                    <td><%=node.getPassword()%></td>

                </tr>

                <%
                             }
                %>
            </tbody>
        </table>
    </div>
</div>
<%
}
            else {
%>
<form action="nodemanager.jsp" method="POST" >
    <table width="75%">
        <tr>
            <td width="48%">IP Address_1 </td>
            <td width="52%">
                <input type="text" name="ip1" />
            </td>
        </tr>
        <tr>
            <td width="48%">User Name_1 </td>
            <td width="52%">
                <input type="text" name="uname1" />
            </td>
        </tr>
        <tr>
            <td width="48%">Password_1 </td>
            <td width="52%">
                <input type="password" name="pswd1" />
            </td>
        </tr>
        <tr>
            <td width="48%">IP Address_2 </td>
            <td width="52%">
                <input type="text" name="ip2" />
            </td>
        </tr>
        <tr>
            <td width="48%">User Name_2</td>
            <td width="52%">
                <input type="text" name="uname2" />
            </td>
        </tr>
        <tr>
            <td width="48%">Password_2 </td>
            <td width="52%">
                <input type="password" name="pswd2" />
            </td>
        </tr>
    </table>
    <p>
        <input type="submit" name="Submit" value="Add Node" />
        <input type="reset" name="Reset" value="Reset" />
    </p>
</form>
<%
}}
} catch (Exception e) {
CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request, e);
%>
<script type="text/javascript">
    location.href = "../admin/error.jsp";
</script>
<%
            return;
    }
      //  }

%>




