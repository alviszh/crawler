package com.microservice.dao.entity.crawler.housing.huangshi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_huangshi_html")
public class HousingFundHuangShiHtml extends IdEntity{

	private String taskid;							
	private String type;
	private Integer pagenumber;	
	private String url;	
	private String html;
	@Override
	public String toString() {
		return "HousingFundHuangShiHtml [taskid=" + taskid + ", type=" + type + ", pagenumber=" + pagenumber + ", url="
				+ url + ", html=" + html + "]";
	}
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
	public Integer getPagenumber() {
		return pagenumber;
	}
	public void setPagenumber(Integer pagenumber) {
		this.pagenumber = pagenumber;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
}
