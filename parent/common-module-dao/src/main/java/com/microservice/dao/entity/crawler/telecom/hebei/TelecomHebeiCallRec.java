package com.microservice.dao.entity.crawler.telecom.hebei;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 河北电信-通话详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_hebei_callresult")
public class TelecomHebeiCallRec extends IdEntity{
	
	private String taskid;
	private String month;						//月份
	private String otherNum;					//对方号码
	private String callType;					//呼叫类型
	private String callTime;					//呼叫时间
	private String callDuration;				//通话时长
	private String reducedUse;					//折算使用量
	private String basicFee;					//基本费（元）
	private String longDistanceFee;				//长途费（元）
	private String discountsFee;				//优惠费（元）
	private String otherFee;					//其他费用（元）
	private String feeType;						//费用类型
	private String total;						//合计（元）
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
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
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getReducedUse() {
		return reducedUse;
	}
	public void setReducedUse(String reducedUse) {
		this.reducedUse = reducedUse;
	}
	public String getBasicFee() {
		return basicFee;
	}
	public void setBasicFee(String basicFee) {
		this.basicFee = basicFee;
	}
	public String getLongDistanceFee() {
		return longDistanceFee;
	}
	public void setLongDistanceFee(String longDistanceFee) {
		this.longDistanceFee = longDistanceFee;
	}
	public String getDiscountsFee() {
		return discountsFee;
	}
	public void setDiscountsFee(String discountsFee) {
		this.discountsFee = discountsFee;
	}
	public String getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
	@Override
	public String toString() {
		return "TelecomHebeiCallRec [taskid=" + taskid + ", otherNum=" + otherNum + ", callType=" + callType
				+ ", callTime=" + callTime + ", callDuration=" + callDuration + ", reducedUse=" + reducedUse
				+ ", basicFee=" + basicFee + ", longDistanceFee=" + longDistanceFee + ", discountsFee=" + discountsFee
				+ ", otherFee=" + otherFee + ", feeType=" + feeType + ", total=" + total + "]";
	}
	

}
