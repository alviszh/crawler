package com.microservice.dao.entity.crawler.telecom.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_beijing_callresult")
public class TelecomBeijingCallThremResult extends IdEntity {

	private String callid;

	private String calltype1;// 主叫或被叫

	private String calltype2;// 国内通话

	private String address;// 通话地点

	private String othernum;// 对方号码

	private String startdate;// 开始时间

	private String callcosts;// 通话费用

	private String othercosts;// 其他费用

	private String calltime;// 通话时长

	private String costs;// 总费用

	private Integer userid;

	private String taskid;

	public String getCallid() {
		return callid;
	}

	public void setCallid(String callid) {
		this.callid = callid;
	}

	public String getCalltype1() {
		return calltype1;
	}

	public void setCalltype1(String calltype1) {
		this.calltype1 = calltype1;
	}

	public String getCalltype2() {
		return calltype2;
	}

	public void setCalltype2(String calltype2) {
		this.calltype2 = calltype2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOthernum() {
		return othernum;
	}

	public void setOthernum(String othernum) {
		this.othernum = othernum;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getCallcosts() {
		return callcosts;
	}

	public void setCallcosts(String callcosts) {
		this.callcosts = callcosts;
	}

	public String getOthercosts() {
		return othercosts;
	}

	public void setOthercosts(String othercosts) {
		this.othercosts = othercosts;
	}

	public String getCalltime() {
		return calltime;
	}

	public void setCalltime(String calltime) {
		this.calltime = calltime;
	}

	public String getCosts() {
		return costs;
	}

	public void setCosts(String costs) {
		this.costs = costs;
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
		return "TelecomBeijingCallThremResult [callid=" + callid + ", calltype1=" + calltype1 + ", calltype2="
				+ calltype2 + ", address=" + address + ", othernum=" + othernum + ", startdate=" + startdate
				+ ", callcosts=" + callcosts + ", othercosts=" + othercosts + ", calltime=" + calltime + ", costs="
				+ costs + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

}