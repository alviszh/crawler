package com.crawler.mobile.json;

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MessageSqlResult<T> implements Serializable {
	private T data;

	private String usernum;
	
	private String startdate;
	
	private String enddate;
	
	private StatusCodeRec message;

	public StatusCodeRec getMessage() {
		return message;
	}

	public void setMessage(StatusCodeRec message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


	public String getUsernum() {
		return usernum;
	}

	public void setUsernum(String usernum) {
		this.usernum = usernum;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
	
	

}
