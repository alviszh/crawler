package com.microservice.dao.entity.crawler.housing.zhuzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_zhuzhou_userinfo",indexes = {@Index(name = "index_housing_zhuzhou_userinfo_taskid", columnList = "taskid")})
public class HousingFundZhuZhouUserInfo extends IdEntity{
	
	private String personalNum;//个人账号
	private String cardType;//证件类型
	private String cardNum;//证件号码
	private String name;//姓名
	private String companyNum;//单位账号
	private String company;//单位名称
	private String phone;//手机号码
	private String openDate;//开户日期
	private String status;//个人账户状态
	private String yf;//是否冻结 
	private String yfMoney;//是否贷款
	private String endDate;//缴至年月
	private String personalBase;//个人缴存基数
	private String monthPay;//月缴存额
	private String companyPay;//单位月缴存额
	private String personalMonth;//个人月缴存额
	private String companyRatio;//单位缴存比例
	private String personalRatio;//个人缴存比例
	private String fee;//个人账户余额
	private String connectNum;//联名卡号
	private String taskid;//
	@Override
	public String toString() {
		return "HousingFundZhuZhouUserInfo [personalNum=" + personalNum + ", cardType=" + cardType + ", cardNum="
				+ cardNum + ", name=" + name + ", companyNum=" + companyNum + ", company=" + company + ", phone="
				+ phone + ", openDate=" + openDate + ", status=" + status + ", yf=" + yf + ", yfMoney=" + yfMoney
				+ ", endDate=" + endDate + ", personalBase=" + personalBase + ", monthPay=" + monthPay + ", companyPay="
				+ companyPay + ", personalMonth=" + personalMonth + ", companyRatio=" + companyRatio
				+ ", personalRatio=" + personalRatio + ", fee=" + fee + ", connectNum=" + connectNum + ", taskid="
				+ taskid + "]";
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getYf() {
		return yf;
	}
	public void setYf(String yf) {
		this.yf = yf;
	}
	public String getYfMoney() {
		return yfMoney;
	}
	public void setYfMoney(String yfMoney) {
		this.yfMoney = yfMoney;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPersonalBase() {
		return personalBase;
	}
	public void setPersonalBase(String personalBase) {
		this.personalBase = personalBase;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonalMonth() {
		return personalMonth;
	}
	public void setPersonalMonth(String personalMonth) {
		this.personalMonth = personalMonth;
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
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getConnectNum() {
		return connectNum;
	}
	public void setConnectNum(String connectNum) {
		this.connectNum = connectNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
