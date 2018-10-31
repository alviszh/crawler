package com.crawler.monitor.json;
/**
 * @description:
 * @author: sln 
 * @date: 2018年3月23日 下午2:12:40 
 */
public class MonitorJsTempBean {
	private String jsmd5;   //将网页中的js分别加密
	private String jscode;    //存储js源码
	private String afterfilterjscode;  //经过正则处理用于计算长度的源码内容
	private int jscontentlength;   //经过正则处理之后js内容的字符长度
	public String getJsmd5() {
		return jsmd5;
	}
	public void setJsmd5(String jsmd5) {
		this.jsmd5 = jsmd5;
	}
	public String getJscode() {
		return jscode;
	}
	public void setJscode(String jscode) {
		this.jscode = jscode;
	}
	public String getAfterfilterjscode() {
		return afterfilterjscode;
	}
	public void setAfterfilterjscode(String afterfilterjscode) {
		this.afterfilterjscode = afterfilterjscode;
	}
	public int getJscontentlength() {
		return jscontentlength;
	}
	public void setJscontentlength(int jscontentlength) {
		this.jscontentlength = jscontentlength;
	}
	
}
