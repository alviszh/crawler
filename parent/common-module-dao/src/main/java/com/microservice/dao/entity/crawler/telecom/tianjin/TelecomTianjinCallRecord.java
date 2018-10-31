package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 通话记录实体
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_callrecord",indexes = {@Index(name = "index_telecom_tianjin_callrecord_taskid", columnList = "taskid")})
public class TelecomTianjinCallRecord extends IdEntity implements Serializable {
	private static final long serialVersionUID = 7414345547806809649L;
	private String taskid;
//	查询开始时间
	private String startdate;
//	查询结束时间
	private String enddate;
//	主叫号码
	private String callingnum;
//	被叫号码
	private String callednum;
//	通话类型
	private String linktype;
//	通话地点
	private String calladdress;
//	起始时间
	private String starttime;
//	通话时长(秒)
	private String costtime;
//	费用（元）
	private String totalcharge;
//	优惠费用
	private String  discount;
	public TelecomTianjinCallRecord() {
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
	public String getCallingnum() {
		return callingnum;
	}
	public void setCallingnum(String callingnum) {
		this.callingnum = callingnum;
	}
	public String getCallednum() {
		return callednum;
	}
	public void setCallednum(String callednum) {
		this.callednum = callednum;
	}
	public String getLinktype() {
		return linktype;
	}
	public void setLinktype(String linktype) {
		this.linktype = linktype;
	}
	public String getCalladdress() {
		return calladdress;
	}
	public void setCalladdress(String calladdress) {
		this.calladdress = calladdress;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getCosttime() {
		return costtime;
	}
	public void setCosttime(String costtime) {
		this.costtime = costtime;
	}
	public String getTotalcharge() {
		return totalcharge;
	}
	public void setTotalcharge(String totalcharge) {
		this.totalcharge = totalcharge;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public TelecomTianjinCallRecord(String taskid, String startdate, String enddate, String callingnum,
			String callednum, String linktype, String calladdress, String starttime, String costtime,
			String totalcharge, String discount) {
		super();
		this.taskid = taskid;
		this.startdate = startdate;
		this.enddate = enddate;
		this.callingnum = callingnum;
		this.callednum = callednum;
		this.linktype = linktype;
		this.calladdress = calladdress;
		this.starttime = starttime;
		this.costtime = costtime;
		this.totalcharge = totalcharge;
		this.discount = discount;
	}
}

