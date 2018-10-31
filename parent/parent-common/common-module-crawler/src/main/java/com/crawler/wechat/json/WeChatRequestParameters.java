package com.crawler.wechat.json;

import java.io.Serializable;


public class WeChatRequestParameters implements Serializable{

	private static final long serialVersionUID = -5115154845415106526L;


    public String taskid;
    public String city;                //所属城市
    public String loginType;
    public String name;
    public String idnum;
    public String username;
    private String baseCode;//二维码的base64码 
    private String qrUrl;//二维码解析后的链接地址

	private String ip;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "WeChatRequestParameters [taskid=" + taskid + ", city=" + city + ", loginType=" + loginType + ", name="
				+ name + ", idnum=" + idnum + ", username=" + username + ", baseCode=" + baseCode + ", qrUrl=" + qrUrl
				+ ", ip=" + ip + ", port=" + port + ", webdriverHandle=" + webdriverHandle + "]";
	}
	private String port;
	private String webdriverHandle;//selenium 的window hander
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBaseCode() {
		return baseCode;
	}
	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}
	public String getQrUrl() {
		return qrUrl;
	}
	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
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
	
	public String getWebdriverHandle() {
		return webdriverHandle;
	}
	public void setWebdriverHandle(String webdriverHandle) {
		this.webdriverHandle = webdriverHandle;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
