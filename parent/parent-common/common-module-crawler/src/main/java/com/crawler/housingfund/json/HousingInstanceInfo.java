package com.crawler.housingfund.json;

public class HousingInstanceInfo {

	
	private String appName;

	private String serverId;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Override
	public String toString() {
		return "HousingInstanceInfo [appName=" + appName + ", serverId=" + serverId + "]";
	}
	
	
}
