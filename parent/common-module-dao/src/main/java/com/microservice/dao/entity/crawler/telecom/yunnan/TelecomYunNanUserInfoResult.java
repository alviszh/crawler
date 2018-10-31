package com.microservice.dao.entity.crawler.telecom.yunnan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_yunnan_userinfo")
public class TelecomYunNanUserInfoResult extends IdEntity {

	private String username ;
	
	private String usertype;
	
	private String useridcard;
	
	private String userpostcode;

	private String useremail;
	
	private String userstardate;
	
	private String address;
		
	private Integer userid;

	private String taskid;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getUserpostcode() {
		return userpostcode;
	}

	public void setUserpostcode(String userpostcode) {
		this.userpostcode = userpostcode;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserstardate() {
		return userstardate;
	}

	public void setUserstardate(String userstardate) {
		this.userstardate = userstardate;
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
		return "TelecomYunNanUserInfoResult [username=" + username + ", usertype=" + usertype + ", useridcard="
				+ useridcard + ", userpostcode=" + userpostcode + ", useremail=" + useremail + ", userstardate="
				+ userstardate + ", address=" + address + ", userid=" + userid + ", taskid=" + taskid + "]";
	}


}