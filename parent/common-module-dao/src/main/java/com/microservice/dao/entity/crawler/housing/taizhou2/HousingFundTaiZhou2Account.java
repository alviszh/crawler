package com.microservice.dao.entity.crawler.housing.taizhou2;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_taizhou2_account",indexes = {@Index(name = "index_housing_taizhou2_account_taskid", columnList = "taskid")})
public class HousingFundTaiZhou2Account extends IdEntity{

	private String datea;//记账日期
	private String type;//处理类型
	private String payDate;//汇缴年月
	private String getFund;//公积金收入
	private String outFund;//公积金支出
	private String fundInterest;//公积金利息
	private String fundFee;//公积金余额
	private String getSubsidy;//补贴收入
	private String outSubsidy;//补贴支出
	private String subsidyInterest;//补贴利息
	private String subsidyFee;//补贴余额
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundTaiZhou2Account [datea=" + datea + ", type=" + type + ", payDate=" + payDate + ", getFund="
				+ getFund + ", outFund=" + outFund + ", fundInterest=" + fundInterest + ", fundFee=" + fundFee
				+ ", getSubsidy=" + getSubsidy + ", outSubsidy=" + outSubsidy + ", subsidyInterest=" + subsidyInterest
				+ ", subsidyFee=" + subsidyFee + ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getGetFund() {
		return getFund;
	}
	public void setGetFund(String getFund) {
		this.getFund = getFund;
	}
	public String getOutFund() {
		return outFund;
	}
	public void setOutFund(String outFund) {
		this.outFund = outFund;
	}
	public String getFundInterest() {
		return fundInterest;
	}
	public void setFundInterest(String fundInterest) {
		this.fundInterest = fundInterest;
	}
	public String getFundFee() {
		return fundFee;
	}
	public void setFundFee(String fundFee) {
		this.fundFee = fundFee;
	}
	public String getGetSubsidy() {
		return getSubsidy;
	}
	public void setGetSubsidy(String getSubsidy) {
		this.getSubsidy = getSubsidy;
	}
	public String getOutSubsidy() {
		return outSubsidy;
	}
	public void setOutSubsidy(String outSubsidy) {
		this.outSubsidy = outSubsidy;
	}
	public String getSubsidyInterest() {
		return subsidyInterest;
	}
	public void setSubsidyInterest(String subsidyInterest) {
		this.subsidyInterest = subsidyInterest;
	}
	public String getSubsidyFee() {
		return subsidyFee;
	}
	public void setSubsidyFee(String subsidyFee) {
		this.subsidyFee = subsidyFee;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
