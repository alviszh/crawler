package com.microservice.dao.entity.crawler.middle;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "browser_cluster")
public class BrowserCluster extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2306689823937070230L;
	
	//服务器ip
	@Column(name = "instance_ip")
	private String instanceIp;
	
	//服务器ip
	@Column(name = "port")
	private String port;
	
	//是否在可用状态
	@Column(name = "inuse")
	private Integer inuse;
	
	//微服务名称
	@Column(name = "app_name")
	private String appName;
	
	//WindowHandle id or name(seleunim)
	@Column(name = "window_handle")
	private String windowHandle;
	
	//WindowHandle id or name(seleunim)
	@Column(name = "taskid")
	private String taskid;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "request_path")
	private String requestPath;
	
	@Column(name = "interval_time")
	private Long intervalTime;

	public Long getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Long intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getInstanceIp() {
		return instanceIp;
	}

	public void setInstanceIp(String instanceIp) {
		this.instanceIp = instanceIp;
	}

	public Integer getInuse() {
		return inuse;
	}

	public void setInuse(Integer inuse) {
		this.inuse = inuse;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getWindowHandle() {
		return windowHandle;
	}

	public void setWindowHandle(String windowHandle) {
		this.windowHandle = windowHandle;
	}

	@Override
	public String toString() {
		return "BrowserCluster [id="+ id +", instanceIp=" + instanceIp + ", port=" + port + ", inuse=" + inuse + ", appName="
				+ appName + ", windowHandle=" + windowHandle + ", taskid=" + taskid + ", updateTime=" + updateTime
				+ ", requestPath=" + requestPath + ", intervalTime=" + intervalTime + "]";
	}

	

	

	
	
	
	
	
	
	


}
