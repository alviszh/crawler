package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_business_info",indexes = {@Index(name = "index_mobile_business_info_taskid", columnList = "taskId")})


/*
 * 手机业务信息 
 */
public class MobileBusinessInfo extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;		//唯一标识
	private String brand;		//品牌名称
	private String businessName;		//业务名称
	private String businessFee;		//业务花费
	private String feeDuration;		//费用周期
	private String effectTime;		//生效日期
	private String telephoneNumber; //用户手机号
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getBusinessFee() {
		return businessFee;
	}
	public void setBusinessFee(String businessFee) {
		this.businessFee = businessFee;
	}
	public String getFeeDuration() {
		return feeDuration;
	}
	public void setFeeDuration(String feeDuration) {
		this.feeDuration = feeDuration;
	}
	public String getEffectTime() {
		return effectTime;
	}
	public void setEffectTime(String effectTime) {
		this.effectTime = effectTime;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Override
	public String toString() {
		return "MobileBusinessInfo [taskId=" + taskId + ", brand=" + brand + ", businessName=" + businessName
				+ ", businessFee=" + businessFee + ", feeDuration=" + feeDuration + ", effectTime=" + effectTime
				+ ", telephoneNumber=" + telephoneNumber + ", resource=" + resource + "]";
	}
	
	
}
