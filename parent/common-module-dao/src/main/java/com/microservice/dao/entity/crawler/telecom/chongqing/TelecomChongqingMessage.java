package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 电信短信详单
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_message" ,indexes = {@Index(name = "index_telecom_chongqing_message_taskid", columnList = "taskid")})
public class TelecomChongqingMessage extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 业务类型
	 */
	private String smsType;
	
	/**
	 * 对方号码
	 */
	private String smsMobile;
	
	/**
	 * 发送时间
	 */
	private String smsTime;
	
	/**
	 * 费用（元）
	 */
	private String smsCost;
	
	/**
	 * 漫游状态
	 */
	private String roam;
	
	/**
	 * 是否在套餐内
	 */
	private String inpackage;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public String getSmsMobile() {
		return smsMobile;
	}

	public void setSmsMobile(String smsMobile) {
		this.smsMobile = smsMobile;
	}

	public String getSmsTime() {
		return smsTime;
	}

	public void setSmsTime(String smsTime) {
		this.smsTime = smsTime;
	}

	public String getSmsCost() {
		return smsCost;
	}

	public void setSmsCost(String smsCost) {
		this.smsCost = smsCost;
	}

	public String getRoam() {
		return roam;
	}

	public void setRoam(String roam) {
		this.roam = roam;
	}

	public String getInpackage() {
		return inpackage;
	}

	public void setInpackage(String inpackage) {
		this.inpackage = inpackage;
	}

	@Override
	public String toString() {
		return "TelecomChongqingMessage [taskid=" + taskid + ", smsType=" + smsType + ", smsMobile=" + smsMobile
				+ ", smsTime=" + smsTime + ", smsCost=" + smsCost + ", roam=" + roam + ", inpackage=" + inpackage + "]";
	}

	public TelecomChongqingMessage(String taskid, String smsType, String smsMobile, String smsTime, String smsCost,
			String roam, String inpackage) {
		super();
		this.taskid = taskid;
		this.smsType = smsType;
		this.smsMobile = smsMobile;
		this.smsTime = smsTime;
		this.smsCost = smsCost;
		this.roam = roam;
		this.inpackage = inpackage;
	}

	public TelecomChongqingMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}