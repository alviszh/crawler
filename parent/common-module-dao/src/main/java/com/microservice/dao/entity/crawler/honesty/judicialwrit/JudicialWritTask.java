package com.microservice.dao.entity.crawler.honesty.judicialwrit;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "judicial_writ_task" ,indexes = {@Index(name = "judicial_writ_task_taskid", columnList = "taskid")})
public class JudicialWritTask extends IdEntitySocial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String taskid;

	private String description;

	private Boolean finished;//爬虫任务是否全部完成
	
	private Integer error_code; //错误代码  

	private String error_message; //错误信息 
	
	private Date etltime;
	
	private String cookies;
	
	private String guid;
	
	private String vl5x;
	
	private String number;
	
	private Integer writsum;
	

	public Integer getWritsum() {
		return writsum;
	}

	public void setWritsum(Integer writsum) {
		this.writsum = writsum;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getVl5x() {
		return vl5x;
	}

	public void setVl5x(String vl5x) {
		this.vl5x = vl5x;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

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

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public Date getEtltime() {
		return etltime;
	}

	public void setEtltime(Date etltime) {
		this.etltime = etltime;
	}


}
