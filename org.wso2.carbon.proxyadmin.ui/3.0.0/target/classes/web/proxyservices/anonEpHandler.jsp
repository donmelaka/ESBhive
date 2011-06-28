<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.apache.axiom.om.impl.builder.StAXOMBuilder" %>
<%@ page import="java.io.ByteArrayInputStream" %>
<%@ page import="javax.xml.stream.XMLStreamException" %>
<%@ page import="org.apache.axiom.om.OMElement" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("org.wso2.carbon.proxyadmin.ui.i18n.Resources",
            request.getLocale());
    String anonEpAction = request.getParameter("anonEpAction");
    String forwardTo = "";
    if (request.getParameter("cancelled") != null && "true".equals(request.getParameter("cancelled"))) {
        forwardTo = "index.jsp?header=" + session.getAttribute("header") + "&fromdesign=true";
        // removes common attributes
        removeCommonSessionAttributes(session);
    } else {
        if (anonEpAction != null && !"".equals(anonEpAction)) {
            // send path
            // sets the anonOriginator as anonEpHandler.jsp. This will be the page to which result should be returned
            session.setAttribute("anonOriginator", "../proxyservices/anonEpHandler.jsp");
            session.setAttribute("header", request.getParameter("header"));
            session.setAttribute("epMode", "anon");
            ProxyData pd = (ProxyData) session.getAttribute("proxy");
            session.setAttribute("proxy", pd);
            if (bundle.getString("anon.add").equals(anonEpAction)) {
                // going to add a new EP
                forwardTo = "../endpoints/index.jsp";
                // remove anonEpXML attribute from session if exists
                if (session.getAttribute("anonEpXML") != null) {
                    session.removeAttribute("anonEpXML");
                }
                forwardTo = forwardTo + "?toppage=false";
            } else if (bundle.getString("anon.edit").equals(anonEpAction)) {
                // going to modify the existing EP
                String anonEpXML = pd.getEndpointXML();
                if (anonEpXML != null && !"".equals(anonEpXML)) {
                    try {
                        StAXOMBuilder builder = new StAXOMBuilder(new ByteArrayInputStream(anonEpXML.getBytes()));
                        OMElement elem = builder.getDocumentElement();
                        if ((elem = elem.getFirstElement()) != null) {
                            String localName = elem.getLocalName();
                            if ("address".equals(localName)) {
                                // current one is an address EP
                                forwardTo = "../endpoints/addressEndpoint.jsp";
                            } else if ("wsdl".equals(localName)) {
                                // current one is an wsdl EP
                                forwardTo = "../endpoints/WSDLEndpoint.jsp";
                            } else if ("failover".equals(localName)) {
                                // current one is an failover EP
                                forwardTo = "../endpoints/failOverEndpoint.jsp";
                            } else if ("loadbalance".equals(localName)) {
                                // current one is an loadBalance EP
                                forwardTo = "../endpoints/loadBalanceEndpoint.jsp";
                            } else if ("default".equals(localName)) {
                                // current one is an loadBalance EP
                                forwardTo = "../endpoints/defaultEndpoint.jsp";
                            }
                        }
                    } catch (XMLStreamException e) {
                        // todo - handle error
                    }
                    forwardTo = forwardTo + "?toppage=false";
                    session.setAttribute("anonEpXML", anonEpXML);
                }
            }
        } else {
            // return path
            ProxyData pd = (ProxyData) session.getAttribute("proxy");
            String anonEpXML = (String) session.getAttribute("anonEpXML");
            // the user may have cancelled the operation and therefore the anonEpXML may be null as well
            if (anonEpXML != null && !"".equals(anonEpXML)) {
                pd.setEndpointXML(anonEpXML);
            }
            forwardTo = "index.jsp?header=" + session.getAttribute("header") + "&fromdesign=true";
            removeCommonSessionAttributes(session);
        }
    }
%>

<%!
    void removeCommonSessionAttributes(HttpSession session) {
        session.removeAttribute("anonOriginator");
        session.removeAttribute("epMode");
        session.removeAttribute("anonEpXML");
        session.removeAttribute("header");
    }
%>

<script type="text/javascript">
    if (window.location.href.indexOf('originator') != -1 ||
            window.location.href.indexOf('cancelled') != -1) {
        window.location.href = '<%=forwardTo%>';
    } else {
        window.location.href = 'index.jsp';
    }
</script>