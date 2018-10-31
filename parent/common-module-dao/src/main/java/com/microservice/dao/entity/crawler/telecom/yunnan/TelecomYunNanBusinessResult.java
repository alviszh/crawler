package com.microservice.dao.entity.crawler.telecom.yunnan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_yunnan_business")
public class TelecomYunNanBusinessResult extends IdEntity {

	private String businessname ;
	
	private String businessstardate;
	
	private String businessenddate;
	
	private String businessdescription;
		
	private Integer userid;

	private String taskid;

	public String getBusinessname() {
		return businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}

	public String getBusinessstardate() {
		return businessstardate;
	}

	public void setBusinessstardate(String businessstardate) {
		this.businessstardate = businessstardate;
	}

	public String getBusinessenddate() {
		return businessenddate;
	}

	public void setBusinessenddate(String businessenddate) {
		this.businessenddate = businessenddate;
	}

	public String getBusinessdescription() {
		return businessdescription;
	}

	public void setBusinessdescription(String businessdescription) {
		this.businessdescription = businessdescription;
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
		return "TelecomYunNanBusinessResult [businessname=" + businessname + ", businessstardate=" + businessstardate
				+ ", businessenddate=" + businessenddate + ", businessdescription=" + businessdescription + ", userid="
				+ userid + ", taskid=" + taskid + "]";
	}

}