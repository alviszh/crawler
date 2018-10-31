package com.microservice.dao.entity.crawler.telecom.shandong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_shandong_integral", indexes = {@Index(name = "index_telecom_shandong_integral_taskid", columnList = "taskid")})
public class TelecomShandongIntegral extends IdEntity {

	private String taskid;
	private String integralDate;			//日期
	private String integralCount;			//调整积分
	private String integralSum;				//积分余额
	private String integralSource;			//积分来源
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getIntegralDate() {
		return integralDate;
	}
	public void setIntegralDate(String integralDate) {
		this.integralDate = integralDate;
	}
	public String getIntegralCount() {
		return integralCount;
	}
	public void setIntegralCount(String integralCount) {
		this.integralCount = integralCount;
	}
	public String getIntegralSum() {
		return integralSum;
	}
	public void setIntegralSum(String integralSum) {
		this.integralSum = integralSum;
	}
	public String getIntegralSource() {
		return integralSource;
	}
	public void setIntegralSource(String integralSource) {
		this.integralSource = integralSource;
	}
	@Override
	public String toString() {
		return "TelecomShandongIntegral [taskid=" + taskid + ", integralDate=" + integralDate + ", integralCount="
				+ integralCount + ", integralSum=" + integralSum + ", integralSource=" + integralSource + "]";
	}
	
}