package com.crawler.bank.json;

import java.io.Serializable;
import java.util.Map;

public class IdleInstanceInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7083574384145377283L;

	public String ipAddr;

	public String instanceId;

	public String app;

	public String vipAddress;

	public String hostName;
	
	public Integer port;

	public String status;

	public Map<String, String> metadata;

	public Long lastUpdatedTimestamp;

	public Long lastDirtyTimestamp;

	public String actionType;

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getVipAddress() {
		return vipAddress;
	}

	public void setVipAddress(String vipAddress) {
		this.vipAddress = vipAddress;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
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

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "IdleInstanceInfo [ipAddr=" + ipAddr + ", instanceId=" + instanceId + ", app=" + app + ", vipAddress="
				+ vipAddress + ", hostName=" + hostName + ", port=" + port + ", status=" + status + ", metadata="
				+ metadata + ", lastUpdatedTimestamp=" + lastUpdatedTimestamp + ", lastDirtyTimestamp="
				+ lastDirtyTimestamp + ", actionType=" + actionType + "]";
	}
	
	
	
	
	
	

}
