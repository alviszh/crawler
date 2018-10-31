package com.microservice.dao.entity.crawler.telecom.liaoning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "telecom_liaoning_smsresult",indexes = {@Index(name = "index_telecom_liaoning_smsresult_taskid", columnList = "taskid")})
public class TelecomLiaoNingSMSThremResult extends IdEntity{

	private String callPhone;//对方号码
	
	private String beginDate;//发送时间
	
	private String callType;//话单类型
	
	private String feeKind;//短信类型
	
	private String fee;//通信费
	
	private String total;//合计(元)
	
	private String infoFee;//信息费
	
	private String monthFee;//包月费

	private Integer userid;

	private String taskid;
	
	
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCallPhone() {
		return callPhone;
	}

	public void setCallPhone(String callPhone) {
		this.callPhone = callPhone;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getFeeKind() {
		return feeKind;
	}

	public void setFeeKind(String feeKind) {
		this.feeKind = feeKind;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getInfoFee() {
		return infoFee;
	}

	public void setInfoFee(String infoFee) {
		this.infoFee = infoFee;
	}

	public String getMonthFee() {
		return monthFee;
	}

	public void setMonthFee(String monthFee) {
		this.monthFee = monthFee;
	}

	@Override
	public String toString() {
		return "TelecomLiaoNingSMSThremResult [callPhone=" + callPhone + ", beginDate=" + beginDate + ", callType="
				+ callType + ", feeKind=" + feeKind + ", fee=" + fee + ", total=" + total + ", infoFee=" + infoFee
				+ ", monthFee=" + monthFee + "]";
	}
	
	
}
