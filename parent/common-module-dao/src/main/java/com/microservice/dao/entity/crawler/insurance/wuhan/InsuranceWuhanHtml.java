package com.microservice.dao.entity.crawler.insurance.wuhan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_wuhan_html")
public class InsuranceWuhanHtml extends IdEntity{
	
	private String taskid;	//uuid唯一标识		
	private String html;	//网页源代码
	private String url;		//url
	private String description;	//步骤描述
	private String insuranceType;	//保险类型
	private String pageCount;// 页数
	
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Column(columnDefinition="text")
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	@Override
	public String toString() {
		return "HtmlInsuranceChangchun [taskid=" + taskid + ", html=" + html + ", url=" + url + ", description="
				+ description + ", insuranceType=" + insuranceType + "]";
	}
	
}
