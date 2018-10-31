package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description   存储所有的html页面信息
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_html",indexes = {@Index(name = "index_telecom_jiangxi_html_taskid", columnList = "taskid")})
public class TelecomJiangxiHtml extends IdEntity implements Serializable {
	private static final long serialVersionUID = -1151358381063673885L;
	private String taskid;							//uuid 前端通过uuid访问状态结果
	private String type;
	private Integer pageCount;	 //由于在传参的时候改了每页显示的条数，所以将当前页码设置为默认的1
	private String url;	
	private String html;
	private String yearmonth;   //将爬取信息的所属月份进行存储  
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
	@Column(columnDefinition="text")   //有的url很长，需要
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
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
}

