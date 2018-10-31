package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 短信记录实体
 * @author sln
 * @date 2017年8月23日 下午3:11:31
 */
@Entity
@Table(name = "telecom_shanxi3_smsrecord",indexes = {@Index(name = "index_telecom_shanxi3_smsrecord_taskid", columnList = "taskid")})
public class TelecomShanxi3SMSRecord extends IdEntity implements Serializable{
	private static final long serialVersionUID = 3752282226317442145L;
	private String taskid;
//	通信类型
	private String communicationtype;
//	对方号码
	private String othernum;
//	发送时间
	private String sendtime;
//	总费用
	private String totalcost;
//	折算流量
	private String convertedflow;
//	漫游状态
	private String roamstatus;
//	费用合计
	private String totalexpenses;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCommunicationtype() {
		return communicationtype;
	}
	public void setCommunicationtype(String communicationtype) {
		this.communicationtype = communicationtype;
	}
	public String getOthernum() {
		return othernum;
	}
	public void setOthernum(String othernum) {
		this.othernum = othernum;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getTotalcost() {
		return totalcost;
	}
	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}
	public String getConvertedflow() {
		return convertedflow;
	}
	public void setConvertedflow(String convertedflow) {
		this.convertedflow = convertedflow;
	}
	public String getRoamstatus() {
		return roamstatus;
	}
	public void setRoamstatus(String roamstatus) {
		this.roamstatus = roamstatus;
	}
	public TelecomShanxi3SMSRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getTotalexpenses() {
		return totalexpenses;
	}
	public void setTotalexpenses(String totalexpenses) {
		this.totalexpenses = totalexpenses;
	}
	public TelecomShanxi3SMSRecord(String taskid, String communicationtype, String othernum, String sendtime,
			String totalcost, String convertedflow, String roamstatus, String totalexpenses) {
		super();
		this.taskid = taskid;
		this.communicationtype = communicationtype;
		this.othernum = othernum;
		this.sendtime = sendtime;
		this.totalcost = totalcost;
		this.convertedflow = convertedflow;
		this.roamstatus = roamstatus;
		this.totalexpenses = totalexpenses;
	}
}

