package com.microservice.dao.entity.crawler.telecom.phone.wdzj;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "wdzj_information_item_code")
public class WdzjInformation extends  IdEntity{
	
	private String company;           //公司
	
	private String consumerHotline;     //客服电话
		
	private String landlinePhone;       //座机电话

	private String email;             //服务邮箱
	
	private String fax;              //传真
	
	private String address;          //通信地址
	
	private String zipCode;          //邮编
	
	private String branchOffice;          //分公司
	
	private String url;                //公司页面链接
	
	private String customerQQ;       //客服QQ
	
	private String qqGroup;                //官方QQ群
	@Override
	public String toString() {
		return "WdzjInformation [company=" + company + ", consumerHotline=" + consumerHotline
				+ ",  landlinePhone=" + landlinePhone + ", email=" + email + ", fax=" + fax
				+ ",  address=" + address + ", zipCode=" + zipCode + ", branchOffice=" + branchOffice
				+ ",  url=" + url + ",  customerQQ=" + customerQQ + ",  qqGroup=" + qqGroup+ "]";
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getConsumerHotline() {
		return consumerHotline;
	}

	public void setConsumerHotline(String consumerHotline) {
		this.consumerHotline = consumerHotline;
	}

	public String getLandlinePhone() {
		return landlinePhone;
	}

	public void setLandlinePhone(String landlinePhone) {
		this.landlinePhone = landlinePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getBranchOffice() {
		return branchOffice;
	}

	public void setBranchOffice(String branchOffice) {
		this.branchOffice = branchOffice;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCustomerQQ() {
		return customerQQ;
	}

	public void setCustomerQQ(String customerQQ) {
		this.customerQQ = customerQQ;
	}

	public String getQqGroup() {
		return qqGroup;
	}

	public void setQqGroup(String qqGroup) {
		this.qqGroup = qqGroup;
	}
	
	
}
