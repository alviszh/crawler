package com.microservice.dao.entity.crawler.insurance.taizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="insurance_taizhou_injury",indexes = {@Index(name = "index_insurance_taizhou_injury_taskid", columnList = "taskid")})
public class InsuranceTaiZhouInjury extends IdEntity{

	private String datea;//年月
	private String money;//伤残津贴
	private String inMoney;//伤残津贴养老金补差
	private String careMoney;//护理费
	private String name;//姓名
	private String IDNum;//身份证号码
	private String personalNum;//个人编码
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceTaiZhouInjury [datea=" + datea + ", money=" + money + ", inMoney=" + inMoney + ", careMoney="
				+ careMoney + ", name=" + name + ", IDNum=" + IDNum + ", personalNum=" + personalNum + ", taskid="
				+ taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getInMoney() {
		return inMoney;
	}
	public void setInMoney(String inMoney) {
		this.inMoney = inMoney;
	}
	public String getCareMoney() {
		return careMoney;
	}
	public void setCareMoney(String careMoney) {
		this.careMoney = careMoney;
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
