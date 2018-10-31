package com.microservice.dao.entity.crawler.telecom.neimenggu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 呼和浩特电信缴费信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_neimenggu_paymsg",indexes = {@Index(name = "index_telecom_neimenggu_paymsg_taskid", columnList = "taskid")})
public class TelecomNeimengguPayMsg  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**缴费日期*/     
	@Column(name="pay_date")
	private String paydate;
	
	/**交费金额*/     
	@Column(name="pay_money")
	private String paymoney;
	
	/**销账金额*/     
	@Column(name="xz_money")
	private String xzmoney;
	
	/**缴费地点*/     
	@Column(name="pay_addr")
	private String payaddr;
	
	/**缴费方式*/    
	@Column(name="pay_way")
	private String payway;
	
	/**违约金*/    
	@Column(name="break_money")
	private String breakmoney;

	
	
	public String getXzmoney() {
		return xzmoney;
	}

	public void setXzmoney(String xzmoney) {
		this.xzmoney = xzmoney;
	}

	public String getBreakmoney() {
		return breakmoney;
	}

	public void setBreakmoney(String breakmoney) {
		this.breakmoney = breakmoney;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}


	public String getPayaddr() {
		return payaddr;
	}

	public void setPayaddr(String payaddr) {
		this.payaddr = payaddr;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
}
