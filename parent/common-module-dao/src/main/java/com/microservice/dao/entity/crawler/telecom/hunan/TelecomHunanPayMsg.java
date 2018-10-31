package com.microservice.dao.entity.crawler.telecom.hunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 湖南电信缴费信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_hunan_paymsg",indexes = {@Index(name = "index_telecom_hunan_paymsg_taskid", columnList = "taskid")})
public class TelecomHunanPayMsg  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**入账日期*/     
	@Column(name="pay_date")
	private String paydate;
	
	/**入账金额*/     
	@Column(name="pay_money")
	private String paymoney;
	
	
	/**使用范围*/     
	@Column(name="scope")
	private String scope;
	
	/**交费渠道*/    
	@Column(name="pay_ditch")
	private String payditch;
	
	/**交费方式*/    
	@Column(name="pay_way")
	private String payway;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getPayditch() {
		return payditch;
	}

	public void setPayditch(String payditch) {
		this.payditch = payditch;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}


	
}
