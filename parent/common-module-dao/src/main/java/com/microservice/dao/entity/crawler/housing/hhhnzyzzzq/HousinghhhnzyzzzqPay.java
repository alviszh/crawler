package com.microservice.dao.entity.crawler.housing.hhhnzyzzzq;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45 
 */
@Entity
@Table(name="housing_hhhnzyzzzq_pay",indexes = {@Index(name = "index_housing_hhhnzyzzzq_pay_taskid", columnList = "taskid")})
public class HousinghhhnzyzzzqPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	
	//公积金账号
	@Column(name="housingNumber")
	private String housingNumber;		
	
	//缴存年月
	@Column(name="month")
	private String month;		
	
	//摘要
	@Column(name="zy")
	private String zy;		
	
	//提取
	@Column(name="tq")
	private String tq;		
	
	//缴存
	@Column(name="jc")
	private String jc;		
	
	//上期余额
	@Column(name="sqye")
	private String sqye;		
	
	//本期余额
	@Column(name="bqye")
	private String bqye;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getHousingNumber() {
		return housingNumber;
	}

	public void setHousingNumber(String housingNumber) {
		this.housingNumber = housingNumber;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public String getTq() {
		return tq;
	}

	public void setTq(String tq) {
		this.tq = tq;
	}

	public String getJc() {
		return jc;
	}

	public void setJc(String jc) {
		this.jc = jc;
	}

	public String getSqye() {
		return sqye;
	}

	public void setSqye(String sqye) {
		this.sqye = sqye;
	}

	public String getBqye() {
		return bqye;
	}

	public void setBqye(String bqye) {
		this.bqye = bqye;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
	
	
	
}
