package com.microservice.dao.entity.crawler.housing.zhanjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_zhanjiang_account",indexes = {@Index(name = "index_housing_zhanjiang_account_taskid", columnList = "taskid")})
public class HousingFundZhanJiangAccount extends IdEntity{

	private String datea;//日期
	private String desrc;//摘要
	private String getMoney;//收入金额
	private String outMoney;//支出金额
	private String fee;//余额
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundZhanJiangAccount [datea=" + datea + ", desrc=" + desrc + ", getMoney=" + getMoney
				+ ", outMoney=" + outMoney + ", fee=" + fee + ", taskid=" + taskid + "]";
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
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
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
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
