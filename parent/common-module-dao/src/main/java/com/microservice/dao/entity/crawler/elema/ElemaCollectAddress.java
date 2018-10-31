package com.microservice.dao.entity.crawler.elema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="elema_collectAddress",indexes = {@Index(name = "index_elema_collectAddress_taskid", columnList = "taskid")})
public class ElemaCollectAddress extends IdEntity{
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**店铺名称 */
	@Column(name="name")
	private String name;
	
	
	/**店铺地址 */
	@Column(name="address")
	private String address;
	
	
	/**店铺描述 */
	@Column(name="description")
	private String description;
	
	
	/**店铺Id */
	@Column(name="authentic_id")
	private String authentic_id;
	
	
	/**店铺联系电话 */
	@Column(name="phone")
	private String phone;


	public String getTaskid() {
		return taskid;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getAuthentic_id() {
		return authentic_id;
	}


	public void setAuthentic_id(String authentic_id) {
		this.authentic_id = authentic_id;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
