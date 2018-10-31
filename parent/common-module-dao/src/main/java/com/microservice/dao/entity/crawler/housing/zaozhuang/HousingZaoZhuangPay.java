package com.microservice.dao.entity.crawler.housing.zaozhuang;

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
@Table(name="housing_zaozhuang_pay")
public class HousingZaoZhuangPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//个人账号
	@Column(name="grzh")
	private String grzh;		
	//姓名
	@Column(name="xm")
	private String xm;		
	//记账日期
	@Column(name="jzrq")
	private String jzrq;		
	//归集和提取业务类型
	@Column(name="gjhtqywlx")
	private String gjhtqywlx;		
	//发生额
	@Column(name="fse")
	private String fse;		
	//当年归集发生额
	@Column(name="dngjfse")
	private String dngjfse;		
	//上年结转发生额
	@Column(name="snjzfse")
	private String snjzfse;		
	//发生利息额
	@Column(name="fslxe")
	private String fslxe;		
	//业务摘要
	@Column(name="ywzy")
	private String ywzy;		
	//提取原因
	@Column(name="tqyy")
	private String tqyy;		
	//提取方式
	@Column(name="tqfs")
	private String tqfs;		
	//业务流水号
	@Column(name="ywlsh")
	private String ywlsh;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getGrzh() {
		return grzh;
	}
	public void setGrzh(String grzh) {
		this.grzh = grzh;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
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
	public String getDngjfse() {
		return dngjfse;
	}
	public void setDngjfse(String dngjfse) {
		this.dngjfse = dngjfse;
	}
	public String getSnjzfse() {
		return snjzfse;
	}
	public void setSnjzfse(String snjzfse) {
		this.snjzfse = snjzfse;
	}
	public String getFslxe() {
		return fslxe;
	}
	public void setFslxe(String fslxe) {
		this.fslxe = fslxe;
	}
	public String getYwzy() {
		return ywzy;
	}
	public void setYwzy(String ywzy) {
		this.ywzy = ywzy;
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
	public String getYwlsh() {
		return ywlsh;
	}
	public void setYwlsh(String ywlsh) {
		this.ywlsh = ywlsh;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
	
	
	
}
