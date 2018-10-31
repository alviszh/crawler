package com.microservice.dao.entity.crawler.bank.pabchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="pab_credit_china_transflow",indexes = {@Index(name = "index_pab_credit_china_transflow_taskid", columnList = "taskid")})
public class Pab_credit_ChinaTransFlow  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 交易时间*/
	@Column(name="jysj")
	private String jysj;
	
	/** 记账日期*/
	@Column(name="jzrq")
	private String jzrq;
	
	/** 卡号末四位*/
	@Column(name="khmsw")
	private String khmsw;
	
	/** 交易摘要*/
	@Column(name="jyzy")
	private String jyzy;
	
	/** 人民币金额*/
	@Column(name="rmbje")
	private String rmbje;
	
	public String getJysj() {
		return jysj;
	}
	public void setJysj(String jysj) {
		this.jysj = jysj;
	}
	public String getJzrq() {
		return jzrq;
	}
	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}
	public String getKhmsw() {
		return khmsw;
	}
	public void setKhmsw(String khmsw) {
		this.khmsw = khmsw;
	}
	public String getJyzy() {
		return jyzy;
	}
	public void setJyzy(String jyzy) {
		this.jyzy = jyzy;
	}
	public String getRmbje() {
		return rmbje;
	}
	public void setRmbje(String rmbje) {
		this.rmbje = rmbje;
	}
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
	
}
