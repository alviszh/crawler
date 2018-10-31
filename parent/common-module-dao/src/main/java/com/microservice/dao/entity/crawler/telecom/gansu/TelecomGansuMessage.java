package com.microservice.dao.entity.crawler.telecom.gansu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGansu_Message",indexes = {@Index(name = "index_telecomGansu_Message_taskid", columnList = "taskid")}) 
public class TelecomGansuMessage extends IdEntity{

	private String sendDate;//发送时间
	
	private String sendType;//呼叫类型
	
	private String hisNum;//对方号码
	
	private String money;//话费
	
	private String taskid;

	
	
	public TelecomGansuMessage() {
		super();
	}

	public TelecomGansuMessage(String sendDate, String sendType, String hisNum, String money, String taskid) {
		super();
		this.sendDate = sendDate;
		this.sendType = sendType;
		this.hisNum = hisNum;
		this.money = money;
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomGansuMessage [sendDate=" + sendDate + ", sendType=" + sendType + ", hisNum=" + hisNum
				+ ", money=" + money + ", taskid=" + taskid + "]";
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getHisNum() {
		return hisNum;
	}

	public void setHisNum(String hisNum) {
		this.hisNum = hisNum;
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
