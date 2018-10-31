package com.microservice.dao.entity.crawler.telecom.hainan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hainan_userinfo")
public class TelecomHaiNanUserInfoResult extends IdEntity {

	private String username ;
	
	private String usertype;
	
	private String useridcard;
	
	private String address;

	private Integer userid;

	private String taskid;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getUseridcard() {
		return useridcard;
	}

	public void setUseridcard(String useridcard) {
		this.useridcard = useridcard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomHaiNanUserInfoResult [username=" + username + ", usertype=" + usertype + ", useridcard="
				+ useridcard + ", address=" + address + ", userid=" + userid + ", taskid=" + taskid + "]";
	}



}