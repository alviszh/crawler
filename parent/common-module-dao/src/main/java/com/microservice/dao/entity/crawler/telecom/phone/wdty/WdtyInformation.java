package com.microservice.dao.entity.crawler.telecom.phone.wdty;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "wdty_information_item_code")
public class WdtyInformation extends  IdEntity{

    private String company;           //公司
    
    private String customerQQ;       //客服QQ
	
	private String qqGroup;                //官方QQ群
	
	private String consumerHotline;     //客服电话
	
	private String address;          //公司详细地址
	
	private String url;                //公司页面链接
	
	@Override
	public String toString() {
		return "WdtyInformation [company=" + company + ", customerQQ=" + customerQQ
				+ ",  qqGroup=" + qqGroup + ", consumerHotline=" + consumerHotline
				+ ",  address=" + address + ",  url=" + url + "]";
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public String getConsumerHotline() {
		return consumerHotline;
	}

	public void setConsumerHotline(String consumerHotline) {
		this.consumerHotline = consumerHotline;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
