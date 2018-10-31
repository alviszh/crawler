package com.microservice.dao.entity.crawler.telecom.heilongjiang;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_heilongjiang_callresult")
public class TelecomCallThemResult extends IdEntity {

	private String usernumber;

	private String type;// 计费区号

	private String othernum;//其他号码

	private String date;//日期

	private String time;//时间

	private String money;//费用

	private String teletype;//主叫或被叫

	private String othermoney;//附件费用

	private String guishudi;//归属地

	private String calltype;//通话类型

	private Integer userid;
	
	private String taskid;

	private String md5;

	
	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTeletype() {
		return teletype;
	}

	public void setTeletype(String teletype) {
		this.teletype = teletype;
	}

	public String getOthermoney() {
		return othermoney;
	}

	public void setOthermoney(String othermoney) {
		this.othermoney = othermoney;
	}

	public String getGuishudi() {
		return guishudi;
	}

	public void setGuishudi(String guishudi) {
		this.guishudi = guishudi;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
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

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String toStringmd5() {
		return "TelecomResult [usernumber=" + usernumber + ", othernum=" + othernum + ", date=" + date + ", time="
				+ time + "]";
	}

	@Override
	public String toString() {
		return "TelecomResult [usernumber=" + usernumber + ", type=" + type + ", othernum=" + othernum + ", date="
				+ date + ", time=" + time + ", money=" + money + ", teletype=" + teletype + ", othermoney=" + othermoney
				+ ", guishudi=" + guishudi + ", calltype=" + calltype + ", userid=" + userid + ", taskid=" + taskid
				+ ", md5=" + md5 + "]";
	}

}
