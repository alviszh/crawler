package com.microservice.dao.entity.crawler.insurance.nantong;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_nantong_shiye_info")
public class InsuranceNanTongShiyeInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;

	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 对应期 */
	@Column(name="dyq")
	private String dyq;
	
	/** 缴费单位名称 */
	@Column(name="jfdwmc")
	private String jfdwmc;
	
	/** 险种类型 */
	@Column(name="xzlx")
	private String xzlx;
	
	/** 缴费对象 */
	@Column(name="jfdx")
	private String jfdx;
	
	/** 缴费基数*/
	@Column(name="jfjs")
	private String jfjs;
	
	/** 缴纳金额*/
	@Column(name="jnje")
	private String jnje;
	
	/** 缴费标识*/
	@Column(name="jfbs")
	private String jfbs;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDyq() {
		return dyq;
	}

	public void setDyq(String dyq) {
		this.dyq = dyq;
	}

	public String getJfdwmc() {
		return jfdwmc;
	}

	public void setJfdwmc(String jfdwmc) {
		this.jfdwmc = jfdwmc;
	}

	public String getXzlx() {
		return xzlx;
	}

	public void setXzlx(String xzlx) {
		this.xzlx = xzlx;
	}

	public String getJfdx() {
		return jfdx;
	}

	public void setJfdx(String jfdx) {
		this.jfdx = jfdx;
	}

	public String getJfjs() {
		return jfjs;
	}

	public void setJfjs(String jfjs) {
		this.jfjs = jfjs;
	}

	public String getJnje() {
		return jnje;
	}

	public void setJnje(String jnje) {
		this.jnje = jnje;
	}

	public String getJfbs() {
		return jfbs;
	}

	public void setJfbs(String jfbs) {
		this.jfbs = jfbs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}