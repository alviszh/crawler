package com.microservice.dao.entity.crawler.xuexin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 学信网task表
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "task_xuexin" ,indexes = {@Index(name = "index_task_xuexin_taskid", columnList = "taskid")})
public class TaskXuexin extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477338959881567237L;

	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String cookies;
	
	private String loginInfo;		//登录信息
	
	private Integer education_status;	//学历信息状态
	
	private Integer school_status;	//学籍信息状态
	

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

	public Integer getEducation_status() {
		return education_status;
	}

	public void setEducation_status(Integer education_status) {
		this.education_status = education_status;
	}

	public Integer getSchool_status() {
		return school_status;
	}

	public void setSchool_status(Integer school_status) {
		this.school_status = school_status;
	}

	
}
