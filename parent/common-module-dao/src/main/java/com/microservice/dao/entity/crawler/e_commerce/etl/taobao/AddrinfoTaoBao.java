package com.microservice.dao.entity.crawler.e_commerce.etl.taobao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="addrinfo_taobao") //交易明细
public class AddrinfoTaoBao extends IdEntity implements Serializable {

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String consignee;
	private String area;
	private String address;
	private String phone;
	private String fixedPhone;
	private String email;
	private String isDefaultAddr;

	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFixedPhone() {
		return fixedPhone;
	}

	public void setFixedPhone(String fixedPhone) {
		this.fixedPhone = fixedPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsDefaultAddr() {
		return isDefaultAddr;
	}

	public void setIsDefaultAddr(String isDefaultAddr) {
		this.isDefaultAddr = isDefaultAddr;
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
		return "AddrinfoTaoBao [taskId=" + taskId + ", consignee=" + consignee + ", area=" + area + ", address="
				+ address + ", phone=" + phone + ", fixedPhone=" + fixedPhone + ", email=" + email + ", isDefaultAddr="
				+ isDefaultAddr + ", resource=" + resource + "]";
	}
	
	
}
