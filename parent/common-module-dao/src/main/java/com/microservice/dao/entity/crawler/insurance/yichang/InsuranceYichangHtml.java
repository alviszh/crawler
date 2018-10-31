package com.microservice.dao.entity.crawler.insurance.yichang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "insurance_yichang_html" ,indexes = {@Index(name = "index_insurance_yichang_html_taskid", columnList = "taskid")})
public class InsuranceYichangHtml extends IdEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5696594865542208352L;

	/**
	 * taskid  uuid 前端通过uuid访问状态结果
	 */
	private String taskid;
	
	private String type;
	private String pageCount;	
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
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
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
	public InsuranceYichangHtml() {
		super();
	}
	public InsuranceYichangHtml(String taskid, String type, String pageCount, String url, String html) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.pageCount = pageCount;
		this.url = url;
		this.html = html;
	}
	
	
	
}
