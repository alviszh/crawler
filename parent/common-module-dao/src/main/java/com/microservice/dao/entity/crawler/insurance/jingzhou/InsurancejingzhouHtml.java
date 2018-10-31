package com.microservice.dao.entity.crawler.insurance.jingzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 荆州社保 页面
 * @author DougeChow
 *
 */
@Entity
@Table(name="insurance_jingzhou_html")
public class InsurancejingzhouHtml extends InsuranceJingzhouBasicBean{

	private static final long serialVersionUID = -1646403919031277347L;
	
	/** 爬取批次号 */
	@Column(name="task_id")
	private String taskid;
	
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


	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	
	
}
