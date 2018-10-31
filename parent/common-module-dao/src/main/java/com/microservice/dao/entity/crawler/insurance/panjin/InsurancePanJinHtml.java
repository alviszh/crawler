package com.microservice.dao.entity.crawler.insurance.panjin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description: 盘锦社保信息html页面存储实体
 * @author sln
 */
@Entity
@Table(name = "insurance_panjin_html",indexes = {@Index(name = "index_insurance_panjin_html_taskid", columnList = "taskid")})
public class InsurancePanJinHtml extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5879953430328446204L;
	private String taskid;							
	private String type;
	private Integer pagenumber;	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getPagenumber() {
		return pagenumber;
	}
	public void setPagenumber(Integer pagenumber) {
		this.pagenumber = pagenumber;
	}
	@Column(columnDefinition="text")
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
}
