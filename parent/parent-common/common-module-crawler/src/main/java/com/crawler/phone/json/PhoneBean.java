package com.crawler.phone.json;

import java.io.Serializable;

public class PhoneBean implements Serializable{
	private static final long serialVersionUID = -8417362139272119200L;
    private String taskId;
	
	private String inquireType;           //是否查询
	
    private String phoneType;         //电话号类型
	
	private String markTimes;         //标记次数
	
	private String markType;          //标记类型
		
	private String phone;            //电话
	
	private String phonenumFlag;            //号码标识
	
	@Override
	public String toString() {
		return "InquirePhone [taskId=" + taskId + ", inquireType=" + inquireType+ ", phoneType=" + phoneType 
				+ ", markTimes=" + markTimes + ", markType=" + markType + ", phone=" + phone 
			    + ", phonenumFlag=" + phonenumFlag+ "]";
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getInquireType() {
		return inquireType;
	}
	public void setInquireType(String inquireType) {
		this.inquireType = inquireType;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	public String getMarkTimes() {
		return markTimes;
	}
	public void setMarkTimes(String markTimes) {
		this.markTimes = markTimes;
	}
	public String getMarkType() {
		return markType;
	}
	public void setMarkType(String markType) {
		this.markType = markType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhonenumFlag() {
		return phonenumFlag;
	}
	public void setPhonenumFlag(String phonenumFlag) {
		this.phonenumFlag = phonenumFlag;
	}

}
