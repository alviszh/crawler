package com.microservice.dao.entity.crawler.insurance.fuyang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 德州社保 医保信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_fuyang_yibao_info")
public class InsurancefuyangYibaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 个人编号 */
	@Column(name="grbh")
	private String grbh;
	
	/** 日期 */
	@Column(name="rq")
	private String rq;
	
	/** 摘要*/
	@Column(name="zy")
	private String zy;
	
	/** 收入*/
	@Column(name="sr")
	private String sr;
	
	/** 支出*/
	@Column(name="zc")
	private String zc;
	
	/** 变更金额*/
	@Column(name="bgje")
	private String bgje;
	
	/** 余额*/
	@Column(name="ye")
	private String ye;

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

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public String getSr() {
		return sr;
	}

	public void setSr(String sr) {
		this.sr = sr;
	}

	public String getZc() {
		return zc;
	}

	public void setZc(String zc) {
		this.zc = zc;
	}

	public String getBgje() {
		return bgje;
	}

	public void setBgje(String bgje) {
		this.bgje = bgje;
	}

	public String getYe() {
		return ye;
	}

	public void setYe(String ye) {
		this.ye = ye;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}