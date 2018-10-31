package com.crawler.domain.json;

import java.io.Serializable;

public class IdAuthRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2401350337631607559L;

	private String customerCretNum;
	
	private String customerName; 
	
	private String token;;

	public String getCustomerCretNum() {
		return customerCretNum;
	}

	public void setCustomerCretNum(String customerCretNum) {
		this.customerCretNum = customerCretNum;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "IdAuthRequest [customerCretNum=" + customerCretNum + ", customerName=" + customerName + ", token="
				+ token + "]";
	}

	
	
	
	
	
}
