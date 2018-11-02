package com.microservice.dao.entity.crawler.telecom.shanghai;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 上海电信-短信详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_shanghai_msg")
public class TelecomShanghaiMsg extends IdEntity{
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TelecomShanghaiMsg [taskid=" + taskid + ", beginTime=" + beginTime + ", callType=" + callType
				+ ", chargedParty=" + chargedParty + ", fee1=" + fee1 + ", flowConversion=" + flowConversion
				+ ", index=" + index + ", prodId=" + prodId + ", productLineName=" + productLineName + ", targetParty="
				+ targetParty + ", temporaryCount=" + temporaryCount + "]";
	}
	private String taskid;
	private String beginTime;				//发送时间
	private String callType;				//通信类型
	private String chargedParty;			//发送人号码
	private String fee1;
	private String flowConversion;
	
	@Column(name="index_number")
	private String index;
	
	private String prodId;
	private String productLineName;	
	private String targetParty;				//被叫号码
	private String temporaryCount;			//临时话费
	
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
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
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
	public String getFlowConversion() {
		return flowConversion;
	}
	public void setFlowConversion(String flowConversion) {
		this.flowConversion = flowConversion;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
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

}
