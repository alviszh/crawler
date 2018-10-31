package com.microservice.dao.entity.crawler.telecom.qinghai;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_qinghai_callresult")
public class TelecomQingHaiCallThremResult extends IdEntity {

	private String callarea;//主叫区号

	private String callphone;// 主叫号码

	private String callareaother;// 被叫区号

	private String callphoneother;// 被叫号码

	private String date;// 通话开始时间

	private String calltime;// 通话时长
	
	private String calltype;// 通话类型

	private String callcosts;// 通话费用

	private String type;// 类型 （主叫或被叫）

	private Integer userid;

	private String taskid;

	public String getCallarea() {
		return callarea;
	}

	public void setCallarea(String callarea) {
		this.callarea = callarea;
	}

	public String getCallphone() {
		return callphone;
	}

	public void setCallphone(String callphone) {
		this.callphone = callphone;
	}

	public String getCallareaother() {
		return callareaother;
	}

	public void setCallareaother(String callareaother) {
		this.callareaother = callareaother;
	}

	public String getCallphoneother() {
		return callphoneother;
	}

	public void setCallphoneother(String callphoneother) {
		this.callphoneother = callphoneother;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCalltime() {
		return calltime;
	}

	public void setCalltime(String calltime) {
		this.calltime = calltime;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getCallcosts() {
		return callcosts;
	}

	public void setCallcosts(String callcosts) {
		this.callcosts = callcosts;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		return "TelecomQingHaiCallThremResult [callarea=" + callarea + ", callphone=" + callphone + ", callareaother="
				+ callareaother + ", callphoneother=" + callphoneother + ", date=" + date + ", calltime=" + calltime
				+ ", calltype=" + calltype + ", callcosts=" + callcosts + ", type=" + type + ", userid=" + userid
				+ ", taskid=" + taskid + "]";
	}

}