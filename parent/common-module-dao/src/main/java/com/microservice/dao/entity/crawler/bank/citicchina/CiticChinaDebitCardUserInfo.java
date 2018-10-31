package com.microservice.dao.entity.crawler.bank.citicchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="citicchina_debitcard_userinfo",indexes = {@Index(name = "index_citicchina_debitcard_userinfo_taskid", columnList = "taskid")}) 
public class CiticChinaDebitCardUserInfo extends IdEntity implements Serializable{

    private String name;
	
	private String sex;
	
	private String lastTime;
	
	private String phone;
	
	private String Idcard;
	
	private String email;
	
	private String addr;
	
	private String code;
	
	private String username;//用户名
	
	private String num;//认证手机号
	
	private String taskid;

	@Override
	public String toString() {
		return "CiticChinaCreditCardUserInfo [name=" + name + ", sex=" + sex + ", lastTime=" + lastTime + ", phone="
				+ phone + ", Idcard=" + Idcard + ", email=" + email + ", addr=" + addr + ", code=" + code
				+ ", username=" + username + ", num=" + num + ", taskid=" + taskid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdcard() {
		return Idcard;
	}

	public void setIdcard(String idcard) {
		Idcard = idcard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
