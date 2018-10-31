package com.microservice.dao.entity.crawler.insurance.taizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="insurance_taizhou_endowment",indexes = {@Index(name = "index_insurance_taizhou_endowment_taskid", columnList = "taskid")})
public class InsuranceTaiZhouEndowment extends IdEntity{

	private String taskid;
	private String datea;//记账年月
	private String inDatea;//费款所属期
	private String type;//账户类型
	private String base;//缴费基数
	private String personalMoney;//个人缴纳金额
	private String company;//单位划拨金额
	private String month;//月数
	private String name;//姓名
	private String IDNum;//身份证
	private String personalNum;//个人编号
	@Override
	public String toString() {
		return "InsuranceTaiZhouEndowment [taskid=" + taskid + ", datea=" + datea + ", inDatea=" + inDatea + ", type="
				+ type + ", base=" + base + ", personalMoney=" + personalMoney + ", company=" + company + ", month="
				+ month + ", name=" + name + ", IDNum=" + IDNum + ", personalNum=" + personalNum + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getInDatea() {
		return inDatea;
	}
	public void setInDatea(String inDatea) {
		this.inDatea = inDatea;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getPersonalMoney() {
		return personalMoney;
	}
	public void setPersonalMoney(String personalMoney) {
		this.personalMoney = personalMoney;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	
	
}
