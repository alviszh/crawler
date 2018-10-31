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
@Table(name="housing_xuancheng_pay")
public class HousingxuanchengPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//日期
	@Column(name="rq")
	private String rq;		
	//摘要
	@Column(name="zy")
	private String zy;		
	//借方金额
	@Column(name="jfje")
	private String jfje;		
	//贷方金额
	@Column(name="dfje")
	private String dfje;		
	//金额
	@Column(name="je")
	private String je;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getJfje() {
		return jfje;
	}
	public void setJfje(String jfje) {
		this.jfje = jfje;
	}
	public String getDfje() {
		return dfje;
	}
	public void setDfje(String dfje) {
		this.dfje = dfje;
	}
	public String getJe() {
		return je;
	}
	public void setJe(String je) {
		this.je = je;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
}
