package com.microservice.dao.entity.crawler.maimai;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "task_maimai" ,indexes = {@Index(name = "index_task_maimai_taskid", columnList = "taskid")})
public class TaskMaimai extends IdEntity implements Serializable{
	private static final long serialVersionUID = -7601637293254927953L;

	private String taskid;// uuid 前端通过uuid访问状态结果

//	@JsonBackReference
	private String cookies;
	
	private String loginInfo;		//登录信息
	
	private Integer userinfoStatus;     //用户的基本信息

	private Integer userEducationsStatus;  //用户的教育经历
	
	private Integer userWorkExpsStatus;    //用户的工作经历
	
	private Integer friendUserInfoStatus;   //用户朋友的基本信息
	
	private Integer friendEducationsStatus;  //用户朋友的教育经历
	
	private Integer friendWorkExpsStatus;   //用户朋友的工作经历
	
	@Override
	public String toString() {
		return "TaskMaimai [taskid=" + taskid  + ", cookies=" + cookies + ", userinfoStatus=" + userinfoStatus 
				+ ", loginInfo="+ loginInfo + ", userWorkExpsStatus=" + userWorkExpsStatus
				+ ", friendUserInfoStatus=" + friendUserInfoStatus + ", friendUserInfoStatus=" + friendUserInfoStatus 
				+ ", friendEducationsStatus="+ friendEducationsStatus + ", friendWorkExpsStatus=" + friendWorkExpsStatus + "]";
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	@Column(columnDefinition="text")
	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	@Column(columnDefinition="text")
	public String getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(String loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public Integer getUserinfoStatus() {
		return userinfoStatus;
	}

	public void setUserinfoStatus(Integer userinfoStatus) {
		this.userinfoStatus = userinfoStatus;
	}


	
	public Integer getUserEducationsStatus() {
		return userEducationsStatus;
	}

	public void setUserEducationsStatus(Integer userEducationsStatus) {
		this.userEducationsStatus = userEducationsStatus;
	}

	public Integer getUserWorkExpsStatus() {
		return userWorkExpsStatus;
	}

	public void setUserWorkExpsStatus(Integer userWorkExpsStatus) {
		this.userWorkExpsStatus = userWorkExpsStatus;
	}

	public Integer getFriendUserInfoStatus() {
		return friendUserInfoStatus;
	}

	public void setFriendUserInfoStatus(Integer friendUserInfoStatus) {
		this.friendUserInfoStatus = friendUserInfoStatus;
	}

	public Integer getFriendEducationsStatus() {
		return friendEducationsStatus;
	}

	public void setFriendEducationsStatus(Integer friendEducationsStatus) {
		this.friendEducationsStatus = friendEducationsStatus;
	}

	public Integer getFriendWorkExpsStatus() {
		return friendWorkExpsStatus;
	}

	public void setFriendWorkExpsStatus(Integer friendWorkExpsStatus) {
		this.friendWorkExpsStatus = friendWorkExpsStatus;
	}

	
}
