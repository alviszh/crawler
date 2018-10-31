package com.microservice.dao.entity.crawler.telecom.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_beijing_smsresult")
public class TelecomBeijingSMSThremResult extends IdEntity {

	private String callid;

	private String type;//业务类型

	private String smstype;// 收发类型

	private String othernum;// 对方号码

	private String date;// 开始时间

	private String costs;// 总费用

	private Integer userid;

	private String taskid;

	public String getCallid() {
		return callid;
	}

	public void setCallid(String callid) {
		this.callid = callid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSmstype() {
		return smstype;
	}

	public void setSmstype(String smstype) {
		this.smstype = smstype;
	}

	public String getOthernum() {
		return othernum;
	}

	public void setOthernum(String othernum) {
		this.othernum = othernum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCosts() {
		return costs;
	}

	public void setCosts(String costs) {
		this.costs = costs;
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
		return "TelecomBeijingSMSThremResult [callid=" + callid + ", type=" + type + ", smstype=" + smstype
				+ ", othernum=" + othernum + ", date=" + date + ", costs=" + costs + ", userid=" + userid + ", taskid="
				+ taskid + "]";
	}

	

}