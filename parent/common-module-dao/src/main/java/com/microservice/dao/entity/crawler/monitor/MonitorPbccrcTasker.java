package com.microservice.dao.entity.crawler.monitor;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;

/**
 * @description: 人行征信定时爬取任务
 * @author: sln 
 */
@Entity
@Table(name = "monitor_pbccrc_tasker")
public class MonitorPbccrcTasker extends IdEntity implements Serializable {
	private static final long serialVersionUID = 3425387511211533159L;
	private String username;	//用户名
	private String password;     //密码
	private String tradecode;    //授权码
	private String paramsjson;    //参数json串
	private Integer isneedmonitor; //	是否需要监控（1——需要，0——暂时不需要）
	private String appname;   //微服务名称
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTradecode() {
		return tradecode;
	}
	public void setTradecode(String tradecode) {
		this.tradecode = tradecode;
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
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public MonitorPbccrcTasker() {
		super();
	}
	public MonitorPbccrcTasker(String username, String password, String tradecode, String paramsjson,
			Integer isneedmonitor, String appname) {
		super();
		this.username = username;
		this.password = password;
		this.tradecode = tradecode;
		this.paramsjson = paramsjson;
		this.isneedmonitor = isneedmonitor;
		this.appname = appname;
	}
}
