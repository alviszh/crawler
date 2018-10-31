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
@Table(name="official_content")
public class OfficialContent extends IdEntitySocial implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String url;
	
	private String title;
	
	private String time;
	
	private String taskid;
	
	private String linkurl;

	private String websitename;
	
	private String keyword;
	
	private String description;
	
	@Column(columnDefinition="text")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@Column(columnDefinition="text")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(columnDefinition="text")
	public String getWebsitename() {
		return websitename;
	}

	public void setWebsitename(String websitename) {
		this.websitename = websitename;
	}
	@Column(columnDefinition="text")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(columnDefinition="text")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Column(columnDefinition="text")
	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	@Override
	public String toString() {
		return "OfficialContent [url=" + url + ", title=" + title + ", websitename=" + websitename + ", time=" + time
				+ ", taskid=" + taskid + ", linkurl=" + linkurl + "]";
	}
	
	
	
}
