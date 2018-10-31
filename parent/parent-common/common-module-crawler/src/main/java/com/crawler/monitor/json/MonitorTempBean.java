package com.crawler.monitor.json;

import org.jsoup.nodes.Document;
/**
 * 该类留jscount
 * @author sln
 *
 */
public class MonitorTempBean {
	private String html;
	private Document doc;
	private int currentJsCount;
	private long htmlid;
	private String taskid;
	
	/////////////////////////
	private int webStatusCode;
	private String exceptioninfo;
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public int getCurrentJsCount() {
		return currentJsCount;
	}
	public void setCurrentJsCount(int currentJsCount) {
		this.currentJsCount = currentJsCount;
	}
	
	public long getHtmlid() {
		return htmlid;
	}
	public void setHtmlid(long htmlid) {
		this.htmlid = htmlid;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
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
}
