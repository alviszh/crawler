package com.microservice.dao.entity.crawler.housing.chaozhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_chaozhou_base")
public class HousingChaoZhouBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String compNum;                 //单位账号
	private String compName;                //单位名称
	private String housingNum;              //公积金账号
	private String name;                    //姓名
	private String Num;                     //证件号码
	private String birth;                   //出生年月
	private String wagesBase;               //工资基数
	private String perPay;                  //个人缴存率(%)
	private String compPay;					//单位缴存率(%)
	private String financePay;				//财政缴存率(%)
	private String perPayNum;				//个人缴存额
	private String compPayNum;				//单位缴存额
	private String financePayNum;			//财政缴存额
	private String openAccountDate;			//开户日期
	private String laborSecurity;			//劳动保障卡号
	private String lastBalance;             //上期余额
	private String thisBalance;				//本期余额
	private String totalBalance;			//总余额
	private String taskid;
	public String getCompNum() {
		return compNum;
	}
	public void setCompNum(String compNum) {
		this.compNum = compNum;
	}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public String getHousingNum() {
		return housingNum;
	}
	public void setHousingNum(String housingNum) {
		this.housingNum = housingNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return Num;
	}
	public void setNum(String num) {
		Num = num;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getWagesBase() {
		return wagesBase;
	}
	public void setWagesBase(String wagesBase) {
		this.wagesBase = wagesBase;
	}
	public String getPerPay() {
		return perPay;
	}
	public void setPerPay(String perPay) {
		this.perPay = perPay;
	}
	public String getCompPay() {
		return compPay;
	}
	public void setCompPay(String compPay) {
		this.compPay = compPay;
	}
	public String getFinancePay() {
		return financePay;
	}
	public void setFinancePay(String financePay) {
		this.financePay = financePay;
	}
	public String getPerPayNum() {
		return perPayNum;
	}
	public void setPerPayNum(String perPayNum) {
		this.perPayNum = perPayNum;
	}
	public String getCompPayNum() {
		return compPayNum;
	}
	public void setCompPayNum(String compPayNum) {
		this.compPayNum = compPayNum;
	}
	public String getFinancePayNum() {
		return financePayNum;
	}
	public void setFinancePayNum(String financePayNum) {
		this.financePayNum = financePayNum;
	}
	public String getOpenAccountDate() {
		return openAccountDate;
	}
	public void setOpenAccountDate(String openAccountDate) {
		this.openAccountDate = openAccountDate;
	}
	public String getLaborSecurity() {
		return laborSecurity;
	}
	public void setLaborSecurity(String laborSecurity) {
		this.laborSecurity = laborSecurity;
	}
	public String getLastBalance() {
		return lastBalance;
	}
	public void setLastBalance(String lastBalance) {
		this.lastBalance = lastBalance;
	}
	public String getThisBalance() {
		return thisBalance;
	}
	public void setThisBalance(String thisBalance) {
		this.thisBalance = thisBalance;
	}
	public String getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
