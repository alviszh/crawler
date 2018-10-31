package com.microservice.dao.entity.crawler.housing.linyi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_linyi_account",indexes = {@Index(name = "index_housing_linyi_account_taskid", columnList = "taskid")})
public class HousingFundLinYiAccount extends IdEntity implements Serializable{

	private String idNum;//身份证号
	
	private String fundNum;//公积金账号
	
	private String datea;//发生日期
	
	private String info;//摘要
	
	private String money;//发生额（元）
	
	private String descc;//备注
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingFundLinYiAccount [idNum=" + idNum + ", fundNum=" + fundNum + ", datea=" + datea + ", info="
				+ info + ", money=" + money + ", descc=" + descc + ", taskid=" + taskid + "]";
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getFundNum() {
		return fundNum;
	}

	public void setFundNum(String fundNum) {
		this.fundNum = fundNum;
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getDescc() {
		return descc;
	}

	public void setDescc(String descc) {
		this.descc = descc;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
	
}
