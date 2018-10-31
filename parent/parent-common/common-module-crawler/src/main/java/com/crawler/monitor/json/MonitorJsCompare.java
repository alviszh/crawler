package com.crawler.monitor.json;
/**
 * @description: 存储js的比对项内容  （长度，MD5加密后的密文）
 * @author: sln 
 */
public class MonitorJsCompare {
	private String currentJsMd5;
	private int currentJsContentLength;
	private String afterTreatJs;
	public String getCurrentJsMd5() {
		return currentJsMd5;
	}
	public void setCurrentJsMd5(String currentJsMd5) {
		this.currentJsMd5 = currentJsMd5;
	}
	public int getCurrentJsContentLength() {
		return currentJsContentLength;
	}
	public void setCurrentJsContentLength(int currentJsContentLength) {
		this.currentJsContentLength = currentJsContentLength;
	}
	public String getAfterTreatJs() {
		return afterTreatJs;
	}
	public void setAfterTreatJs(String afterTreatJs) {
		this.afterTreatJs = afterTreatJs;
	}
}
