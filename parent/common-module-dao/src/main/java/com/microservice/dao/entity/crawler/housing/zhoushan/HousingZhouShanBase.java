package com.microservice.dao.entity.crawler.housing.zhoushan;

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
@Table(name="housing_zhoushan_base")
public class HousingZhouShanBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//职工账号
	@Column(name="zgzh")
	private String zgzh;		
	//证件号
	@Column(name="zjh")
	private String zjh;		
	//月工资额
	@Column(name="ygze")
	private String ygze;		
	//个人月汇缴额
	@Column(name="gryhje")
	private String gryhje;		
	//姓名
	@Column(name="xm")
	private String xm;		
	//手机号码
	@Column(name="sjhm")
	private String sjhm;		
	//单位比例
	@Column(name="dwbl")
	private String dwbl;		
	//单位月汇缴额
	@Column(name="dwyhje")
	private String dwyhje;		
	//账户状态
	@Column(name="zhzt")
	private String zhzt;		
	//开户日期
	@Column(name="khrq")
	private String khrq;		
	//个人比例
	@Column(name="grbl")
	private String grbl;		
	//账户余额
	@Column(name="zhye")
	private String zhye;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getZgzh() {
		return zgzh;
	}
	public void setZgzh(String zgzh) {
		this.zgzh = zgzh;
	}
	public String getZjh() {
		return zjh;
	}
	public void setZjh(String zjh) {
		this.zjh = zjh;
	}
	public String getYgze() {
		return ygze;
	}
	public void setYgze(String ygze) {
		this.ygze = ygze;
	}
	public String getGryhje() {
		return gryhje;
	}
	public void setGryhje(String gryhje) {
		this.gryhje = gryhje;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public String getDwbl() {
		return dwbl;
	}
	public void setDwbl(String dwbl) {
		this.dwbl = dwbl;
	}
	public String getDwyhje() {
		return dwyhje;
	}
	public void setDwyhje(String dwyhje) {
		this.dwyhje = dwyhje;
	}
	public String getZhzt() {
		return zhzt;
	}
	public void setZhzt(String zhzt) {
		this.zhzt = zhzt;
	}
	public String getKhrq() {
		return khrq;
	}
	public void setKhrq(String khrq) {
		this.khrq = khrq;
	}
	public String getGrbl() {
		return grbl;
	}
	public void setGrbl(String grbl) {
		this.grbl = grbl;
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
