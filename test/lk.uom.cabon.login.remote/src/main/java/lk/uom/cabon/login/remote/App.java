package lk.uom.cabon.login.remote;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.authenticator.proxy.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.proxy.AuthenticationExceptionException;
import org.wso2.carbon.ui.loggeduserinfo.ProxyAdminException;
import org.wso2.carbon.ui.loggeduserinfo.ProxyServiceAdminStub;



/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws RemoteException, AuthenticationExceptionException
    {
    	String esb_home="/home/pubudu/L4S1/project/wso2esb-3.0.1";
    	
    	System.setProperty("javax.net.ssl.trustStore", 
				esb_home + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks"); 
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
    	ConfigurationContext ctx = 
			ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
		
    	String serviceEPR = "https://localhost:9443/services/" + "AuthenticationAdmin";
    	AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);
    	
    	ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);
        
        boolean isLogged = stub.login("admin", "admin", "127.0.0.1");
        
        
        
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


        
        String serviceEPR4 = "https://localhost:9443/services/" + "ProxyServiceAdmin";


    	ProxyServiceAdminStub stub4= new ProxyServiceAdminStub(ctx,serviceEPR4);

    	ServiceClient client4 = stub4._getServiceClient();
        Options option = client4.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
        String[] ret = null;
        try {
            ret = stub4.getAvailableSequences();           
        } catch (ProxyAdminException ex) {
            ex.printStackTrace();
        }

        
        for(int i=0;i<ret.length;i++){
            System.out.println(ret[i]);
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
    	stub.logout();
    	
    	
    	
    	
    	
    		
    		
        
        
    }
}

