package com.microservice.dao.entity.crawler.insurance.sz.shanxi3;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi3_unemployment_person")
public class InsuranceShanXi3UnemploymentPerson extends IdEntity{
	
	private String taskid;	
	private String uname;					//姓名
	private String personStatus;			//人员状态
	private String payType;					//缴费状态
	private String registerDate;			//失业登记时间
	private String getDate;					//待遇开始领取时间
	private String enjoyMonth;				//应享受月数
	private String enjoyedMonth;			//已享受月数
	private String residueEnjoyMonth;		//剩余享受月数
	private String grantStatus;				//发放状态
	private String participationDate;		//参保日期
	private String organization;			//经办机构名称
	
	@Override
	public String toString() {
		return "InsuranceShanXi3UnemploymentPerson [taskid=" + taskid + ", uname=" + uname + ", personStatus="
				+ personStatus + ", payType=" + payType + ", registerDate=" + registerDate + ", getDate=" + getDate
				+ ", enjoyMonth=" + enjoyMonth + ", enjoyedMonth=" + enjoyedMonth + ", residueEnjoyMonth="
				+ residueEnjoyMonth + ", grantStatus=" + grantStatus + ", participationDate=" + participationDate
				+ ", organization=" + organization + "]";
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
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getGetDate() {
		return getDate;
	}
	public void setGetDate(String getDate) {
		this.getDate = getDate;
	}
	public String getEnjoyMonth() {
		return enjoyMonth;
	}
	public void setEnjoyMonth(String enjoyMonth) {
		this.enjoyMonth = enjoyMonth;
	}
	public String getEnjoyedMonth() {
		return enjoyedMonth;
	}
	public void setEnjoyedMonth(String enjoyedMonth) {
		this.enjoyedMonth = enjoyedMonth;
	}
	public String getResidueEnjoyMonth() {
		return residueEnjoyMonth;
	}
	public void setResidueEnjoyMonth(String residueEnjoyMonth) {
		this.residueEnjoyMonth = residueEnjoyMonth;
	}
	public String getGrantStatus() {
		return grantStatus;
	}
	public void setGrantStatus(String grantStatus) {
		this.grantStatus = grantStatus;
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
