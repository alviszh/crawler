package com.crawler.housingfund.json;

import java.io.Serializable;

public class HousingFundRequestParameters implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5115154845415106526L;

	public String taskId;
	public String city;				//所属城市
	public String loginType;
	public String username;
	public String password;

	@Override
	public String toString() {
		return "HousingFundRequestParameters [taskId=" + taskId + ", city=" + city + ", loginType=" + loginType
				+ ", username=" + username + ", password=" + password + "]";
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
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

}
