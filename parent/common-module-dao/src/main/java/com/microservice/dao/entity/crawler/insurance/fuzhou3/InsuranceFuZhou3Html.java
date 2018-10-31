package com.microservice.dao.entity.crawler.insurance.fuzhou3;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 */
@Entity
@Table(name="insurance_fuzhou3_html")
public class InsuranceFuZhou3Html extends IdEntity implements Serializable{

	private static final long serialVersionUID = -1646403919031277347L;
	
	/** 爬取批次号 */
	@Column(name="task_id")
	private String taskId;
	
	/** 爬取网页类别  */
	@Column(name="type")
	private String type;
	
	/** 爬取页码  */
	@Column(name="page_count")
	private Integer pageCount;
	
	/** 爬取网址  */
	@Column(name="url")
	private String url;	
	
	/** 爬取网页  */
	private String html;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	@Column(name="html",columnDefinition="text")
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	@Override
	public String toString() {
		return "InsuranceShenzhenHtml [taskId=" + taskId + ", type=" + type + ", pageCount=" + pageCount + ", url="
				+ url + ", html=" + html + "]";
	}

	
}
