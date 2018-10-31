package com.microservice.dao.entity.crawler.telecom.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_beijing_integra")
public class TelecomBeijingIntegraResult extends IdEntity {

	private String date;//账期

	private String type;//积分类别名称

	private String num;//积分值

	private String phone;//用户号码

	private Integer userid;

	private String taskid;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
		return "TelecomBeijingIntegraResult [date=" + date + ", type=" + type + ", num=" + num + ", phone=" + phone
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}

}