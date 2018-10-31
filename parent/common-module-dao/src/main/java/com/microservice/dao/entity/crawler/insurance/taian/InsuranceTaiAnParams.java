package com.microservice.dao.entity.crawler.insurance.taian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name = "insurance_taian_params",indexes = {@Index(name = "index_insurance_taian_params_taskid", columnList = "taskid")})
public class InsuranceTaiAnParams extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5879953430328446204L;
	private String taskid;						
	private String usersession;
	private String ex;  //扩展字段
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUsersession() {
		return usersession;
	}
	public void setUsersession(String usersession) {
		this.usersession = usersession;
	}
	public String getEx() {
		return ex;
	}
	public void setEx(String ex) {
		this.ex = ex;
	}
}
