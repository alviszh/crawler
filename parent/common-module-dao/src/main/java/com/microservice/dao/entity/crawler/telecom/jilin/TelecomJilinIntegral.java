package com.microservice.dao.entity.crawler.telecom.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_jilin_integral", indexes = {@Index(name = "index_telecom_jilin_integral_taskid", columnList = "taskid")})
public class TelecomJilinIntegral extends IdEntity {

	private String taskid;
	private String integralDate;			//积分账期
	private String integralType;			//积分类型
	private String integralCount;			//积分值
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
	public String getIntegralType() {
		return integralType;
	}
	public void setIntegralType(String integralType) {
		this.integralType = integralType;
	}
	public String getIntegralCount() {
		return integralCount;
	}
	public void setIntegralCount(String integralCount) {
		this.integralCount = integralCount;
	}
	@Override
	public String toString() {
		return "TelecomJilinIntegral [taskid=" + taskid + ", integralDate=" + integralDate + ", integralType="
				+ integralType + ", integralCount=" + integralCount + "]";
	}
	
	
	

}