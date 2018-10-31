package com.crawler.aws.json;

import java.io.Serializable;

public class HttpProxyBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8417362139272119200L;
	
	
	private String ip;
	
	private String port;
	
	private String name;
	
	private String instanceId;


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public String toString() {
		return "HttpProxyBean [ip=" + ip + ", port=" + port + ", name=" + name + ", instanceId=" + instanceId + "]";
	}

	

	
	
	
	

}
