package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 短信记录实体
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_smsrecord",indexes = {@Index(name = "index_telecom_jiangxi_smsrecord_taskid", columnList = "taskid")})
public class TelecomJiangxiSMSRecord extends IdEntity implements Serializable{
	private static final long serialVersionUID = 8956363924001547580L;
	private String taskid;
//	接收号码
	private String getnum;
//	发送时间
	private String sendtime;
//	总费用（元）
	private String totalcost;
//	查询月份
	private String qrymonth;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getGetnum() {
		return getnum;
	}
	public void setGetnum(String getnum) {
		this.getnum = getnum;
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
	public String getQrymonth() {
		return qrymonth;
	}
	public void setQrymonth(String qrymonth) {
		this.qrymonth = qrymonth;
	}
	
	public TelecomJiangxiSMSRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomJiangxiSMSRecord(String taskid, String getnum, String sendtime, String totalcost, String qrymonth) {
		super();
		this.taskid = taskid;
		this.getnum = getnum;
		this.sendtime = sendtime;
		this.totalcost = totalcost;
		this.qrymonth = qrymonth;
	}
	
}

