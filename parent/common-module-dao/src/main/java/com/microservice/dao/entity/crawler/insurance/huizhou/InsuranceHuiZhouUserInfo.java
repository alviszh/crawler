package com.microservice.dao.entity.crawler.insurance.huizhou;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_huizhou_userinfo")
public class InsuranceHuiZhouUserInfo extends IdEntity {
	

	private String institutionName;	//所属机构
	private String institutionNum;	//社会保险登记号码
	private String unitname;		//单位名称
	private String idnum;			//证件号码
	private String username;		//姓名
	private String gender;			//性别
	private String birthdate;		//出生日期
	private String personalState;	//个人身份
	private String householdState;	//户口性质
	private String employmenState;	 //用工性质
	private String firstworkDate;	//参加工作日期
	private String workerType;		//职工类别
	private String civilserviceType;//公务员类别
	private String paymentType;		//缴费人员类别
	private String persionFirst;	//养老保险参保时间
	private String persionmonthNum;	//养老保险实际月数
	private String lostFirst;		//失业保险参保时间
	private String lostMonthNum;	//失业保险实际月数
	private String contactPersion;	//联系人
	private String contactTel;		//联系电话
	private String zipcode;			//邮政编码
	private String householdSeat;	//户口所在地
	private String liveAddress;	//居住地地址

	private String taskid;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public String getInstitutionNum() {
		return institutionNum;
	}
	public void setInstitutionNum(String institutionNum) {
		this.institutionNum = institutionNum;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getPersonalState() {
		return personalState;
	}
	public void setPersonalState(String personalState) {
		this.personalState = personalState;
	}
	public String getHouseholdState() {
		return householdState;
	}
	public void setHouseholdState(String householdState) {
		this.householdState = householdState;
	}
	public String getEmploymenState() {
		return employmenState;
	}
	public void setEmploymenState(String employmenState) {
		this.employmenState = employmenState;
	}
	public String getFirstworkDate() {
		return firstworkDate;
	}
	public void setFirstworkDate(String firstworkDate) {
		this.firstworkDate = firstworkDate;
	}
	public String getWorkerType() {
		return workerType;
	}
	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}
	public String getCivilserviceType() {
		return civilserviceType;
	}
	public void setCivilserviceType(String civilserviceType) {
		this.civilserviceType = civilserviceType;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPersionFirst() {
		return persionFirst;
	}
	public void setPersionFirst(String persionFirst) {
		this.persionFirst = persionFirst;
	}
	public String getPersionmonthNum() {
		return persionmonthNum;
	}
	public void setPersionmonthNum(String persionmonthNum) {
		this.persionmonthNum = persionmonthNum;
	}
	public String getLostFirst() {
		return lostFirst;
	}
	public void setLostFirst(String lostFirst) {
		this.lostFirst = lostFirst;
	}
	public String getLostMonthNum() {
		return lostMonthNum;
	}
	public void setLostMonthNum(String lostMonthNum) {
		this.lostMonthNum = lostMonthNum;
	}
	public String getContactPersion() {
		return contactPersion;
	}
	public void setContactPersion(String contactPersion) {
		this.contactPersion = contactPersion;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getHouseholdSeat() {
		return householdSeat;
	}
	public void setHouseholdSeat(String householdSeat) {
		this.householdSeat = householdSeat;
	}
	public String getLiveAddress() {
		return liveAddress;
	}
	public void setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
	}
	@Override
	public String toString() {
		return "InsuranceHuiZhouUserInfo [institutionName=" + institutionName + ", institutionNum=" + institutionNum
				+ ", unitname=" + unitname + ", idnum=" + idnum + ", username=" + username + ", gender=" + gender
				+ ", birthdate=" + birthdate + ", personalState=" + personalState + ", householdState=" + householdState
				+ ", employmenState=" + employmenState + ", firstworkDate=" + firstworkDate + ", workerType="
				+ workerType + ", civilserviceType=" + civilserviceType + ", paymentType=" + paymentType
				+ ", persionFirst=" + persionFirst + ", persionmonthNum=" + persionmonthNum + ", lostFirst=" + lostFirst
				+ ", lostMonthNum=" + lostMonthNum + ", contactPersion=" + contactPersion + ", contactTel=" + contactTel
				+ ", zipcode=" + zipcode + ", householdSeat=" + householdSeat + ", liveAddress=" + liveAddress
				+ ", taskid=" + taskid + "]";
	}
	public InsuranceHuiZhouUserInfo(String institutionName, String institutionNum, String unitname, String idnum,
			String username, String gender, String birthdate, String personalState, String householdState,
			String employmenState, String firstworkDate, String workerType, String civilserviceType, String paymentType,
			String persionFirst, String persionmonthNum, String lostFirst, String lostMonthNum, String contactPersion,
			String contactTel, String zipcode, String householdSeat, String liveAddress, String taskid) {
		super();
		this.institutionName = institutionName;
		this.institutionNum = institutionNum;
		this.unitname = unitname;
		this.idnum = idnum;
		this.username = username;
		this.gender = gender;
		this.birthdate = birthdate;
		this.personalState = personalState;
		this.householdState = householdState;
		this.employmenState = employmenState;
		this.firstworkDate = firstworkDate;
		this.workerType = workerType;
		this.civilserviceType = civilserviceType;
		this.paymentType = paymentType;
		this.persionFirst = persionFirst;
		this.persionmonthNum = persionmonthNum;
		this.lostFirst = lostFirst;
		this.lostMonthNum = lostMonthNum;
		this.contactPersion = contactPersion;
		this.contactTel = contactTel;
		this.zipcode = zipcode;
		this.householdSeat = householdSeat;
		this.liveAddress = liveAddress;
		this.taskid = taskid;
	}
	public InsuranceHuiZhouUserInfo() {
		super();
	}
}
