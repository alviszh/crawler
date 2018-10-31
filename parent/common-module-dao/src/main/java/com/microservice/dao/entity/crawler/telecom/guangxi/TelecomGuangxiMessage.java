package com.microservice.dao.entity.crawler.telecom.guangxi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGuangxi_message",indexes = {@Index(name = "index_telecomGuangxi_message_taskid", columnList = "taskid")}) 
public class TelecomGuangxiMessage extends IdEntity{

    private String nameType;//业务类型
	
	private String getType;//收发类型
	
	private String myNum;//本机号码
	
	private String hisNum;//对方号码
	
	private String startTime;//发送开始时间
	
	private String aboveMoney;//优惠前费用
	
	private String behindMoney;//优惠后费用
	
	private String useTime;//折算使用量
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomNanningMessage [nameType=" + nameType + ", getType=" + getType + ", myNum=" + myNum + ", hisNum="
				+ hisNum + ", startTime=" + startTime + ", aboveMoney=" + aboveMoney + ", behindMoney=" + behindMoney
				+ ", useTime=" + useTime + ", taskid=" + taskid + "]";
	}

	public String getNameType() {
		return nameType;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public String getGetType() {
		return getType;
	}

	public void setGetType(String getType) {
		this.getType = getType;
	}

	public String getMyNum() {
		return myNum;
	}

	public void setMyNum(String myNum) {
		this.myNum = myNum;
	}

	public String getHisNum() {
		return hisNum;
	}

	public void setHisNum(String hisNum) {
		this.hisNum = hisNum;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getAboveMoney() {
		return aboveMoney;
	}

	public void setAboveMoney(String aboveMoney) {
		this.aboveMoney = aboveMoney;
	}

	public String getBehindMoney() {
		return behindMoney;
	}

	public void setBehindMoney(String behindMoney) {
		this.behindMoney = behindMoney;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
