package com.microservice.dao.entity.crawler.insurance.qianjiang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_qianjiang_info")
public class InsuranceqianjiangInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 单位名称 */
	@Column(name="dwmc")
	private String dwmc;
	
	/** 险种类型*/
	@Column(name="xzlx")
	private String xzlx;
	
	/** 缴费状态*/
	@Column(name="jfzt")
	private String jfzt;
	
	/** 缴费年月*/
	@Column(name="jfny")
	private String jfny;
	
	/** 缴费基数*/
	@Column(name="jfjs")
	private String jfjs;
	
	/** 缴费金额*/
	@Column(name="jfje")
	private String jfje;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getXzlx() {
		return xzlx;
	}

	public void setXzlx(String xzlx) {
		this.xzlx = xzlx;
	}

	public String getJfzt() {
		return jfzt;
	}

	public void setJfzt(String jfzt) {
		this.jfzt = jfzt;
	}

	public String getJfny() {
		return jfny;
	}

	public void setJfny(String jfny) {
		this.jfny = jfny;
	}

	public String getJfjs() {
		return jfjs;
	}

	public void setJfjs(String jfjs) {
		this.jfjs = jfjs;
	}

	public String getJfje() {
		return jfje;
	}

	public void setJfje(String jfje) {
		this.jfje = jfje;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}