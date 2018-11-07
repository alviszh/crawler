package com.crawler.opendata.json.developer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpenData_Business_Consumer implements Serializable{

	private static final long serialVersionUID = 6939378958427845855L;

	private String name;
	
	private String phone;
	
	private String email;
	
	private String job;
	
	private String getType;//接受方式
	
	private String StringProductList;//接受方式
	
//	private Set<Product> productSet = new HashSet<Product>();
	private List<Product> productList = new ArrayList<Product>();
	
	//多对多映射
	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	@Override
	public String toString() {
		return "OpenData_Business_Consumer [name=" + name + ", phone=" + phone + ", email=" + email + ", job=" + job
				+ ", getType=" + getType + ", productList=" + productList + "]";
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getGetType() {
		return getType;
	}

	public void setGetType(String getType) {
		this.getType = getType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStringProductList() {
		return StringProductList;
	}

	public void setStringProductList(String stringProductList) {
		StringProductList = stringProductList;
	}
	

}