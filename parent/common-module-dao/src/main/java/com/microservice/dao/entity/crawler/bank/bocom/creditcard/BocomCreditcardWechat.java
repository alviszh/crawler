/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocom.creditcard;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-22 15:52:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name="bocom_creditcard_wechat",indexes = {@Index(name = "index_bocom_creditcard_wechat_taskid", columnList = "taskid")})
public class BocomCreditcardWechat extends IdEntity {

	@Override
	public String toString() {
		return "BocomCreditcardWechat [contactType=" + contactType + ", contactName=" + contactName + ", verified="
				+ verified + ", isAutoBind=" + isAutoBind + ", taskid=" + taskid + "]";
	}

	private String contactType;// WECHAT 微信
	private String contactName;//微信昵称
	private String verified;//true 含义未知
	private String isAutoBind;//"" 含义未知
	
	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getVerified() {
		return verified;
	}

	public void setIsAutoBind(String isAutoBind) {
		this.isAutoBind = isAutoBind;
	}

	public String getIsAutoBind() {
		return isAutoBind;
	}

}
