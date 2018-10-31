package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description   存储所有的html页面信息
 * @author sln
 * @date 2017年8月23日 下午6:30:42
 */
@Entity
@Table(name = "telecom_shanxi3_html",indexes = {@Index(name = "index_telecom_shanxi3_html_taskid", columnList = "taskid")})
public class TelecomShanxi3Html extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4056159958637416049L;
	private String taskid;							//uuid 前端通过uuid访问状态结果
	private String type;
	private Integer pageCount;	 //由于在传参的时候改了每页显示的条数，所以将当前页码设置为默认的1
	private String url;	
	private String html;
	private String yearmonth;   //将爬取信息的所属月份进行存储  
	private Integer monthtotalrow;   //用于通话或者短信记录中，记录每个月的总记录数
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
	public Integer getMonthtotalrow() {
		return monthtotalrow;
	}
	public void setMonthtotalrow(Integer monthtotalrow) {
		this.monthtotalrow = monthtotalrow;
	}
	public TelecomShanxi3Html(String taskid, String type, Integer pageCount, String url, String html, String yearmonth,
			Integer monthtotalrow) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.pageCount = pageCount;
		this.url = url;
		this.html = html;
		this.yearmonth = yearmonth;
		this.monthtotalrow = monthtotalrow;
	}
	public TelecomShanxi3Html(String taskid, String type, Integer pageCount, String url, String html) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.pageCount = pageCount;
		this.url = url;
		this.html = html;
	}
	public TelecomShanxi3Html() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}

