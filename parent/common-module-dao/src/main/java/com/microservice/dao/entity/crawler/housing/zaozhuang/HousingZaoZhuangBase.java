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
@Table(name="housing_zaozhuang_base")
public class HousingZaoZhuangBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name="taskid")
	private String taskid;
	//姓名
	@Column(name="xm")
	private String xm;
	//出生年月
	@Column(name="csny")
	private String csny;
	//性别
	@Column(name="xb")
	private String xb;
	//证件类型
	@Column(name="zjlx")
	private String zjlx;
	//证件号码
	@Column(name="zjhm")
	private String zjhm;
	//手机号码
	@Column(name="sjhm")
	private String sjhm;
	//固定电话号码
	@Column(name="gddhhm")
	private String gddhhm;
	//邮政编码
	@Column(name="yzbm")
	private String yzbm;
	//家庭月收入
	@Column(name="jtysr")
	private String jtysr;
	//家庭住址
	@Column(name="jtzz")
	private String jtzz;
	//婚姻状况
	@Column(name="hyzk")
	private String hyzk;
	//贷款情况
	@Column(name="dkqk")
	private String dkqk;
	//账户账号
	@Column(name="zhzh")
	private String zhzh;
	//账户状态
	@Column(name="zhzt")
	private String zhzt;
	//账户余额
	@Column(name="zhye")
	private String zhye;
	//开户日期
	@Column(name="khrq")
	private String khrq;
	//单位名称
	@Column(name="dwmc")
	private String dwmc;
	//单位账号
	@Column(name="dwzh")
	private String dwzh;
	
	//缴存比例
	@Column(name="jcbl")
	private String jcbl;
	//个人缴存基数
	@Column(name="grjcjs")
	private String grjcjs;
	//月缴存额
	@Column(name="yjce")
	private String yjce;
	//开户行
	@Column(name="khh")
	private String khh;
	//个人存款账户号码
	@Column(name="grckzhhm")
	private String grckzhhm;
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
	public String getCsny() {
		return csny;
	}
	public void setCsny(String csny) {
		this.csny = csny;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
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
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public String getGddhhm() {
		return gddhhm;
	}
	public void setGddhhm(String gddhhm) {
		this.gddhhm = gddhhm;
	}
	public String getYzbm() {
		return yzbm;
	}
	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}
	public String getJtysr() {
		return jtysr;
	}
	public void setJtysr(String jtysr) {
		this.jtysr = jtysr;
	}
	public String getJtzz() {
		return jtzz;
	}
	public void setJtzz(String jtzz) {
		this.jtzz = jtzz;
	}
	public String getHyzk() {
		return hyzk;
	}
	public void setHyzk(String hyzk) {
		this.hyzk = hyzk;
	}
	public String getDkqk() {
		return dkqk;
	}
	public void setDkqk(String dkqk) {
		this.dkqk = dkqk;
	}
	public String getZhzh() {
		return zhzh;
	}
	public void setZhzh(String zhzh) {
		this.zhzh = zhzh;
	}
	public String getZhzt() {
		return zhzt;
	}
	public void setZhzt(String zhzt) {
		this.zhzt = zhzt;
	}
	public String getZhye() {
		return zhye;
	}
	public void setZhye(String zhye) {
		this.zhye = zhye;
	}
	public String getKhrq() {
		return khrq;
	}
	public void setKhrq(String khrq) {
		this.khrq = khrq;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getDwzh() {
		return dwzh;
	}
	public void setDwzh(String dwzh) {
		this.dwzh = dwzh;
	}
	public String getJcbl() {
		return jcbl;
	}
	public void setJcbl(String jcbl) {
		this.jcbl = jcbl;
	}
	public String getGrjcjs() {
		return grjcjs;
	}
	public void setGrjcjs(String grjcjs) {
		this.grjcjs = grjcjs;
	}
	public String getYjce() {
		return yjce;
	}
	public void setYjce(String yjce) {
		this.yjce = yjce;
	}
	public String getKhh() {
		return khh;
	}
	public void setKhh(String khh) {
		this.khh = khh;
	}
	public String getGrckzhhm() {
		return grckzhhm;
	}
	public void setGrckzhhm(String grckzhhm) {
		this.grckzhhm = grckzhhm;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
