package com.crawler.bank.json;

import java.io.Serializable;

public class BrowserInfo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2006762594459256702L;

	public String handler;
	
	public String status;
	
	public String action;

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "BrowserInfo [handler=" + handler + ", status=" + status + ", parser=" + action + "]";
	}
	
	

}
