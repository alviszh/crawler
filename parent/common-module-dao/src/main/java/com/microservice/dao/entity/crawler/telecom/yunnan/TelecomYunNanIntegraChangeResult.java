package com.microservice.dao.entity.crawler.telecom.yunnan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_yunnan_integra_change")
public class TelecomYunNanIntegraChangeResult extends IdEntity {

	private String month;//月份
		
	private String consumpoints;//消费积分
		
	private String rewardpoints;//奖励积分

	private String total;//合计
	
	private Integer userid;

	private String taskid;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getConsumpoints() {
		return consumpoints;
	}

	public void setConsumpoints(String consumpoints) {
		this.consumpoints = consumpoints;
	}

	public String getRewardpoints() {
		return rewardpoints;
	}

	public void setRewardpoints(String rewardpoints) {
		this.rewardpoints = rewardpoints;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomYunNanIntegraChangeResult [month=" + month + ", consumpoints=" + consumpoints + ", rewardpoints="
				+ rewardpoints + ", total=" + total + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

	
	
}