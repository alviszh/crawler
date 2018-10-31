package com.microservice.dao.entity.crawler.housing.huangshan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45 
 */
@Entity
@Table(name="housing_huangshan_pay",indexes = {@Index(name = "index_housing_huangshan_pay_taskid", columnList = "taskid")})
public class HousinghuangshanPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//交易日期
	@Column(name="jyrq")
	private String jyrq;		
	//个人账号
	@Column(name="grzh")
	private String grzh;		
	//姓名
	@Column(name="xm")
	private String xm;		
	//发生额
	@Column(name="fse")
	private String fse;		
	//余额
	@Column(name="ye")
	private String ye;		
	//开始年月
	@Column(name="ksny")
	private String ksny;		
	//终止年月
	@Column(name="zzny")
	private String zzny;		
	//备注
	@Column(name="bz")
	private String bz;		
	//入账状态
	@Column(name="rzzt")
	private String rzzt;
	//业务类型
	@Column(name="ywlx")
	private String ywlx;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getJyrq() {
		return jyrq;
	}
	public void setJyrq(String jyrq) {
		this.jyrq = jyrq;
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
	public String getFse() {
		return fse;
	}
	public void setFse(String fse) {
		this.fse = fse;
	}
	public String getYe() {
		return ye;
	}
	public void setYe(String ye) {
		this.ye = ye;
	}
	public String getKsny() {
		return ksny;
	}
	public void setKsny(String ksny) {
		this.ksny = ksny;
	}
	public String getZzny() {
		return zzny;
	}
	public void setZzny(String zzny) {
		this.zzny = zzny;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getRzzt() {
		return rzzt;
	}
	public void setRzzt(String rzzt) {
		this.rzzt = rzzt;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	
	
	
}
