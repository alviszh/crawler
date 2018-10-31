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
@Table(name="housing_huangshan_base",indexes = {@Index(name = "index_housing_huangshan_base_taskid", columnList = "taskid")})
public class HousinghuangshanBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//个人账号
	@Column(name="grzh")
	private String grzh;		
	//姓名
	@Column(name="xm")
	private String xm;		
	//证件号码
	@Column(name="zjhm")
	private String zjhm;		
	//个人账户状态
	@Column(name="grzhzt")
	private String grzhzt;		
	//个人账号余额
	@Column(name="grzhye")
	private String grzhye;		
	//开户日期
	@Column(name="khrq")
	private String khrq;		
	//单位缴存比例
	@Column(name="dwjcbl")
	private String dwjcbl;		
	//个人缴存比例
	@Column(name="grjcbl")
	private String grjcbl;		
	//个人缴存基数
	@Column(name="grjcjs")
	private String grjcjs;		
	//月缴存总额
	@Column(name="yjcze")
	private String yjcze;		
	//单位月缴存额
	@Column(name="dwyjce")
	private String dwyjce;		
	//个人月缴存额
	@Column(name="gryjce")
	private String gryjce;		
	//缴至年月
	@Column(name="jzny")
	private String jzny;		
	//单位账号
	@Column(name="dwzh")
	private String dwzh;		
	//是否贷款
	@Column(name="sfdk")
	private String sfdk;		
	//手机号码
	@Column(name="sjhm")
	private String sjhm;		
	//借款合同号
	@Column(name="jkhth")
	private String jkhth;
	//单位名称
	@Column(name="dwmc")
	private String dwmc;
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
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
	public String getZjhm() {
		return zjhm;
	}
	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}
	public String getGrzhzt() {
		return grzhzt;
	}
	public void setGrzhzt(String grzhzt) {
		this.grzhzt = grzhzt;
	}
	public String getGrzhye() {
		return grzhye;
	}
	public void setGrzhye(String grzhye) {
		this.grzhye = grzhye;
	}
	public String getKhrq() {
		return khrq;
	}
	public void setKhrq(String khrq) {
		this.khrq = khrq;
	}
	public String getDwjcbl() {
		return dwjcbl;
	}
	public void setDwjcbl(String dwjcbl) {
		this.dwjcbl = dwjcbl;
	}
	public String getGrjcbl() {
		return grjcbl;
	}
	public void setGrjcbl(String grjcbl) {
		this.grjcbl = grjcbl;
	}
	public String getGrjcjs() {
		return grjcjs;
	}
	public void setGrjcjs(String grjcjs) {
		this.grjcjs = grjcjs;
	}
	public String getYjcze() {
		return yjcze;
	}
	public void setYjcze(String yjcze) {
		this.yjcze = yjcze;
	}
	public String getDwyjce() {
		return dwyjce;
	}
	public void setDwyjce(String dwyjce) {
		this.dwyjce = dwyjce;
	}
	public String getGryjce() {
		return gryjce;
	}
	public void setGryjce(String gryjce) {
		this.gryjce = gryjce;
	}
	public String getJzny() {
		return jzny;
	}
	public void setJzny(String jzny) {
		this.jzny = jzny;
	}
	public String getDwzh() {
		return dwzh;
	}
	public void setDwzh(String dwzh) {
		this.dwzh = dwzh;
	}
	public String getSfdk() {
		return sfdk;
	}
	public void setSfdk(String sfdk) {
		this.sfdk = sfdk;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public String getJkhth() {
		return jkhth;
	}
	public void setJkhth(String jkhth) {
		this.jkhth = jkhth;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
