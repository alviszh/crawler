package com.crawler.bank.json;

public class BrowserClusterBean {

	private String instanceIp;

	// 服务器ip
	private String port;

	// 是否在可用状态
	private Integer inuse;

	// 微服务名称
	private String appName;

	// WindowHandle id or name(seleunim)
	private String windowHandle;

	public BrowserClusterBean(String instanceIp, String port, Integer inuse, String appName, String windowHandle,
			String taskid, String requestPath, Long intervalTime) {
		super();
		this.instanceIp = instanceIp;
		this.port = port;
		this.inuse = inuse;
		this.appName = appName;
		this.windowHandle = windowHandle;
		this.taskid = taskid;
		this.requestPath = requestPath;
		this.intervalTime = intervalTime;
	}

	// WindowHandle id or name(seleunim)
	private String taskid;

	private String requestPath;

	private Long intervalTime;

	public String getInstanceIp() {
		return instanceIp;
	}

	public void setInstanceIp(String instanceIp) {
		this.instanceIp = instanceIp;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
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

	public String getWindowHandle() {
		return windowHandle;
	}

	public void setWindowHandle(String windowHandle) {
		this.windowHandle = windowHandle;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public Long getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Long intervalTime) {
		this.intervalTime = intervalTime;
	}

}
