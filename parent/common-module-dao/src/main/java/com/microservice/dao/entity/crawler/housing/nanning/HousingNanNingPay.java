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
@Table(name="housing_nanning_pay")
public class HousingNanNingPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	
	//身份证号码
	@Column(name="cardid")
	private String cardid;		
	
	//记账日期
	@Column(name="jzrq")
	private String jzrq;		
	
	//业务类型
	@Column(name="ywlx")
	private String ywlx;		
	
	//汇交年月
	@Column(name="hjny")
	private String hjny;		
	
	//增加额
	@Column(name="zje")
	private String zje;		
	
	//减少额
	@Column(name="jse")
	private String jse;		
	
	//发生利息
	@Column(name="fslx")
	private String fslx;		
	
	//单位账号
	@Column(name="dwzh")
	private String dwzh;		
	
	//流水号
	@Column(name="lsh")
	private String lsh;		
	
	//提取原因
	@Column(name="tqyy")
	private String tqyy;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getJzrq() {
		return jzrq;
	}

	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getHjny() {
		return hjny;
	}

	public void setHjny(String hjny) {
		this.hjny = hjny;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getJse() {
		return jse;
	}

	public void setJse(String jse) {
		this.jse = jse;
	}

	public String getFslx() {
		return fslx;
	}

	public void setFslx(String fslx) {
		this.fslx = fslx;
	}

	public String getDwzh() {
		return dwzh;
	}

	public void setDwzh(String dwzh) {
		this.dwzh = dwzh;
	}

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public String getTqyy() {
		return tqyy;
	}

	public void setTqyy(String tqyy) {
		this.tqyy = tqyy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
}
