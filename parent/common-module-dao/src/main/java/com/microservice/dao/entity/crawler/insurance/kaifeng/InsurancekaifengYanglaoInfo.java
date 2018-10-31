package com.microservice.dao.entity.crawler.insurance.kaifeng;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_kaifeng_yanglao_info")
public class InsurancekaifengYanglaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;

	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 起止年月 */
	@Column(name="year")
	private String year;
	
	/** 单位编号 */
	@Column(name="dwbh")
	private String dwbh;
	
	/** 单位名称 */
	@Column(name="dwmc")
	private String dwmc;
	
	/** 申报工资 */
	@Column(name="sbgz")
	private String sbgz;
	
	/** 缴费基数*/
	@Column(name="jfjs")
	private String jfjs;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDwbh() {
		return dwbh;
	}

	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getSbgz() {
		return sbgz;
	}

	public void setSbgz(String sbgz) {
		this.sbgz = sbgz;
	}

	public String getJfjs() {
		return jfjs;
	}

	public void setJfjs(String jfjs) {
		this.jfjs = jfjs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}