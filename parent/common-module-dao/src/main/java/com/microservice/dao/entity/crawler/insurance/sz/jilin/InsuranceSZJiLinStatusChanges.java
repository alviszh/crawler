package com.microservice.dao.entity.crawler.insurance.sz.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_jilin_statuschanges",indexes = {@Index(name = "index_insurance_sz_jilin_statuschanges_taskid", columnList = "taskid")})
public class InsuranceSZJiLinStatusChanges extends IdEntity{

	private String taskid;
	
	private String num;
	
	private String idcard;
	
	private String name;
	
	private String dw_num;
	
	private String dw_name;
	
	private String changes_lei;//变更类型
	
	private String changes_time;
	
	private String changes_cause;//变更原因

	public InsuranceSZJiLinStatusChanges(String taskid, String num, String idcard, String name, String dw_num,
			String dw_name, String changes_lei, String changes_time, String changes_cause) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.idcard = idcard;
		this.name = name;
		this.dw_num = dw_num;
		this.dw_name = dw_name;
		this.changes_lei = changes_lei;
		this.changes_time = changes_time;
		this.changes_cause = changes_cause;
	}

	public InsuranceSZJiLinStatusChanges() {
		super();
	}

	@Override
	public String toString() {
		return "InsuranceBaiShanStatusChanges [taskid=" + taskid + ", num=" + num + ", idcard=" + idcard + ", name="
				+ name + ", dw_num=" + dw_num + ", dw_name=" + dw_name + ", changes_lei=" + changes_lei
				+ ", changes_time=" + changes_time + ", changes_cause=" + changes_cause + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDw_num() {
		return dw_num;
	}

	public void setDw_num(String dw_num) {
		this.dw_num = dw_num;
	}

	public String getDw_name() {
		return dw_name;
	}

	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}

	public String getChanges_lei() {
		return changes_lei;
	}

	public void setChanges_lei(String changes_lei) {
		this.changes_lei = changes_lei;
	}

	public String getChanges_time() {
		return changes_time;
	}

	public void setChanges_time(String changes_time) {
		this.changes_time = changes_time;
	}

	public String getChanges_cause() {
		return changes_cause;
	}

	public void setChanges_cause(String changes_cause) {
		this.changes_cause = changes_cause;
	}
	
	
}
