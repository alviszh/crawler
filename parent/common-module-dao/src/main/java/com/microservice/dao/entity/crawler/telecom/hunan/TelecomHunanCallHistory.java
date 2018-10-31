package com.microservice.dao.entity.crawler.telecom.hunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 湖南电信通话记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_hunan_callhistory",indexes = {@Index(name = "index_telecom_hunan_callhistory_taskid", columnList = "taskid")})
public class TelecomHunanCallHistory  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**话单类型*/
	@Column(name="correspondence_type")
	private String correspondencetype;
	
	
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



	public String getCorrespondencetype() {
		return correspondencetype;
	}

	public void setCorrespondencetype(String correspondencetype) {
		this.correspondencetype = correspondencetype;
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
