package com.microservice.dao.entity.crawler.unicom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_noteresult",indexes = {@Index(name = "index_unicom_noteresult_taskid", columnList = "taskid")})
public class UnicomNoteResult extends IdEntity {

	private String usernumber;//手机号

	private Integer userid;

	private String taskid;
	private String amount;//未知
	private String fee;//未知
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date smsdate;//发送日期
	private String smstime;//发送时间
	private String businesstype;//未知
	private String othernum;//其他手机号
	private String smstype;//短信类型 猜测为接受或发送
	private String otherarea;//未知 空字符串
	private String homearea;//归属地
	private String deratefee;//未知 空字符串

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmount() {
		return amount;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getFee() {
		return fee;
	}

	public void setSmsdate(Date smsdate) {
		this.smsdate = smsdate;
	}

	public Date getSmsdate() {
		return smsdate;
	}

	public void setSmstime(String smstime) {
		this.smstime = smstime;
	}

	public String getSmstime() {
		return smstime;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setOthernum(String othernum) {
		this.othernum = othernum;
	}

	public String getOthernum() {
		return othernum;
	}

	public void setSmstype(String smstype) {
		this.smstype = smstype;
	}

	public String getSmstype() {
		return smstype;
	}

	public void setOtherarea(String otherarea) {
		this.otherarea = otherarea;
	}

	public String getOtherarea() {
		return otherarea;
	}

	public void setHomearea(String homearea) {
		this.homearea = homearea;
	}

	public String getHomearea() {
		return homearea;
	}

	public void setDeratefee(String deratefee) {
		this.deratefee = deratefee;
	}

	public String getDeratefee() {
		return deratefee;
	}

	
	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
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
		return "UnicomNoteResult [usernumber=" + usernumber + ", userid=" + userid + ", taskid=" + taskid + ", amount="
				+ amount + ", fee=" + fee + ", smsdate=" + smsdate + ", smstime=" + smstime + ", businesstype="
				+ businesstype + ", othernum=" + othernum + ", smstype=" + smstype + ", otherarea=" + otherarea
				+ ", homearea=" + homearea + ", deratefee=" + deratefee + "]";
	}

}