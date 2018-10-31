package com.microservice.dao.entity.crawler.insurance.kaifeng;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_kaifeng_baseinfo")
public class InsurancekaifengBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 个人编号 */
	@Column(name="grbh")
	private String grbh;
	
	/** 职工姓名 */
	@Column(name="zgxm")
	private String zgxm;
	
	/** 性别 */
	@Column(name="xb")
	private String xb;
	
	/** 民族 */
	@Column(name="mz")
	private String mz;
	
	/** 人员状态 */
	@Column(name="ryzt")
	private String ryzt;
	
	/** 参加工作时间 */
	@Column(name="cjgzsj")
	private String cjgzsj;
	
	/** 出生日期 */
	@Column(name="csrq")
	private String csrq;
	
	/** 身份证号码*/
	@Column(name="sfzhm")
	private String sfzhm;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getGrbh() {
		return grbh;
	}

	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}

	public String getZgxm() {
		return zgxm;
	}

	public void setZgxm(String zgxm) {
		this.zgxm = zgxm;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getRyzt() {
		return ryzt;
	}

	public void setRyzt(String ryzt) {
		this.ryzt = ryzt;
	}

	public String getCjgzsj() {
		return cjgzsj;
	}

	public void setCjgzsj(String cjgzsj) {
		this.cjgzsj = cjgzsj;
	}

	public String getCsrq() {
		return csrq;
	}

	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}

	public String getSfzhm() {
		return sfzhm;
	}

	public void setSfzhm(String sfzhm) {
		this.sfzhm = sfzhm;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}