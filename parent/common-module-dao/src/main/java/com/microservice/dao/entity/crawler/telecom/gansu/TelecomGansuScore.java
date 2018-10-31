package com.microservice.dao.entity.crawler.telecom.gansu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGansu_score",indexes = {@Index(name = "index_telecomGansu_score_taskid", columnList = "taskid")}) 
public class TelecomGansuScore extends IdEntity{

	private String month;
	
	private String sendScore;//赠送积分
	
	private String rewardScore;//奖励积分
	
	private String sumScore;
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomGansuScore [month=" + month + ", sendScore=" + sendScore + ", rewardScore=" + rewardScore
				+ ", sumScore=" + sumScore + ", taskid=" + taskid + "]";
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSendScore() {
		return sendScore;
	}

	public void setSendScore(String sendScore) {
		this.sendScore = sendScore;
	}

	public String getRewardScore() {
		return rewardScore;
	}

	public void setRewardScore(String rewardScore) {
		this.rewardScore = rewardScore;
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

	public TelecomGansuScore(String month, String sendScore, String rewardScore, String sumScore, String taskid) {
		super();
		this.month = month;
		this.sendScore = sendScore;
		this.rewardScore = rewardScore;
		this.sumScore = sumScore;
		this.taskid = taskid;
	}

	public TelecomGansuScore() {
		super();
	}
	
	
	
	
	
}
