package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  缴费信息实体
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_chargeinfo",indexes = {@Index(name = "index_telecom_tianjin_chargeinfo_taskid", columnList = "taskid")})
public class TelecomTianjinChargeInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6423807456237177837L;
//	查询开始时间
	private String startdate;
//	查询结束时间
	private String enddate;
//	流水号
	private String flownum;
//	交费时间
	private String chargetime;
//	金额
	private String chargemoney;
//	交费类型
	private String chargetype;
	private String taskid;
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getFlownum() {
		return flownum;
	}
	public void setFlownum(String flownum) {
		this.flownum = flownum;
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
	public String getChargetype() {
		return chargetype;
	}
	public void setChargetype(String chargetype) {
		this.chargetype = chargetype;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public TelecomTianjinChargeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomTianjinChargeInfo(String startdate, String enddate, String flownum, String chargetime,
			String chargemoney, String chargetype, String taskid) {
		super();
		this.startdate = startdate;
		this.enddate = enddate;
		this.flownum = flownum;
		this.chargetime = chargetime;
		this.chargemoney = chargemoney;
		this.chargetype = chargetype;
		this.taskid = taskid;
	}
	
}

