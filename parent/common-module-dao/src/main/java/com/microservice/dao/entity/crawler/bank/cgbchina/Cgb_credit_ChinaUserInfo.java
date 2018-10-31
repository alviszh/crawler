package com.microservice.dao.entity.crawler.bank.cgbchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="cgb_credit_china_userinfo",indexes = {@Index(name = "index_cgb_credit_china_userinfo_taskid", columnList = "taskid")})
public class Cgb_credit_ChinaUserInfo extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**姓名*/ 
	@Column(name="name")
	private String name ;
	
	/**安全手机号*/ 
	@Column(name="phone")
	private String phone ;
	
	/**出生日期*/ 
	@Column(name="date")
	private String date ;
	
	/**性别*/ 
	@Column(name="sex")
	private String sex ;
	
	/**联系电话*/ 
	@Column(name="phone2")
	private String phone2 ;
	
	/**email*/ 
	@Column(name="email")
	private String email ;
	
	/**联系地址*/ 
	@Column(name="attr")
	private String attr ;
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	
}
