package com.microservice.dao.entity.crawler.housing.nantong;

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
@Table(name="housing_nantong_pay")
public class HousingNanTongPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//业务日期
	@Column(name="ywrq")
	private String ywrq;		
	//汇缴年月
	@Column(name="hjny")
	private String hjny;		
	//摘要
	@Column(name="zy")
	private String zy;		
	//收入
	@Column(name="sr")
	private String sr;		
	//支出
	@Column(name="zc")
	private String zc;		
	//余额
	@Column(name="ye")
	private String ye;		
	//补贴收入
	@Column(name="btsr")
	private String btsr;		
	//补贴支出
	@Column(name="btzc")
	private String btzc;		
	//补贴余额
	@Column(name="btye")
	private String btye;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getYwrq() {
		return ywrq;
	}
	public void setYwrq(String ywrq) {
		this.ywrq = ywrq;
	}
	public String getHjny() {
		return hjny;
	}
	public void setHjny(String hjny) {
		this.hjny = hjny;
	}
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getSr() {
		return sr;
	}
	public void setSr(String sr) {
		this.sr = sr;
	}
	public String getZc() {
		return zc;
	}
	public void setZc(String zc) {
		this.zc = zc;
	}
	public String getYe() {
		return ye;
	}
	public void setYe(String ye) {
		this.ye = ye;
	}
	public String getBtsr() {
		return btsr;
	}
	public void setBtsr(String btsr) {
		this.btsr = btsr;
	}
	public String getBtzc() {
		return btzc;
	}
	public void setBtzc(String btzc) {
		this.btzc = btzc;
	}
	public String getBtye() {
		return btye;
	}
	public void setBtye(String btye) {
		this.btye = btye;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}
