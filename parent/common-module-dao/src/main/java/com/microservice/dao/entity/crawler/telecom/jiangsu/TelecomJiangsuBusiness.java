package com.microservice.dao.entity.crawler.telecom.jiangsu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 业务
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_jiangsu_business" ,indexes = {@Index(name = "index_telecom_jiangsu_business_taskid", columnList = "taskid")})
public class TelecomJiangsuBusiness extends IdEntity {

	/**
	 * taskid
	 */
	private String taskid;

	/**
	 * 业务名称
	 */
	private String offerSpecName;

	/**
	 * 订购时间
	 */
	private String startDt;

	/**
	 * 资费
	 */
	private String fee;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOfferSpecName() {
		return offerSpecName;
	}

	public void setOfferSpecName(String offerSpecName) {
		this.offerSpecName = offerSpecName;
	}

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "TelecomJiangsuBusiness [taskid=" + taskid + ", offerSpecName=" + offerSpecName + ", startDt=" + startDt
				+ ", fee=" + fee + "]";
	}

	public TelecomJiangsuBusiness(String taskid, String offerSpecName, String startDt, String fee) {
		super();
		this.taskid = taskid;
		this.offerSpecName = offerSpecName;
		this.startDt = startDt;
		this.fee = fee;
	}

	public TelecomJiangsuBusiness() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}