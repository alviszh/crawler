package com.microservice.dao.entity.crawler.telecom.beijing;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_beijing_userinfo")
public class TelecomBeijingUserInfo extends IdEntity {

	private String name;//姓名
	
	private String type;//客户类型
	
	private String contactnumber;//联系电话

	private String address;//地址

	private String postcode;//邮政编码

	private String email;//邮箱

	private String nettime;//入网时间
	
	private Integer userid;

	private String taskid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContactnumber() {
		return contactnumber;
	}

	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNettime() {
		return nettime;
	}

	public void setNettime(String nettime) {
		this.nettime = nettime;
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
		return "TelecomBeijingUserInfo [name=" + name + ", type=" + type + ", contactnumber=" + contactnumber
				+ ", address=" + address + ", postcode=" + postcode + ", email=" + email + ", nettime=" + nettime
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	
	

}