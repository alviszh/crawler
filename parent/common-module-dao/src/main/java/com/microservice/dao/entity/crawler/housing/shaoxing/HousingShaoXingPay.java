package com.microservice.dao.entity.crawler.housing.shaoxing;

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
@Table(name="housing_shaoxing_pay")
public class HousingShaoXingPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name="taskid")
	private String taskid;		
	//记账日期
	@Column(name="jzrq")
	private String jzrq;		
	//归集和提取业务类型
	@Column(name="gjhtqywlx")
	private String gjhtqywlx;		
	//发生额
	@Column(name="fse")
	private String fse;		
	//发生利息额
	@Column(name="fslxe")
	private String fslxe;		
	//提取原因
	@Column(name="tqyy")
	private String tqyy;		
	//提取方式
	@Column(name="tqfs")
	private String tqfs;		
	//账户余额
	@Column(name="zhye")
	private String zhye;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getJzrq() {
		return jzrq;
	}
	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}
	public String getGjhtqywlx() {
		return gjhtqywlx;
	}
	public void setGjhtqywlx(String gjhtqywlx) {
		this.gjhtqywlx = gjhtqywlx;
	}
	public String getFse() {
		return fse;
	}
	public void setFse(String fse) {
		this.fse = fse;
	}
	public String getFslxe() {
		return fslxe;
	}
	public void setFslxe(String fslxe) {
		this.fslxe = fslxe;
	}
	public String getTqyy() {
		return tqyy;
	}
	public void setTqyy(String tqyy) {
		this.tqyy = tqyy;
	}
	public String getTqfs() {
		return tqfs;
	}
	public void setTqfs(String tqfs) {
		this.tqfs = tqfs;
	}
	public String getZhye() {
		return zhye;
	}
	public void setZhye(String zhye) {
		this.zhye = zhye;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
