package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 通话记录实体
 * @author sln
 * @date 2017年8月23日 下午3:25:58
 */
@Entity
@Table(name = "telecom_shanxi3_callrecord",indexes = {@Index(name = "index_telecom_shanxi3_callrecord_taskid", columnList = "taskid")})
public class TelecomShanxi3CallRecord extends IdEntity implements Serializable {
	private static final long serialVersionUID = -1658246245465624566L;
	private String taskid;
//	序号
	private String sortnum;
//	呼叫类型
	private String calltype;
//	对方号码
	private String othernum;
//	通话时长
	private String costtime;
//	折算流量
	private String convertedflow;
//	起始时间
	private String starttime;
//	通话地点
	private String calladdress;
//	费用（元）
	private String totalcharge;
//	通话类型
	private String linktype;
	//总费用
	private String  totalexpenses;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCalltype() {
		return calltype;
	}
	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}
	public String getOthernum() {
		return othernum;
	}
	public void setOthernum(String othernum) {
		this.othernum = othernum;
	}
	public String getCosttime() {
		return costtime;
	}
	public void setCosttime(String costtime) {
		this.costtime = costtime;
	}
	public String getConvertedflow() {
		return convertedflow;
	}
	public void setConvertedflow(String convertedflow) {
		this.convertedflow = convertedflow;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getCalladdress() {
		return calladdress;
	}
	public void setCalladdress(String calladdress) {
		this.calladdress = calladdress;
	}
	public String getTotalcharge() {
		return totalcharge;
	}
	public void setTotalcharge(String totalcharge) {
		this.totalcharge = totalcharge;
	}
	public TelecomShanxi3CallRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getSortnum() {
		return sortnum;
	}
	public void setSortnum(String sortnum) {
		this.sortnum = sortnum;
	}
	public String getLinktype() {
		return linktype;
	}
	public void setLinktype(String linktype) {
		this.linktype = linktype;
	}
	public String getTotalexpenses() {
		return totalexpenses;
	}
	public void setTotalexpenses(String totalexpenses) {
		this.totalexpenses = totalexpenses;
	}
	public TelecomShanxi3CallRecord(String taskid, String sortnum, String calltype, String othernum, String costtime,
			String convertedflow, String starttime, String calladdress, String totalcharge, String linktype,
			String totalexpenses) {
		super();
		this.taskid = taskid;
		this.sortnum = sortnum;
		this.calltype = calltype;
		this.othernum = othernum;
		this.costtime = costtime;
		this.convertedflow = convertedflow;
		this.starttime = starttime;
		this.calladdress = calladdress;
		this.totalcharge = totalcharge;
		this.linktype = linktype;
		this.totalexpenses = totalexpenses;
	}
}

