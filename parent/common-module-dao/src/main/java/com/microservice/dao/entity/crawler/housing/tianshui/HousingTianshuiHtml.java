package com.microservice.dao.entity.crawler.housing.tianshui;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * html源码
 * @author tz
 *
 */
@Entity
@Table(name = "housing_tianshui_html" ,indexes = {@Index(name = "index_housing_tianshui_html_taskid", columnList = "taskid")})
public class HousingTianshuiHtml extends IdEntity {
	
	/**
	 * taskid  uuid 前端通过uuid访问状态结果
	 */
	private String taskid;
	
	private String type;
	private String page_count;	
	private String url;	
	private String html;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPage_count() {
		return page_count;
	}
	public void setPage_count(String page_count) {
		this.page_count = page_count;
	}
	@Column(columnDefinition="text")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(columnDefinition="text")
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
	public HousingTianshuiHtml() {
		super();
	}
	public HousingTianshuiHtml(String taskid, String type, String page_count, String url, String html) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.page_count = page_count;
		this.url = url;
		this.html = html;
	}

}
