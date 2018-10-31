package com.microservice.dao.entity.crawler.telecom.hainan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hainan_callresult")
public class TelecomHaiNanCallThremResult extends IdEntity {

	private String xuhao;
	
	private String type;//呼叫类型
	
	private String calltype;// 通话类型
	
	private String calllocation;// 通话地点

	private String callphoneother;// 对方号码
	
	private String date;// 通话开始时间

	private String calltimeminute;// 通话时长（分）
	
	private String calltimesecond;// 通话时长（秒）
	
	private String callcosts;//费用（元）
		
	private Integer userid;

	private String taskid;
		
	public String getXuhao() {
		return xuhao;
	}

	public void setXuhao(String xuhao) {
		this.xuhao = xuhao;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getCalllocation() {
		return calllocation;
	}

	public void setCalllocation(String calllocation) {
		this.calllocation = calllocation;
	}

	public String getCallphoneother() {
		return callphoneother;
	}

	public void setCallphoneother(String callphoneother) {
		this.callphoneother = callphoneother;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCalltimeminute() {
		return calltimeminute;
	}

	public void setCalltimeminute(String calltimeminute) {
		this.calltimeminute = calltimeminute;
	}

	public String getCalltimesecond() {
		return calltimesecond;
	}

	public void setCalltimesecond(String calltimesecond) {
		this.calltimesecond = calltimesecond;
	}

	public String getCallcosts() {
		return callcosts;
	}

	public void setCallcosts(String callcosts) {
		this.callcosts = callcosts;
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
		return "TelecomHaiNanCallThremResult [xuhao=" + xuhao + ", type=" + type + ", calltype=" + calltype
				+ ", calllocation=" + calllocation + ", callphoneother=" + callphoneother + ", date=" + date
				+ ", calltimeminute=" + calltimeminute + ", calltimesecond=" + calltimesecond + ", callcosts="
				+ callcosts + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

	
}