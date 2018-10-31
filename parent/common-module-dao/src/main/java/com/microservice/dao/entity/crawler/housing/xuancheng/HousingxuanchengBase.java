package com.microservice.dao.entity.crawler.housing.xuancheng;

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
@Table(name="housing_xuancheng_base")
public class HousingxuanchengBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//单位代码
	@Column(name="dwdm")
	private String dwdm;		
	//个人代码
	@Column(name="grdm")
	private String grdm;		
	//单位名称
	@Column(name="dwmc")
	private String dwmc;		
	//个人姓名
	@Column(name="grxm")
	private String grxm;		
	//单位银行账号
	@Column(name="dwyhzh")
	private String dwyhzh;		
	//个人银行账号
	@Column(name="gryhzh")
	private String gryhzh;		
	//身份证号
	@Column(name="sfzh")
	private String sfzh;		
	//余额
	@Column(name="ye")
	private String ye;		
	//起缴月份
	@Column(name="qjyf")
	private String qjyf;		
	//缴至月份
	@Column(name="jzyf")
	private String jzyf;		
	//月应缴额
	@Column(name="yyje")
	private String yyje;		
	//账户状态
	@Column(name="zhzt")
	private String zhzt;		
	//单位缴存比例
	@Column(name="dwjcbl")
	private String dwjcbl;		
	//个人缴存比例
	@Column(name="grjcbl")
	private String grjcbl;		
	//委托银行
	@Column(name="wtyh")
	private String wtyh;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDwdm() {
		return dwdm;
	}
	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}
	public String getGrdm() {
		return grdm;
	}
	public void setGrdm(String grdm) {
		this.grdm = grdm;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getGrxm() {
		return grxm;
	}
	public void setGrxm(String grxm) {
		this.grxm = grxm;
	}
	public String getDwyhzh() {
		return dwyhzh;
	}
	public void setDwyhzh(String dwyhzh) {
		this.dwyhzh = dwyhzh;
	}
	public String getGryhzh() {
		return gryhzh;
	}
	public void setGryhzh(String gryhzh) {
		this.gryhzh = gryhzh;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public String getYe() {
		return ye;
	}
	public void setYe(String ye) {
		this.ye = ye;
	}
	public String getQjyf() {
		return qjyf;
	}
	public void setQjyf(String qjyf) {
		this.qjyf = qjyf;
	}
	public String getJzyf() {
		return jzyf;
	}
	public void setJzyf(String jzyf) {
		this.jzyf = jzyf;
	}
	public String getYyje() {
		return yyje;
	}
	public void setYyje(String yyje) {
		this.yyje = yyje;
	}
	public String getZhzt() {
		return zhzt;
	}
	public void setZhzt(String zhzt) {
		this.zhzt = zhzt;
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
	public String getWtyh() {
		return wtyh;
	}
	public void setWtyh(String wtyh) {
		this.wtyh = wtyh;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
	
}
