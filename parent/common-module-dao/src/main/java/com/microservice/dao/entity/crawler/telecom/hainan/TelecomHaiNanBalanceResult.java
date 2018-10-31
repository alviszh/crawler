package com.microservice.dao.entity.crawler.telecom.hainan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hainan_balance")
public class TelecomHaiNanBalanceResult extends IdEntity {

	private String benqizhi;

	private String name;

	private String benqi;

	private String benqimo;

	private String shangqi;
	
	private String date;

	private Integer userid;

	private String taskid;
	
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBenqizhi() {
		return benqizhi;
	}

	public void setBenqizhi(String benqizhi) {
		this.benqizhi = benqizhi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBenqi() {
		return benqi;
	}

	public void setBenqi(String benqi) {
		this.benqi = benqi;
	}

	public String getBenqimo() {
		return benqimo;
	}

	public void setBenqimo(String benqimo) {
		this.benqimo = benqimo;
	}

	public String getShangqi() {
		return shangqi;
	}

	public void setShangqi(String shangqi) {
		this.shangqi = shangqi;
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
		return "TelecomHaiNanBalanceResult [benqizhi=" + benqizhi + ", name=" + name + ", benqi=" + benqi + ", benqimo="
				+ benqimo + ", shangqi=" + shangqi + ", date=" + date + ", userid=" + userid + ", taskid=" + taskid
				+ "]";
	}

}