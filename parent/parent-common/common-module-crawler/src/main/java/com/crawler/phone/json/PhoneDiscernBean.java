package com.crawler.phone.json;

import java.io.Serializable;
import java.util.List;

public class PhoneDiscernBean implements Serializable{
	
	private int total;

	private List<PhoneBean> phone;

	@Override
	public String toString() {
		return "PhoneDiscernBean [ total=" + total + ", phone=" + phone + "]";
	}
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<PhoneBean> getPhone() {
		return phone;
	}

	public void setPhone(List<PhoneBean> phone) {
		this.phone = phone;
	}
	
	
}
