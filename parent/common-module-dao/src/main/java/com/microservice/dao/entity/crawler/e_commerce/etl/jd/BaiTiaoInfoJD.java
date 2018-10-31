package com.microservice.dao.entity.crawler.e_commerce.etl.jd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="baitiaoinfo_jd") //交易明细
public class BaiTiaoInfoJD extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;

	@Column(name="task_id")
	private String taskId; //唯一标识
	private String loginName;
	private String creditPoints;
	private String debts;
	private String creditLimit;
	
	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCreditPoints() {
		return creditPoints;
	}

	public void setCreditPoints(String creditPoints) {
		this.creditPoints = creditPoints;
	}

	public String getDebts() {
		return debts;
	}

	public void setDebts(String debts) {
		this.debts = debts;
	}
	
	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
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
		return "BaiTiaoInfoJD [taskId=" + taskId + ", loginName=" + loginName + ", creditPoints=" + creditPoints
				+ ", debts=" + debts + ", creditLimit=" + creditLimit + ", resource=" + resource + "]";
	}
		
}
