package com.crawler.mobile.json;

import java.io.Serializable;

public class MessageResult implements Serializable{

	private static final long serialVersionUID = 5315507536737925954L;

	private String type;			//
	
	private String usernum;			//手机号

	private String code;			//服务密码
	
	private String sms_code;		//随机密码

	private Integer user_id;			//user_id
	
	private String task_id;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsernum() {
		return usernum;
	}

	public void setUsernum(String usernum) {
		this.usernum = usernum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSms_code() {
		return sms_code;
	}

	public void setSms_code(String sms_code) {
		this.sms_code = sms_code;
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

	@Override
	public String toString() {
		return "MessageResult [type=" + type + ", usernum=" + usernum + ", code=" + code + ", sms_code=" + sms_code
				+ ", user_id=" + user_id + ", task_id=" + task_id + "]";
	}
}
