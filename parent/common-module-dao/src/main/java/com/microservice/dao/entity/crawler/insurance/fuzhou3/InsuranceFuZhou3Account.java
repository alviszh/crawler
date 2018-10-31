package com.microservice.dao.entity.crawler.insurance.fuzhou3;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_fuzhou3_account")
public class InsuranceFuZhou3Account extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	
	/**工作单位*/
	@Column(name="company")
	private String company;
	
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 身份证号码 */
	@Column(name="card_id")
	private String cardId;
	
	/**参加工作时间	*/
	@Column(name="start_work_time")
	private String startWorkTime;

	/** 缴费截止时间 */
	@Column(name="pay_end_time")
	private String payEndTime;
	
	/** 实际缴费月数*/
	@Column(name="actual_pay_month")
	private String actualPayMonth;
	
	/**累计个人账户*/
	@Column(name="account_total")
	private String accountTotal;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(String startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	public String getPayEndTime() {
		return payEndTime;
	}

	public void setPayEndTime(String payEndTime) {
		this.payEndTime = payEndTime;
	}

	public String getActualPayMonth() {
		return actualPayMonth;
	}

	public void setActualPayMonth(String actualPayMonth) {
		this.actualPayMonth = actualPayMonth;
	}

	public String getAccountTotal() {
		return accountTotal;
	}

	public void setAccountTotal(String accountTotal) {
		this.accountTotal = accountTotal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}