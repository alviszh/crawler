package com.microservice.dao.entity.crawler.telecom.guangdong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guangdong_callthrem",indexes = {@Index(name = "index_telecom_guangdong_callthrem_taskid", columnList = "taskid")})
public class TelecomGuangDongCallThremResult extends IdEntity{

	private String taskid;
	
	private Integer userid;
	
	private String calltype;//呼叫类型
	
	private String dialnumber;//对方号码
	
	private String calldate;//通话日期
	
	private String duration;//通话时长
	
	private String callmoney;//通话费用
	
	private String datecalltype;//通话类型
	
	private String callland;//主叫通话地

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

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getDialnumber() {
		return dialnumber;
	}

	public void setDialnumber(String dialnumber) {
		this.dialnumber = dialnumber;
	}

	public String getCalldate() {
		return calldate;
	}

	public void setCalldate(String calldate) {
		this.calldate = calldate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCallmoney() {
		return callmoney;
	}

	public void setCallmoney(String callmoney) {
		this.callmoney = callmoney;
	}

	public String getDatecalltype() {
		return datecalltype;
	}

	public void setDatecalltype(String datecalltype) {
		this.datecalltype = datecalltype;
	}

	public String getCallland() {
		return callland;
	}

	public void setCallland(String callland) {
		this.callland = callland;
	}

	@Override
	public String toString() {
		return "TelecomGuangDongCallThremResult [taskid=" + taskid + ", userid=" + userid + ", calltype=" + calltype
				+ ", dialnumber=" + dialnumber + ", calldate=" + calldate + ", duration=" + duration + ", callmoney="
				+ callmoney + ", datecalltype=" + datecalltype + ", callland=" + callland + "]";
	}
	
	
}
