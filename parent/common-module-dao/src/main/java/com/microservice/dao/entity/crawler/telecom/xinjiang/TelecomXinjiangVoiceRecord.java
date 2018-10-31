package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_voicerecord" ,indexes = {@Index(name = "index_telecom_xinjiang_voicerecord_taskid", columnList = "taskid")})
public class TelecomXinjiangVoiceRecord extends IdEntity {

	private String startTime;//起始时间
	private String talkTime;//通话时长（秒）
	private String dataReduce;//流量抵减（MB）
	private String type;//呼叫类型
	private String calledNumber;//被叫号码
	private String callPalce;//通话地点
	private String callType;//通话类型
	private String basicFee;//基本费或漫游费
	private String longFee;//长途费
	private String otherFee;//其他费
	private String benefit;//优惠减免
	private String total;//小计
	private String taskid;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getTalkTime() {
		return talkTime;
	}
	public void setTalkTime(String talkTime) {
		this.talkTime = talkTime;
	}
	public String getDataReduce() {
		return dataReduce;
	}
	public void setDataReduce(String dataReduce) {
		this.dataReduce = dataReduce;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCalledNumber() {
		return calledNumber;
	}
	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}
	public String getCallPalce() {
		return callPalce;
	}
	public void setCallPalce(String callPalce) {
		this.callPalce = callPalce;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getBasicFee() {
		return basicFee;
	}
	public void setBasicFee(String basicFee) {
		this.basicFee = basicFee;
	}
	public String getLongFee() {
		return longFee;
	}
	public void setLongFee(String longFee) {
		this.longFee = longFee;
	}
	public String getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}
	public String getBenefit() {
		return benefit;
	}
	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "TelecomXinjiangVoiceRecord [startTime=" + startTime + ", talkTime=" + talkTime + ", dataReduce="
				+ dataReduce + ", type=" + type + ", calledNumber=" + calledNumber + ", callPalce=" + callPalce
				+ ", callType=" + callType + ", basicFee=" + basicFee + ", longFee=" + longFee + ", otherFee="
				+ otherFee + ", benefit=" + benefit + ", taskid=" + taskid + "]";
	}
}