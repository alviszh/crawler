package com.microservice.dao.entity.crawler.telecom.fujian;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 福建电信通话记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_fujian_callhistory",indexes = {@Index(name = "index_telecom_fujian_callhistory_taskid", columnList = "taskid")})
public class TelecomFujianCallHistory  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	
	/**通话时长*/
	@Column(name="communicate_time")
	private String communicatetime;
	
	/**通话时间*/
	@Column(name="communicate_time2")
	private String communicatetime2;
	
	/**通话地点*/
	@Column(name="communicate_addr")
	private String communicateaddr;
	
	/**对方号码*/
	@Column(name="opposite_phone")
	private String oppositephone;
	
	/**呼叫类型*/
	@Column(name="call_type")
	private String calltype;
	
	
	/**费用合计（月）*/
	@Column(name="cost_total")
	private String costtotal;
	


	public String getCosttotal() {
		return costtotal;
	}

	public void setCosttotal(String costtotal) {
		this.costtotal = costtotal;
	}


	public String getCommunicatetime() {
		return communicatetime;
	}

	public void setCommunicatetime(String communicatetime) {
		this.communicatetime = communicatetime;
	}

	public String getCommunicatetime2() {
		return communicatetime2;
	}

	public void setCommunicatetime2(String communicatetime2) {
		this.communicatetime2 = communicatetime2;
	}

	public String getCommunicateaddr() {
		return communicateaddr;
	}

	public void setCommunicateaddr(String communicateaddr) {
		this.communicateaddr = communicateaddr;
	}

	public String getOppositephone() {
		return oppositephone;
	}

	public void setOppositephone(String oppositephone) {
		this.oppositephone = oppositephone;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
