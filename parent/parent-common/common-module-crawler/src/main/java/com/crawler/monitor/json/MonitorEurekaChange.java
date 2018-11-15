package com.crawler.monitor.json;

public class MonitorEurekaChange {
	private String appname;
	//当前环境下的微服务分组
	private String servicegroup;
	private String changedetail;
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getServicegroup() {
		return servicegroup;
	}
	public void setServicegroup(String servicegroup) {
		this.servicegroup = servicegroup;
	}
	
	public String getChangedetail() {
		return changedetail;
	}
	public void setChangedetail(String changedetail) {
		this.changedetail = changedetail;
	}
	@Override
	public String toString() {
		return "MonitorEurekaChange [appname=" + appname + ", servicegroup=" + servicegroup + ", changedetail="
				+ changedetail + "]";
	}
}
