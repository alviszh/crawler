package com.crawler.monitor.json;
/**
 * 作为网站监控过程中的响应的状态码和异常信息的载体
 * @author sln
 *
 */
public class MonitorWebUsableBean {
	private int webStatusCode;
	private String exceptioninfo;
	private String sourcecode;  //获取的响应源码
	public int getWebStatusCode() {
		return webStatusCode;
	}
	public void setWebStatusCode(int webStatusCode) {
		this.webStatusCode = webStatusCode;
	}
	public String getExceptioninfo() {
		return exceptioninfo;
	}
	public void setExceptioninfo(String exceptioninfo) {
		this.exceptioninfo = exceptioninfo;
	}
	public String getSourcecode() {
		return sourcecode;
	}
	public void setSourcecode(String sourcecode) {
		this.sourcecode = sourcecode;
	}
}
