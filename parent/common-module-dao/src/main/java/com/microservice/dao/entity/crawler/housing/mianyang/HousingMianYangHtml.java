package com.microservice.dao.entity.crawler.housing.mianyang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 绵阳公积金信息页面存储公用html
 */
@Entity
@Table(name="housing_mianyang_html",indexes = {@Index(name = "index_housing_mianyang_html_taskid", columnList = "taskid")})
public class HousingMianYangHtml extends IdEntity implements Serializable {
	private static final long serialVersionUID = 7006573596788413374L;
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
		return "HousingChongqingHtml [taskid=" + taskid + ", type=" + type + ", pageCount=" + pageCount + ", url=" + url
				+ ", html=" + html + "]";
	}

	public HousingMianYangHtml(String taskid, String type, Integer pageCount, String url, String html) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.pageCount = pageCount;
		this.url = url;
		this.html = html;
	}
	public HousingMianYangHtml() {
		super();
		// TODO Auto-generated constructor stub
	}
}
