package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 通话记录详单
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_callrecord" ,indexes = {@Index(name = "index_telecom_chongqing_callrecord_taskid", columnList = "taskid")})
public class TelecomChongqingCallRecord extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;

	
	/**
	 * 对方号码
	 */
	private String callMobile;
	
	/**
	 * 呼叫类型 
	 */
	private String callType;

	/**
	 * 起始时间 
	 */
	private String callTime;

	/**
	 * 通话时长（秒） 
	 */
	private String callTimeCost;
	
	/**
	 * 费用（元） 
	 */
	private String callFee;

	/**
	 *  通话类型
	 */
	private String callStyle;

	/**
	 * 使用地点
	 */
	private String callArea;
	
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

	public String getCallMobile() {
		return callMobile;
	}

	public void setCallMobile(String callMobile) {
		this.callMobile = callMobile;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getCallTimeCost() {
		return callTimeCost;
	}

	public void setCallTimeCost(String callTimeCost) {
		this.callTimeCost = callTimeCost;
	}

	public String getCallFee() {
		return callFee;
	}

	public void setCallFee(String callFee) {
		this.callFee = callFee;
	}

	public String getCallStyle() {
		return callStyle;
	}

	public void setCallStyle(String callStyle) {
		this.callStyle = callStyle;
	}

	public String getCallArea() {
		return callArea;
	}

	public void setCallArea(String callArea) {
		this.callArea = callArea;
	}

	public String getInpackage() {
		return inpackage;
	}

	public void setInpackage(String inpackage) {
		this.inpackage = inpackage;
	}

	@Override
	public String toString() {
		return "TelecomChongqingCallRecord [taskid=" + taskid + ", callMobile=" + callMobile + ", callType=" + callType
				+ ", callTime=" + callTime + ", callTimeCost=" + callTimeCost + ", callFee=" + callFee + ", callStyle="
				+ callStyle + ", callArea=" + callArea + ", inpackage=" + inpackage + "]";
	}

	public TelecomChongqingCallRecord(String taskid, String callMobile, String callType, String callTime,
			String callTimeCost, String callFee, String callStyle, String callArea, String inpackage) {
		super();
		this.taskid = taskid;
		this.callMobile = callMobile;
		this.callType = callType;
		this.callTime = callTime;
		this.callTimeCost = callTimeCost;
		this.callFee = callFee;
		this.callStyle = callStyle;
		this.callArea = callArea;
		this.inpackage = inpackage;
	}

	public TelecomChongqingCallRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}