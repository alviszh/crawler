package com.microservice.dao.entity.crawler.e_commerce.etl.pro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_ecommerce_credit_info",indexes = {@Index(name = "index_pro_ecommerce_credit_info_taskid", columnList = "taskId")})
public class ProEcommerceCreditInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String creditName;
	private String creditLimit;
	private String avialableLimit;
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
	public String getCreditName() {
		return creditName;
	}
	public void setCreditName(String creditName) {
		this.creditName = creditName;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getAvialableLimit() {
		return avialableLimit;
	}
	public void setAvialableLimit(String avialableLimit) {
		this.avialableLimit = avialableLimit;
	}
	@Override
	public String toString() {
		return "ProEcommerceCreditInfo [taskId=" + taskId + ", resource=" + resource + ", creditName=" + creditName
				+ ", creditLimit=" + creditLimit + ", avialableLimit=" + avialableLimit + "]";
	}
	
	
		
}	
