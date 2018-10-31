package com.microservice.dao.entity.crawler.telecom.hainan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hainan_business")
public class TelecomHaiNanBusinessResult extends IdEntity {

	private String NWKSTARTDATE;

	private String NWKCODE;

	private String NWKENDDATE;

	private String NWKNAME;

	private String FEE;
	
	private Integer userid;

	private String taskid;

	public void setNWKSTARTDATE(String NWKSTARTDATE) {
		this.NWKSTARTDATE = NWKSTARTDATE;
	}

	public String getNWKSTARTDATE() {
		return this.NWKSTARTDATE;
	}

	public void setNWKCODE(String NWKCODE) {
		this.NWKCODE = NWKCODE;
	}

	public String getNWKCODE() {
		return this.NWKCODE;
	}

	public void setNWKENDDATE(String NWKENDDATE) {
		this.NWKENDDATE = NWKENDDATE;
	}

	public String getNWKENDDATE() {
		return this.NWKENDDATE;
	}

	public void setNWKNAME(String NWKNAME) {
		this.NWKNAME = NWKNAME;
	}

	public String getNWKNAME() {
		return this.NWKNAME;
	}

	public void setFEE(String FEE) {
		this.FEE = FEE;
	}

	public String getFEE() {
		return this.FEE;
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
		return "TelecomHaiNanBusinessResult [NWKSTARTDATE=" + NWKSTARTDATE + ", NWKCODE=" + NWKCODE + ", NWKENDDATE="
				+ NWKENDDATE + ", NWKNAME=" + NWKNAME + ", FEE=" + FEE + ", userid=" + userid + ", taskid=" + taskid
				+ "]";
	}

}