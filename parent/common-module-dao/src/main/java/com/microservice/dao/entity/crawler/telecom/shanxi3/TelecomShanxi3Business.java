package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  业务信息实体
 * @author sln
 * @date 2017年8月24日 上午10:03:00
 */
@Entity
@Table(name = "telecom_shanxi3_business",indexes = {@Index(name = "index_telecom_shanxi3_business_taskid", columnList = "taskid")})
public class TelecomShanxi3Business extends IdEntity implements Serializable {
	private static final long serialVersionUID = -6617753697113973283L;
	private String taskid;
//	套餐名称
	private String businessname;
//	接入号码
	private String accessnum;
//	服务区
	private String servicearea;
//	状态
	private String status;
//	账户名称
	private String accountname;
//	流量结转状态
	private String flowcarrystatus;
//	购买时间
	private String buytime;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getBusinessname() {
		return businessname;
	}
	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}
	public String getAccessnum() {
		return accessnum;
	}
	public void setAccessnum(String accessnum) {
		this.accessnum = accessnum;
	}
	public String getServicearea() {
		return servicearea;
	}
	public void setServicearea(String servicearea) {
		this.servicearea = servicearea;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public String getFlowcarrystatus() {
		return flowcarrystatus;
	}
	public void setFlowcarrystatus(String flowcarrystatus) {
		this.flowcarrystatus = flowcarrystatus;
	}
	public String getBuytime() {
		return buytime;
	}
	public void setBuytime(String buytime) {
		this.buytime = buytime;
	}
	public TelecomShanxi3Business() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomShanxi3Business(String taskid, String businessname, String accessnum, String servicearea,
			String status, String accountname, String flowcarrystatus, String buytime) {
		super();
		this.taskid = taskid;
		this.businessname = businessname;
		this.accessnum = accessnum;
		this.servicearea = servicearea;
		this.status = status;
		this.accountname = accountname;
		this.flowcarrystatus = flowcarrystatus;
		this.buytime = buytime;
	}
	

}

