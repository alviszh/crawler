package com.microservice.dao.entity.crawler.insurance.nantong;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_nantong_baseinfo")
public class InsuranceNanTongBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 个人编号 */
	@Column(name="grbh")
	private String grbh;
	
	/** 性别 */
	@Column(name="xb")
	private String xb;
	
	/** 公务员标识 */
	@Column(name="gwybs")
	private String gwybs;
	
	/** 人员状态 */
	@Column(name="ryzt")
	private String ryzt;
	
	/** 单位名称 */
	@Column(name="dwmc")
	private String dwmc;
	
	/** 身份证号码*/
	@Column(name="sfzhm")
	private String sfzhm;
	
	/** 民族*/
	@Column(name="mz")
	private String mz;
	
	/** 社保卡号*/
	@Column(name="sbkh")
	private String sbkh;
	
	/** 单位编号*/
	@Column(name="dwbh")
	private String dwbh;
	
	/** 姓名*/
	@Column(name="xm")
	private String xm;
	
	/** 出生日期*/
	@Column(name="csrq")
	private String csrq;
	
	/** 离退休日期*/
	@Column(name="ltxrq")
	private String ltxrq;

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

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getGwybs() {
		return gwybs;
	}

	public void setGwybs(String gwybs) {
		this.gwybs = gwybs;
	}

	public String getRyzt() {
		return ryzt;
	}

	public void setRyzt(String ryzt) {
		this.ryzt = ryzt;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getSfzhm() {
		return sfzhm;
	}

	public void setSfzhm(String sfzhm) {
		this.sfzhm = sfzhm;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getSbkh() {
		return sbkh;
	}

	public void setSbkh(String sbkh) {
		this.sbkh = sbkh;
	}

	public String getDwbh() {
		return dwbh;
	}

	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getCsrq() {
		return csrq;
	}

	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}

	public String getLtxrq() {
		return ltxrq;
	}

	public void setLtxrq(String ltxrq) {
		this.ltxrq = ltxrq;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	
}