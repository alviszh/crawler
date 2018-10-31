package com.microservice.dao.entity.crawler.insurance.fuyang;

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
@Table(name="insurance_fuyang_baseinfo")
public class InsurancefuyangBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 个人编号 */
	@Column(name="grbh")
	private String grbh;
	
	/** 姓名 */
	@Column(name="xm")
	private String xm;
	
	/** 性别 */
	@Column(name="xb")
	private String xb;
	
	/** 身份证号 */
	@Column(name="sfzh")
	private String sfzh;
	
	/** 出生年月 */
	@Column(name="csny")
	private String csny;
	
	/** 民族 */
	@Column(name="mz")
	private String mz;
	
	/** 参加工作时间 */
	@Column(name="cjgzsj")
	private String cjgzsj;
	
	/**人员状态 */
	@Column(name="ryzt")
	private String ryzt;
	
	/** 个人身份 */
	@Column(name="grsf")
	private String grsf;
	
	/** 医疗待遇类别 */
	@Column(name="yldylb")
	private String yldylb;
	
	/** 医保卡号 */
	@Column(name="ybkh")
	private String ybkh;
	
	/** 退休时间 */
	@Column(name="txsj")
	private String txsj;

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

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getCsny() {
		return csny;
	}

	public void setCsny(String csny) {
		this.csny = csny;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getCjgzsj() {
		return cjgzsj;
	}

	public void setCjgzsj(String cjgzsj) {
		this.cjgzsj = cjgzsj;
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

	public String getYldylb() {
		return yldylb;
	}

	public void setYldylb(String yldylb) {
		this.yldylb = yldylb;
	}

	public String getYbkh() {
		return ybkh;
	}

	public void setYbkh(String ybkh) {
		this.ybkh = ybkh;
	}

	public String getTxsj() {
		return txsj;
	}

	public void setTxsj(String txsj) {
		this.txsj = txsj;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}