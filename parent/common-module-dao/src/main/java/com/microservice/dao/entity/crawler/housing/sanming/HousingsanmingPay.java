package com.microservice.dao.entity.crawler.housing.sanming;

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
@Table(name="housing_sanming_pay")
public class HousingsanmingPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name="taskid")
	private String taskid;		
	//账号
	@Column(name="zh")
	private String zh;		
	//日期
	@Column(name="rq")
	private String rq;		
	//摘要
	@Column(name="zy")
	private String zy;		
	//支取金额
	@Column(name="zqje")
	private String zqje;		
	//缴存金额
	@Column(name="jcje")
	private String jcje;		
	//金额
	@Column(name="je")
	private String je;		
	//账别
	@Column(name="zb")
	private String zb;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getZh() {
		return zh;
	}
	public void setZh(String zh) {
		this.zh = zh;
	}
	public String getRq() {
		return rq;
	}
	public void setRq(String rq) {
		this.rq = rq;
	}
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getZqje() {
		return zqje;
	}
	public void setZqje(String zqje) {
		this.zqje = zqje;
	}
	public String getJcje() {
		return jcje;
	}
	public void setJcje(String jcje) {
		this.jcje = jcje;
	}
	public String getJe() {
		return je;
	}
	public void setJe(String je) {
		this.je = je;
	}
	public String getZb() {
		return zb;
	}
	public void setZb(String zb) {
		this.zb = zb;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
	
}
