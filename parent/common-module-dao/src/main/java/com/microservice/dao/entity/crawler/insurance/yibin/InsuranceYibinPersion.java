package com.microservice.dao.entity.crawler.insurance.yibin;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
/**
 * 潍坊社保养老保险
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_yibin_persion")
public class InsuranceYibinPersion extends IdEntity {
	
	private String useraccount;	//个人编号
	private String paymonth;	//费款所属期
	private String type;	//缴费类型
	private String paybase;//缴费基数
	private String companyAmount;	//单位应缴金额
	private String personAmount;	//个人应缴金额
	private String companyActuallyAmount;	//单位实缴金额
	private String personActuallyAmount;	//个人实缴金额
	private String paySign;	//缴费标志
	private String companyName;	//当期所属单位
	private String taskid;
	
	public String getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}

	public String getPaymonth() {
		return paymonth;
	}

	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		return "InsuranceYibinPersion [useraccount=" + useraccount + ", paymonth=" + paymonth + ", type=" + type
				+ ", paybase=" + paybase + ", companyAmount=" + companyAmount + ", personAmount=" + personAmount
				+ ", companyActuallyAmount=" + companyActuallyAmount + ", personActuallyAmount=" + personActuallyAmount
				+ ", paySign=" + paySign + ", companyName=" + companyName + ", taskid=" + taskid + "]";
	}

	public InsuranceYibinPersion(String useraccount, String paymonth, String type, String paybase, String companyAmount,
			String personAmount, String companyActuallyAmount, String personActuallyAmount, String paySign,
			String companyName, String taskid) {
		super();
		this.useraccount = useraccount;
		this.paymonth = paymonth;
		this.type = type;
		this.paybase = paybase;
		this.companyAmount = companyAmount;
		this.personAmount = personAmount;
		this.companyActuallyAmount = companyActuallyAmount;
		this.personActuallyAmount = personActuallyAmount;
		this.paySign = paySign;
		this.companyName = companyName;
		this.taskid = taskid;
	}
	public InsuranceYibinPersion() {
		super();
		
	}
}
