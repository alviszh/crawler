package com.microservice.dao.entity.crawler.bank.cgbchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="cgb_credit_china_transinfo",indexes = {@Index(name = "index_cgb_credit_china_transinfo_taskid", columnList = "taskid")})
public class Cgb_credit_ChinaTransInfo  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**卡号*/ 
	@Column(name="cardnumber")
	private String cardnumber ;
	
	/**账单周期*/ 
	@Column(name="period")
	private String period ;
	
	/**本期应还总额*/ 
	@Column(name="newbalance")
	private String newbalance ;
	
	/**最低还款额*/ 
	@Column(name="minpayment")
	private String minpayment ;
	
	/**最后还款日*/ 
	@Column(name="paymentdate")
	private String paymentdate ;
	
	/**清算货币*/ 
	@Column(name="currency")
	private String currency ;
	
	/**户口消费额度*/ 
	@Column(name="creditlimit")
	private String creditlimit ;
	
	/**积分类型*/ 
	@Column(name="integraltype")
	private String integraltype ;
	
	/**上期余额*/ 
	@Column(name="periodyue")
	private String periodyue ;
	
	/**本期新增*/ 
	@Column(name="add")
	private String add;	
	
	/**本期扣减*/ 
	@Column(name="subtract")
	private String subtract;	
	
	/**本期余额*/ 
	@Column(name="yue")
	private String yue;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getNewbalance() {
		return newbalance;
	}

	public void setNewbalance(String newbalance) {
		this.newbalance = newbalance;
	}

	public String getMinpayment() {
		return minpayment;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setMinpayment(String minpayment) {
		this.minpayment = minpayment;
	}

	public String getPaymentdate() {
		return paymentdate;
	}

	public void setPaymentdate(String paymentdate) {
		this.paymentdate = paymentdate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCreditlimit() {
		return creditlimit;
	}

	public void setCreditlimit(String creditlimit) {
		this.creditlimit = creditlimit;
	}

	public String getIntegraltype() {
		return integraltype;
	}

	public void setIntegraltype(String integraltype) {
		this.integraltype = integraltype;
	}

	public String getPeriodyue() {
		return periodyue;
	}

	public void setPeriodyue(String periodyue) {
		this.periodyue = periodyue;
	}

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getSubtract() {
		return subtract;
	}

	public void setSubtract(String subtract) {
		this.subtract = subtract;
	}

	public String getYue() {
		return yue;
	}

	public void setYue(String yue) {
		this.yue = yue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
	
}
