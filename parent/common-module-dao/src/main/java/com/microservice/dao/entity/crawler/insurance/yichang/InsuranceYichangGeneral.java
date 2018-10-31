package com.microservice.dao.entity.crawler.insurance.yichang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 社保缴费
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_yichang_general" ,indexes = {@Index(name = "index_insurance_yichang_general_taskid", columnList = "taskid")})
public class InsuranceYichangGeneral extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5373156896300717173L;

	private String taskid;
	
	/**
	 * 缴费所属期
	 */
	private String period_of_payment;
	/**
	 * 险种类型
	 */
	private String insurance_type;
	/**
	 * 缴费基数
	 */
	private String pay_num;
	/**
	 * 本期应缴
	 */
	private String payable;
	/**
	 * 款项
	 */
	private String money;
	/**
	 * 缴费类型
	 */
	private String payment_type;
	/**
	 * 缴费标记
	 */
	private String pay_mark;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPeriod_of_payment() {
		return period_of_payment;
	}
	public void setPeriod_of_payment(String period_of_payment) {
		this.period_of_payment = period_of_payment;
	}
	public String getInsurance_type() {
		return insurance_type;
	}
	public void setInsurance_type(String insurance_type) {
		this.insurance_type = insurance_type;
	}
	public String getPay_num() {
		return pay_num;
	}
	public void setPay_num(String pay_num) {
		this.pay_num = pay_num;
	}
	public String getPayable() {
		return payable;
	}
	public void setPayable(String payable) {
		this.payable = payable;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getPay_mark() {
		return pay_mark;
	}
	public void setPay_mark(String pay_mark) {
		this.pay_mark = pay_mark;
	}
	public InsuranceYichangGeneral() {
		super();
	}
	public InsuranceYichangGeneral(String taskid, String period_of_payment, String insurance_type, String pay_num,
			String payable, String money, String payment_type, String pay_mark) {
		super();
		this.taskid = taskid;
		this.period_of_payment = period_of_payment;
		this.insurance_type = insurance_type;
		this.pay_num = pay_num;
		this.payable = payable;
		this.money = money;
		this.payment_type = payment_type;
		this.pay_mark = pay_mark;
	}
	
}
