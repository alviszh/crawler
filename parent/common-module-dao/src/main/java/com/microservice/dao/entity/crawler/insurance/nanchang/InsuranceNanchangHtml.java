package com.microservice.dao.entity.crawler.insurance.nanchang;


import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 南昌社保信息html
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_nanchang_html" ,indexes = {@Index(name = "index_insurance_nanchang_html_taskid", columnList = "taskid")})
public class InsuranceNanchangHtml extends IdEntity{

	/**
	 * taskid  uuid 前端通过uuid访问状态结果
	 */
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
	@Override
	public String toString() {
		return "InsuranceNHtml [taskid=" + taskid + ", type=" + type + ", pageCount=" + pageCount + ", url="
				+ url + ", html=" + html + "]";
	}
	public InsuranceNanchangHtml(String taskid, String type, Integer pageCount, String url, String html) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.pageCount = pageCount;
		this.url = url;
		this.html = html;
	}
	public InsuranceNanchangHtml() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
