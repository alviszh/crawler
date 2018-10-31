package com.microservice.dao.entity.crawler.telecom.sichuan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_sichuan_businessmessage",indexes = {@Index(name = "index_telecom_sichuan_businessmessage_taskid", columnList = "taskid")})
public class TelecomSiChuanBusinessMessage extends IdEntity{

	private String productnumber;//产品号码
	
	private String businessname;//产品名称
	
	private String starttime;//开始时间
	
	private String endtime;//结束时间
	
	private Integer userid;
	
	private String taskid;

	

	public String getProductnumber() {
		return productnumber;
	}

	public void setProductnumber(String productnumber) {
		this.productnumber = productnumber;
	}

	public String getBusinessname() {
		return businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}


	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
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
	
	
}
