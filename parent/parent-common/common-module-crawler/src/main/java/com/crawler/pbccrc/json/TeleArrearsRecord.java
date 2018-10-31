package com.crawler.pbccrc.json;

//公共记录 -- 电信欠费信息
public class TeleArrearsRecord {

	private String operator;//运营商
	
	private String businessType;//业务类型
	
	private String accountingDate;//记账年月
	
	private String businessOpeningDate;//业务开通时间
	
	private String arrearsAmount;//欠费金额

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getAccountingDate() {
		return accountingDate;
	}

	public void setAccountingDate(String accountingDate) {
		this.accountingDate = accountingDate;
	}

	public String getBusinessOpeningDate() {
		return businessOpeningDate;
	}

	public void setBusinessOpeningDate(String businessOpeningDate) {
		this.businessOpeningDate = businessOpeningDate;
	}

	public String getArrearsAmount() {
		return arrearsAmount;
	}

	public void setArrearsAmount(String arrearsAmount) {
		this.arrearsAmount = arrearsAmount;
	}

	@Override
	public String toString() {
		return "TeleArrearsRecord [operator=" + operator + ", businessType=" + businessType + ", accountingDate="
				+ accountingDate + ", businessOpeningDate=" + businessOpeningDate + ", arrearsAmount=" + arrearsAmount
				+ "]";
	}
	
	
	
	
	
	
	
	
	
}
