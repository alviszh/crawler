package com.microservice.dao.entity.crawler.telecom.guangxi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGuangxi_bill",indexes = {@Index(name = "index_telecomGuangxi_bill_taskid", columnList = "taskid")}) 
public class TelecomGuangxiBill extends IdEntity{
	private String month;//月份
    private String favourable;//优惠费用
	
	private String mealMoney;//套餐月基本费
	
	private String messageMoney;//国内短信费
	
	private String netMoney;//无线宽带费
	
	private String landMoney;//国内通话费
	
	private String fourMoney;//4G包月流量包费
	
	private String redMoney;//红包返还
	
	private String sumMoney;//小计
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomGuangxiBill [month=" + month + ", favourable=" + favourable + ", mealMoney=" + mealMoney
				+ ", messageMoney=" + messageMoney + ", netMoney=" + netMoney + ", landMoney=" + landMoney
				+ ", fourMoney=" + fourMoney + ", redMoney=" + redMoney + ", sumMoney=" + sumMoney + ", taskid="
				+ taskid + "]";
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getFavourable() {
		return favourable;
	}

	public void setFavourable(String favourable) {
		this.favourable = favourable;
	}

	public String getMealMoney() {
		return mealMoney;
	}

	public void setMealMoney(String mealMoney) {
		this.mealMoney = mealMoney;
	}

	public String getMessageMoney() {
		return messageMoney;
	}

	public void setMessageMoney(String messageMoney) {
		this.messageMoney = messageMoney;
	}

	public String getNetMoney() {
		return netMoney;
	}

	public void setNetMoney(String netMoney) {
		this.netMoney = netMoney;
	}

	public String getLandMoney() {
		return landMoney;
	}

	public void setLandMoney(String landMoney) {
		this.landMoney = landMoney;
	}

	public String getFourMoney() {
		return fourMoney;
	}

	public void setFourMoney(String fourMoney) {
		this.fourMoney = fourMoney;
	}

	public String getRedMoney() {
		return redMoney;
	}

	public void setRedMoney(String redMoney) {
		this.redMoney = redMoney;
	}

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
	


	

	

}
