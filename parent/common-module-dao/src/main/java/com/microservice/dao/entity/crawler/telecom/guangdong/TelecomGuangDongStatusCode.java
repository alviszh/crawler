package com.microservice.dao.entity.crawler.telecom.guangdong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guangdong_statuscode",indexes = {@Index(name = "index_telecom_guangdong_statuscode_taskid", columnList = "taskid")})
public class TelecomGuangDongStatusCode extends IdEntity{

	private String taskid;
	
	private Integer userid;
	
	private String  month;
	
	private String cdate;
	
	private String xin;
	
	private String dang;

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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getXin() {
		return xin;
	}

	public void setXin(String xin) {
		this.xin = xin;
	}

	public String getDang() {
		return dang;
	}

	public void setDang(String dang) {
		this.dang = dang;
	}

	@Override
	public String toString() {
		return "TelecomGuangDongStatusCode [taskid=" + taskid + ", userid=" + userid + ", month=" + month + ", cdate="
				+ cdate + ", xin=" + xin + ", dang=" + dang + "]";
	}
	
	
}
