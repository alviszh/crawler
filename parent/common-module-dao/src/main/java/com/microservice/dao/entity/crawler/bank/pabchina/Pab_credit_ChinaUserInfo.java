package com.microservice.dao.entity.crawler.bank.pabchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="pab_credit_china_userinfo",indexes = {@Index(name = "index_pab_credit_china_userinfo_taskid", columnList = "taskid")})
public class Pab_credit_ChinaUserInfo extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 卡号 */
	@Column(name="kh")
	private String kh;
	
	/** 卡种 */
	@Column(name="kz")
	private String kz;
	
	/** 卡面 */
	@Column(name="km")
	private String km;
	
	/** 每月账单日 */
	@Column(name="zdr")
	private String zdr;
	
	/** 信用额度 */
	@Column(name="xyed")
	private String xyed;
	
	/** 电子邮箱 */
	@Column(name="dzyx")
	private String dzyx;
	
	/** 住宅地址 */
	@Column(name="zzdz")
	private String zzdz;
	
	/** 单位电话 */
	@Column(name="dwdh")
	private String dwdh;
	
	/** 单位地址 */
	@Column(name="dwdz")
	private String dwdz;
	
	/** 账单地址 */
	@Column(name="zddz")
	private String zddz;
	
	/** 姓名 */
	@Column(name="xm")
	private String xm;
	
	/** 出生日期 */
	@Column(name="csrq")
	private String csrq;
	
	private static final long serialVersionUID = -7225639204374657354L;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getKh() {
		return kh;
	}
	public void setKh(String kh) {
		this.kh = kh;
	}
	public String getKz() {
		return kz;
	}
	public void setKz(String kz) {
		this.kz = kz;
	}
	public String getKm() {
		return km;
	}
	public void setKm(String km) {
		this.km = km;
	}
	public String getZdr() {
		return zdr;
	}
	public void setZdr(String zdr) {
		this.zdr = zdr;
	}
	public String getXyed() {
		return xyed;
	}
	public void setXyed(String xyed) {
		this.xyed = xyed;
	}
	public String getDzyx() {
		return dzyx;
	}
	public void setDzyx(String dzyx) {
		this.dzyx = dzyx;
	}
	public String getZzdz() {
		return zzdz;
	}
	public void setZzdz(String zzdz) {
		this.zzdz = zzdz;
	}
	public String getDwdh() {
		return dwdh;
	}
	public void setDwdh(String dwdh) {
		this.dwdh = dwdh;
	}
	public String getDwdz() {
		return dwdz;
	}
	public void setDwdz(String dwdz) {
		this.dwdz = dwdz;
	}
	public String getZddz() {
		return zddz;
	}
	public void setZddz(String zddz) {
		this.zddz = zddz;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	
}
