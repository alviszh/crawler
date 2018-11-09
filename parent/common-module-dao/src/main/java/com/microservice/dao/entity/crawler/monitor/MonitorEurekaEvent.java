package com.microservice.dao.entity.crawler.monitor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 用于存储eureka微服务的变动信息，便于将来图形化统计
 * @author: sln 
 */
@Entity
@Table(name = "monitor_eureka_change")
public class MonitorEurekaEvent extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5267936261360654014L;
	private String appname;
	private String eventime;
	private String eventype;
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getEventime() {
		return eventime;
	}
	public void setEventime(String eventime) {
		this.eventime = eventime;
	}
	public String getEventype() {
		return eventype;
	}
	public void setEventype(String eventype) {
		this.eventype = eventype;
	}
	public MonitorEurekaEvent() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MonitorEurekaEvent(String appname, String eventime, String eventype) {
		super();
		this.appname = appname;
		this.eventime = eventime;
		this.eventype = eventype;
	}
}
