package com.microservice.dao.entity.crawler.insurance.haerbin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 德州社保 参保基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_haerbin_baseinfo")
public class InsuranceHaerbinBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;

	/** 个人编号 */
	@Column(name="grbh")
	private String grbh;
	
	/** 社保卡号 */
	@Column(name="sbkh")
	private String sbkh;
	
	/** 身份证号 */
	@Column(name="sfzh")
	private String sfzh;
	
	/** 姓名 */
	@Column(name="xm")
	private String xm;
	
	/** 性别 */
	@Column(name="xb")
	private String xb;
	
	/** 人员状态 */
	@Column(name="ryzt")
	private String ryzt;
	
	/** 个人身份 */
	@Column(name="grsf")
	private String grsf;
	
	/** 公务员标识 */
	@Column(name="gwybs")
	private String gwybs;

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

	public String getSbkh() {
		return sbkh;
	}

	public void setSbkh(String sbkh) {
		this.sbkh = sbkh;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getRyzt() {
		return ryzt;
	}

	public void setRyzt(String ryzt) {
		this.ryzt = ryzt;
	}

	public String getGrsf() {
		return grsf;
	}

	public void setGrsf(String grsf) {
		this.grsf = grsf;
	}

	public String getGwybs() {
		return gwybs;
	}

	public void setGwybs(String gwybs) {
		this.gwybs = gwybs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}