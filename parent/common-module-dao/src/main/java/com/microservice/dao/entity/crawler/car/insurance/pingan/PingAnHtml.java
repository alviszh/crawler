package com.microservice.dao.entity.crawler.car.insurance.pingan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="car_insurance_pingan_html")
public class PingAnHtml extends IdEntity implements Serializable{

	private String taskid;
	
	private String url;
	
	private String htmlText;
	
	private String messagesText;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(columnDefinition="text")
	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public String getMessagesText() {
		return messagesText;
	}

	public void setMessagesText(String messagesText) {
		this.messagesText = messagesText;
	}
	
	
}
