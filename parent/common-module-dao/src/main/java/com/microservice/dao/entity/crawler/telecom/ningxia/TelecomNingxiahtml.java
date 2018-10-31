package com.microservice.dao.entity.crawler.telecom.ningxia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 宁夏电信界面信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_ningxia_html",indexes = {@Index(name = "index_telecom_ningxia_html_taskid", columnList = "taskid")})
public class TelecomNingxiahtml  extends IdEntity{
	@Column(name="userid")
	private Integer userid;
	/** 爬取批次号 */
	@Column(name="taskid")
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

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	
	
}
