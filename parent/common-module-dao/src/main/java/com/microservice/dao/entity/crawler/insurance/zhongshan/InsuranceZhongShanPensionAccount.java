package com.microservice.dao.entity.crawler.insurance.zhongshan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhongshan_pensionaccount")
public class InsuranceZhongShanPensionAccount extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 身份证号 */
	@Column(name="card_id")
	private String cardId;
	
	/**基本养老保险缴费月数 */
	@Column(name="basic_endowment_month")
	private String basicEndowmentMonth;

	/** 农村养老保险缴费月数 */
	@Column(name="countryside_endowment_month")
	private String countrysideEndowmentMonth;
	
	/** 居民养老保险缴费月数*/
	@Column(name="resident_endowment_month")
	private String residentEndowmentMonth;
	/**单位划入个帐*/
	@Column(name="unit_delimit")
	private String unitDelimit;
	/**个人缴纳*/
	@Column(name="personal_payment")
	private String personalPayment;
	/**合计*/
	@Column(name="total")
	private String total;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	public String getBasicEndowmentMonth() {
		return basicEndowmentMonth;
	}

	public void setBasicEndowmentMonth(String basicEndowmentMonth) {
		this.basicEndowmentMonth = basicEndowmentMonth;
	}

	public String getCountrysideEndowmentMonth() {
		return countrysideEndowmentMonth;
	}

	public void setCountrysideEndowmentMonth(String countrysideEndowmentMonth) {
		this.countrysideEndowmentMonth = countrysideEndowmentMonth;
	}

	public String getResidentEndowmentMonth() {
		return residentEndowmentMonth;
	}

	public void setResidentEndowmentMonth(String residentEndowmentMonth) {
		this.residentEndowmentMonth = residentEndowmentMonth;
	}

	public String getUnitDelimit() {
		return unitDelimit;
	}

	public void setUnitDelimit(String unitDelimit) {
		this.unitDelimit = unitDelimit;
	}

	public String getPersonalPayment() {
		return personalPayment;
	}

	public void setPersonalPayment(String personalPayment) {
		this.personalPayment = personalPayment;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}