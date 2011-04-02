package lk.uom.esbhive;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.authenticator.proxy.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.proxy.AuthenticationExceptionException;
import org.wso2.carbon.esbnode.mgt.EsbNodeManagerStub;
import org.wso2.carbon.esbnode.mgt.GetEsbNodesResponse;
import org.wso2.carbon.esbnode.mgt.data.xsd.EsbNode;
//import org.wso2.carbon.ui.loggeduserinfo.ProxyAdminException;
//import org.wso2.carbon.ui.loggeduserinfo.ProxyServiceAdminStub;

/**
 * Hello world!
 *
 */
public class App {
// main method should be method

   // private String userName;
    //private String password;
    //private String ipAddress;
    private AuthenticationAdminStub stub;
    private ConfigurationContext ctx;

    public static void main(String[] args) throws RemoteException, AuthenticationExceptionException {
        App app1 = new App();
        app1.remoteLogin("admin", "admin", "localhost");
        app1.remoteLogout();

    }

    public void remoteLogin(String userName, String password, String ipAddress) throws RemoteException, AuthenticationExceptionException {
        String esb_home = "C:/WSO2/wso2esb-3.0.1/wso2esb-3.0.1";

        System.setProperty("javax.net.ssl.trustStore",
                esb_home + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
         ctx =
                ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

        String serviceEPR = "https://localhost:9443/services/" + "AuthenticationAdmin";//url of the service
        stub = new AuthenticationAdminStub(ctx, serviceEPR);

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean isLogged = stub.login(userName, password, ipAddress);



        String cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
                HTTPConstants.COOKIE_STRING);

        /*
        String serviceEPR5 = "https://localhost:9443/services/" + "SequenceAdmin";


        SequenceAdminServiceStub stub5= new SequenceAdminServiceStub(ctx,serviceEPR5);

        ServiceClient client5 = stub5._getServiceClient();
        Options option = client5.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);

        String s = null;
        try {
        s = stub5.enableTracing("test");
        } catch (SequenceEditorException ex) {
        ex.printStackTrace();
        }

        System.out.println(s);*/



        String serviceEPR4 = "https://localhost:9443/services/" + "EsbNodeManger";

        EsbNodeManagerStub stub4 = new EsbNodeManagerStub(ctx, serviceEPR4);

        ServiceClient client4 = stub4._getServiceClient();
        Options option = client4.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
        String[] ret = null;
        EsbNode e1 = new EsbNode();
        e1.setIpAddress("localhost");
        e1.setPassword("admin");
        e1.setUserName("admin");
        stub4.addEsbNode(e1);
        EsbNode[] t = stub4.getEsbNodes();

        for (int i = 0; i < t.length; i++) {
            System.out.println(t[i].getIpAddress());
        }

        e1.setIpAddress("localhost");
        e1.setPassword("admin1");
        e1.setUserName("admin1");
        t = stub4.addEsbNodeWithReturn(e1);

        for (int i = 0; i < t.length; i++) {
            System.out.println(t[i].getIpAddress());
        }




        /*
        String serviceEPR3 = "https://localhost:9443/services/" + "UserAdmin";


        UserAdminStub stub3= new UserAdminStub(ctx,serviceEPR3);

        ServiceClient client3 = stub3._getServiceClient();
        Options option = client3.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);

        UserStoreInfo lsi = null;
        try {
        lsi = stub3.getUserStoreInfo();
        } catch (UserAdminExceptionException ex) {
        ex.printStackTrace();
        }

        System.out.println(lsi.getAdminRole());

         */
        /*
        String serviceEPR2 = "https://localhost:9443/services/" + "LoggedUserInfoAdmin";


        LoggedUserInfoAdminStub stub2= new LoggedUserInfoAdminStub(ctx,serviceEPR2);

        ServiceClient client2 = stub2._getServiceClient();
        Options option = client2.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);


        System.out.println(isLogged);
        LoggedUserInfo lui = stub2.getUserInfo();

        System.out.println(lui.getUserName());

         */

    }

    public void remoteLogout() throws RemoteException, AuthenticationExceptionException {
        stub.logout();
    }
/*
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String IpAddress) {
        this.ipAddress = IpAddress;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }*/
}
