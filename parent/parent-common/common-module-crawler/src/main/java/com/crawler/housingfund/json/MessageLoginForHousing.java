package com.crawler.housingfund.json;

public class MessageLoginForHousing {

	private String num;				//登录账号

	private String city;

	private String countNumber;        //职工账号
	
	private String telephone;          //手机号码

	private String hosingFundNumber;    //个人公积金账号

	private String username;			//姓名
	
	private String password;			//服务密码
	
	private String sms_code;			//随机密码
	
	private String logintype;         //登录类型

	private Integer user_id;			
	
	private String task_id;

	//天津公积金
	private String goalImage;        //目标图片
	private String operateImage;        //操作图片
	private String coordinate;         //qzb-tianjin-坐标
	private String result;         //获取坐标的结果
	
	private String callback; //用于解决跨域问题
	
	private String ip;
	private String port;
	private String webdriverHandle;//selenium 的window hander
	
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

	public void setCountNumber(String countNumber) {
		this.countNumber = countNumber;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getCountNumber() { return  countNumber;}

	public void setCountNumbe(String countNumber) {
		this.countNumber = countNumber;
	}

	
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getHosingFundNumber() {
		return  hosingFundNumber;
	}

	public void setHosingFundNumber(String hosingFundNumber) {
		this.hosingFundNumber = hosingFundNumber;
	}

	public String getGoalImage() {
		return goalImage;
	}

	public void setGoalImage(String goalImage) {
		this.goalImage = goalImage;
	}

	public String getOperateImage() {
		return operateImage;
	}

	public void setOperateImage(String operateImage) {
		this.operateImage = operateImage;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {this.city = city; }

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

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

	public String getSms_code() {
		return sms_code;
	}

	public void setSms_code(String sms_code) {
		this.sms_code = sms_code;
	}

	

	public String getLogintype() {
		return logintype;
	}

	public void setLogintype(String logintype) {
		this.logintype = logintype;
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

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "MessageLoginForHousing [num=" + num + ", username=" + username + ", password=" + password
				+ ", sms_code=" + sms_code + ", logintype=" + logintype + ", user_id=" + user_id + ", task_id="
				+ task_id + "]";
	}

	
}
