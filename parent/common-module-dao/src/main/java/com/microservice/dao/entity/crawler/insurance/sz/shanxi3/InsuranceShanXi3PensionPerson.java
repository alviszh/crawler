package com.microservice.dao.entity.crawler.insurance.sz.shanxi3;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi3_pension_person")
public class InsuranceShanXi3PensionPerson extends IdEntity{
	
	private String taskid;
	private String uname;					//姓名
	private String statePersonnel;			//人员状态
	private String paymentStatus;			//缴费状态
	private String organizationDate;		//参保日期
	private String cumulativeBalance;		//当前个人账户累积储存额
	private String organizationName;		//经办机构名称	
	
	@Override
	public String toString() {
		return "InsuranceShanXi3PensionPerson [taskid=" + taskid + ", uname=" + uname + ", statePersonnel="
				+ statePersonnel + ", paymentStatus=" + paymentStatus + ", organizationDate=" + organizationDate
				+ ", cumulativeBalance=" + cumulativeBalance + ", organizationName=" + organizationName + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getStatePersonnel() {
		return statePersonnel;
	}
	public void setStatePersonnel(String statePersonnel) {
		this.statePersonnel = statePersonnel;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getOrganizationDate() {
		return organizationDate;
	}
	public void setOrganizationDate(String organizationDate) {
		this.organizationDate = organizationDate;
	}
	public String getCumulativeBalance() {
		return cumulativeBalance;
	}
	public void setCumulativeBalance(String cumulativeBalance) {
		this.cumulativeBalance = cumulativeBalance;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	

}
