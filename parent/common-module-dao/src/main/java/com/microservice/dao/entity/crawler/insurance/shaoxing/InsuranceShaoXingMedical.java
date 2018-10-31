package com.microservice.dao.entity.crawler.insurance.shaoxing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_shaoxing_medical",indexes = {@Index(name = "index_insurance_shaoxing_medical_taskid", columnList = "taskid")}) 
public class InsuranceShaoXingMedical extends IdEntity{

	private String status;//险种
	private String time;//应交年月
	private String base;//缴费基数
	private String payCompany;//单位应缴
	private String payPersonal;//个人应缴
	private String getMoney;//到账情况
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceShaoXingMedical [status=" + status + ", time=" + time + ", base=" + base + ", payCompany="
				+ payCompany + ", payPersonal=" + payPersonal + ", getMoney=" + getMoney + ", taskid=" + taskid + "]";
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getPayCompany() {
		return payCompany;
	}
	public void setPayCompany(String payCompany) {
		this.payCompany = payCompany;
	}
	public String getPayPersonal() {
		return payPersonal;
	}
	public void setPayPersonal(String payPersonal) {
		this.payPersonal = payPersonal;
	}
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
