package com.microservice.dao.entity.crawler.telecom.phone.inquire;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="inquire_phone_item_code",indexes = {@Index(name = "index_inquire_phone_item_code_phone", columnList = "phone")})
//@Table(name = "inquire_phone_item_code")
public class InquirePhone extends IdEntity{
	
	private String taskId;
	
	private String inquireType;           //是否查询
	
    private String phoneType;         //电话号类型
	
	private String markTimes;         //标记次数
	
	private String markType;          //标记类型
	
	private String phone;            //电话
	
	private String phonenumFlag;            //号码标识
	
	private String updateTime;
	
	private String remarkDescription;        //备注
	@Override
	public String toString() {
		return "InquirePhone [taskId=" + taskId + ", inquireType=" + inquireType+ ", phoneType=" + phoneType 
				+ ", markTimes=" + markTimes + ", markType=" + markType + ", phone=" + phone 
				+ ", remarkDescription=" + remarkDescription + ", phonenumFlag=" + phonenumFlag
				+ ", updateTime=" + updateTime+ "]";
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
	public String getRemarkDescription() {
		return remarkDescription;
	}
	public void setRemarkDescription(String remarkDescription) {
		this.remarkDescription = remarkDescription;
	}
	public String getPhonenumFlag() {
		return phonenumFlag;
	}
	public void setPhonenumFlag(String phonenumFlag) {
		this.phonenumFlag = phonenumFlag;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	
}
