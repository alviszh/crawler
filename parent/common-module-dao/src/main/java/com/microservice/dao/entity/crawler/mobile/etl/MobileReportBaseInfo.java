package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_report_baseinfo",indexes = {@Index(name = "index_mobile_report_baseinfo_taskid", columnList = "taskId")})

public class MobileReportBaseInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String dataGainTime;
	private String updateTime;
	private String idcardNum;
	private String mobile;
	private String commonUseTelephone;
	private String city;
	private String crawlerMonthsShould;
	private String crawlerMonthsFail;
	private String crawlerMonthsWithoutRecord;
	private String crawlerMonthsWithRecord;
	private String userSource;
	private String cellPhoneReliability;
	private String userStatus;
	private String cellPhoneAvailableBalance;
	private String cellPhoneRegTime;
	private String cellPhoneDuration;
	private String contractNum;
	private String name;
	private String idCard;
	private String addr;
	private String businessName;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getDataGainTime() {
		return dataGainTime;
	}
	public void setDataGainTime(String dataGainTime) {
		this.dataGainTime = dataGainTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getIdcardNum() {
		return idcardNum;
	}
	public void setIdcardNum(String idcardNum) {
		this.idcardNum = idcardNum;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCommonUseTelephone() {
		return commonUseTelephone;
	}
	public void setCommonUseTelephone(String commonUseTelephone) {
		this.commonUseTelephone = commonUseTelephone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCrawlerMonthsShould() {
		return crawlerMonthsShould;
	}
	public void setCrawlerMonthsShould(String crawlerMonthsShould) {
		this.crawlerMonthsShould = crawlerMonthsShould;
	}
	public String getCrawlerMonthsFail() {
		return crawlerMonthsFail;
	}
	public void setCrawlerMonthsFail(String crawlerMonthsFail) {
		this.crawlerMonthsFail = crawlerMonthsFail;
	}
	public String getCrawlerMonthsWithoutRecord() {
		return crawlerMonthsWithoutRecord;
	}
	public void setCrawlerMonthsWithoutRecord(String crawlerMonthsWithoutRecord) {
		this.crawlerMonthsWithoutRecord = crawlerMonthsWithoutRecord;
	}
	public String getCrawlerMonthsWithRecord() {
		return crawlerMonthsWithRecord;
	}
	public void setCrawlerMonthsWithRecord(String crawlerMonthsWithRecord) {
		this.crawlerMonthsWithRecord = crawlerMonthsWithRecord;
	}
	public String getUserSource() {
		return userSource;
	}
	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}
	public String getCellPhoneReliability() {
		return cellPhoneReliability;
	}
	public void setCellPhoneReliability(String cellPhoneReliability) {
		this.cellPhoneReliability = cellPhoneReliability;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getCellPhoneAvailableBalance() {
		return cellPhoneAvailableBalance;
	}
	public void setCellPhoneAvailableBalance(String cellPhoneAvailableBalance) {
		this.cellPhoneAvailableBalance = cellPhoneAvailableBalance;
	}
	public String getCellPhoneRegTime() {
		return cellPhoneRegTime;
	}
	public void setCellPhoneRegTime(String cellPhoneRegTime) {
		this.cellPhoneRegTime = cellPhoneRegTime;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@Column(columnDefinition="text")
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	public String getCellPhoneDuration() {
		return cellPhoneDuration;
	}
	public void setCellPhoneDuration(String cellPhoneDuration) {
		this.cellPhoneDuration = cellPhoneDuration;
	}
	@Override
	public String toString() {
		return "MobileReportBaseInfo [taskId=" + taskId + ", dataGainTime=" + dataGainTime + ", updateTime="
				+ updateTime + ", idcardNum=" + idcardNum + ", mobile=" + mobile + ", commonUseTelephone="
				+ commonUseTelephone + ", city=" + city + ", crawlerMonthsShould=" + crawlerMonthsShould
				+ ", crawlerMonthsFail=" + crawlerMonthsFail + ", crawlerMonthsWithoutRecord="
				+ crawlerMonthsWithoutRecord + ", crawlerMonthsWithRecord=" + crawlerMonthsWithRecord + ", userSource="
				+ userSource + ", cellPhoneReliability=" + cellPhoneReliability + ", userStatus=" + userStatus
				+ ", cellPhoneAvailableBalance=" + cellPhoneAvailableBalance + ", cellPhoneRegTime=" + cellPhoneRegTime
				+ ", cellPhoneDuration=" + cellPhoneDuration + ", contractNum=" + contractNum + ", name=" + name
				+ ", idCard=" + idCard + ", addr=" + addr + ", businessName=" + businessName + "]";
	}
		
}
