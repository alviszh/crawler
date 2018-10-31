package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 增值业务
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_increment" ,indexes = {@Index(name = "index_telecom_chongqing_increment_taskid", columnList = "taskid")})
public class TelecomChongqingIncrement extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;

	/**
	 * 业务名称
	 */
	private String businessName;
	
	/**
	 * 业务代码/号码
	 */
	private String businessCode;
	
	/**
	 * 计费类型
	 */
	private String chargetype;
	
	/**
	 * 使用时间
	 */
	private String useTime;
	
	/**
	 * 通话时长（秒）
	 */
	private String callTimeCost;
	
	/**
	 * 费用（元）
	 */
	private String fee;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getChargetype() {
		return chargetype;
	}

	public void setChargetype(String chargetype) {
		this.chargetype = chargetype;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getCallTimeCost() {
		return callTimeCost;
	}

	public void setCallTimeCost(String callTimeCost) {
		this.callTimeCost = callTimeCost;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "TelecomChongqingIncrementBusiness [taskid=" + taskid + ", businessName=" + businessName
				+ ", businessCode=" + businessCode + ", chargetype=" + chargetype + ", useTime=" + useTime
				+ ", callTimeCost=" + callTimeCost + ", fee=" + fee + "]";
	}

	public TelecomChongqingIncrement(String taskid, String businessName, String businessCode, String chargetype,
			String useTime, String callTimeCost, String fee) {
		super();
		this.taskid = taskid;
		this.businessName = businessName;
		this.businessCode = businessCode;
		this.chargetype = chargetype;
		this.useTime = useTime;
		this.callTimeCost = callTimeCost;
		this.fee = fee;
	}

	public TelecomChongqingIncrement() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}