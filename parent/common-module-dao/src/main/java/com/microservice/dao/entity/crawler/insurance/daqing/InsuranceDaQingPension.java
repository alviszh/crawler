package com.microservice.dao.entity.crawler.insurance.daqing;

import com.microservice.dao.entity.IdEntity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "insurance_daqing_pension",indexes = {@Index(name = "index_insurance_daqing_pension_taskid", columnList = "taskid")})
public class InsuranceDaQingPension extends IdEntity implements Serializable{
	private static final long serialVersionUID = 3845870656982563673L;
	private String taskid;
//	个人编号
	private String pernum;
//	结算期
	private String settleperiod;
//	养老待遇金额
	private String pensionsum;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPernum() {
		return pernum;
	}
	public void setPernum(String pernum) {
		this.pernum = pernum;
	}
	public String getSettleperiod() {
		return settleperiod;
	}
	public void setSettleperiod(String settleperiod) {
		this.settleperiod = settleperiod;
	}
	public String getPensionsum() {
		return pensionsum;
	}
	public void setPensionsum(String pensionsum) {
		this.pensionsum = pensionsum;
	}
}
