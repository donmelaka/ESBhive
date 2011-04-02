/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Melaka
 */
package org.wso2.carbon.esbnode.mgt.data;

public class EsbNode {
	
	private String IpAddress;
        private String userName;
        private String password;
	
	public String getIpAddress(){
		return this.IpAddress;
	}
	
	public void setIpAddress(String IpAddress){
		this.IpAddress = IpAddress;
	}
        public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
        public String getPassword(){
		return this.password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
}

