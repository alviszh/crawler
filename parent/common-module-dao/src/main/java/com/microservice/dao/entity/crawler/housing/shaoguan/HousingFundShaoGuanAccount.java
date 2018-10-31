package com.microservice.dao.entity.crawler.housing.shaoguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_shaoguan_account",indexes = {@Index(name = "index_housing_shaoguan_account_taskid", columnList = "taskid")})
public class HousingFundShaoGuanAccount extends IdEntity{

	private String datea;//业务日期
	private String desrc;//业务摘要
	private String money;//发生额
	private String fee;//余额
	private String tadkid;
	@Override
	public String toString() {
		return "HousingFundShaoGuanAccount [datea=" + datea + ", desrc=" + desrc + ", money=" + money + ", fee=" + fee
				+ ", tadkid=" + tadkid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getDesrc() {
		return desrc;
	}
	public void setDesrc(String desrc) {
		this.desrc = desrc;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getTadkid() {
		return tadkid;
	}
	public void setTadkid(String tadkid) {
		this.tadkid = tadkid;
	}
	
}
