package com.microservice.dao.entity.crawler.insurance.shenzhen;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 深圳社保 缴费明细
 * @author rongshengxu
 *
 */
@Entity
@Table(name="insurance_shenzhen_paydetail")
public class InsuranceShenzhenPayDetail extends IdEntity implements Serializable{

	private static final long serialVersionUID = 4165580883380555879L;

	/** 登录名 */
	@Column(name="login_name")
	private String loginName;
	
	/** 爬取批次号 */
	@Column(name="task_id")
	private String taskId;
	
	/** 参保类别 */
	@Column(name="insured_type")
	private String insuredType;
	
	/** 缴费单位 */
	@Column(name="pay_company")
	private String payCompany;
	
	/** 年月 */
	@Column(name="insured_date")
	private String insuredDate;
	
	/** 缴费基数 */
	@Column(name="pay_quota")
	private String payQuota;
	
	/** 个人交 */
	@Column(name="personal_pay")
	private String personalPay;
	
	/** 单位交 */
	@Column(name="company_pay")
	private String companyPay;
	
	/** 缴费合计 */
	@Column(name="pay_total")
	private String payTotal;
	
	/** 划入个人账户 */
	@Column(name="pay_total")
	private String cutinPersonal;
	
	/** 备注 */
	@Column(name="remark")
	private String remark;


	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getInsuredType() {
		return insuredType;
	}

	public void setInsuredType(String insuredType) {
		this.insuredType = insuredType;
	}

	public String getPayCompany() {
		return payCompany;
	}

	public void setPayCompany(String payCompany) {
		this.payCompany = payCompany;
	}

	public String getInsuredDate() {
		return insuredDate;
	}

	public void setInsuredDate(String insuredDate) {
		this.insuredDate = insuredDate;
	}

	public String getPayQuota() {
		return payQuota;
	}

	public void setPayQuota(String payQuota) {
		this.payQuota = payQuota;
	}

	public String getPersonalPay() {
		return personalPay;
	}

	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}

	public String getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}

	public String getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(String payTotal) {
		this.payTotal = payTotal;
	}

	public String getCutinPersonal() {
		return cutinPersonal;
	}

	public void setCutinPersonal(String cutinPersonal) {
		this.cutinPersonal = cutinPersonal;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
