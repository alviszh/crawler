package com.microservice.dao.entity.crawler.telecom.shanxi1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西电信流量详单
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_shanxi1_flow" ,indexes = {@Index(name = "index_telecom_shanxi1_flow_taskid", columnList = "taskid")})
public class TelecomShanxi1Flow extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;

	
	/**
	 * 网络类型描述
	 */
	private String netType;

	/**
	 * 上网开始时间
	 */
	private String netTime;

	/**
	 * 网络流量(KB)
	 */
	private String netFlow;

	/**
	 * 上网金额	
	 */
	private String netFee;

	/**
	 * 上网时长
	 */
	private String netTimeCost;

	/**
	 * 上网地区
	 */
	private String netArea;
	
	/**
	 * 使用业务名称
	 */
	private String netBusiness;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getNetTime() {
		return netTime;
	}

	public void setNetTime(String netTime) {
		this.netTime = netTime;
	}

	public String getNetFlow() {
		return netFlow;
	}

	public void setNetFlow(String netFlow) {
		this.netFlow = netFlow;
	}

	public String getNetFee() {
		return netFee;
	}

	public void setNetFee(String netFee) {
		this.netFee = netFee;
	}

	public String getNetTimeCost() {
		return netTimeCost;
	}

	public void setNetTimeCost(String netTimeCost) {
		this.netTimeCost = netTimeCost;
	}

	public String getNetArea() {
		return netArea;
	}

	public void setNetArea(String netArea) {
		this.netArea = netArea;
	}

	public String getNetBusiness() {
		return netBusiness;
	}

	public void setNetBusiness(String netBusiness) {
		this.netBusiness = netBusiness;
	}

	@Override
	public String toString() {
		return "TelecomShanxi1Flow [taskid=" + taskid + ", netType=" + netType + ", netTime=" + netTime + ", netFlow="
				+ netFlow + ", netFee=" + netFee + ", netTimeCost=" + netTimeCost + ", netArea=" + netArea
				+ ", netBusiness=" + netBusiness + "]";
	}

	public TelecomShanxi1Flow(String taskid, String netType, String netTime, String netFlow, String netFee,
			String netTimeCost, String netArea, String netBusiness) {
		super();
		this.taskid = taskid;
		this.netType = netType;
		this.netTime = netTime;
		this.netFlow = netFlow;
		this.netFee = netFee;
		this.netTimeCost = netTimeCost;
		this.netArea = netArea;
		this.netBusiness = netBusiness;
	}

	public TelecomShanxi1Flow() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}