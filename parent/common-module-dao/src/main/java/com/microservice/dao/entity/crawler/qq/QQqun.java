package com.microservice.dao.entity.crawler.qq;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.pbccrc.AbstractEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="qq_qun")
public class QQqun extends AbstractEntity implements Serializable{

	private String taskid;
	
	private String groupcode;
	
	private String groupname;
	
	private String total_member;
	
	private String notfriends;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getTotal_member() {
		return total_member;
	}

	public void setTotal_member(String total_member) {
		this.total_member = total_member;
	}

	public String getNotfriends() {
		return notfriends;
	}

	public void setNotfriends(String notfriends) {
		this.notfriends = notfriends;
	}
	
	
	
	
}
