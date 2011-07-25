<%@ page import="org.wso2.carbon.proxyadmin.ui.client.ProxyServiceAdminClient" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.regex.Pattern" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("org.wso2.carbon.proxyadmin.ui.i18n.Resources",
            request.getLocale());
    String header = request.getParameter("header");

    String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
    ProxyServiceAdminClient client = new ProxyServiceAdminClient(
            configContext, backendServerURL, cookie, request.getLocale());

    String source = request.getParameter("srcTextArea");
    ProxyData pd;
    String param = request.getParameter("submit");
    String forwardTo = "";

    // global validation for the name: checks whether any invalid character is present or not
    boolean validName = false;
    boolean invalidCharInName = false;
    String str;
    int index = source.indexOf(" name");
    if (index != -1) {
        str = source.substring(index);
        index = str.indexOf("\"");
        if (index != -1) {
            int end = str.indexOf("\" ");
            if (end != -1) {
                // str contains whatever there is inside the first double quotes which immediately
                // follows the name attribute
                str = str.substring(index + 1, end);
                // regular expression to match any string containing the following special characters
                // Note: _ (underscore) character is allowed to be present in the text
                // ~ ! @ # $ % ^ & * ( ) \ / + = - : ; < > ' " ? [ ] { } | \s ,
                if (Pattern.matches("\\p{Alnum}*[~!@#$%^&*()\\+=\\-:;<>\\s?\\[\\]{},/\\\\\"]+\\p{Alnum}*", str)) {
                    session.setAttribute("proxyXML", source);
                    forwardTo = "source.jsp?header=" + header;
                    CarbonUIMessage.sendCarbonUIMessage(bundle.getString("invalid.char.in.name"),
                            CarbonUIMessage.ERROR, request);
                    invalidCharInName = true;
                } else {
                    // the name does not match with given regular expression means that it does not contain any
                    // special characters. Therefore, it is a valid name
                    validName = true;
                }
            }
        }
    }

    if (validName && !invalidCharInName) {
        try {
            // replaces any & mark to &amp;
            // this is a lazy way of doing this, first change all &amp; into & and then change all & into &amp;
            // a good way would be to use regular expressions
            source = source.replaceAll("&amp;", "&").replaceAll("&","&amp;");
            pd = client.getDesignView(source);
            session.setAttribute("proxy", pd);

            // checks whether we are going to submit or just switching to design view
            if (param != null) {

                // ok we are going to submit
                if (pd.getName() == null || "".equals(pd.getName())) {

                    // missing proxy name
                    // now the XML is wrong so set the wrong XML to the session for user to see that
                    // and remove the proxy data object from session since it is wrong
                    session.removeAttribute("proxy");
                    session.setAttribute("proxyXML", source);
                    forwardTo = "source.jsp?header=" + header;
                    CarbonUIMessage.sendCarbonUIMessage(bundle.getString("proxy.name.missing"),
                            CarbonUIMessage.ERROR, request);
                } else if((pd.getInSeqKey() == null || "".equals(pd.getInSeqKey())) &&
                                (pd.getInSeqXML() == null || "".equals(pd.getInSeqXML())) &&
                                (pd.getEndpointKey() == null || "".equals(pd.getEndpointKey())) &&
                                (pd.getEndpointXML() == null || "".equals(pd.getEndpointXML()))) {
                    // missing in sequence or endpoint
                    // now the XML is wrong so set the wrong XML to the session for user to see that
                    // and remove the proxy data object from session since it is wrong
                    session.removeAttribute("proxy");
                    session.setAttribute("proxyXML", source);
                    forwardTo = "source.jsp?header=" + header;
                    CarbonUIMessage.sendCarbonUIMessage(bundle.getString("proxy.target.missing"),
                            CarbonUIMessage.ERROR, request);
                } else {

                    // validation went smooth and fine for the submission
                    forwardTo = "submit.jsp?forwardTo=" + request.getParameter("forwardTo") + "&submit=" + param + "&originator=sourceToData.jsp&header=" + header;
                }
            } else if ((param = request.getParameter("return")) != null) {

                // forward to design page and this does not require validation
                forwardTo = param + "?header=" + header + "&ordinal=1";
            }
        } catch (Exception e) {

            // could not create proxy data object from the given source
            session.setAttribute("proxyXML", source);
            forwardTo = "source.jsp?header=" + header;
            String msg = bundle.getString("unable.to.generate.proxy.data.from.source") + ": " +
                    e.getMessage().replaceAll("\n", "").replaceAll("\"", "'"); 
            CarbonUIMessage.sendCarbonUIMessage(msg, CarbonUIMessage.ERROR, request);
        }
    } else if (!validName && !invalidCharInName) {
        session.setAttribute("proxyXML", source);
        forwardTo = "source.jsp?header=" + header;
        CarbonUIMessage.sendCarbonUIMessage(bundle.getString("invalid.name"),
                CarbonUIMessage.ERROR, request);
    }
%>

<script type="text/javascript">
    if (window.location.href.indexOf('originator') != -1) {
        window.location.href='<%=forwardTo%>';
    } else {
        window.location.href='source.jsp';
    }
</script>