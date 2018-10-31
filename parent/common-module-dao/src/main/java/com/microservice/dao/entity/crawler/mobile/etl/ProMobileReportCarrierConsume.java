package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_carrier_consume",indexes = {@Index(name = "index_pro_mobile_report_carrier_consume_taskid", columnList = "taskId")})

public class ProMobileReportCarrierConsume extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String carrier;
	private String phonenum;
	private String city;
	private String callMonth;
	private String communicateCounts;
	private String dialCounts;
	private String dialDuration;
	private String calledCounts;
	private String calledDuration;
	private String messageCounts;
	private String voiceFee;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCallMonth() {
		return callMonth;
	}
	public void setCallMonth(String callMonth) {
		this.callMonth = callMonth;
	}
	public String getCommunicateCounts() {
		return communicateCounts;
	}
	public void setCommunicateCounts(String communicateCounts) {
		this.communicateCounts = communicateCounts;
	}
	public String getDialCounts() {
		return dialCounts;
	}
	public void setDialCounts(String dialCounts) {
		this.dialCounts = dialCounts;
	}
	public String getDialDuration() {
		return dialDuration;
	}
	public void setDialDuration(String dialDuration) {
		this.dialDuration = dialDuration;
	}
	public String getCalledCounts() {
		return calledCounts;
	}
	public void setCalledCounts(String calledCounts) {
		this.calledCounts = calledCounts;
	}
	public String getCalledDuration() {
		return calledDuration;
	}
	public void setCalledDuration(String calledDuration) {
		this.calledDuration = calledDuration;
	}
	public String getMessageCounts() {
		return messageCounts;
	}
	public void setMessageCounts(String messageCounts) {
		this.messageCounts = messageCounts;
	}
	public String getVoiceFee() {
		return voiceFee;
	}
	public void setVoiceFee(String voiceFee) {
		this.voiceFee = voiceFee;
	}
	@Override
	public String toString() {
		return "ProMobileReportCarrierConsume [taskId=" + taskId + ", carrier=" + carrier + ", phonenum=" + phonenum
				+ ", city=" + city + ", callMonth=" + callMonth + ", communicateCounts=" + communicateCounts
				+ ", dialCounts=" + dialCounts + ", dialDuration=" + dialDuration + ", calledCounts=" + calledCounts
				+ ", calledDuration=" + calledDuration + ", messageCounts=" + messageCounts + ", voiceFee=" + voiceFee
				+ "]";
	}
	
	
}
