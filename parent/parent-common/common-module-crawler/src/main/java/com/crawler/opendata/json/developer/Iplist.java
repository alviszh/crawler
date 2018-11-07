package com.crawler.opendata.json.developer;

import java.io.Serializable;

/**
 * IP
 *
 * @author zmy
 *
 */
public class Iplist  implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4961348868422315732L;

	/**
	 * 应用
	 */
	private App app;
	
	/**
	 * iP
	 */
	private String ip;
	/**
	 * 端口
	 */
	private String port;
	/**
	 * ip描述
	 */
	private String describe;

	public App getApp() {
		return app;
	}
	public void setApp(App app) {
		this.app = app;
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
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}


}