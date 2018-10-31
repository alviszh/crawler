package com.microservice.dao.entity.crawler.insurance.qianjiang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_qianjiang_baseinfo")
public class InsuranceqianjiangBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 姓名 */
	@Column(name="xm")
	private String xm;
	
	/** 身份证号码 */
	@Column(name="sfzhm")
	private String sfzhm;
	
	/** 社保卡号 */
	@Column(name="sbkh")
	private String sbkh;
	
	/** 参保状态 */
	@Column(name="cbzt")
	private String cbzt;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getSfzhm() {
		return sfzhm;
	}

	public void setSfzhm(String sfzhm) {
		this.sfzhm = sfzhm;
	}

	public String getSbkh() {
		return sbkh;
	}

	public void setSbkh(String sbkh) {
		this.sbkh = sbkh;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCbzt() {
		return cbzt;
	}

	public void setCbzt(String cbzt) {
		this.cbzt = cbzt;
	}
}