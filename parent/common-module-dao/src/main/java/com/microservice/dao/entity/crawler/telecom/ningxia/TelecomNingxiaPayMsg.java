package com.microservice.dao.entity.crawler.telecom.ningxia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 宁夏电信缴费信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_ningxia_paymsg",indexes = {@Index(name = "index_telecom_ningxia_paymsg_taskid", columnList = "taskid")})
public class TelecomNingxiaPayMsg  extends IdEntity{
	@Column(name="userid")
	private Integer userid;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
    ///////////////////////////////////////////////交易单查询/////////////////////////////////////////////
	/**缴费日期*/     //交易单查询
	@Column(name="pay_date")
	private String paydate;
	
	/**缴费金额*/     //交易单查询
	@Column(name="pay_money")
	private String paymoney;
	
	/**缴费地点*/     //交易单查询
	@Column(name="pay_addr")
	private String payaddr;
	
	/**缴费方式*/    //交易单查询
	@Column(name="pay_way")
	private String payway;

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

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
}
