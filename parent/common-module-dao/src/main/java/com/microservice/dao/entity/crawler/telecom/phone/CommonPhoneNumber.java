package com.microservice.dao.entity.crawler.telecom.phone;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "common_phone_number_item_code")
public class CommonPhoneNumber extends IdEntity{

    private String oneClass;     //一级分类
	
	private String twoClass;     //二级分类
		
	private String phone;     //电话
	
	@Override
	public String toString() {
		return "PhoneDictionary [oneClass=" + oneClass + ", twoClass=" + twoClass+ ", phone=" + phone+ "]";
	}

	public String getOneClass() {
		return oneClass;
	}

	public void setOneClass(String oneClass) {
		this.oneClass = oneClass;
	}

	public String getTwoClass() {
		return twoClass;
	}

	public void setTwoClass(String twoClass) {
		this.twoClass = twoClass;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
