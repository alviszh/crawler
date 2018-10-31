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
@Table(name="housing_shaoxing_base")
public class HousingShaoXingBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name="taskid")
	private String taskid;		
	//姓名
	@Column(name="xingming")
	private String xingming;		
	//证件类型
	@Column(name="zjlx")
	private String zjlx;		
	//固定电话号码
	@Column(name="gddhhm")
	private String gddhhm;		
	//家庭住址
	@Column(name="jtzz")
	private String jtzz;		
	//出生年月
	@Column(name="csny")
	private String csny;		
	//证件号码
	@Column(name="zjhm")
	private String zjhm;		
	//邮政编码
	@Column(name="yzbm")
	private String yzbm;		
	//婚姻状况
	@Column(name="hyzk")
	private String hyzk;		
	//性别
	@Column(name="xingbie")
	private String xingbie;		
	//手机号码
	@Column(name="sjhm")
	private String sjhm;		
	//家庭月收入
	@Column(name="jtysr")
	private String jtysr;		
	//开户日期
	@Column(name="djrq")
	private String djrq;		
	//个人缴存基数
	@Column(name="grjcjs")
	private String grjcjs;		
	//个人存款账户号码
	@Column(name="grckzhhm")
	private String grckzhhm;		
	//单位名称
	@Column(name="dwmc")
	private String dwmc;		
	//月缴存额
	@Column(name="yjce")
	private String yjce;		
	//补贴比例
	@Column(name="btbl")
	private String btbl;		
	//缴存比例
	@Column(name="jcbl")
	private String jcbl;		
	//个人存款账户开户银行名称
	@Column(name="grckzhkhyhmc")
	private String grckzhkhyhmc;		
	//月补贴金额
	@Column(name="ybtje")
	private String ybtje;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getXingming() {
		return xingming;
	}
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getZjlx() {
		return zjlx;
	}
	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}
	public String getGddhhm() {
		return gddhhm;
	}
	public void setGddhhm(String gddhhm) {
		this.gddhhm = gddhhm;
	}
	public String getJtzz() {
		return jtzz;
	}
	public void setJtzz(String jtzz) {
		this.jtzz = jtzz;
	}
	public String getCsny() {
		return csny;
	}
	public void setCsny(String csny) {
		this.csny = csny;
	}
	public String getZjhm() {
		return zjhm;
	}
	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}
	public String getYzbm() {
		return yzbm;
	}
	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}
	public String getHyzk() {
		return hyzk;
	}
	public void setHyzk(String hyzk) {
		this.hyzk = hyzk;
	}
	public String getXingbie() {
		return xingbie;
	}
	public void setXingbie(String xingbie) {
		this.xingbie = xingbie;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public String getJtysr() {
		return jtysr;
	}
	public void setJtysr(String jtysr) {
		this.jtysr = jtysr;
	}
	public String getDjrq() {
		return djrq;
	}
	public void setDjrq(String djrq) {
		this.djrq = djrq;
	}
	public String getGrjcjs() {
		return grjcjs;
	}
	public void setGrjcjs(String grjcjs) {
		this.grjcjs = grjcjs;
	}
	public String getGrckzhhm() {
		return grckzhhm;
	}
	public void setGrckzhhm(String grckzhhm) {
		this.grckzhhm = grckzhhm;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getYjce() {
		return yjce;
	}
	public void setYjce(String yjce) {
		this.yjce = yjce;
	}
	public String getBtbl() {
		return btbl;
	}
	public void setBtbl(String btbl) {
		this.btbl = btbl;
	}
	public String getJcbl() {
		return jcbl;
	}
	public void setJcbl(String jcbl) {
		this.jcbl = jcbl;
	}
	public String getGrckzhkhyhmc() {
		return grckzhkhyhmc;
	}
	public void setGrckzhkhyhmc(String grckzhkhyhmc) {
		this.grckzhkhyhmc = grckzhkhyhmc;
	}
	public String getYbtje() {
		return ybtje;
	}
	public void setYbtje(String ybtje) {
		this.ybtje = ybtje;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
}
