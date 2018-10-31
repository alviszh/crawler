package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_relationship_info",indexes = {@Index(name = "index_mobile_relationship_info_taskid", columnList = "taskId")})

/*
 * 亲情号信息
 */

public class MobileRelationshipInfo extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String memberNumber;
	private String menberType;
	private String menberShortnumber;
	private String telephoneNumber; //用户手机号
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getMemberNumber() {
		return memberNumber;
	}
	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}
	public String getMenberType() {
		return menberType;
	}
	public void setMenberType(String menberType) {
		this.menberType = menberType;
	}
	public String getMenberShortnumber() {
		return menberShortnumber;
	}
	public void setMenberShortnumber(String menberShortnumber) {
		this.menberShortnumber = menberShortnumber;
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
		return "MobileRelationshipInfo [taskId=" + taskId + ", memberNumber=" + memberNumber + ", menberType="
				+ menberType + ", menberShortnumber=" + menberShortnumber + ", telephoneNumber=" + telephoneNumber
				+ ", resource=" + resource + "]";
	}
	
	
	
}
