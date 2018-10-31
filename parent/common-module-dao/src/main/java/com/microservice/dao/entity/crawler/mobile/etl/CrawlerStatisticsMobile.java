package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name="crawler_statistics_mobile")
public class CrawlerStatisticsMobile extends IdEntity implements Serializable {

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String crawlerNum;	
	private String ascriptionCity;	
	private String ascriptionProvince;	
	private String etlNum;
	private String crawlerStatus;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCrawlerNum() {
		return crawlerNum;
	}
	public void setCrawlerNum(String crawlerNum) {
		this.crawlerNum = crawlerNum;
	}
	public String getAscriptionCity() {
		return ascriptionCity;
	}
	public void setAscriptionCity(String ascriptionCity) {
		this.ascriptionCity = ascriptionCity;
	}
	public String getAscriptionProvince() {
		return ascriptionProvince;
	}
	public void setAscriptionProvince(String ascriptionProvince) {
		this.ascriptionProvince = ascriptionProvince;
	}
	public String getEtlNum() {
		return etlNum;
	}
	public void setEtlNum(String etlNum) {
		this.etlNum = etlNum;
	}
	public String getCrawlerStatus() {
		return crawlerStatus;
	}
	public void setCrawlerStatus(String crawlerStatus) {
		this.crawlerStatus = crawlerStatus;
	}
	@Override
	public String toString() {
		return "CrawlerStatisticsMobile [taskId=" + taskId + ", crawlerNum=" + crawlerNum + ", ascriptionCity="
				+ ascriptionCity + ", ascriptionProvince=" + ascriptionProvince + ", etlNum=" + etlNum
				+ ", crawlerStatus=" + crawlerStatus + "]";
	}
	
	
}
