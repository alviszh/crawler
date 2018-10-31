package com.microservice.dao.entity.crawler.telecom.liaoning;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_liaoning_phoneschemes",indexes = {@Index(name = "index_telecom_liaoning_phoneschemes_taskid", columnList = "taskid")})
public class TelecomLiaoNingPhoneschemes extends IdEntity {

	private String offerSpecName;//业务名称
	
	private String offerDescription;//业务说明
	
	private String startDt;//开通时间
	
	private String endDt;//到期时间
	
	private Integer userid;
	
	private String Taskid;

	public String getOfferSpecName() {
		return offerSpecName;
	}

	public void setOfferSpecName(String offerSpecName) {
		this.offerSpecName = offerSpecName;
	}

	
	@Column(columnDefinition="text")
	public String getOfferDescription() {
		return offerDescription;
	}

	public void setOfferDescription(String offerDescription) {
		this.offerDescription = offerDescription;
	}

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return Taskid;
	}

	public void setTaskid(String taskid) {
		Taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomLiaoNingPhoneschemes [offerSpecName=" + offerSpecName + ", offerDescription=" + offerDescription
				+ ", startDt=" + startDt + ", endDt=" + endDt + ", userid=" + userid + ", Taskid=" + Taskid + "]";
	}
	
	

	
}
