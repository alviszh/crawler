package com.microservice.dao.entity.crawler.maimai;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 脉脉用户朋友的教育经历
 * @author zh
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="maimai_friend_educations",indexes = {@Index(name = "index_maimai_friend_educations_taskid", columnList = "taskid")})
public class MaimaiFriendEducations extends IdEntity{
	private String taskid;
	private String mm_id;       //脉脉账号在脉脉系统中的ID
	private String friend_id;   //朋友在脉脉系统中的ID	
	private String school;      //毕业学校
	private String department;  //专业
	private String degree;      //学历(0:专科; 1: 本科; 2: 硕士; 3: 博士; 4: 博士后; 5: 其他; 255:其他)
	private String start_date;  //入学时间
	private String end_date;    //毕业时间(包括年月, 如:2009-7; 如果为空表示至今或用户没有填写;)
	private String description; //教育经历描述
	
	
	@Override
	public String toString() {
		return "MaimaiFriendEducations [taskid=" + taskid + ", mm_id=" + mm_id + ", school=" + school
				+ ", department=" + department+ ", degree=" + degree+ ", start_date=" + start_date
				+ ", end_date=" + end_date+ ", description=" + description + ", friend_id=" + friend_id+ "]";
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getMm_id() {
		return mm_id;
	}
	public void setMm_id(String mm_id) {
		this.mm_id = mm_id;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getFriend_id() {
		return friend_id;
	}

	public void setFriend_id(String friend_id) {
		this.friend_id = friend_id;
	}

}
