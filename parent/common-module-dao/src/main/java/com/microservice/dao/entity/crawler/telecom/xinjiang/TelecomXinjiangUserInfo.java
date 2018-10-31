package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_userinfo" ,indexes = {@Index(name = "index_telecom_xinjiang_userinfo_taskid", columnList = "taskid")})
public class TelecomXinjiangUserInfo extends IdEntity {

	private String username; // 机主姓名
	private String certType; // 证件类型
	private String certNumber;// 证件号码
	private String contactPhone;// 联系电话
	private String contactAddress;// 通信地址
	private String contactEmail; // 邮箱
	private String postCod; // 邮编
	private String createDate; // 创建日期
	private String ownFee; // 欠费
	private String amount; // 余额
	private String useablePoint;//可用积分
	private String invalidPoint;//即将到期积分
	private String taskid;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertNumber() {
		return certNumber;
	}
	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getPostCod() {
		return postCod;
	}
	public void setPostCod(String postCod) {
		this.postCod = postCod;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getOwnFee() {
		return ownFee;
	}
	public void setOwnFee(String ownFee) {
		this.ownFee = ownFee;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getUseablePoint() {
		return useablePoint;
	}
	public void setUseablePoint(String useablePoint) {
		this.useablePoint = useablePoint;
	}
	public String getInvalidPoint() {
		return invalidPoint;
	}
	public void setInvalidPoint(String invalidPoint) {
		this.invalidPoint = invalidPoint;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomXinjiangUserInfo [username=" + username + ", certType=" + certType + ", certNumber=" + certNumber
				+ ", contactPhone=" + contactPhone + ", contactAddress=" + contactAddress + ", contactEmail="
				+ contactEmail + ", postCod=" + postCod + ", createDate=" + createDate + ", ownFee=" + ownFee
				+ ", amount=" + amount + ", taskid=" + taskid + "]";
	}
}