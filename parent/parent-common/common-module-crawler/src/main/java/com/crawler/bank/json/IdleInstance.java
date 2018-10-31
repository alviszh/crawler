package com.crawler.bank.json;

import java.io.Serializable;

public class IdleInstance implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2274193632187309277L;
	
	public IdleInstanceInfo idleInstanceInfo;
	
	public BrowserInfo browserInfo;
	
	public String message;
	
	public String messageCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public IdleInstanceInfo getIdleInstanceInfo() {
		return idleInstanceInfo;
	}

	public void setIdleInstanceInfo(IdleInstanceInfo idleInstanceInfo) {
		this.idleInstanceInfo = idleInstanceInfo;
	}

	public BrowserInfo getBrowserInfo() {
		return browserInfo;
	}

	public void setBrowserInfo(BrowserInfo browserInfo) {
		this.browserInfo = browserInfo;
	}

	@Override
	public String toString() {
		return "IdleInstance [idleInstanceInfo=" + idleInstanceInfo + ", browserInfo=" + browserInfo + ", message="
				+ message + ", messageCode=" + messageCode + "]";
	}

	

	
	

}
