package com.crawler.monitor.json;

public class MonitorLoginPageHtmlMailBean{
	private String taskid;
	private String url;      //登录url地址
	private String webtype;    //网站类型名称	
	private String developer;    //网站负责人
	private String comparetaskid;  //参照taskid
	private String jscountchangedetail;
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
	public String getWebtype() {
		return webtype;
	}
	public void setWebtype(String webtype) {
		this.webtype = webtype;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getComparetaskid() {
		return comparetaskid;
	}
	public void setComparetaskid(String comparetaskid) {
		this.comparetaskid = comparetaskid;
	}
	public String getJscountchangedetail() {
		return jscountchangedetail;
	}
	public void setJscountchangedetail(String jscountchangedetail) {
		this.jscountchangedetail = jscountchangedetail;
	}
	
	public MonitorLoginPageHtmlMailBean() {
		super();
	}
	public MonitorLoginPageHtmlMailBean(String taskid, String url, String webtype, String developer,
			String comparetaskid, String jscountchangedetail) {
		super();
		this.taskid = taskid;
		this.url = url;
		this.webtype = webtype;
		this.developer = developer;
		this.comparetaskid = comparetaskid;
		this.jscountchangedetail = jscountchangedetail;
	}
	
}
