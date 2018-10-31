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
@Table(name="housing_zhoushan_pay")
public class HousingZhouShanPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//产生日期
	@Column(name="csrq")
	private String csrq;		
	//所属年月
	@Column(name="ssny")
	private String ssny;		
	//单位金额
	@Column(name="dwje")
	private String dwje;		
	//个人金额
	@Column(name="grje")
	private String grje;		
	//缴交单位
	@Column(name="jjdw")
	private String jjdw;		
	//缴交原因
	@Column(name="jjyy")
	private String jjyy;		
	//单据状态
	@Column(name="djzt")
	private String djzt;		
	//结算方式
	@Column(name="jsfs")
	private String jsfs;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	public String getSsny() {
		return ssny;
	}
	public void setSsny(String ssny) {
		this.ssny = ssny;
	}
	public String getDwje() {
		return dwje;
	}
	public void setDwje(String dwje) {
		this.dwje = dwje;
	}
	public String getGrje() {
		return grje;
	}
	public void setGrje(String grje) {
		this.grje = grje;
	}
	public String getJjdw() {
		return jjdw;
	}
	public void setJjdw(String jjdw) {
		this.jjdw = jjdw;
	}
	public String getJjyy() {
		return jjyy;
	}
	public void setJjyy(String jjyy) {
		this.jjyy = jjyy;
	}
	public String getDjzt() {
		return djzt;
	}
	public void setDjzt(String djzt) {
		this.djzt = djzt;
	}
	public String getJsfs() {
		return jsfs;
	}
	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
