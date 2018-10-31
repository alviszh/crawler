package com.microservice.dao.entity.crawler.housing.hhhnzyzzzq;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45 
 */
@Entity
@Table(name="housing_hhhnzyzzzq_base",indexes = {@Index(name = "index_housing_hhhnzyzzzq_base_taskid", columnList = "taskid")})
public class HousinghhhnzyzzzqBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//用户名
	@Column(name="uname")
	private String uname;		
	//姓名
	@Column(name="name")
	private String name;		
	//身份证号
	@Column(name="cardid")
	private String cardid;		
	//手机
	@Column(name="phone")
	private String phone;		
	//所属地区
	@Column(name="addr")
	private String addr;		
	//公积金账号
	@Column(name="housingNumber")
	private String housingNumber;
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
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getHousingNumber() {
		return housingNumber;
	}
	public void setHousingNumber(String housingNumber) {
		this.housingNumber = housingNumber;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}	
	
}
