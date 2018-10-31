/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.housing.changzhou;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-02-27 11:21:48
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_changzhou_tranrows",indexes = {@Index(name = "index_housing_changzhou_tranrows_taskid", columnList = "taskid")})
public class HousingChangZhouTranRows  extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String SNNAME; //公司名称
	private String QRRQ;//入账日期
	private String LX; //利息
	private String HJNY;//汇缴年月
	private String YE; //余额
	private String CLLXNAME; //处理类型
	private String SPNAME;//姓名
	private String SNCODE;
	private String ZC; //支出
	private String SR; //收入
	
	private Integer userid;

	private String taskid;
	
	

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setSNNAME(String SNNAME) {
		this.SNNAME = SNNAME;
	}

	public String getSNNAME() {
		return SNNAME;
	}

	public void setQRRQ(String QRRQ) {
		this.QRRQ = QRRQ;
	}

	public String getQRRQ() {
		return QRRQ;
	}

	public void setLX(String LX) {
		this.LX = LX;
	}

	public String getLX() {
		return LX;
	}

	public void setHJNY(String HJNY) {
		this.HJNY = HJNY;
	}

	public String getHJNY() {
		return HJNY;
	}

	public void setYE(String YE) {
		this.YE = YE;
	}

	public String getYE() {
		return YE;
	}

	public void setCLLXNAME(String CLLXNAME) {
		this.CLLXNAME = CLLXNAME;
	}

	public String getCLLXNAME() {
		return CLLXNAME;
	}

	public void setSPNAME(String SPNAME) {
		this.SPNAME = SPNAME;
	}

	public String getSPNAME() {
		return SPNAME;
	}

	public void setSNCODE(String SNCODE) {
		this.SNCODE = SNCODE;
	}

	public String getSNCODE() {
		return SNCODE;
	}

	public void setZC(String ZC) {
		this.ZC = ZC;
	}

	public String getZC() {
		return ZC;
	}

	public void setSR(String SR) {
		this.SR = SR;
	}

	public String getSR() {
		return SR;
	}

	@Override
	public String toString() {
		return "HousingChangZhouTranRows [SNNAME=" + SNNAME + ", QRRQ=" + QRRQ + ", LX=" + LX + ", HJNY=" + HJNY
				+ ", YE=" + YE + ", CLLXNAME=" + CLLXNAME + ", SPNAME=" + SPNAME + ", SNCODE=" + SNCODE + ", ZC=" + ZC
				+ ", SR=" + SR + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

	
}