package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_pointrecord" ,indexes = {@Index(name = "index_telecom_xinjiang_pointrecord_taskid", columnList = "taskid")})
public class TelecomXinjiangPointRecord extends IdEntity {

	private String accountDate;//账期
	private String costPoint;//消费积分
	private String rewardPoints;//奖励积分
	private String total;//合计
	
	private String taskid;

	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getCostPoint() {
		return costPoint;
	}
	public void setCostPoint(String costPoint) {
		this.costPoint = costPoint;
	}
	public String getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(String rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomXinjiangPointRecord [accountDate=" + accountDate + ", costPoint=" + costPoint + ", rewardPoints="
				+ rewardPoints + ", total=" + total + ", taskid=" + taskid + "]";
	}

}