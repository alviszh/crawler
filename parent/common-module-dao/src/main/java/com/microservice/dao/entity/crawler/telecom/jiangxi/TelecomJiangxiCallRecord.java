package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 通话记录实体
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_callrecord",indexes = {@Index(name = "index_telecom_jiangxi_callrecord_taskid", columnList = "taskid")})
public class TelecomJiangxiCallRecord extends IdEntity implements Serializable {
	private static final long serialVersionUID = -5949165298033507092L;
	private String taskid;
//	序号 
//	private String sortnum;
//	呼叫类型
	private String calltype;
//	通话类型
	private String contactype;
//	通话地点
	private String contactaddr;
//	对方号码
	private String othernum;
//	通话开始时间
	private String callstartime;
//	基本通话费（元）
	private String basicallcost;
//	长途通话费（元）
	private String longwaycost;
//	通话时长
	private String contactime;
//	费用小计
	private String totalcharge;
//	查询月份
	private String qrymonth;
	
	public String getQrymonth() {
		return qrymonth;
	}
	public void setQrymonth(String qrymonth) {
		this.qrymonth = qrymonth;
	}
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
	public String getContactype() {
		return contactype;
	}
	public void setContactype(String contactype) {
		this.contactype = contactype;
	}
	public String getContactaddr() {
		return contactaddr;
	}
	public void setContactaddr(String contactaddr) {
		this.contactaddr = contactaddr;
	}
	public String getOthernum() {
		return othernum;
	}
	public void setOthernum(String othernum) {
		this.othernum = othernum;
	}
	public String getCallstartime() {
		return callstartime;
	}
	public void setCallstartime(String callstartime) {
		this.callstartime = callstartime;
	}
	public String getBasicallcost() {
		return basicallcost;
	}
	public void setBasicallcost(String basicallcost) {
		this.basicallcost = basicallcost;
	}
	public String getLongwaycost() {
		return longwaycost;
	}
	public void setLongwaycost(String longwaycost) {
		this.longwaycost = longwaycost;
	}
	public String getContactime() {
		return contactime;
	}
	public void setContactime(String contactime) {
		this.contactime = contactime;
	}
	public String getTotalcharge() {
		return totalcharge;
	}
	public void setTotalcharge(String totalcharge) {
		this.totalcharge = totalcharge;
	}
	public TelecomJiangxiCallRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomJiangxiCallRecord(String taskid, String calltype, String contactype, String contactaddr,
			String othernum, String callstartime, String basicallcost, String longwaycost, String contactime,
			String totalcharge, String qrymonth) {
		super();
		this.taskid = taskid;
		this.calltype = calltype;
		this.contactype = contactype;
		this.contactaddr = contactaddr;
		this.othernum = othernum;
		this.callstartime = callstartime;
		this.basicallcost = basicallcost;
		this.longwaycost = longwaycost;
		this.contactime = contactime;
		this.totalcharge = totalcharge;
		this.qrymonth = qrymonth;
	}
	
}

