package com.microservice.dao.entity.crawler.monitor;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;

/**
 * @description:电商定时爬取任务
 * 					
 * @author: sln 
 */
@Entity
@Table(name = "monitor_ecommerce_tasker")
public class MonitorEcommerceTasker extends IdEntity implements Serializable {
	private static final long serialVersionUID = -5796876834600199305L;
	private String developer;   //负责人
	private String paramsjson;    //参数json串
	private Integer isneedmonitor; //	是否需要监控（1——需要，0——暂时不需要）
	private String webtype;    //网站类型
	private String appname;   //微服务名称
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getParamsjson() {
		return paramsjson;
	}
	public void setParamsjson(String paramsjson) {
		this.paramsjson = paramsjson;
	}
	public Integer getIsneedmonitor() {
		return isneedmonitor;
	}
	public void setIsneedmonitor(Integer isneedmonitor) {
		this.isneedmonitor = isneedmonitor;
	}
	public String getWebtype() {
		return webtype;
	}
	public void setWebtype(String webtype) {
		this.webtype = webtype;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
}
