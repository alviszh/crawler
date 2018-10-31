package com.microservice.dao.entity.crawler.housing.yuncheng;

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
@Table(name="housing_yuncheng_pay")
public class HousingyunchengPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//日期
	@Column(name="rq")
	private String rq;		
	//摘要
	@Column(name="zy")
	private String zy;		
	//借方
	@Column(name="jf")
	private String jf;		
	//贷方
	@Column(name="df")
	private String df;		
	//余额
	@Column(name="ye")
	private String ye;
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
	public String getJf() {
		return jf;
	}
	public void setJf(String jf) {
		this.jf = jf;
	}
	public String getDf() {
		return df;
	}
	public void setDf(String df) {
		this.df = df;
	}
	public String getYe() {
		return ye;
	}
	public void setYe(String ye) {
		this.ye = ye;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
	
}
