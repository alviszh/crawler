package com.crawler.aws.json;

import java.io.Serializable;
import java.util.List;

public class HttpProxyRes implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7463181586825861329L;

	private String ip;
	
	private String port;
	
	private boolean result;
	
	private String message;

	private String name; 
	
	private String instanceId;
	
	private int totalnum; 
	
	private String city;
	
	private String updateTime;
	
	private int errornum;
	
	private List<HttpProxyBean> httpProxyBeanSet;

	public boolean getResult() {
		return result;
	}
	
	public void setResult(boolean result) {
		this.result = result;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	public int getErrornum() {
		return errornum;
	}

	public void setErrornum(int errornum) {
		this.errornum = errornum;
	}

	public List<HttpProxyBean> getHttpProxyBeanSet() {
		return httpProxyBeanSet;
	}

	public void setHttpProxyBeanSet(List<HttpProxyBean> httpProxyBeanSet) {
		this.httpProxyBeanSet = httpProxyBeanSet;
	}

	
	


}
