package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_vitality",indexes = {@Index(name = "index_pro_mobile_report_vitality_taskid", columnList = "taskId")})

public class ProMobileReportVitality extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String communicateDays;
	private String messageDays;
	private String payCounts;
	private String communicateCounts;
	private String contactNums;
	private String locationCounts;
	private String dialCounts;
	private String calledCounts;
	private String dialNums;
	private String calledNums;
	private String messageCounts;
	private String communicateDuration;
	private String dialDuration;
	private String calledDuration;
	private String avgDuration;
	private String withoutDialCallDays;
	private String withoutDialCallDaysProportion;
	private String withoutCallDays;
	private String withoutCallDaysProportion;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCommunicateDays() {
		return communicateDays;
	}
	public void setCommunicateDays(String communicateDays) {
		this.communicateDays = communicateDays;
	}
	public String getMessageDays() {
		return messageDays;
	}
	public void setMessageDays(String messageDays) {
		this.messageDays = messageDays;
	}
	public String getPayCounts() {
		return payCounts;
	}
	public void setPayCounts(String payCounts) {
		this.payCounts = payCounts;
	}
	public String getCommunicateCounts() {
		return communicateCounts;
	}
	public void setCommunicateCounts(String communicateCounts) {
		this.communicateCounts = communicateCounts;
	}
	public String getContactNums() {
		return contactNums;
	}
	public void setContactNums(String contactNums) {
		this.contactNums = contactNums;
	}
	public String getLocationCounts() {
		return locationCounts;
	}
	public void setLocationCounts(String locationCounts) {
		this.locationCounts = locationCounts;
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
	public String getDialNums() {
		return dialNums;
	}
	public void setDialNums(String dialNums) {
		this.dialNums = dialNums;
	}
	public String getCalledNums() {
		return calledNums;
	}
	public void setCalledNums(String calledNums) {
		this.calledNums = calledNums;
	}
	public String getMessageCounts() {
		return messageCounts;
	}
	public void setMessageCounts(String messageCounts) {
		this.messageCounts = messageCounts;
	}
	public String getCommunicateDuration() {
		return communicateDuration;
	}
	public void setCommunicateDuration(String communicateDuration) {
		this.communicateDuration = communicateDuration;
	}
	public String getDialDuration() {
		return dialDuration;
	}
	public void setDialDuration(String dialDuration) {
		this.dialDuration = dialDuration;
	}
	public String getCalledDuration() {
		return calledDuration;
	}
	public void setCalledDuration(String calledDuration) {
		this.calledDuration = calledDuration;
	}
	public String getAvgDuration() {
		return avgDuration;
	}
	public void setAvgDuration(String avgDuration) {
		this.avgDuration = avgDuration;
	}
	public String getWithoutDialCallDays() {
		return withoutDialCallDays;
	}
	public void setWithoutDialCallDays(String withoutDialCallDays) {
		this.withoutDialCallDays = withoutDialCallDays;
	}
	public String getWithoutDialCallDaysProportion() {
		return withoutDialCallDaysProportion;
	}
	public void setWithoutDialCallDaysProportion(String withoutDialCallDaysProportion) {
		this.withoutDialCallDaysProportion = withoutDialCallDaysProportion;
	}
	public String getWithoutCallDays() {
		return withoutCallDays;
	}
	public void setWithoutCallDays(String withoutCallDays) {
		this.withoutCallDays = withoutCallDays;
	}
	public String getWithoutCallDaysProportion() {
		return withoutCallDaysProportion;
	}
	public void setWithoutCallDaysProportion(String withoutCallDaysProportion) {
		this.withoutCallDaysProportion = withoutCallDaysProportion;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportVitality [taskId=" + taskId + ", communicateDays=" + communicateDays + ", messageDays="
				+ messageDays + ", payCounts=" + payCounts + ", communicateCounts=" + communicateCounts
				+ ", contactNums=" + contactNums + ", locationCounts=" + locationCounts + ", dialCounts=" + dialCounts
				+ ", calledCounts=" + calledCounts + ", dialNums=" + dialNums + ", calledNums=" + calledNums
				+ ", messageCounts=" + messageCounts + ", communicateDuration=" + communicateDuration
				+ ", dialDuration=" + dialDuration + ", calledDuration=" + calledDuration + ", avgDuration="
				+ avgDuration + ", withoutDialCallDays=" + withoutDialCallDays + ", withoutDialCallDaysProportion="
				+ withoutDialCallDaysProportion + ", withoutCallDays=" + withoutCallDays
				+ ", withoutCallDaysProportion=" + withoutCallDaysProportion + ", dataType=" + dataType + "]";
	}
	
}
