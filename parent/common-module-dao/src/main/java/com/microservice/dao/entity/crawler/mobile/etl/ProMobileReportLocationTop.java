package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_location_top",indexes = {@Index(name = "index_pro_mobile_report_location_top_taskid", columnList = "taskId")})
public class ProMobileReportLocationTop extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String location;
	private String communicateCounts;
	private String phonenumCounts;
	private String communicateDuration;
	private String dialCounts;
	private String calledCounts;
	private String dialDuration;
	private String calledDuration;
	private String avgDialDuration;
	private String avgCalledDuration;
	private String dialCountsProportion;
	private String calledCountsProportion;
	private String dialDurationProportion;
	private String calledDurationProportion;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCommunicateCounts() {
		return communicateCounts;
	}
	public void setCommunicateCounts(String communicateCounts) {
		this.communicateCounts = communicateCounts;
	}
	public String getPhonenumCounts() {
		return phonenumCounts;
	}
	public void setPhonenumCounts(String phonenumCounts) {
		this.phonenumCounts = phonenumCounts;
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
	public String getAvgDialDuration() {
		return avgDialDuration;
	}
	public void setAvgDialDuration(String avgDialDuration) {
		this.avgDialDuration = avgDialDuration;
	}
	public String getAvgCalledDuration() {
		return avgCalledDuration;
	}
	public void setAvgCalledDuration(String avgCalledDuration) {
		this.avgCalledDuration = avgCalledDuration;
	}
	public String getDialCountsProportion() {
		return dialCountsProportion;
	}
	public void setDialCountsProportion(String dialCountsProportion) {
		this.dialCountsProportion = dialCountsProportion;
	}
	public String getCalledCountsProportion() {
		return calledCountsProportion;
	}
	public void setCalledCountsProportion(String calledCountsProportion) {
		this.calledCountsProportion = calledCountsProportion;
	}
	public String getDialDurationProportion() {
		return dialDurationProportion;
	}
	public void setDialDurationProportion(String dialDurationProportion) {
		this.dialDurationProportion = dialDurationProportion;
	}
	public String getCalledDurationProportion() {
		return calledDurationProportion;
	}
	public void setCalledDurationProportion(String calledDurationProportion) {
		this.calledDurationProportion = calledDurationProportion;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportLocationTop [taskId=" + taskId + ", location=" + location + ", communicateCounts="
				+ communicateCounts + ", phonenumCounts=" + phonenumCounts + ", communicateDuration="
				+ communicateDuration + ", dialCounts=" + dialCounts + ", calledCounts=" + calledCounts
				+ ", dialDuration=" + dialDuration + ", calledDuration=" + calledDuration + ", avgDialDuration="
				+ avgDialDuration + ", avgCalledDuration=" + avgCalledDuration + ", dialCountsProportion="
				+ dialCountsProportion + ", calledCountsProportion=" + calledCountsProportion
				+ ", dialDurationProportion=" + dialDurationProportion + ", calledDurationProportion="
				+ calledDurationProportion + ", dataType=" + dataType + "]";
	}
		
}
