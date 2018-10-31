package com.microservice.dao.entity.crawler.insurance.sz.shanxi3;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi3_injury_person")
public class InsuranceShanXi3InjuryPerson extends IdEntity{
	
	private String taskid;	
	private String uname;					//姓名
	private String personStatus;			//人员状态
	private String payType;					//缴费状态
	private String participationDate;		//参保日期
	private String organization;			//经办机构名称
	
	@Override
	public String toString() {
		return "InsuranceShanXi3InjuryPerson [taskid=" + taskid + ", uname=" + uname + ", personStatus=" + personStatus
				+ ", payType=" + payType + ", participationDate=" + participationDate + ", organization=" + organization
				+ "]";
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
	public String getPersonStatus() {
		return personStatus;
	}
	public void setPersonStatus(String personStatus) {
		this.personStatus = personStatus;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getParticipationDate() {
		return participationDate;
	}
	public void setParticipationDate(String participationDate) {
		this.participationDate = participationDate;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}

}
