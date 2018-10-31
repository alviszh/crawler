package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_group_net_info",indexes = {@Index(name = "index_mobile_group_net_info_taskid", columnList = "taskId")})

/*
 * 集团网名称及子电话号码 
 */
public class MobileGroupNetInfo extends IdEntity implements Serializable {

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;		//唯一标识
	private String groupNetName;		//集团网名称
	private String groupNetNumber;		//集团网号码
	private String telephoneNumber; //用户手机号
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getGroupNetName() {
		return groupNetName;
	}
	public void setGroupNetName(String groupNetName) {
		this.groupNetName = groupNetName;
	}
	public String getGroupNetNumber() {
		return groupNetNumber;
	}
	public void setGroupNetNumber(String groupNetNumber) {
		this.groupNetNumber = groupNetNumber;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Override
	public String toString() {
		return "MobileGroupNetInfo [taskId=" + taskId + ", groupNetName=" + groupNetName + ", groupNetNumber="
				+ groupNetNumber + ", telephoneNumber=" + telephoneNumber + ", resource=" + resource + "]";
	}
	
	
}
