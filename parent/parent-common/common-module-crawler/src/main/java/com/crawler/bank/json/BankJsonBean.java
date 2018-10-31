package com.crawler.bank.json;

import java.io.Serializable;

public class BankJsonBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4540902127125955830L;


	private String taskid;//任务ID

	private String bankType;//所属银行

	private String loginType;//登录方式（银行卡卡号、邮箱账号、或其他）

	private String loginName;//登录名

	private String cardType;//卡类型 (储蓄卡，信用卡)

	private String password;//密码

	private String webdriverHandle;//selenium 的window hander

	private String appName;//微服务APP 名称

	private String verification;//验证码
	
	private String answer;//答案

	private String cardNumber;//银行卡号

	//@JsonBackReference
	private String ip;

	//@JsonBackReference
	private String port;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWebdriverHandle() {
		return webdriverHandle;
	}

	public void setWebdriverHandle(String webdriverHandle) {
		this.webdriverHandle = webdriverHandle;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public String toString() {
		return "BankJsonBean [taskid=" + taskid + ", bankType=" + bankType + ", loginType=" + loginType + ", loginName="
				+ loginName + ", password=" + password + ", webdriverHandle=" + webdriverHandle + ", appName=" + appName
				+ ", verification=" + verification + ", cardNumber=" + cardNumber + ", ip=" + ip + ", port=" + port
				+ "]";
	}

}
