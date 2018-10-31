package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 短信记录实体
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_smsrecord",indexes = {@Index(name = "index_telecom_tianjin_smsrecord_taskid", columnList = "taskid")})
public class TelecomTianjinSMSRecord extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5609713822061866934L;
	private String taskid;
//	查询开始时间
	private String startdate;
//	查询结束时间
	private String enddate;
//	发送号码
	private String sendnum;
//	接收号码
	private String getnum;
//	发送时间
	private String sendtime;
//	费用
	private String totalcost;
//	优惠费用
	private String discount;
	public TelecomTianjinSMSRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
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
	public String getSendnum() {
		return sendnum;
	}
	public void setSendnum(String sendnum) {
		this.sendnum = sendnum;
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
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public TelecomTianjinSMSRecord(String taskid, String startdate, String enddate, String sendnum, String getnum,
			String sendtime, String totalcost, String discount) {
		super();
		this.taskid = taskid;
		this.startdate = startdate;
		this.enddate = enddate;
		this.sendnum = sendnum;
		this.getnum = getnum;
		this.sendtime = sendtime;
		this.totalcost = totalcost;
		this.discount = discount;
	}
	
}

