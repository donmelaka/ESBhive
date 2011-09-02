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
<%@ page import="org.wso2.carbon.proxyadmin.xsd.ProxyData" %>
<%@ page import="org.esbhive.proxyconf.mgt.ui.ProxyConfManagerClient" %>
<%@ page import="org.esbhive.proxyconf.mgt.client.ProxyDataList" %>
<%@ page import="org.esbhive.proxyconf.mgt.client.ProEsb" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>

<link type="text/css" href="../dialog/js/jqueryui/tabs/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="../dialog/js/jqueryui/tabs/jquery-1.2.6.min.js"></script>
<script type="text/javascript" src="../dialog/js/jqueryui/tabs/jquery-ui-1.6.custom.min.js"></script>
<script type="text/javascript" src="../dialog/js/jqueryui/tabs/jquery.cookie.js"></script>


<script type="text/javascript">
    var allowTabChange = true;
    var emtpyEntries = false;

    $(function() {
        var $myTabs = $("#tabs");

        $myTabs.tabs({
            select: function(event, ui) {
                if (!allowTabChange) {
                    alert("Tab selection is disabled, while you are in the middle of a workflow");
                }
                return allowTabChange;
            },

            show: function(event, ui) {
                var selectedTab = $myTabs.tabs('option', 'selected');
                allowTabChange = true;
            }
        });

        $myTabs.tabs('select', 0);
        if(emtpyEntries){
           $myTabs.tabs('select', 1);
        }
    });
</script>

<%
        String serverURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
        ConfigurationContext configContext =
                (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
        String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);

        NodeManagerClient client;
        ProxyConfManagerClient client2;
        org.esbhive.node.mgt.client.ESBNode[] nodes = new org.esbhive.node.mgt.client.ESBNode[0];
        ProxyData[] prodatalist=new ProxyData[0];
        String[] keylist=new String[0];

       // if(request.getMethod().equalsIgnoreCase("POST")){


        try {
            client = new NodeManagerClient(configContext, serverURL, cookie);
            client2 = new ProxyConfManagerClient(configContext, serverURL, cookie);

           // nodes=client.getNodes();
           if(request.getMethod().equalsIgnoreCase("POST")){
            org.esbhive.node.mgt.client.ESBNode me = new org.esbhive.node.mgt.client.ESBNode();
            me.setPassword(request.getParameter("pswd1"));
            me.setUsername(request.getParameter("uname1"));

            org.esbhive.node.mgt.client.ESBNode him = new org.esbhive.node.mgt.client.ESBNode();
            him.setIpAndPort(request.getParameter("ip2"));
            him.setPassword(request.getParameter("pswd2"));
            him.setUsername(request.getParameter("uname2"));
            //nodes = client.addNodeToHive(me,him);
%>

<div id="middle">
    <h2>Node Management</h2>

    <div id="workArea">
        <table class="styledLeft" id="moduleTable">
            <thead>
                <tr>
                    <th width="40%">Ip Address</th>                            
                    <th width="40%">Https Port</th>
                    <th width="40%">Synapse Port</th>
                    <th width="40%">User Name</th>                    

                </tr>
            </thead>
            <tbody>
                <%
                             for(org.esbhive.node.mgt.client.ESBNode node:nodes){

                %>
                <tr>
                    
                    
                    <td><%=node.getIp()%></td>
                    <td><%=node.getHttpsPort()%></td>
                    <td><%=node.getSynapsePort()%></td>
                    <td><%=node.getUsername()%></td>     
            
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
    <div id="workArea" style="background-color:#F4F4F4;">
        <img border="2" src="../node-mgt/ESBhive_logo_icon.png" >
    </div>
</div>
<div id="middle">
    
    
    <h2>Node Management</h2>
    
    
    <div id="workArea" style="background-color:#F4F4F4;">
    <div id="tabs">

        <ul>
                <li><a href="#tabs-1">tab1</a></li>
                <li><a href="#tabs-2">tab2</a></li>

            </ul>

    
        <div id="tabs-1">
        <table class="styledLeft" id="moduleTable">
            <thead>
                <tr>
                     <th width="40%">Ip Address</th>
                     <th width="40%">Proxies</th>
                    <th width="40%">Https Port</th>
                    <th width="40%">Synapse Port</th>
                    <th width="40%">User Name</th>
                </tr>
            </thead>
            <tbody>
                <%
                             for(org.esbhive.node.mgt.client.ESBNode node:nodes){

                %>
                <tr>
                   <td><%=node.getIp()%></td>
                    <%
                            //=
                            ProxyDataList pdl=client2.getProxyDataList(node.getIpAndPort());
                           String proxies="";
                            if(pdl!=null){
                                prodatalist= pdl.getProxyDataList();
                            if(prodatalist!=null){
                            for(ProxyData pdata:prodatalist){
                                if(!pdata.getName().isEmpty()){
                                    if(!proxies.equals("")){
                                        proxies= proxies + ", " + pdata.getName();
                                    }
                                    else{
                                        proxies= proxies + pdata.getName();
                                    }                                                                  //.concat(pdata.getName());

                            }}}}%>
                            <td><%=proxies%></td>
                           <%

                    %>
                    <td><%=node.getHttpsPort()%></td>
                    <td><%=node.getSynapsePort()%></td>
                    <td><%=node.getUsername()%></td> 

                </tr>
                  
                <%
                             }
                %>
            </tbody>
        </table>
    </div>
            
            
<div id="tabs-2">
            <table class="styledLeft" id="moduleTable1">
                <thead>
                <tr>
                 <th>Proxies/ESBs</th>
                    <%
                             for(org.esbhive.node.mgt.client.ESBNode node1:nodes){

                %>
                 <th><%=node1.getIp()%></th>

                <%
                }
                %>
                </tr>
                </thead>
                <tbody>
                <%
                    keylist=client2.getKeySet();
                %>
                
                    <%
                    if(keylist!=null){
                        for(int i=0;i<keylist.length;i++){
                    %>
                    <tr>
                    <td><%=keylist[i]%></td>
                    <%
                        ProEsb proesb=client2.getProEsb(keylist[i]);
                        org.esbhive.node.mgt.xsd.ESBNode[] esbNodes=new org.esbhive.node.mgt.xsd.ESBNode[0];
                        if(proesb!=null){
                        esbNodes=proesb.getESBNodes();
                        
                        if(esbNodes!=null){
                    %>

                     <%
                             for(org.esbhive.node.mgt.client.ESBNode node2:nodes){
                                 boolean isinlist=false;
                                for(org.esbhive.node.mgt.xsd.ESBNode xsdnode:esbNodes){
                                    if(node2.getIp().equals(xsdnode.getIp())){
                                        isinlist=true;
                                    }
                                }
                             if(isinlist){
                                //print real
                                 %>
                                 <td>real</td>
                                 <%
                             }
                             else {
                                //print dummy
                                %>
                                 <td>dummy</td>
                                 <%
                             }
                %>


                <%
                }
                %>
                    <%
                        }}
                    %>
                    </tr>
                    <%
                        }}
                    %>
                
                <tbody>

            </table>
    </div>
    </div>
    </div>
</div>
<%
}
            else {
%>
<form action="nodemanager.jsp" method="POST" >
    <table width="75%">      
        <tr>
            <td width="48%">This Nodes's Loign Username</td>
            <td width="52%">
                <input type="text" name="uname1" />
            </td>
        </tr>
        <tr>
            <td width="48%">This Nodes's Loign Password</td>
            <td width="52%">
                <input type="password" name="pswd1" />
            </td>
        </tr>
        <tr>
            <td width="48%">Other Node's ip:port</td>
            <td width="52%">
                <input type="text" name="ip2" />
            </td>
        </tr>
        <tr>
            <td width="48%">Other Node's Login Username</td>
            <td width="52%">
                <input type="text" name="uname2" />
            </td>
        </tr>
        <tr>
            <td width="48%">Other Node's Login Password</td>
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




