package com.microservice.dao.entity.crawler.telecom.liaoning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_liaoning_balance",indexes = {@Index(name = "index_telecom_liaoning_balance_taskid", columnList = "taskid")})
public class TelecomLiaoNingBalance extends IdEntity{

	private String realTimeChargesInfo ; //本月已消费
	
	private String balanceInfo; //当前账户余额
	
	private String taskid ;

	private Integer Userid ;
	
	


	public Integer getUserid() {
		return Userid;
	}

	public void setUserid(Integer userid) {
		Userid = userid;
	}

	public String getRealTimeChargesInfo() {
		return realTimeChargesInfo;
	}

	public void setRealTimeChargesInfo(String realTimeChargesInfo) {
		this.realTimeChargesInfo = realTimeChargesInfo;
	}

	public String getBalanceInfo() {
		return balanceInfo;
	}

	public void setBalanceInfo(String balanceInfo) {
		this.balanceInfo = balanceInfo;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomLiaoNingBalance [realTimeChargesInfo=" + realTimeChargesInfo + ", balanceInfo=" + balanceInfo
				+ ", taskid=" + taskid + ", Userid=" + Userid + "]";
	}
	
	
}
