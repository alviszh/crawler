package com.microservice.dao.entity.crawler.housing.eerduosi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_eerduosi_params",indexes = {@Index(name = "index_housing_eerduosi_params_taskid", columnList = "taskid")})
public class HousingEErDuoSiParams extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5879953430328446204L;
	private String taskid;						
	private String path;
	private String ex;  //扩展字段
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getEx() {
		return ex;
	}
	public void setEx(String ex) {
		this.ex = ex;
	}
}
