package com.microservice.dao.entity.crawler.telecom.heilongjiang;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_heilongjiang_cusstringresult")
public class TelecomCusThemStringResult extends IdEntity {

	private String html;

	private Integer userid;

	private String taskid;

	@Column(columnDefinition="text") 
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


	public String getTaskid() {
		return taskid;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	@Override
	public String toString() {
		return "TelecomCusThemStringResult [html=" + html + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

}
