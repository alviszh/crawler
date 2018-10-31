package com.microservice.dao.entity.crawler.housing.liuan;

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
@Table(name="housing_liuan_base")
public class HousingliuanBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name="taskid")
	private String taskid;		
	//用户名
	@Column(name="yhm")
	private String yhm;		
	//出生日期
	@Column(name="csrq")
	private String csrq;		
	//出生地
	@Column(name="csd")
	private String csd;		
	//居住地
	@Column(name="jzd")
	private String jzd;		
	//真实姓名
	@Column(name="zsxm")
	private String zsxm;		
	//证件类型
	@Column(name="zjlx")
	private String zjlx;		
	//证件号码
	@Column(name="zjhm")
	private String zjhm;		
	//性别
	@Column(name="xb")
	private String xb;		
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
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	public String getCsd() {
		return csd;
	}
	public void setCsd(String csd) {
		this.csd = csd;
	}
	public String getJzd() {
		return jzd;
	}
	public void setJzd(String jzd) {
		this.jzd = jzd;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getZjlx() {
		return zjlx;
	}
	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}
	public String getZjhm() {
		return zjhm;
	}
	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
	
}
