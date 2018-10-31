package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_addvalueitem" ,indexes = {@Index(name = "index_telecom_xinjiang_addvalueitem_taskid", columnList = "taskid")})
public class TelecomXinjiangAddvalueItem extends IdEntity {

	private String serviceName;//产品名称
	private String serviceFee;//业务代码
	private String effectiveDate;//生效时间
	private String failureDate;//失效时间
	private String type;//业务类型 0是增值业务 1是开通的功能

	private String taskid;

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getFailureDate() {
		return failureDate;
	}
	public void setFailureDate(String failureDate) {
		this.failureDate = failureDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomXinjiangAddvalueItem [serviceName=" + serviceName + ", serviceFee=" + serviceFee
				+ ", effectiveDate=" + effectiveDate + ", failureDate=" + failureDate + ", type=" + type + ", taskid="
				+ taskid + "]";
	}
}