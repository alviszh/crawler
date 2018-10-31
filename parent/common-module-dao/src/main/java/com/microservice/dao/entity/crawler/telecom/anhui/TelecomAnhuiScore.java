package com.microservice.dao.entity.crawler.telecom.anhui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecom_anhui_score",indexes = {@Index(name = "index_telecom_anhui_score_taskid", columnList = "taskid")}) 
public class TelecomAnhuiScore extends IdEntity{

	private String month;//月份
	
	private String status;//积分类型名称
	
	private String newScore;//新增积分
	
	private String taskid;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNewScore() {
		return newScore;
	}

	public void setNewScore(String newScore) {
		this.newScore = newScore;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomAnhuiScore [month=" + month + ", status=" + status + ", newScore=" + newScore + ", taskid="
				+ taskid + "]";
	}
}
