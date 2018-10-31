package com.microservice.dao.entity.crawler.monitor;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;

/**
 * @description: 社保定时爬取任务
 * @author: sln 
 */
@Entity
@Table(name = "monitor_insurance_tasker")
public class MonitorInsuranceTasker extends IdEntity implements Serializable {
	private static final long serialVersionUID = -5796876834600199305L;
	public String name;			//姓名
	public String idnum;		//身份证号
	public String city;	        //城市 
	private String developer;   //负责人
	private String paramsjson;    //参数json串
	private Integer isneedmonitor; //	是否需要监控（1——需要，0——暂时不需要）
	private String ex;    //扩展字段
	private String appname;   //微服务名称
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
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
	public String getEx() {
		return ex;
	}
	public void setEx(String ex) {
		this.ex = ex;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public MonitorInsuranceTasker() {
		super();
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public MonitorInsuranceTasker(String name, String idnum, String city, String developer, String paramsjson,
			Integer isneedmonitor, String ex, String appname) {
		super();
		this.name = name;
		this.idnum = idnum;
		this.city = city;
		this.developer = developer;
		this.paramsjson = paramsjson;
		this.isneedmonitor = isneedmonitor;
		this.ex = ex;
		this.appname = appname;
	}
	
}
