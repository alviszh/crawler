package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_social_analysis_summary",indexes = {@Index(name = "index_pro_mobile_report_social_analysis_summary_taskid", columnList = "taskId")})
public class ProMobileReportSocialAnalysisSummary extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String socialRange;
	private String socialIntimacy;
	private String socialCenter;
	private String isLocal;
	private String interflowCount;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getSocialRange() {
		return socialRange;
	}
	public void setSocialRange(String socialRange) {
		this.socialRange = socialRange;
	}
	public String getSocialIntimacy() {
		return socialIntimacy;
	}
	public void setSocialIntimacy(String socialIntimacy) {
		this.socialIntimacy = socialIntimacy;
	}
	public String getSocialCenter() {
		return socialCenter;
	}
	public void setSocialCenter(String socialCenter) {
		this.socialCenter = socialCenter;
	}
	public String getIsLocal() {
		return isLocal;
	}
	public void setIsLocal(String isLocal) {
		this.isLocal = isLocal;
	}
	public String getInterflowCount() {
		return interflowCount;
	}
	public void setInterflowCount(String interflowCount) {
		this.interflowCount = interflowCount;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportSocialAnalysisSummary [taskId=" + taskId + ", socialRange=" + socialRange
				+ ", socialIntimacy=" + socialIntimacy + ", socialCenter=" + socialCenter + ", isLocal=" + isLocal
				+ ", interflowCount=" + interflowCount + ", dataType=" + dataType + "]";
	}
	
	
}
