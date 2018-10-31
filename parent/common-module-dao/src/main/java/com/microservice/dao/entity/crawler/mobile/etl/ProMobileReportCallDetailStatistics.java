package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_call_detail_statistics",indexes = {@Index(name = "index_pro_mobile_report_call_detail_statistics_taskid", columnList = "taskId")})
public class ProMobileReportCallDetailStatistics extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String phonenum;
	private String phonenumFlag;
	private String phonenumType;
	private String city;
	private String communicateCounts;
	private String communicateDuration;
	private String dialCounts;
	private String calledCounts;
	private String morningCounts;
	private String noonCounts;
	private String afternoonCounts;
	private String nightCounts;
	private String midnightCounts;
	private String weekdaysCounts;
	private String weekendsCounts;
	private String holidayCounts;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getPhonenumFlag() {
		return phonenumFlag;
	}
	public void setPhonenumFlag(String phonenumFlag) {
		this.phonenumFlag = phonenumFlag;
	}
	public String getPhonenumType() {
		return phonenumType;
	}
	public void setPhonenumType(String phonenumType) {
		this.phonenumType = phonenumType;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCommunicateCounts() {
		return communicateCounts;
	}
	public void setCommunicateCounts(String communicateCounts) {
		this.communicateCounts = communicateCounts;
	}
	public String getCommunicateDuration() {
		return communicateDuration;
	}
	public void setCommunicateDuration(String communicateDuration) {
		this.communicateDuration = communicateDuration;
	}
	public String getDialCounts() {
		return dialCounts;
	}
	public void setDialCounts(String dialCounts) {
		this.dialCounts = dialCounts;
	}
	public String getCalledCounts() {
		return calledCounts;
	}
	public void setCalledCounts(String calledCounts) {
		this.calledCounts = calledCounts;
	}
	public String getMorningCounts() {
		return morningCounts;
	}
	public void setMorningCounts(String morningCounts) {
		this.morningCounts = morningCounts;
	}
	public String getNoonCounts() {
		return noonCounts;
	}
	public void setNoonCounts(String noonCounts) {
		this.noonCounts = noonCounts;
	}
	public String getAfternoonCounts() {
		return afternoonCounts;
	}
	public void setAfternoonCounts(String afternoonCounts) {
		this.afternoonCounts = afternoonCounts;
	}
	public String getNightCounts() {
		return nightCounts;
	}
	public void setNightCounts(String nightCounts) {
		this.nightCounts = nightCounts;
	}
	public String getMidnightCounts() {
		return midnightCounts;
	}
	public void setMidnightCounts(String midnightCounts) {
		this.midnightCounts = midnightCounts;
	}
	public String getWeekdaysCounts() {
		return weekdaysCounts;
	}
	public void setWeekdaysCounts(String weekdaysCounts) {
		this.weekdaysCounts = weekdaysCounts;
	}
	public String getWeekendsCounts() {
		return weekendsCounts;
	}
	public void setWeekendsCounts(String weekendsCounts) {
		this.weekendsCounts = weekendsCounts;
	}
	public String getHolidayCounts() {
		return holidayCounts;
	}
	public void setHolidayCounts(String holidayCounts) {
		this.holidayCounts = holidayCounts;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportCallDetailStatistics [taskId=" + taskId + ", phonenum=" + phonenum + ", phonenumFlag="
				+ phonenumFlag + ", phonenumType=" + phonenumType + ", city=" + city + ", communicateCounts="
				+ communicateCounts + ", communicateDuration=" + communicateDuration + ", dialCounts=" + dialCounts
				+ ", calledCounts=" + calledCounts + ", morningCounts=" + morningCounts + ", noonCounts=" + noonCounts
				+ ", afternoonCounts=" + afternoonCounts + ", nightCounts=" + nightCounts + ", midnightCounts="
				+ midnightCounts + ", weekdaysCounts=" + weekdaysCounts + ", weekendsCounts=" + weekendsCounts
				+ ", holidayCounts=" + holidayCounts + ", dataType=" + dataType + "]";
	}
	
	
	
		
}
