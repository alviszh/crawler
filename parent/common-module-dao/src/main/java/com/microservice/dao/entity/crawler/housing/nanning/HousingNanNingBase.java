package com.microservice.dao.entity.crawler.housing.nanning;

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
@Table(name="housing_nanning_base")
public class HousingNanNingBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//姓名
	@Column(name="name")
	private String name;		
	//单位名称
	@Column(name="dwmc")
	private String dwmc;		
	//联系电话
	@Column(name="lxdh")
	private String lxdh;		
	//身份证号
	@Column(name="cardid")
	private String cardid;		
	//单位账号
	@Column(name="dwzh")
	private String dwzh;		
	//银行账号
	@Column(name="yhzh")
	private String yhzh;		
	//开户日期
	@Column(name="khrq")
	private String khrq;		
	//个人编号
	@Column(name="grbh")
	private String grbh;		
	//银行名称
	@Column(name="yhmc")
	private String yhmc;		
	//缴存基数
	@Column(name="jcjs")
	private String jcjs;		
	//个人缴存额
	@Column(name="grjce")
	private String grjce;		
	//余额
	@Column(name="yue")
	private String yue;		
	//缴存比例
	@Column(name="jcbl")
	private String jcbl;		
	//单位缴存额
	@Column(name="dwjce")
	private String dwjce;		
	//缴存状态
	@Column(name="jczt")
	private String jczt;		
	//当前缴存年月
	@Column(name="dqjcny")
	private String dqjcny;		
	//月缴存合计
	@Column(name="yjchj")
	private String yjchj;		
	//账户状态
	@Column(name="zhzt")
	private String zhzt;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getDwzh() {
		return dwzh;
	}
	public void setDwzh(String dwzh) {
		this.dwzh = dwzh;
	}
	public String getYhzh() {
		return yhzh;
	}
	public void setYhzh(String yhzh) {
		this.yhzh = yhzh;
	}
	public String getKhrq() {
		return khrq;
	}
	public void setKhrq(String khrq) {
		this.khrq = khrq;
	}
	public String getGrbh() {
		return grbh;
	}
	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}
	public String getYhmc() {
		return yhmc;
	}
	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}
	
	
	public String getJcjs() {
		return jcjs;
	}
	public void setJcjs(String jcjs) {
		this.jcjs = jcjs;
	}
	public String getGrjce() {
		return grjce;
	}
	public void setGrjce(String grjce) {
		this.grjce = grjce;
	}
	public String getDqjcny() {
		return dqjcny;
	}
	public void setDqjcny(String dqjcny) {
		this.dqjcny = dqjcny;
	}
	public String getYue() {
		return yue;
	}
	public void setYue(String yue) {
		this.yue = yue;
	}
	public String getJcbl() {
		return jcbl;
	}
	public void setJcbl(String jcbl) {
		this.jcbl = jcbl;
	}
	public String getDwjce() {
		return dwjce;
	}
	public void setDwjce(String dwjce) {
		this.dwjce = dwjce;
	}
	public String getJczt() {
		return jczt;
	}
	public void setJczt(String jczt) {
		this.jczt = jczt;
	}
	
	public String getYjchj() {
		return yjchj;
	}
	public void setYjchj(String yjchj) {
		this.yjchj = yjchj;
	}
	public String getZhzt() {
		return zhzt;
	}
	public void setZhzt(String zhzt) {
		this.zhzt = zhzt;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
	
}
