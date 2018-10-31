package com.microservice.dao.entity.crawler.insurance.shanghai;


import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 上海社保个人基本信息
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_shanghai_userinfo" ,indexes = {@Index(name = "index_insurance_shanghai_userinfo_taskid", columnList = "taskid")})
public class InsuranceShanghaiUserInfo extends IdEntity {
	
	/**
	 * taskid  uuid 前端通过uuid访问状态结果
	 */
	private String taskid;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 身份证号
	 */
	private String idNumber;

	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 电话
	 */
	private String telephone;
	
	/**
	 * 参保单位名称
	 */
	private String company;
	
	/**
	 * 区县
	 */
	private String area;
	
	/**
	 * 街道
	 */
	private String street;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 邮编
	 */
	private String postal;

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

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	@Override
	public String toString() {
		return "InsuranceShanghaiUserInfo [taskid=" + taskid + ", name=" + name + ", idNumber=" + idNumber + ", mobile="
				+ mobile + ", telephone=" + telephone + ", company=" + company + ", area=" + area + ", street=" + street
				+ ", address=" + address + ", postal=" + postal + "]";
	}

	public InsuranceShanghaiUserInfo(String taskid, String name, String idNumber, String mobile, String telephone,
			String company, String area, String street, String address, String postal) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idNumber = idNumber;
		this.mobile = mobile;
		this.telephone = telephone;
		this.company = company;
		this.area = area;
		this.street = street;
		this.address = address;
		this.postal = postal;
	}
	
	public InsuranceShanghaiUserInfo() {
		super();
	}

}
