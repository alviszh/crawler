package com.crawler.monitor.json;

public class EurekaInstanceBean {
	
	private String appName;
	
	private String serverId;
	
	private Long timestamp;
	
	private String eventType;
	
	private String instanceId;
	
	private String hostName;
	
	private String ipAddr;
	
	private String homePageUrl;
	
	private String vipAddress;

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

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getHomePageUrl() {
		return homePageUrl;
	}

	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}

	public String getVipAddress() {
		return vipAddress;
	}

	public void setVipAddress(String vipAddress) {
		this.vipAddress = vipAddress;
	}

	@Override
	public String toString() {
		return "EurekaInstanceBean [appName=" + appName + ", serverId=" + serverId + ", timestamp=" + timestamp
				+ ", eventType=" + eventType + ", instanceId=" + instanceId + ", hostName=" + hostName + ", ipAddr="
				+ ipAddr + ", homePageUrl=" + homePageUrl + ", vipAddress=" + vipAddress + "]";
	}
}
