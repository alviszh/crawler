package com.microservice.dao.entity.crawler.telecom.ningxia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 宁夏电信通话记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_ningxia_callhistory",indexes = {@Index(name = "index_telecom_ningxia_callhistory_taskid", columnList = "taskid")})
public class TelecomNingxiaCallHistory  extends IdEntity{
	@Column(name="userid")
	private Integer userid;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
    ///////////////////////////////////////订单查询组//////////////////////////////////
	
	/**通信类型*/
	@Column(name="correspondence_type")
	private String correspondencetype;
	
	
	/**通话时长*/
	@Column(name="communicate_time")
	private String communicatetime;
	
	/**通话时间*/
	@Column(name="communicate_time2")
	private String communicatetime2;
	
	/**通话地点*/
	@Column(name="communicate_addr")
	private String communicateaddr;
	
	/**对方号码*/
	@Column(name="opposite_phone")
	private String oppositephone;
	
	/**呼叫类型*/
	@Column(name="call_type")
	private String calltype;
	
	/**通话周期*/
	@Column(name="call_period")
	private String callperiod;
	
	/**费用合计（月）*/
	@Column(name="cost_total")
	private String costtotal;
	
	/**本地费或漫游费*/
	@Column(name="locality_roam_cost")
	private String localityroamcost;
	
	/**长途费*/
	@Column(name="long_cost")
	private String longcost;
	
	/**费用小计（次）*/
	@Column(name="cost_subtotal")
	private String costsubtotal;
	
	/**港澳台长途*/
	@Column(name="g_a_t_long")
	private String gatlong;
	
	/**港澳台漫游*/
	@Column(name="g_a_t_roam")
	private String gatroam;

	public String getCallperiod() {
		return callperiod;
	}

	public void setCallperiod(String callperiod) {
		this.callperiod = callperiod;
	}

	public String getCosttotal() {
		return costtotal;
	}

	public void setCosttotal(String costtotal) {
		this.costtotal = costtotal;
	}

	public String getLocalityroamcost() {
		return localityroamcost;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public void setLocalityroamcost(String localityroamcost) {
		this.localityroamcost = localityroamcost;
	}

	public String getLongcost() {
		return longcost;
	}

	public void setLongcost(String longcost) {
		this.longcost = longcost;
	}

	public String getCostsubtotal() {
		return costsubtotal;
	}

	public void setCostsubtotal(String costsubtotal) {
		this.costsubtotal = costsubtotal;
	}

	public String getGatlong() {
		return gatlong;
	}

	public void setGatlong(String gatlong) {
		this.gatlong = gatlong;
	}

	public String getGatroam() {
		return gatroam;
	}

	public void setGatroam(String gatroam) {
		this.gatroam = gatroam;
	}

	public String getCorrespondencetype() {
		return correspondencetype;
	}

	public void setCorrespondencetype(String correspondencetype) {
		this.correspondencetype = correspondencetype;
	}

	public String getCommunicatetime() {
		return communicatetime;
	}

	public void setCommunicatetime(String communicatetime) {
		this.communicatetime = communicatetime;
	}

	public String getCommunicatetime2() {
		return communicatetime2;
	}

	public void setCommunicatetime2(String communicatetime2) {
		this.communicatetime2 = communicatetime2;
	}

	public String getCommunicateaddr() {
		return communicateaddr;
	}

	public void setCommunicateaddr(String communicateaddr) {
		this.communicateaddr = communicateaddr;
	}

	public String getOppositephone() {
		return oppositephone;
	}

	public void setOppositephone(String oppositephone) {
		this.oppositephone = oppositephone;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
