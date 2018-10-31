package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  缴费信息实体
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_chargeinfo",indexes = {@Index(name = "index_telecom_jiangxi_chargeinfo_taskid", columnList = "taskid")})
public class TelecomJiangxiChargeInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -3373657383007297645L;
//	查询月份
	private String qrymonth;
//	付费方式
	private String chargeway;
//	付费时间
	private String chargetime;
//	付费金额(分)
	private String chargemoney;
//	付费地点
	private String chargeaddr;
	private String taskid;
	public String getQrymonth() {
		return qrymonth;
	}
	public void setQrymonth(String qrymonth) {
		this.qrymonth = qrymonth;
	}
	public String getChargeway() {
		return chargeway;
	}
	public void setChargeway(String chargeway) {
		this.chargeway = chargeway;
	}
	public String getChargetime() {
		return chargetime;
	}
	public void setChargetime(String chargetime) {
		this.chargetime = chargetime;
	}
	public String getChargemoney() {
		return chargemoney;
	}
	public void setChargemoney(String chargemoney) {
		this.chargemoney = chargemoney;
	}
	public String getChargeaddr() {
		return chargeaddr;
	}
	public void setChargeaddr(String chargeaddr) {
		this.chargeaddr = chargeaddr;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public TelecomJiangxiChargeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomJiangxiChargeInfo(String qrymonth, String chargeway, String chargetime, String chargemoney,
			String chargeaddr, String taskid) {
		super();
		this.qrymonth = qrymonth;
		this.chargeway = chargeway;
		this.chargetime = chargetime;
		this.chargemoney = chargemoney;
		this.chargeaddr = chargeaddr;
		this.taskid = taskid;
	}
}

