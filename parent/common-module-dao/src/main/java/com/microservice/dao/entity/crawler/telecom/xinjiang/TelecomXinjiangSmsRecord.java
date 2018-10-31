package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_smsrecord" ,indexes = {@Index(name = "index_telecom_xinjiang_smsrecord_taskid", columnList = "taskid")})
public class TelecomXinjiangSmsRecord extends IdEntity {

	private String smsType;//通讯类型
	private String smsNumber;//对方号码
	private String sendTime;//发送时间
	private String longStatus;//漫游状态
	private String totalFee;//总费用
	private String dataReduce;//流量抵减
	private String taskid;

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public String getSmsNumber() {
		return smsNumber;
	}

	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getLongStatus() {
		return longStatus;
	}

	public void setLongStatus(String longStatus) {
		this.longStatus = longStatus;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getDataReduce() {
		return dataReduce;
	}

	public void setDataReduce(String dataReduce) {
		this.dataReduce = dataReduce;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomXinjiangSmsRecord [smsType=" + smsType + ", smsNumber=" + smsNumber + ", sendTime=" + sendTime
				+ ", longStatus=" + longStatus + ", totalFee=" + totalFee + ", dataReduce=" + dataReduce + ", taskid="
				+ taskid + "]";
	}
}