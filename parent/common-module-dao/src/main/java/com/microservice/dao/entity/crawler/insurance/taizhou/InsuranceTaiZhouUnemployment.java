package com.microservice.dao.entity.crawler.insurance.taizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_taizhou_unemployment",indexes = {@Index(name = "index_insurance_taizhou_unemployment_taskid", columnList = "taskid")})
public class InsuranceTaiZhouUnemployment extends IdEntity{

	private String getMonth;//领取月数
	private String month;//未领取月数
	private String IDNum;//身份证
	private String name;//姓名
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceTaiZhouUnemployment [getMonth=" + getMonth + ", month=" + month + ", IDNum=" + IDNum
				+ ", name=" + name + ", taskid=" + taskid + "]";
	}
	public String getGetMonth() {
		return getMonth;
	}
	public void setGetMonth(String getMonth) {
		this.getMonth = getMonth;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
