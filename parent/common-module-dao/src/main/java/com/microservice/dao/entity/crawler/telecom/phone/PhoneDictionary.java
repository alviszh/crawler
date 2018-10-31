package com.microservice.dao.entity.crawler.telecom.phone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "phone_dictionary_item_code")
public class PhoneDictionary extends IdEntity{

	private String oneClass;     //一级分类
	
	private String twoClass;     //二级分类
		
	private String threeClass;  //三级分类

	private String phone;     //电话
	
	private String image;    //图片
	
	private String website;   //网址
	
	@Override
	public String toString() {
		return "PhoneDictionary [oneClass=" + oneClass + ", twoClass=" + twoClass + ", threeClass=" + threeClass
				+ ", phone=" + phone  + ", image=" + image + ", website=" + website+ "]";
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


	public String getThreeClass() {
		return threeClass;
	}


	public void setThreeClass(String threeClass) {
		this.threeClass = threeClass;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(columnDefinition="text")
	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}	
	
	
	
}
