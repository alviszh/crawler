package com.microservice.dao.entity.crawler.telecom.guangdong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guangdong_smsthrem",indexes = {@Index(name = "index_telecom_guangdong_smsthrem_taskid", columnList = "taskid")})
public class TelecomGuangDongSMSThrem extends IdEntity{
	
	private String taskid;
	
	private Integer userid;

	private String usernumber;//用户号码
	
	private String dnumber;//对方号码
	
	private String smsdate;//通信日期
	
	private String smsmoney;//通信费
	
	private String smstype;//短信类型
	
	
	
	@Override
	public String toString() {
		return "TelecomGuangDongSMSThrem [taskid=" + taskid + ", userid=" + userid + ", usernumber=" + usernumber
				+ ", dnumber=" + dnumber + ", smsdate=" + smsdate + ", smsmoney=" + smsmoney + ", smstype=" + smstype
				+ "]";
	}

	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}

	public String getDnumber() {
		return dnumber;
	}

	public void setDnumber(String dnumber) {
		this.dnumber = dnumber;
	}

	public String getSmsdate() {
		return smsdate;
	}

	public void setSmsdate(String smsdate) {
		this.smsdate = smsdate;
	}

	public String getSmsmoney() {
		return smsmoney;
	}

	public void setSmsmoney(String smsmoney) {
		this.smsmoney = smsmoney;
	}

	public String getSmstype() {
		return smstype;
	}

	public void setSmstype(String smstype) {
		this.smstype = smstype;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
	
}
