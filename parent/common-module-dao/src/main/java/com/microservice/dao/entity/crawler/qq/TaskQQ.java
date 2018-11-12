package com.microservice.dao.entity.crawler.qq;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "task_qq")
public class TaskQQ extends IdEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private String taskid;

	private String cookies;

	private String phase;

	private String phasestatus;//步骤状态

	private String description;		//状态信息

	private Boolean finished;//爬虫任务是否全部完成

	private String qqnum;//qq号码

	private String password;

	private Integer qqMessageStatus;//qq信息状态

	private Integer qqQunStatus;//qq群状态

	private Integer qqFriendStatus;//qq好友状态

	private String crawlerHost;

	private String crawlerPort;
	
	private Date etltime;
	
	private String taskOwner;//数据所属人
	
	private String servicename;
	

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
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

	public String getCrawlerHost() {
		return crawlerHost;
	}

	public void setCrawlerHost(String crawlerHost) {
		this.crawlerHost = crawlerHost;
	}

	public String getCrawlerPort() {
		return crawlerPort;
	}

	public void setCrawlerPort(String crawlerPort) {
		this.crawlerPort = crawlerPort;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getPhasestatus() {
		return phasestatus;
	}

	public void setPhasestatus(String phasestatus) {
		this.phasestatus = phasestatus;
	}

	public Date getEtltime() {
		return etltime;
	}

	public void setEtltime(Date etltime) {
		this.etltime = etltime;
	}

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	@Override
	public String toString() {
		return "TaskQQ [taskid=" + taskid + ", cookies=" + cookies + ", phase=" + phase + ", phasestatus=" + phasestatus
				+ ", description=" + description + ", finished=" + finished + ", qqnum=" + qqnum + ", password="
				+ password + ", qqMessageStatus=" + qqMessageStatus + ", qqQunStatus=" + qqQunStatus
				+ ", qqFriendStatus=" + qqFriendStatus + ", crawlerHost=" + crawlerHost + ", crawlerPort=" + crawlerPort
				+ ", etltime=" + etltime + ", taskOwner=" + taskOwner + ", servicename=" + servicename + "]";
	}
	
	

}
