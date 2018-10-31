package com.microservice.dao.entity.crawler.telecom.ningxia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 宁夏电信短信记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_ningxia_messagerhistory",indexes = {@Index(name = "index_telecom_ningxia_messagerhistory_taskid", columnList = "taskid")})
public class TelecomNingxiaMessageHistory  extends IdEntity{
	@Column(name="userid")
	private Integer userid;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
    ///////////////////////////////////////订单查询组//////////////////////////////////
	
	/**发送时间*/
	@Column(name="send_time")
	private String sendtime;
	
	/**短信状态*/
	@Column(name="message_status")
	private String messagestatus;
	
	/**对方号码*/
	@Column(name="opposite_phone")
	private String oppositephone;

	/**信息类型*/
	@Column(name="message_type")
	private String messagetype;
	
	/**费用*/
	@Column(name="message_addr")
	private String messageaddr;
	
	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getMessageaddr() {
		return messageaddr;
	}

	public void setMessageaddr(String messageaddr) {
		this.messageaddr = messageaddr;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getMessagestatus() {
		return messagestatus;
	}

	public void setMessagestatus(String messagestatus) {
		this.messagestatus = messagestatus;
	}

	public String getOppositephone() {
		return oppositephone;
	}

	public void setOppositephone(String oppositephone) {
		this.oppositephone = oppositephone;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
}
