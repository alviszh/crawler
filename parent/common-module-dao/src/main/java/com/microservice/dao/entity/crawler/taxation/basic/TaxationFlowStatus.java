package com.microservice.dao.entity.crawler.taxation.basic;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "taxation_flow_status" ,indexes = {@Index(name = "index_taxation_flow_status_taskid", columnList = "taskid")})
public class TaxationFlowStatus extends IdEntity implements Serializable{
	private static final long serialVersionUID = -3568156872348932272L;

	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String description;
	
	private String phase;// 当前步骤

	private String phase_status;// 步骤状态

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaxationFlowStatus(String taskid, String description, String phase, String phase_status) {
		super();
		this.taskid = taskid;
		this.description = description;
		this.setPhase(phase);
		this.setPhase_status(phase_status);
	}
	
	public TaxationFlowStatus() {
		super();
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getPhase_status() {
		return phase_status;
	}

	public void setPhase_status(String phase_status) {
		this.phase_status = phase_status;
	}
	
}
