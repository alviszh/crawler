package com.microservice.dao.entity.crawler.insurance.nanjing;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description: 南京社保信息html页面存储实体
 * @author sln
 * @date 2017年9月26日
 */
@Entity
@Table(name = "insurance_nanjing_html",indexes = {@Index(name = "index_insurance_nanjing_html_taskid", columnList = "taskid")})
public class InsuranceNanjingHtml extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4237363759745937585L;
	private String taskid;							
	private String type;
	private Integer pageCount;	
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
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
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
}
