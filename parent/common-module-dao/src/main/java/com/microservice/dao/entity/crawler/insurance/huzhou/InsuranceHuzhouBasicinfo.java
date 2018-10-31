package com.microservice.dao.entity.crawler.insurance.huzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;


/**
 * 湖州社保首次参保
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_huzhou_basicinfo",indexes = {@Index(name = "index_insurance_huzhou_basicinfo_taskid", columnList = "taskid")})
public class InsuranceHuzhouBasicinfo extends IdEntity {
	private String num; //序号
	private String planarea; //统筹区
	
	private String type; //险种信息
	private String state; //参保情况
	private String paystate; //缴费情况
	private String payinfo; //缴费档次
	private String paybase; //缴费基数
	private String startdate; //本次参保开始时间
	private String taskid;
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPlanarea() {
		return planarea;
	}
	public void setPlanarea(String planarea) {
		this.planarea = planarea;
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
	public String getPaystate() {
		return paystate;
	}
	public void setPaystate(String paystate) {
		this.paystate = paystate;
	}
	public String getPayinfo() {
		return payinfo;
	}
	public void setPayinfo(String payinfo) {
		this.payinfo = payinfo;
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
	public InsuranceHuzhouBasicinfo() {
		super();
	}
	public InsuranceHuzhouBasicinfo(String num, String planarea, String type, String state, String paystate,
			String payinfo, String paybase, String startdate, String taskid) {
		super();
		this.num = num;
		this.planarea = planarea;
		this.type = type;
		this.state = state;
		this.paystate = paystate;
		this.payinfo = payinfo;
		this.paybase = paybase;
		this.startdate = startdate;
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceHuzhouBasicinfo [num=" + num + ", planarea=" + planarea + ", type=" + type + ", state=" + state
				+ ", paystate=" + paystate + ", payinfo=" + payinfo + ", paybase=" + paybase + ", startdate="
				+ startdate + ", taskid=" + taskid + "]";
	}
}
