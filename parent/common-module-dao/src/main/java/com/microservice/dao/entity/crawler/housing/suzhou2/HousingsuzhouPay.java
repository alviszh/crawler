package com.microservice.dao.entity.crawler.housing.suzhou2;

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
@Table(name="housing_suzhou2_pay")
public class HousingsuzhouPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name="taskid")
	private String taskid;		
	//日期
	@Column(name="rq")
	private String rq;		
	//摘要
	@Column(name="zy")
	private String zy;		
	//发生金额
	@Column(name="fsje")
	private String fsje;		
	//借贷标志
	@Column(name="jdbz")
	private String jdbz;		
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
	public String getFsje() {
		return fsje;
	}
	public void setFsje(String fsje) {
		this.fsje = fsje;
	}
	public String getJdbz() {
		return jdbz;
	}
	public void setJdbz(String jdbz) {
		this.jdbz = jdbz;
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
