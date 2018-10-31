package com.crawler.xuexin.json;

import java.io.Serializable;

public class XuexinRequestParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2443128691144029449L;
	public String taskId;
	public String username;
	public String password;

	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "XuexinRequestParameters [taskId=" + taskId + ", username=" + username + ", password=" + password + "]";
	}
	
}
