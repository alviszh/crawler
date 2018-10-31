package com.microservice.dao.entity.crawler.telecom.guangxi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGuangxi_score",indexes = {@Index(name = "index_telecomGuangxi_score_taskid", columnList = "taskid")}) 
public class TelecomGuangxiScore extends IdEntity{

    private  String month;//月份
	
	private String useScore;//消费积分
	
	private String netScore;//在网密码
	
	private String increaseScore;//倍增几分
	
	private String giveScore;//奖励积分
	
	private String sendScore;//转赠积分
	
	private String sumScore;//当月积分累计
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomNanningScore [month=" + month + ", useScore=" + useScore + ", netScore=" + netScore
				+ ", increaseScore=" + increaseScore + ", giveScore=" + giveScore + ", sendScore=" + sendScore
				+ ", sumScore=" + sumScore + ", taskid=" + taskid + "]";
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getUseScore() {
		return useScore;
	}

	public void setUseScore(String useScore) {
		this.useScore = useScore;
	}

	public String getNetScore() {
		return netScore;
	}

	public void setNetScore(String netScore) {
		this.netScore = netScore;
	}

	public String getIncreaseScore() {
		return increaseScore;
	}

	public void setIncreaseScore(String increaseScore) {
		this.increaseScore = increaseScore;
	}

	public String getGiveScore() {
		return giveScore;
	}

	public void setGiveScore(String giveScore) {
		this.giveScore = giveScore;
	}

	public String getSendScore() {
		return sendScore;
	}

	public void setSendScore(String sendScore) {
		this.sendScore = sendScore;
	}

	public String getSumScore() {
		return sumScore;
	}

	public void setSumScore(String sumScore) {
		this.sumScore = sumScore;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
