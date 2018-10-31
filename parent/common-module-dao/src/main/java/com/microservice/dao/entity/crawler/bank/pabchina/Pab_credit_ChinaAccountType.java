package com.microservice.dao.entity.crawler.bank.pabchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="pab_credit_china_accounttype",indexes = {@Index(name = "index_pab_credit_china_accounttype_taskid", columnList = "taskid")})
public class Pab_credit_ChinaAccountType extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 账单月份 */
	@Column(name="zdyf")
	private String zdyf;
	
	/** 人民币 */
	@Column(name="rmb")
	private String rmb;
	
	/** 美元 */
	@Column(name="my")
	private String my;
	
	private static final long serialVersionUID = -7225639204374657354L;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getZdyf() {
		return zdyf;
	}
	public void setZdyf(String zdyf) {
		this.zdyf = zdyf;
	}
	public String getRmb() {
		return rmb;
	}
	public void setRmb(String rmb) {
		this.rmb = rmb;
	}
	public String getMy() {
		return my;
	}
	public void setMy(String my) {
		this.my = my;
	}
	
	
	
}
