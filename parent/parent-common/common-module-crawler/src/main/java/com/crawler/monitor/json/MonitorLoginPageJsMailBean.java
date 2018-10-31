package com.crawler.monitor.json;

public class MonitorLoginPageJsMailBean {
	private String taskid;
	private String url;    //登录url
	private String jspath;  //网页中js的路径
	private String webtype;    //网站类型名称	
	private String developer;    //网站负责人
	private String comparetaskid;  //参照taskid
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
	public String getJspath() {
		return jspath;
	}
	public void setJspath(String jspath) {
		this.jspath = jspath;
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
	public MonitorLoginPageJsMailBean() {
		super();
	}
	public MonitorLoginPageJsMailBean(String taskid, String url, String jspath, String webtype, String developer,
			String comparetaskid) {
		super();
		this.taskid = taskid;
		this.url = url;
		this.jspath = jspath;
		this.webtype = webtype;
		this.developer = developer;
		this.comparetaskid = comparetaskid;
	}
}
