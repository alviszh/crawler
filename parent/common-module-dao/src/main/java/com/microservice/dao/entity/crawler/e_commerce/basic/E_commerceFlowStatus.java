package com.microservice.dao.entity.crawler.e_commerce.basic;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 运营商步骤状态记录表
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_flow_status" ,indexes = {@Index(name = "index_e_commerce_flow_status_taskid", columnList = "taskid")})
public class E_commerceFlowStatus  extends IdEntity implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3568156872348932272L;

	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String description;

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

	public E_commerceFlowStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public E_commerceFlowStatus(String taskid, String description) {
		super();
		this.taskid = taskid;
		this.description = description;
	}
	
}
