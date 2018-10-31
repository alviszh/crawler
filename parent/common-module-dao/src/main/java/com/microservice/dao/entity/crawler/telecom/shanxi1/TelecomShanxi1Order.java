package com.microservice.dao.entity.crawler.telecom.shanxi1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西电信用户订购业务信息
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_shanxi1_order" ,indexes = {@Index(name = "index_telecom_shanxi1_order_taskid", columnList = "taskid")})
public class TelecomShanxi1Order extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;

	
	/**
	 * 套餐类型
	 */
	private String offerType;
	
	/**
	 * 套餐名称
	 */
	private String offerName;
	
	/**
	 * 详细介绍
	 */
	private String offerDesc;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	@Column(columnDefinition="text")
	public String getOfferDesc() {
		return offerDesc;
	}

	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}

	@Override
	public String toString() {
		return "TelecomShanxi1UserInfo2 [taskid=" + taskid + ", offerType=" + offerType + ", offerName=" + offerName
				+ ", offerDesc=" + offerDesc + "]";
	}

	public TelecomShanxi1Order(String taskid, String offerType, String offerName, String offerDesc) {
		super();
		this.taskid = taskid;
		this.offerType = offerType;
		this.offerName = offerName;
		this.offerDesc = offerDesc;
	}

	public TelecomShanxi1Order() {
		super();
		// TODO Auto-generated constructor stub
	}


}