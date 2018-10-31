package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 
 * @Description: 我的话费实体   爬取里边的余额实体
 * @author sln
 * @date 2017年9月8日
 */
@Entity
@Table(name = "telecom_shanxi3_balance",indexes = {@Index(name = "index_telecom_shanxi3_balance_taskid", columnList = "taskid")})
public class TelecomShanxi3Balance extends IdEntity implements Serializable {
	private static final long serialVersionUID = 2996199189557179318L;
	private String commonbalance;
	private String specialbalance;
	private String totalbalance;
	private String taskid;
	public String getCommonbalance() {
		return commonbalance;
	}
	public void setCommonbalance(String commonbalance) {
		this.commonbalance = commonbalance;
	}
	public String getSpecialbalance() {
		return specialbalance;
	}
	public void setSpecialbalance(String specialbalance) {
		this.specialbalance = specialbalance;
	}
	public String getTotalbalance() {
		return totalbalance;
	}
	public void setTotalbalance(String totalbalance) {
		this.totalbalance = totalbalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public TelecomShanxi3Balance() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomShanxi3Balance(String commonbalance, String specialbalance, String totalbalance, String taskid) {
		super();
		this.commonbalance = commonbalance;
		this.specialbalance = specialbalance;
		this.totalbalance = totalbalance;
		this.taskid = taskid;
	}
}
