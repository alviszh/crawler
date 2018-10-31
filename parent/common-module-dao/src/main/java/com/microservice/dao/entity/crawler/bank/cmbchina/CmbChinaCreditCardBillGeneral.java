package com.microservice.dao.entity.crawler.bank.cmbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 招商银行信用卡账单信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "cmbchina_creditcard_billgeneral" ,indexes = {@Index(name = "index_cmbchina_creditcard_billgeneral_taskid", columnList = "taskid")})
public class CmbChinaCreditCardBillGeneral extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6226606269169233587L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 账单月份
	 */
	private String billMonth;

	/**
	 * 人民币应还总额
	 */
	private String repaymentSumRMB;
	/**
	 * 人民币最低还款额
	 */
	private String repaymentMinRMB;
	/**
	 * 美元应还总额
	 */
	private String repaymentSumDollar;
	/**
	 * 美元最低还款额
	 */
	private String repaymentMinDollar;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getRepaymentSumRMB() {
		return repaymentSumRMB;
	}
	public void setRepaymentSumRMB(String repaymentSumRMB) {
		this.repaymentSumRMB = repaymentSumRMB;
	}
	public String getRepaymentMinRMB() {
		return repaymentMinRMB;
	}
	public void setRepaymentMinRMB(String repaymentMinRMB) {
		this.repaymentMinRMB = repaymentMinRMB;
	}
	public String getRepaymentSumDollar() {
		return repaymentSumDollar;
	}
	public void setRepaymentSumDollar(String repaymentSumDollar) {
		this.repaymentSumDollar = repaymentSumDollar;
	}
	public String getRepaymentMinDollar() {
		return repaymentMinDollar;
	}
	public void setRepaymentMinDollar(String repaymentMinDollar) {
		this.repaymentMinDollar = repaymentMinDollar;
	}
	public CmbChinaCreditCardBillGeneral(String taskid, String billMonth, String repaymentSumRMB,
			String repaymentMinRMB, String repaymentSumDollar, String repaymentMinDollar) {
		super();
		this.taskid = taskid;
		this.billMonth = billMonth;
		this.repaymentSumRMB = repaymentSumRMB;
		this.repaymentMinRMB = repaymentMinRMB;
		this.repaymentSumDollar = repaymentSumDollar;
		this.repaymentMinDollar = repaymentMinDollar;
	}
	public CmbChinaCreditCardBillGeneral() {
		super();
		// TODO Auto-generated constructor stub
	}

}
