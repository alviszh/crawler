package com.crawler.monitor.json;

public class MonitorEurekaChange {
	private String appname;
	private int instancecount;
	private int actualInstanceCount; 
	private String changeDetail;  //监测结果说明：微服务暂时没有；实例数量变化
	//微服务对应的负责人
	private String developer; 
	private String servicename;   //微服务的汉语名称
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public int getInstancecount() {
		return instancecount;
	}
	public void setInstancecount(int instancecount) {
		this.instancecount = instancecount;
	}
	public int getActualInstanceCount() {
		return actualInstanceCount;
	}
	public void setActualInstanceCount(int actualInstanceCount) {
		this.actualInstanceCount = actualInstanceCount;
	}
	public String getChangeDetail() {
		return changeDetail;
	}
	public void setChangeDetail(String changeDetail) {
		this.changeDetail = changeDetail;
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
}
