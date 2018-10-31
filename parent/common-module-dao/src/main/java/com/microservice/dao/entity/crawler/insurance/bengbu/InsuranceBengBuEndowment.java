package com.microservice.dao.entity.crawler.insurance.bengbu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name="insurance_bengbu_endowment",indexes = {@Index(name = "index_insurance_bengbu_endowment_taskid", columnList = "taskid")})
public class InsuranceBengBuEndowment extends IdEntity{

	private String datea;//台账年月
	private String inDate;//费款所属期
	private String companyMoney;//单位应缴金额
	private String personalMoney;//个人应缴金额
	private String sumMoney;//应缴总金额
	private String base;//缴费基数
	private String flag;//划账标志
	private String type;//险种类型
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceBengBuMedical [datea=" + datea + ", inDate=" + inDate + ", companyMoney=" + companyMoney
				+ ", personalMoney=" + personalMoney + ", sumMoney=" + sumMoney + ", base=" + base + ", flag=" + flag
				+ ", type=" + type + ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getCompanyMoney() {
		return companyMoney;
	}
	public void setCompanyMoney(String companyMoney) {
		this.companyMoney = companyMoney;
	}
	public String getPersonalMoney() {
		return personalMoney;
	}
	public void setPersonalMoney(String personalMoney) {
		this.personalMoney = personalMoney;
	}
	public String getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
