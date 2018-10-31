package com.crawler.monitor.json;
/**
 * 网站可用性结果展示，邮件载体
 * @author sln
 *
 */
public class MonitorWebUsableMailBean {
	private String url;      //登录url地址
	private String webtype;   //监控网站类型(运营商/社保......)
	private Integer statuscode;   //网站状态码
	private String exceptioninfo;  //网站异常信息
	private String developer;
	private String dayslinkcode;  //指定天数范围内，某个网站连接状态码
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
	public Integer getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(Integer statuscode) {
		this.statuscode = statuscode;
	}
	public String getExceptioninfo() {
		return exceptioninfo;
	}
	public void setExceptioninfo(String exceptioninfo) {
		this.exceptioninfo = exceptioninfo;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getDayslinkcode() {
		return dayslinkcode;
	}
	public void setDayslinkcode(String dayslinkcode) {
		this.dayslinkcode = dayslinkcode;
	}
	
	public MonitorWebUsableMailBean() {
		super();
	}
	public MonitorWebUsableMailBean(String url, String webtype, Integer statuscode, String exceptioninfo,
			String developer, String dayslinkcode) {
		super();
		this.url = url;
		this.webtype = webtype;
		this.statuscode = statuscode;
		this.exceptioninfo = exceptioninfo;
		this.developer = developer;
		this.dayslinkcode = dayslinkcode;
	}
}
