package com.crawler.mobile.json;

public class MessageLogin {

	private String name;				//手机号

	private String username;			//姓名
	
	private String idNum;				//身份证号码

	private String password;			//服务密码
	
	private String sms_code;			//随机密码
	
	private String appname;

	private Integer user_id;			
	
	private String task_id;
	
	private Integer logintype;
	
	 //@JsonBackReference
    private String ip;

    //@JsonBackReference
    private String port;
    
    

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
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

	public String getSms_code() {
		return sms_code;
	}
	
	public void setSms_code(String sms_code) {
		this.sms_code = sms_code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

	public Integer getLogintype() {
		return logintype;
	}

	public void setLogintype(Integer logintype) {
		this.logintype = logintype;
	}

	@Override
	public String toString() {
		return "MessageLogin [name=" + name + ", username=" + username + ", idNum=" + idNum + ", password=" + password
				+ ", sms_code=" + sms_code + ", appname=" + appname + ", user_id=" + user_id + ", task_id=" + task_id
				+ ", logintype=" + logintype + ", ip=" + ip + ", port=" + port + "]";
	}

}
