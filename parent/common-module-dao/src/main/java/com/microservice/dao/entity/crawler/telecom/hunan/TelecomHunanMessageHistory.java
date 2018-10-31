package com.microservice.dao.entity.crawler.telecom.hunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 湖南电信短信记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_hunan_messagerhistory",indexes = {@Index(name = "index_telecom_hunan_messagerhistory_taskid", columnList = "taskid")})
public class TelecomHunanMessageHistory  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**发送时间*/
	@Column(name="send_time")
	private String sendtime;
	
	/**对方号码*/
	@Column(name="opposite_phone")
	private String oppositephone;

	/**本机号码*/
	@Column(name="my_phone")
	private String myphone;
	
	/**话单类型*/
	@Column(name="bus_type")
	private String bustype;
	
	/**费用*/
	@Column(name="message_addr")
	private String messageaddr;
	
	/**帐目类型*/
	@Column(name="zmlx")
	private String zmlx;
	

	
	public String getZmlx() {
		return zmlx;
	}

	public void setZmlx(String zmlx) {
		this.zmlx = zmlx;
	}

	public String getMyphone() {
		return myphone;
	}

	public void setMyphone(String myphone) {
		this.myphone = myphone;
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


	public String getBustype() {
		return bustype;
	}

	public void setBustype(String bustype) {
		this.bustype = bustype;
	}
	
}
