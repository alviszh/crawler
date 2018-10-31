package com.microservice.dao.entity.crawler.telecom.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_beijing_charges")
public class TelecomBeijingChargesResult extends IdEntity {

	private String  arrears1;//号码级

	private String arrears2;// 账户级

	private String charges1;// 号码级通用余额
	private String charges2;// 号码级专用余额

	private String charges3;//账户级通用余额

	private String charges4;// 账户级专用余额

	private Integer userid;

	private String taskid;

	public String getArrears1() {
		return arrears1;
	}

	public void setArrears1(String arrears1) {
		this.arrears1 = arrears1;
	}

	public String getArrears2() {
		return arrears2;
	}

	public void setArrears2(String arrears2) {
		this.arrears2 = arrears2;
	}

	public String getCharges1() {
		return charges1;
	}

	public void setCharges1(String charges1) {
		this.charges1 = charges1;
	}

	public String getCharges2() {
		return charges2;
	}

	public void setCharges2(String charges2) {
		this.charges2 = charges2;
	}

	public String getCharges3() {
		return charges3;
	}

	public void setCharges3(String charges3) {
		this.charges3 = charges3;
	}

	public String getCharges4() {
		return charges4;
	}

	public void setCharges4(String charges4) {
		this.charges4 = charges4;
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
		return "TelecomBeijingChargesResult [arrears1=" + arrears1 + ", arrears2=" + arrears2 + ", charges1=" + charges1
				+ ", charges2=" + charges2 + ", charges3=" + charges3 + ", charges4=" + charges4 + ", userid=" + userid
				+ ", taskid=" + taskid + "]";
	}

	

}