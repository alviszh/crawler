package com.microservice.dao.entity.crawler.telecom.shanxi1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西电信短信详单
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_shanxi1_message" ,indexes = {@Index(name = "index_telecom_shanxi1_message_taskid", columnList = "taskid")})
public class TelecomShanxi1Message extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	/**
	 * 对方手机号码
	 */
	private String smsMobile;

	/**
	 * 短信发送时间
	 */
	private String smsTime;
	/**
	 * 短信金额
	 */
	private String smsCost;
	/**
	 * 短信收发类型
	 * 	smsType 0	发送
	 * 	smsType 1	接收
	 */
	private String smsType;
	/**
	 * 短信/彩信
	 * smsStyle 0	短信
	 * smsStyle 1	彩信
	 */
	private String smsStyle;
	/**
	 * 国内/国际
	 * smsArea 0		国内
	 * smsArea 1		国际
	 * smsArea null 	--
	 */
	private String smsArea;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public String getSmsStyle() {
		return smsStyle;
	}
	public void setSmsStyle(String smsStyle) {
		this.smsStyle = smsStyle;
	}
	public String getSmsArea() {
		return smsArea;
	}
	public void setSmsArea(String smsArea) {
		this.smsArea = smsArea;
	}
	@Override
	public String toString() {
		return "TelecomShanxi1Message [taskid=" + taskid + ", smsMobile=" + smsMobile + ", smsTime=" + smsTime
				+ ", smsCost=" + smsCost + ", smsType=" + smsType + ", smsStyle=" + smsStyle + ", smsArea=" + smsArea
				+ "]";
	}
	public TelecomShanxi1Message(String taskid, String smsMobile, String smsTime, String smsCost, String smsType,
			String smsStyle, String smsArea) {
		super();
		this.taskid = taskid;
		this.smsMobile = smsMobile;
		this.smsTime = smsTime;
		this.smsCost = smsCost;
		this.smsType = smsType;
		this.smsStyle = smsStyle;
		this.smsArea = smsArea;
	}
	public TelecomShanxi1Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}