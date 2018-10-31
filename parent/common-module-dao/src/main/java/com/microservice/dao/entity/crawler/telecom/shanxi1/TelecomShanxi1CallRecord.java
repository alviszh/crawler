package com.microservice.dao.entity.crawler.telecom.shanxi1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西电信通话记录详单
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_shanxi1_callrecord" ,indexes = {@Index(name = "index_telecom_shanxi1_callrecord_taskid", columnList = "taskid")})
public class TelecomShanxi1CallRecord extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;

	
	/**
	 * 通话类型
	 * callType：1	被叫
	 * callType：0	主叫
	 */
	private String callType;

	/**
	 * 通话手机号码
	 */
	private String callMobile;

	/**
	 * 通话时间
	 */
	private String callTime;

	/**
	 * 通话时长
	 */
	private String callTimeCost;

	/**
	 *  通话种类
	 *  callStyle 0	本地
	 *  callStyle 1	长途
	 *  callStyle 2	漫游
	 */
	private String callStyle;

	/**
	 * 使用地市
	 */
	private String callArea;
	
	/**
	 * 通话费用
	 */
	private String callFee;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCallMobile() {
		return callMobile;
	}

	public void setCallMobile(String callMobile) {
		this.callMobile = callMobile;
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

	public String getCallFee() {
		return callFee;
	}

	public void setCallFee(String callFee) {
		this.callFee = callFee;
	}

	@Override
	public String toString() {
		return "TelecomShanxi1Record [taskid=" + taskid + ", callType=" + callType + ", callMobile=" + callMobile
				+ ", callTime=" + callTime + ", callTimeCost=" + callTimeCost + ", callStyle=" + callStyle
				+ ", callArea=" + callArea + ", callFee=" + callFee + "]";
	}

	public TelecomShanxi1CallRecord(String taskid, String callType, String callMobile, String callTime, String callTimeCost,
			String callStyle, String callArea, String callFee) {
		super();
		this.taskid = taskid;
		this.callType = callType;
		this.callMobile = callMobile;
		this.callTime = callTime;
		this.callTimeCost = callTimeCost;
		this.callStyle = callStyle;
		this.callArea = callArea;
		this.callFee = callFee;
	}

	public TelecomShanxi1CallRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}