package com.microservice.dao.entity.crawler.telecom.shanghai;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 上海电信-通话详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_shanghai_callresult")
public class TelecomShanghaiCallRec extends IdEntity{
	
	private String taskid;
	private String beginTime;				//开始时间
	private String callDuriation;			//通话时长
	private String callType;				//呼叫类型
	private String calledPartyVisitedCity;	//被呼叫人所在城市
	private String callingPartyVisitedCity;	//呼叫人所在城市
	private String chargedParty;			//呼叫人号码
	private String fee1;
	private String fee3;
	private String fee4;
	private String flowConversion;
	private String forwardingFlag;
	private String fowardingNumber;
	
	@Column(name="index_number")
	private String index;
	private String longDistanceType;		//长途类型
	private String prodId;
	private String productLineName;			
	private String targetParty;				//被叫号码
	private String temporaryCount;			//临时话费
	private String totalFee;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getCalledPartyVisitedCity() {
		return calledPartyVisitedCity;
	}
	public void setCalledPartyVisitedCity(String calledPartyVisitedCity) {
		this.calledPartyVisitedCity = calledPartyVisitedCity;
	}
	public String getCallingPartyVisitedCity() {
		return callingPartyVisitedCity;
	}
	public void setCallingPartyVisitedCity(String callingPartyVisitedCity) {
		this.callingPartyVisitedCity = callingPartyVisitedCity;
	}
	public String getChargedParty() {
		return chargedParty;
	}
	public void setChargedParty(String chargedParty) {
		this.chargedParty = chargedParty;
	}
	public String getFee1() {
		return fee1;
	}
	public void setFee1(String fee1) {
		this.fee1 = fee1;
	}
	public String getFee3() {
		return fee3;
	}
	public void setFee3(String fee3) {
		this.fee3 = fee3;
	}
	public String getFee4() {
		return fee4;
	}
	public void setFee4(String fee4) {
		this.fee4 = fee4;
	}
	public String getFlowConversion() {
		return flowConversion;
	}
	public void setFlowConversion(String flowConversion) {
		this.flowConversion = flowConversion;
	}
	public String getForwardingFlag() {
		return forwardingFlag;
	}
	public void setForwardingFlag(String forwardingFlag) {
		this.forwardingFlag = forwardingFlag;
	}
	public String getFowardingNumber() {
		return fowardingNumber;
	}
	public void setFowardingNumber(String fowardingNumber) {
		this.fowardingNumber = fowardingNumber;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getLongDistanceType() {
		return longDistanceType;
	}
	public void setLongDistanceType(String longDistanceType) {
		this.longDistanceType = longDistanceType;
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProductLineName() {
		return productLineName;
	}
	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}
	public String getTargetParty() {
		return targetParty;
	}
	public void setTargetParty(String targetParty) {
		this.targetParty = targetParty;
	}
	public String getTemporaryCount() {
		return temporaryCount;
	}
	public void setTemporaryCount(String temporaryCount) {
		this.temporaryCount = temporaryCount;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

}
