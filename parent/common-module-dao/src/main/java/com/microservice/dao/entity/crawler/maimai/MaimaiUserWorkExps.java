package com.microservice.dao.entity.crawler.maimai;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 脉脉用户工作经历
 * @author zh
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="maimai_user_work_exps",indexes = {@Index(name = "index_maimai_user_work_exps_taskid", columnList = "taskid")})
public class MaimaiUserWorkExps extends IdEntity{
	private String taskid;
	private String mm_id;       //脉脉账号在脉脉系统中的ID
	private String company;     //工作单位
	private String position;    //工作职务
	private String start_date;  //工作开始时间
	private String end_date;    //工作结束时间(包括年月, 如:2009-7; 如果为空表示至今或用户没有填写;)
	private String description;   //工作经历描述
	
	@Override
	public String toString() {
		return "MaimaiUserWorkExps [taskid=" + taskid + ", mm_id=" + mm_id + ", company=" + company
				+ ", position=" + position+ ", start_date=" + start_date
				+ ", end_date=" + end_date+ ", description=" + description+ "]";
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
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
	
	
}
