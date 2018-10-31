package com.microservice.dao.entity.crawler.insurance.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 重庆社保首次参保
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_chongqing_first",indexes = {@Index(name = "index_insurance_chongqing_first_taskid", columnList = "taskid")})
public class InsuranceChongqingFirst extends IdEntity {
	private String type; // 参保类型
	private String firstDate; // 本地首次缴费时间
	private String insuranceState; // 参保状态(不代表待遇享受状态)
	private String taskid;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	public String getInsuranceState() {
		return insuranceState;
	}
	public void setInsuranceState(String insuranceState) {
		this.insuranceState = insuranceState;
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public InsuranceChongqingFirst() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceChongqingFirst(String type, String firstDate, String insuranceState,String taskid) {
		super();
		this.type = type;
		this.firstDate = firstDate;
		this.insuranceState = insuranceState;
		this.taskid=taskid;
	}
	@Override
	public String toString() {
		return "InsuranceChongqingFirst [type=" + type + ", firstDate=" + firstDate + ", insuranceState="
				+ insuranceState + ", taskid=" + taskid + "]";
	}
	
}
