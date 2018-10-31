package com.microservice.dao.entity.crawler.soical.official;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;


@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "official_task")
public class OfficialTask extends IdEntitySocial implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String taskid;
	
	private String description;
	
	private String errormessage;
	
	private String title;
	
	private String url;
	
	public String getErrormessage() {
		return errormessage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(columnDefinition="text")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	
}
