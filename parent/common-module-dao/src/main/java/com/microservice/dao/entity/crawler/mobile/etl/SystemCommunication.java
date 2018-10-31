package com.microservice.dao.entity.crawler.mobile.etl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="system_communication")
public class SystemCommunication extends IdEntity {

	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String loginName;
	private String comments;
	private String results;
	private String url;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	
	@Column(columnDefinition="text")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "SystemCommunication [taskId=" + taskId + ", loginName=" + loginName + ", comments=" + comments
				+ ", results=" + results + ", url=" + url + "]";
	}
	
	
}
