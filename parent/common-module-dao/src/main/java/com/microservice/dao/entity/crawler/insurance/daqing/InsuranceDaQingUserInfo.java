package com.microservice.dao.entity.crawler.insurance.daqing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Created by root on 2017/9/26.
 */

@Entity
@Table(name = "insurance_daqing_userinfo",indexes = {@Index(name = "index_insurance_daqing_userinfo_taskid", columnList = "taskid")})
public class InsuranceDaQingUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -4803273521907522624L;
	private String taskid;
    private String insurnum;  //社会保障卡卡号
    private String idnum;  //身份证号
    private String name;  //姓名
    private String gender;  //性别
    private String unitname;  //单位名称
    private String balance;  //医疗账户余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsurnum() {
		return insurnum;
	}
	public void setInsurnum(String insurnum) {
		this.insurnum = insurnum;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
}
