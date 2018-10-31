package com.test.auth;

import java.io.IOException;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.network.*;

public class AuthTest {
	
	/**
	 * az ad sp create-for-rbac --name AzureJavaTest --password "12qwaszx!@"
	 * 
	 * {
		  "appId": "5f3a7826-175d-4e8b-b7a5-d8a79c5dcec4",
		  "displayName": "AzureJavaTest",
		  "name": "http://AzureJavaTest",
		  "password": "12qwaszx!@",
		  "tenant": "366c5b4a-5742-4eb2-aa3d-d9fbda5f1103"
		}
	 * */

	public static void main(String[] args) throws CloudException, IOException {

		String clientId = "5f3a7826-175d-4e8b-b7a5-d8a79c5dcec4";// 上注释json中的appId
		String tennantId = "366c5b4a-5742-4eb2-aa3d-d9fbda5f1103";// 上注释json中的tenant
		String secret = "12qwaszx!@";// 上注释json中的password

		AzureTokenCredentials credentials = new ApplicationTokenCredentials(clientId, tennantId, secret,AzureEnvironment.AZURE_CHINA);
		Azure azure = Azure.authenticate(credentials).withDefaultSubscription();
		String subId = azure.subscriptionId();
		System.out.println("subId--------"+subId);
	  
		PagedList<NetworkInterface> ins =  azure.networkInterfaces().listByResourceGroup("meidi-ubuntu-group");

		for(NetworkInterface in:ins){
//			System.out.println(in.getNetworkSecurityGroup().key()+"--------资源名称"+in.getNetworkSecurityGroup().name()); //网络安全组
			String netWorkkey = in.id();
			System.out.println(in.id()+"--------资源名称"+ in.name()); //网络接口

			System.out.println("netWorkkey=="+netWorkkey);
			azure.publicIPAddresses().deleteById(netWorkkey);
		}

		//获取公共ip
		PagedList<PublicIPAddress> publicIPAddresses = azure.publicIPAddresses().listByResourceGroup("meidi-ubuntu-group");
		for (PublicIPAddress ip :publicIPAddresses) {
			System.out.println(ip.ipAddress() + "-------资源名称"+ip.name());

			System.out.println(ip.hasAssignedNetworkInterface());
			NicIPConfiguration nicIPC = ip.getAssignedNetworkInterfaceIPConfiguration();
			System.out.println(nicIPC);

			nicIPC.getNetworkSecurityGroup().networkInterfaceIds();
		}

	}

}
