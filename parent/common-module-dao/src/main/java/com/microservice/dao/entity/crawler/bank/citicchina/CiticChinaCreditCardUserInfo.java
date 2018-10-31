package com.microservice.dao.entity.crawler.bank.citicchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="citicchina_creditcard_userinfo",indexes = {@Index(name = "index_citicchina_creditcard_userinfo_taskid", columnList = "taskid")}) 
public class CiticChinaCreditCardUserInfo extends IdEntity implements Serializable{

	private String name;//姓名
	private String phone;//电话号码
	
	private String phoneNum;//紧急联系人号码
	
	private String addr;//地址
	
	private String code;//邮政编码
	
	private String taskid;

	private String email;
	
	private String available;//可用余额

	private String lastNumber;//卡号后四位

	@Override
	public String toString() {
		return "CiticChinaCreditCardUserInfo [name=" + name + ", phone=" + phone + ", phoneNum=" + phoneNum + ", addr="
				+ addr + ", code=" + code + ", taskid=" + taskid + ", email=" + email + ", available=" + available
				+ ", lastNumber=" + lastNumber + "]";
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

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}
	
	
	
}
