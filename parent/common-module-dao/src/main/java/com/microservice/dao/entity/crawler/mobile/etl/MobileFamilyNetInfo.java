package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_family_net_info",indexes = {@Index(name = "index_mobile_family_net_info_taskid", columnList = "taskId")})

/*
 * 家庭网子电话号码
 */

public class MobileFamilyNetInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String familyNetName;	//家庭子网名称
	private String familyNetNumber;	//家庭子网号码
	private String telephoneNumber; //用户手机号
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getFamilyNetName() {
		return familyNetName;
	}
	public void setFamilyNetName(String familyNetName) {
		this.familyNetName = familyNetName;
	}
	public String getFamilyNetNumber() {
		return familyNetNumber;
	}
	public void setFamilyNetNumber(String familyNetNumber) {
		this.familyNetNumber = familyNetNumber;
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
		return "MobileFamilyNetInfo [taskId=" + taskId + ", familyNetName=" + familyNetName + ", familyNetNumber="
				+ familyNetNumber + ", telephoneNumber=" + telephoneNumber + ", resource=" + resource + "]";
	}
	
	
		
}
