package com.microservice.dao.entity.crawler.housing.tongliao;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_tongliao_account",indexes = {@Index(name = "index_housing_tongliao_account_taskid", columnList = "taskid")})
public class HousingFundTongLiaoAccount extends IdEntity{

	private String datea;//记账日期
	
	private String type;//归集和提取业务类型
	
	private String money;//发生额fse
	
	private String interest;//发生利息额fslx
	
	private String fee;//个人余额grye
	
	private String reason;//提取原因tqyy
	
	private String getType;//提取方式tqfs
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingFundTongLiaoAccount [datea=" + datea + ", type=" + type + ", money=" + money + ", interest="
				+ interest + ", fee=" + fee + ", reason=" + reason + ", getType=" + getType + ", taskid=" + taskid
				+ "]";
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getGetType() {
		return getType;
	}

	public void setGetType(String getType) {
		this.getType = getType;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
