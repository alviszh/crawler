package com.microservice.dao.entity.crawler.telecom.zhejiang;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 浙江电信-通话详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_zhejiang_callresult")
public class TelecomZhejiangCallRec extends IdEntity{
	
	private String taskid;
	private String otherNum;				//对方号码
	private String callType;				//呼叫类型
	private String beginTime;				//开始时间
	private String callDuriation;			//通话时长
	private String calledPartyVisitedCity;	//被呼叫人所在城市
	private String callType1;				//通话类型
	private String callFee;					//通话费
	private String remission;				//减免
	private String totalFee;				//费用小计
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getOtherNum() {
		return otherNum;
	}
	public void setOtherNum(String otherNum) {
		this.otherNum = otherNum;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getCallDuriation() {
		return callDuriation;
	}
	public void setCallDuriation(String callDuriation) {
		this.callDuriation = callDuriation;
	}
	public String getCalledPartyVisitedCity() {
		return calledPartyVisitedCity;
	}
	public void setCalledPartyVisitedCity(String calledPartyVisitedCity) {
		this.calledPartyVisitedCity = calledPartyVisitedCity;
	}
	public String getCallType1() {
		return callType1;
	}
	public void setCallType1(String callType1) {
		this.callType1 = callType1;
	}
	public String getCallFee() {
		return callFee;
	}
	public void setCallFee(String callFee) {
		this.callFee = callFee;
	}
	public String getRemission() {
		return remission;
	}
	public void setRemission(String remission) {
		this.remission = remission;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

}
