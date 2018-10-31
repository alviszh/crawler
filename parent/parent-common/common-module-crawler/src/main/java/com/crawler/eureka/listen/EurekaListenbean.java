package com.crawler.eureka.listen;

import java.util.HashMap;


public class EurekaListenbean {

	private String instanceId;
	private String appName;
	private String appGroupName;
	private String ipAddr;
	private String sid;
	private String homePageUrl;
	private String statusPageUrl;
	private String healthCheckUrl;
	private String secureHealthCheckUrl;
	private String vipAddress;
	private String secureVipAddress;
	private int countryId;
	private String hostName;
	private Boolean isCoordinatingDiscoveryServer;
	private HashMap<String, String> metadata;
	private Long lastUpdatedTimestamp;
	private Long lastDirtyTimestamp;
	private String asgName;
	
	private String serverId;
	
	private String eventType;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppGroupName() {
		return appGroupName;
	}

	public void setAppGroupName(String appGroupName) {
		this.appGroupName = appGroupName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}


	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}

	public String getStatusPageUrl() {
		return statusPageUrl;
	}

	public void setStatusPageUrl(String statusPageUrl) {
		this.statusPageUrl = statusPageUrl;
	}

	public String getHealthCheckUrl() {
		return healthCheckUrl;
	}

	public void setHealthCheckUrl(String healthCheckUrl) {
		this.healthCheckUrl = healthCheckUrl;
	}

	public String getSecureHealthCheckUrl() {
		return secureHealthCheckUrl;
	}

	public void setSecureHealthCheckUrl(String secureHealthCheckUrl) {
		this.secureHealthCheckUrl = secureHealthCheckUrl;
	}

	public String getVipAddress() {
		return vipAddress;
	}

	public void setVipAddress(String vipAddress) {
		this.vipAddress = vipAddress;
	}

	public String getSecureVipAddress() {
		return secureVipAddress;
	}

	public void setSecureVipAddress(String secureVipAddress) {
		this.secureVipAddress = secureVipAddress;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}


	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Boolean getIsCoordinatingDiscoveryServer() {
		return isCoordinatingDiscoveryServer;
	}

	public void setIsCoordinatingDiscoveryServer(Boolean isCoordinatingDiscoveryServer) {
		this.isCoordinatingDiscoveryServer = isCoordinatingDiscoveryServer;
	}

	public Long getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(Long lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	public Long getLastDirtyTimestamp() {
		return lastDirtyTimestamp;
	}

	public void setLastDirtyTimestamp(Long lastDirtyTimestamp) {
		this.lastDirtyTimestamp = lastDirtyTimestamp;
	}

	public String getAsgName() {
		return asgName;
	}

	public void setAsgName(String asgName) {
		this.asgName = asgName;
	}

	public HashMap<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getHomePageUrl() {
		return homePageUrl;
	}
	
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	
	

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "EurekaListenbean [instanceId=" + instanceId + ", appName=" + appName + ", appGroupName=" + appGroupName
				+ ", ipAddr=" + ipAddr + ", sid=" + sid + ", homePageUrl=" + homePageUrl + ", statusPageUrl="
				+ statusPageUrl + ", healthCheckUrl=" + healthCheckUrl + ", secureHealthCheckUrl="
				+ secureHealthCheckUrl + ", vipAddress=" + vipAddress + ", secureVipAddress=" + secureVipAddress
				+ ", countryId=" + countryId + ", hostName=" + hostName + ", isCoordinatingDiscoveryServer="
				+ isCoordinatingDiscoveryServer + ", metadata=" + metadata + ", lastUpdatedTimestamp="
				+ lastUpdatedTimestamp + ", lastDirtyTimestamp=" + lastDirtyTimestamp + ", asgName=" + asgName
				+ ", serverId=" + serverId + ", eventType=" + eventType + "]";
	}
}
