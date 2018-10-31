package com.microservice.dao.entity.crawler.telecom.phone.bank;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "bank_address_item_code")
public class BankAddressa extends IdEntity{
	
	private String name;              //银行分行名字
		
	private String phone;             //电话

	private String address;             //银行分行地址
	
	private String cty;                   //银行分行城市
	
	@Override
	public String toString() {
		return "BankAddressa [name=" + name + ", phone=" + phone+ ", address=" + address
				+ ", cty=" + cty   + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCty() {
		return cty;
	}

	public void setCty(String cty) {
		this.cty = cty;
	}
	
	
	

}
