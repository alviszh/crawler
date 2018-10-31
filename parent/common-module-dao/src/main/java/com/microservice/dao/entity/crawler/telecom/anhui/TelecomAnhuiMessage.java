package com.microservice.dao.entity.crawler.telecom.anhui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name="telecom_anhui_message",indexes = {@Index(name = "index_telecom_anhui_message_taskid", columnList = "taskid")}) 
public class TelecomAnhuiMessage extends IdEntity{

	private String smsType;//业务类型   qrySecondLine
	
	private String getType;//收发类型  qryThirdLine
	
	private String hisNum;//对方号码   qryFifthLine
	
	private String sendTime;//发送时间   qrySixthLine
	
	private String money;//费用  qrySeventhLine
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomAnhuiMessage [smsType=" + smsType + ", getType=" + getType + ", hisNum=" + hisNum + ", sendTime="
				+ sendTime + ", money=" + money + ", taskid=" + taskid + "]";
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public String getGetType() {
		return getType;
	}

	public void setGetType(String getType) {
		this.getType = getType;
	}

	public String getHisNum() {
		return hisNum;
	}

	public void setHisNum(String hisNum) {
		this.hisNum = hisNum;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
