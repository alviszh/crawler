package com.microservice.dao.entity.crawler.soical.official;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="official_url")
public class OfficialUrl extends IdEntitySocial implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String taskid;
	
	private String url;
	
	private String websitename;
	
	private String errormessage;

	public String getErrormessage() {
		return errormessage;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebsitename() {
		return websitename;
	}

	public void setWebsitename(String websitename) {
		this.websitename = websitename;
	}

	@Override
	public String toString() {
		return "OfficialUrl [url=" + url + ", websitename=" + websitename + "]";
	}
	
	
}
