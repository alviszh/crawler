package com.microservice.dao.entity.crawler.housing.haerbin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_haerbin_userinfo",indexes = {@Index(name = "index_housing_haerbin_userinfo_taskid", columnList = "taskid")})
public class HousingFundHaErBinUserInfo extends IdEntity{

	private String name;//姓 名
	private String status;//职工类型
	private String cardType;//证件类型
	private String idCard;//身份证号
	private String openDate;//开户日期
	private String phone;//手机号码
	private String personalNum;//个人账号
	private String personalStatus;//个人账户状态
	private String comapnyNum;//单位账号
	private String company;//单位名称
	private String companyRatio;//单位缴存比例
	private String personalRatio;//个人缴存比例
	private String personalBase;//个人缴存基数
	private String lastDate;//最后汇缴年月
	private String personalMonth;//个人月缴存额
	private String companyMonth;//单位月缴存额
	private String monthSave;//月汇缴金额
	private String fee;//账户余额
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundHaErBinUserInfo [name=" + name + ", status=" + status + ", cardType=" + cardType
				+ ", idCard=" + idCard + ", openDate=" + openDate + ", phone=" + phone + ", personalNum=" + personalNum
				+ ", personalStatus=" + personalStatus + ", comapnyNum=" + comapnyNum + ", company=" + company
				+ ", companyRatio=" + companyRatio + ", personalRatio=" + personalRatio + ", personalBase="
				+ personalBase + ", lastDate=" + lastDate + ", personalMonth=" + personalMonth + ", companyMonth="
				+ companyMonth + ", monthSave=" + monthSave + ", fee=" + fee + ", taskid=" + taskid + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getPersonalStatus() {
		return personalStatus;
	}
	public void setPersonalStatus(String personalStatus) {
		this.personalStatus = personalStatus;
	}
	public String getComapnyNum() {
		return comapnyNum;
	}
	public void setComapnyNum(String comapnyNum) {
		this.comapnyNum = comapnyNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyRatio() {
		return companyRatio;
	}
	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}
	public String getPersonalRatio() {
		return personalRatio;
	}
	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}
	public String getPersonalBase() {
		return personalBase;
	}
	public void setPersonalBase(String personalBase) {
		this.personalBase = personalBase;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	public String getPersonalMonth() {
		return personalMonth;
	}
	public void setPersonalMonth(String personalMonth) {
		this.personalMonth = personalMonth;
	}
	public String getCompanyMonth() {
		return companyMonth;
	}
	public void setCompanyMonth(String companyMonth) {
		this.companyMonth = companyMonth;
	}
	public String getMonthSave() {
		return monthSave;
	}
	public void setMonthSave(String monthSave) {
		this.monthSave = monthSave;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
