package com.crawler.pbccrc.json;

public class PbccrcJsonBean {
	private String mapping_id; // uuid 唯一标识
	private String username;
	private String password;
	private String tradecode; // 授权码
	private boolean html; // 是否需要返回html源码
	private String key; // 业务唯一标识
	private String owner;
	private String cookieStr;

	private int version; // 版本
	private boolean isFirst; // 是否是第一次进入登录
	private String serviceName; // 服务名称（人行征信、失信网...）

	// 饿了吗
	// 短信验证码
	private String verification;
	//手机号
	private String phone;

	// @JsonBackReference
	private String ip;

	// @JsonBackReference
	private String port;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTradecode() {
		return tradecode;
	}

	public void setTradecode(String tradecode) {
		this.tradecode = tradecode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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

	public String getMapping_id() {
		return mapping_id;
	}

	public void setMapping_id(String mapping_id) {
		this.mapping_id = mapping_id;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public String getCookieStr() {
		return cookieStr;
	}

	public void setCookieStr(String cookieStr) {
		this.cookieStr = cookieStr;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setIsFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "PbccrcJsonBean{" + "mapping_id='" + mapping_id + '\'' + ", username='" + username + '\''
				+ ", password='" + password + '\'' + ", tradecode='" + tradecode + '\'' + ", html=" + html + ", key='"
				+ key + '\'' + ", owner='" + owner + '\'' + ", cookieStr='" + cookieStr + '\'' + ", version=" + version
				+ ", isFirst=" + isFirst + ", serviceName='" + serviceName + '\'' + ", ip='" + ip + '\'' + ", port='"
				+ port + '\'' + '}';
	}
}
