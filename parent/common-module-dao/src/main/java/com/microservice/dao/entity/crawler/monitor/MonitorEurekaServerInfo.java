package com.microservice.dao.entity.crawler.monitor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 用于监测eureka微服务的变动
 * @author: sln 
 */
@Entity
@Table(name = "monitor_eureka_info")
public class MonitorEurekaServerInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 6763476511298438962L;
	//微服务名称
	private String appname;
	//指定的微服务实例数
	private Integer instancecount;
	//微服务对应的负责人
	private String developer; 
	//微服务的汉语名称
	private String servicename;
	//是否需要监控
	private Integer isneedmonitor;
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public Integer getInstancecount() {
		return instancecount;
	}
	public void setInstancecount(Integer instancecount) {
		this.instancecount = instancecount;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public Integer getIsneedmonitor() {
		return isneedmonitor;
	}
	public void setIsneedmonitor(Integer isneedmonitor) {
		this.isneedmonitor = isneedmonitor;
	}
}
