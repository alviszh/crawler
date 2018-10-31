package com.microservice.dao.entity.crawler.telecom.guangxi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;



//漫游通话

@Entity
@Table(name="telecomGuangxi_call",indexes = {@Index(name = "index_telecomGuangxi_call_taskid", columnList = "taskid")}) 
public class TelecomGuangxiCall extends IdEntity{

    private String startTime;//起始时间
	
	private String addr;//通话地点
	
	private String callType;//呼叫类型
	
	private String hisNum;//对方号码
	
	private String callTime;//通话时长
	
	private String callStatus;//通话类型
	
	private String money;//费用
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomGuangxiCall [startTime=" + startTime + ", addr=" + addr + ", callType=" + callType + ", hisNum="
				+ hisNum + ", callTime=" + callTime + ", callStatus=" + callStatus + ", money=" + money + ", taskid="
				+ taskid + "]";
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getHisNum() {
		return hisNum;
	}

	public void setHisNum(String hisNum) {
		this.hisNum = hisNum;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
}
