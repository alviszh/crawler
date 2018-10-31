/**
 * 
 */
package com.crawler.monitor.json.tasker;

/**
 * @author sln
 * @date 2018年9月25日上午10:29:15
 * @Description: 人行征信定时任务执行结果
 */
public class MonitorStandaloneTaskerBean {
	private String serviceName;
	private String createtime;
	private String owner;
	private Boolean finished;
	private String description;
	private String key;
	private String taskid;
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public MonitorStandaloneTaskerBean() {
		super();
	}
	public MonitorStandaloneTaskerBean(String serviceName, String createtime, String owner, Boolean finished,
			String description, String key, String taskid) {
		super();
		this.serviceName = serviceName;
		this.createtime = createtime;
		this.owner = owner;
		this.finished = finished;
		this.description = description;
		this.key = key;
		this.taskid = taskid;
	}
	
}
