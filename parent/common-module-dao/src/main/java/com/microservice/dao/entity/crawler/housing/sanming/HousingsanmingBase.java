package com.microservice.dao.entity.crawler.housing.sanming;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45 
 */
@Entity
@Table(name="housing_sanming_base")
public class HousingsanmingBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//用户名
	@Column(name="yhm")
	private String yhm;		
	//姓名
	@Column(name="xm")
	private String xm;		
	//姓别
	@Column(name="xb")
	private String xb;		
	//身份证号
	@Column(name="sfzh")
	private String sfzh;		
	//出生日期
	@Column(name="csrq")
	private String csrq;		
	//所属行业
	@Column(name="sshy")
	private String sshy;		
	//单位名称
	@Column(name="dwmc")
	private String dwmc;		
	//民族
	@Column(name="mz")
	private String mz;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getYhm() {
		return yhm;
	}
	public void setYhm(String yhm) {
		this.yhm = yhm;
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
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	public String getSshy() {
		return sshy;
	}
	public void setSshy(String sshy) {
		this.sshy = sshy;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getMz() {
		return mz;
	}
	public void setMz(String mz) {
		this.mz = mz;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
