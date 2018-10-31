package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 电信流量详单
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_flow" ,indexes = {@Index(name = "index_telecom_chongqing_flow_taskid", columnList = "taskid")})
public class TelecomChongqingFlow extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;

	/**
	 * 开始时间
	 */
	private String netTime;
	
	/**
	 * 上网时长
	 */
	private String netTimeCost;
	
	/**
	 * 流量(KB)
	 */
	private String netFlow;
	
	/**
	 * 网络类型
	 */
	private String netType;
	
	/**
	 * 通信地点
	 */
	private String netArea;
	
	/**
	 * 业务类型
	 */
	private String netBusiness;
	
	/**
	 * 费用（元）	
	 */
	private String netFee;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNetTime() {
		return netTime;
	}

	public void setNetTime(String netTime) {
		this.netTime = netTime;
	}

	public String getNetTimeCost() {
		return netTimeCost;
	}

	public void setNetTimeCost(String netTimeCost) {
		this.netTimeCost = netTimeCost;
	}

	public String getNetFlow() {
		return netFlow;
	}

	public void setNetFlow(String netFlow) {
		this.netFlow = netFlow;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
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

	public String getNetFee() {
		return netFee;
	}

	public void setNetFee(String netFee) {
		this.netFee = netFee;
	}

	@Override
	public String toString() {
		return "TelecomChongqingFlow [taskid=" + taskid + ", netTime=" + netTime + ", netTimeCost=" + netTimeCost
				+ ", netFlow=" + netFlow + ", netType=" + netType + ", netArea=" + netArea + ", netBusiness="
				+ netBusiness + ", netFee=" + netFee + "]";
	}

	public TelecomChongqingFlow(String taskid, String netTime, String netTimeCost, String netFlow, String netType,
			String netArea, String netBusiness, String netFee) {
		super();
		this.taskid = taskid;
		this.netTime = netTime;
		this.netTimeCost = netTimeCost;
		this.netFlow = netFlow;
		this.netType = netType;
		this.netArea = netArea;
		this.netBusiness = netBusiness;
		this.netFee = netFee;
	}

	public TelecomChongqingFlow() {
		super();
		// TODO Auto-generated constructor stub
	}


}