package com.microservice.dao.entity.crawler.telecom.hainan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hainan_smsresult")
public class TelecomHaiNanSMSThremResult extends IdEntity {

	private String xuhao;//序号
	
	private String businesstype;// 业务类型

	private String smstype;// 收发类型

	private String smsothercall;// 对方号码
	
	private String date;// 发送时间
		
	private String smscost;// 费用（元

	private Integer userid;

	private String taskid;

	public String getXuhao() {
		return xuhao;
	}

	public void setXuhao(String xuhao) {
		this.xuhao = xuhao;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getSmstype() {
		return smstype;
	}

	public void setSmstype(String smstype) {
		this.smstype = smstype;
	}

	public String getSmsothercall() {
		return smsothercall;
	}

	public void setSmsothercall(String smsothercall) {
		this.smsothercall = smsothercall;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSmscost() {
		return smscost;
	}

	public void setSmscost(String smscost) {
		this.smscost = smscost;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomHaiNanSMSThremResult [xuhao=" + xuhao + ", businesstype=" + businesstype + ", smstype=" + smstype
				+ ", smsothercall=" + smsothercall + ", date=" + date + ", smscost=" + smscost + ", userid=" + userid
				+ ", taskid=" + taskid + "]";
	}	

}