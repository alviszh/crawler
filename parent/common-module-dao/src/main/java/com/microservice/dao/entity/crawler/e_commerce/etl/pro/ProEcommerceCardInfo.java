package com.microservice.dao.entity.crawler.e_commerce.etl.pro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_ecommerce_card_info",indexes = {@Index(name = "index_pro_ecommerce_card_info_taskid", columnList = "taskId")})
public class ProEcommerceCardInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String bankName;
	private String cardNum;
	private String bankType;
	private String cardHolder;
	private String phonenum;
	private String effectTime;
	private String cardStatus;
	private String lastNum;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getCardHolder() {
		return cardHolder;
	}
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getEffectTime() {
		return effectTime;
	}
	public void setEffectTime(String effectTime) {
		this.effectTime = effectTime;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getLastNum() {
		return lastNum;
	}
	public void setLastNum(String lastNum) {
		this.lastNum = lastNum;
	}
	@Override
	public String toString() {
		return "ProEcommerceCardInfo [taskId=" + taskId + ", resource=" + resource + ", bankName=" + bankName
				+ ", cardNum=" + cardNum + ", bankType=" + bankType + ", cardHolder=" + cardHolder + ", phonenum="
				+ phonenum + ", effectTime=" + effectTime + ", cardStatus=" + cardStatus + ", lastNum=" + lastNum + "]";
	}
	
	
}	
