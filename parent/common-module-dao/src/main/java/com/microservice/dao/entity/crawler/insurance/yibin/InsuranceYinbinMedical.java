package com.microservice.dao.entity.crawler.insurance.yibin;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 宜宾医保
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_yibin_medical")
public class InsuranceYinbinMedical extends IdEntity {

	private String useraccount; // 个人编号
	private String type; // 费款科目
	private String paymonth; // 缴费期号
	private String personType; // 当期人员类别
	private String paybase; // 缴费基数
	private String companyAmount; // 单位应缴金额
	private String personAmount; // 个人应缴金额
	private String totalAmount;// 应缴金额合计
	private String companyActuallyAmount; // 单位实缴金额
	private String personActuallyAmount; // 个人实缴金额
	private String totalActuallyAmount; // 实缴金额合计
	private String paySign; // 缴费标志
	private String companyName; // 当期所属单位
	private String taskid;

	public String getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPaymonth() {
		return paymonth;
	}

	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getPaybase() {
		return paybase;
	}

	public void setPaybase(String paybase) {
		this.paybase = paybase;
	}

	public String getCompanyAmount() {
		return companyAmount;
	}

	public void setCompanyAmount(String companyAmount) {
		this.companyAmount = companyAmount;
	}

	public String getPersonAmount() {
		return personAmount;
	}

	public void setPersonAmount(String personAmount) {
		this.personAmount = personAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCompanyActuallyAmount() {
		return companyActuallyAmount;
	}

	public void setCompanyActuallyAmount(String companyActuallyAmount) {
		this.companyActuallyAmount = companyActuallyAmount;
	}

	public String getPersonActuallyAmount() {
		return personActuallyAmount;
	}

	public void setPersonActuallyAmount(String personActuallyAmount) {
		this.personActuallyAmount = personActuallyAmount;
	}

	public String getTotalActuallyAmount() {
		return totalActuallyAmount;
	}

	public void setTotalActuallyAmount(String totalActuallyAmount) {
		this.totalActuallyAmount = totalActuallyAmount;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "InsuranceYinbinMedical [useraccount=" + useraccount + ", type=" + type + ", paymonth=" + paymonth
				+ ", personType=" + personType + ", paybase=" + paybase + ", companyAmount=" + companyAmount
				+ ", personAmount=" + personAmount + ", totalAmount=" + totalAmount + ", companyActuallyAmount="
				+ companyActuallyAmount + ", personActuallyAmount=" + personActuallyAmount + ", totalActuallyAmount="
				+ totalActuallyAmount + ", paySign=" + paySign + ", companyName=" + companyName + ", taskid=" + taskid
				+ "]";
	}

	public InsuranceYinbinMedical(String useraccount, String type, String paymonth, String personType, String paybase,
			String companyAmount, String personAmount, String totalAmount, String companyActuallyAmount,
			String personActuallyAmount, String totalActuallyAmount, String paySign, String companyName,
			String taskid) {
		super();
		this.useraccount = useraccount;
		this.type = type;
		this.paymonth = paymonth;
		this.personType = personType;
		this.paybase = paybase;
		this.companyAmount = companyAmount;
		this.personAmount = personAmount;
		this.totalAmount = totalAmount;
		this.companyActuallyAmount = companyActuallyAmount;
		this.personActuallyAmount = personActuallyAmount;
		this.totalActuallyAmount = totalActuallyAmount;
		this.paySign = paySign;
		this.companyName = companyName;
		this.taskid = taskid;
	}

	public InsuranceYinbinMedical() {
		super();
		// TODO Auto-generated constructor stub
	}
}
