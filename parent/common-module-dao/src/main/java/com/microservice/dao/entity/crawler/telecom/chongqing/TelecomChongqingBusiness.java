package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 重庆用户在用业务情况
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_business" ,indexes = {@Index(name = "index_telecom_chongqing_business_taskid", columnList = "taskid")})
public class TelecomChongqingBusiness extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 套餐名称 OR 产品名称
	 */
	private String offerCompName;
	
	/**
	 * 优惠名称-----套餐信息字段
	 */
	private String favourName;
	
	/**
	 * 生效时间-----套餐信息字段
	 */
	private String favourEffTime;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOfferCompName() {
		return offerCompName;
	}

	public void setOfferCompName(String offerCompName) {
		this.offerCompName = offerCompName;
	}

	public String getFavourName() {
		return favourName;
	}

	public void setFavourName(String favourName) {
		this.favourName = favourName;
	}

	public String getFavourEffTime() {
		return favourEffTime;
	}

	public void setFavourEffTime(String favourEffTime) {
		this.favourEffTime = favourEffTime;
	}

	@Override
	public String toString() {
		return "TelecomChongqingBusiness [taskid=" + taskid + ", offerCompName=" + offerCompName + ", favourName="
				+ favourName + ", favourEffTime=" + favourEffTime + "]";
	}

	public TelecomChongqingBusiness(String taskid, String offerCompName, String favourName, String favourEffTime) {
		super();
		this.taskid = taskid;
		this.offerCompName = offerCompName;
		this.favourName = favourName;
		this.favourEffTime = favourEffTime;
	}

	public TelecomChongqingBusiness() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}