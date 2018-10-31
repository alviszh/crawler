package com.microservice.dao.entity.crawler.housing.wuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_wuzhou_userinfo",indexes = {@Index(name = "index_housing_wuzhou_userinfo_taskid", columnList = "taskid")})
public class HousingWuZhouUserinfo extends IdEntity implements Serializable{
	private String taskid;
	private String staffName;						//姓名
	private String staffNum;						//职工编号
	private String companyName;						//单位
	private String state;							//状态
	private String idNum;							//实际身份证号
	private String code;                            //公积金联名卡号
	private String openDate;                        //开户日期
	private String monthPay;						//月存金额
	private String personalAmount;                  //个人月存额
	private String companyAmount;                   //单位月存额
	private String prevYearBalances;                //上年结存金额
	private String thisYear;                        //本年缴存金额
	private String draw;                            //本年支取金额
	private String principal;						//本金金额
	private String companyPercent;					//单位存缴比例
	private String personalPercent;                 //个人缴存比例
	private String companyDate;					    //公积金单位部分缴至年月
	private String personalDate;                    //公积金个人部分缴至年月
	
	@Override
	public String toString() {
		return "HousingWuZhouUserinfo [taskid=" + taskid + ", staffName=" + staffName + ", staffNum=" + staffNum
				+ ", companyName=" + companyName + ", state=" + state + ", idNum=" + idNum
				+ ", code=" + code + ", openDate=" + openDate + ", monthPay=" +monthPay 
				+ ", personalAmount=" + personalAmount + ", companyAmount=" + companyAmount + ", prevYearBalances=" + prevYearBalances
				+ ", thisYear=" + thisYear + ", draw=" + draw + ", principal=" + principal + ", companyPercent="+ companyPercent 
				+ ", personalPercent=" + personalPercent + ", companyDate=" + companyDate + ",  personalDate=" + personalDate+ "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}

	public String getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}

	public String getCompanyAmount() {
		return companyAmount;
	}

	public void setCompanyAmount(String companyAmount) {
		this.companyAmount = companyAmount;
	}

	public String getPrevYearBalances() {
		return prevYearBalances;
	}

	public void setPrevYearBalances(String prevYearBalances) {
		this.prevYearBalances = prevYearBalances;
	}

	public String getThisYear() {
		return thisYear;
	}

	public void setThisYear(String thisYear) {
		this.thisYear = thisYear;
	}

	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getCompanyPercent() {
		return companyPercent;
	}

	public void setCompanyPercent(String companyPercent) {
		this.companyPercent = companyPercent;
	}

	public String getPersonalPercent() {
		return personalPercent;
	}

	public void setPersonalPercent(String personalPercent) {
		this.personalPercent = personalPercent;
	}

	public String getPersonalDate() {
		return personalDate;
	}

	public void setPersonalDate(String personalDate) {
		this.personalDate = personalDate;
	}

	public String getCompanyDate() {
		return companyDate;
	}

	public void setCompanyDate(String companyDate) {
		this.companyDate = companyDate;
	}
	
	
}
