package com.microservice.dao.entity.crawler.telecom.guizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guizhou_point" ,indexes = {@Index(name = "index_telecom_guizhou_point_taskid", columnList = "taskid")})
public class TelecomGuizhouPoint extends IdEntity {

	private String status;// 积分状态
	private String canuseIntegral;// 当前可用积分
	private String expiresIntegral;// 年底到期积分
	private String taskid;	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCanuseIntegral() {
		return canuseIntegral;
	}
	public void setCanuseIntegral(String canuseIntegral) {
		this.canuseIntegral = canuseIntegral;
	}
	public String getExpiresIntegral() {
		return expiresIntegral;
	}
	public void setExpiresIntegral(String expiresIntegral) {
		this.expiresIntegral = expiresIntegral;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomGuizhouPoint [status=" + status + ", canuseIntegral=" + canuseIntegral + ", expiresIntegral="
				+ expiresIntegral + ", taskid=" + taskid + "]";
	}
	public TelecomGuizhouPoint(String status, String canuseIntegral, String expiresIntegral, String taskid) {
		super();
		this.status = status;
		this.canuseIntegral = canuseIntegral;
		this.expiresIntegral = expiresIntegral;
		this.taskid = taskid;
	}
	public TelecomGuizhouPoint() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}