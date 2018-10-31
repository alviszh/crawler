package com.crawler.qq.json;




import java.io.Serializable;

public class TaskQQ implements Serializable{

	private static final long serialVersionUID = 1L;

	private String taskid;

	private String cookies;

	private String description;		//状态信息

	private Boolean finished;//爬虫任务是否全部完成

	private String qqnum;//qq号码
	
	private String password;
	
	private Integer qqMessageStatus;//qq信息状态
	
	private Integer qqQunStatus;//qq群状态
	
	private Integer qqFriendStatus;//qq好友状态

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public String getQqnum() {
		return qqnum;
	}

	public void setQqnum(String qqnum) {
		this.qqnum = qqnum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getQqMessageStatus() {
		return qqMessageStatus;
	}

	public void setQqMessageStatus(Integer qqMessageStatus) {
		this.qqMessageStatus = qqMessageStatus;
	}

	public Integer getQqQunStatus() {
		return qqQunStatus;
	}

	public void setQqQunStatus(Integer qqQunStatus) {
		this.qqQunStatus = qqQunStatus;
	}

	public Integer getQqFriendStatus() {
		return qqFriendStatus;
	}

	public void setQqFriendStatus(Integer qqFriendStatus) {
		this.qqFriendStatus = qqFriendStatus;
	}

	public TaskQQ(String taskid, String cookies, String description, Boolean finished, String qqnum, String password,
				  Integer qqMessageStatus, Integer qqQunStatus, Integer qqFriendStatus) {
		super();
		this.taskid = taskid;
		this.cookies = cookies;
		this.description = description;
		this.finished = finished;
		this.qqnum = qqnum;
		this.password = password;
		this.qqMessageStatus = qqMessageStatus;
		this.qqQunStatus = qqQunStatus;
		this.qqFriendStatus = qqFriendStatus;
	}

	public TaskQQ() {
		super();
	}

	@Override
	public String toString() {
		return "TaskQQ [taskid=" + taskid + ", cookies=" + cookies + ", description=" + description + ", finished="
				+ finished + ", qqnum=" + qqnum + ", password=" + password + ", qqMessageStatus=" + qqMessageStatus
				+ ", qqQunStatus=" + qqQunStatus + ", qqFriendStatus=" + qqFriendStatus + "]";
	}

	
	
}
