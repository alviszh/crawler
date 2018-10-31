package com.microservice.dao.entity.crawler.e_commerce.etl.jd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="addrinfo_jd") //交易明细
public class AddrInfoJD extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;

	@Column(name="task_id")
	private String taskId; //唯一标识
	private String consignee;
	private String area;
	private String addr;
	private String phone;
	private String fixed_phone;
	
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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFixed_phone() {
		return fixed_phone;
	}

	public void setFixed_phone(String fixed_phone) {
		this.fixed_phone = fixed_phone;
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
		return "AddrInfoJD [taskId=" + taskId + ", consignee=" + consignee + ", area=" + area + ", addr=" + addr
				+ ", phone=" + phone + ", fixed_phone=" + fixed_phone + ", resource=" + resource + "]";
	}
	
	
}
