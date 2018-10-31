package com.crawler.maimai.json;

import java.io.Serializable;

public class MaimaiJsonBean implements Serializable{
	private static final long serialVersionUID = 4540902127125955830L;
	private String task_id;  //uuid 唯一标识
    private String username;
    private String password;
    private String verification; //验证码
    private String code;         //区号          
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
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
	public String getVerification() {
		return verification;
	}
	public void setVerification(String verification) {
		this.verification = verification;
	}
    
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "MaimaiJsonBean [task_id=" + task_id + ", username=" + username + ", password=" + password
				+ ", verification=" + verification + ", code=" + code + "]";
	}
}
