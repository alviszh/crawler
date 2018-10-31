package com.microservice.dao.entity.crawler.insurance.taizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="insurance_taizhou_medical",indexes = {@Index(name = "index_insurance_taizhou_medical_taskid", columnList = "taskid")})
public class InsuranceTaiZhouMedical extends IdEntity{

	private String datea;//日期
	
	private String descr;//摘要
	
	private String inMoney;//划入金额
	private String outMoney;//支出金额
	private String fee;//余额
	private String name;//姓名
	private String IDNum;//身份证
	private String personalNum;//个人编号
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceTaiZhouMedical [datea=" + datea + ", descr=" + descr + ", inMoney=" + inMoney + ", outMoney="
				+ outMoney + ", fee=" + fee + ", name=" + name + ", IDNum=" + IDNum + ", personalNum=" + personalNum
				+ ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getInMoney() {
		return inMoney;
	}
	public void setInMoney(String inMoney) {
		this.inMoney = inMoney;
	}
	public String getOutMoney() {
		return outMoney;
	}
	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
