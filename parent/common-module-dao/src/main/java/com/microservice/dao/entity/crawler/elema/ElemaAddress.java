package com.microservice.dao.entity.crawler.elema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="elema_address",indexes = {@Index(name = "index_elema_address_taskid", columnList = "taskid")})
public class ElemaAddress extends IdEntity{
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**收货人姓名 */
	@Column(name="name")
	private String name;
	/**收货人电话 */
	@Column(name="phone")
	private String phone;
	/**收货人地址Id */
	@Column(name="address_id")
	private String address_id;
	/**收货人地址 */
	@Column(name="address")
	private String address;
	/**收货人详细地址 */
	@Column(name="address_detail")
	private String address_detail;
	/**标签*/
	@Column(name="tag")
	private String tag;
	/**是否已校验   1-已校验；0-未校验 */
	@Column(name="is_valid")
	private String is_valid;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress_id() {
		return address_id;
	}
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress_detail() {
		return address_detail;
	}
	public void setAddress_detail(String address_detail) {
		this.address_detail = address_detail;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getIs_valid() {
		return is_valid;
	}
	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
