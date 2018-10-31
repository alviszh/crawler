package com.microservice.dao.entity.crawler.insurance.ganzhou;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 赣州基本保险情况
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_ganzhou_basicinfo")
public class InsuranceGanZhouBasicinfo extends IdEntity {

	private String useraccount;	//个人编号
	private String type;	//险种类型
	private String state;	//参保状态
	private String paytype;	//缴费类型
	private String paybase;	//缴费基数
	private String startdate;	//参保日期
	private String taskid;
	
	public String getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getPaybase() {
		return paybase;
	}

	public void setPaybase(String paybase) {
		this.paybase = paybase;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceGanZhouBasicinfo [useraccount=" + useraccount + ", type=" + type + ", state=" + state
				+ ", paytype=" + paytype + ", paybase=" + paybase + ", startdate=" + startdate + ", taskid=" + taskid
				+ "]";
	}

	public InsuranceGanZhouBasicinfo(String useraccount, String type, String state, String paytype, String paybase,
			String startdate, String taskid) {
		super();
		this.useraccount = useraccount;
		this.type = type;
		this.state = state;
		this.paytype = paytype;
		this.paybase = paybase;
		this.startdate = startdate;
		this.taskid = taskid;
	}
	
}
