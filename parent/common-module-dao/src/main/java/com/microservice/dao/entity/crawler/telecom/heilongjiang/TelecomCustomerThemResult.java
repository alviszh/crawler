package com.microservice.dao.entity.crawler.telecom.heilongjiang;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_heilongjiang_customresult")
public class TelecomCustomerThemResult extends IdEntity {

	private String name;//客户名称
	
	private String postcode;
	
	private String address;

	private String paydate;// 计费账期

	private String paynum;//本期费用合计

	private String bussoptional;//手机上网月功能费业务可选包

	private String bussrent;//基本月租费

	private String internetpay;//上网及数据通信费

	private String internetchinapay;//手机国内上网费

	private String currentIntegra;//本期末可用积分

	private String lastIntegra;//上期末可用积分

	private String usingIntegra;//当前使用积分
	
	private String riseIntegra;//本期新增积分
	

	private Integer userid;
	
	private String taskid;

	private String md5;
	
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getPaynum() {
		return paynum;
	}

	public void setPaynum(String paynum) {
		this.paynum = paynum;
	}

	public String getBussoptional() {
		return bussoptional;
	}

	public void setBussoptional(String bussoptional) {
		this.bussoptional = bussoptional;
	}

	public String getBussrent() {
		return bussrent;
	}

	public void setBussrent(String bussrent) {
		this.bussrent = bussrent;
	}

	public String getInternetpay() {
		return internetpay;
	}

	public void setInternetpay(String internetpay) {
		this.internetpay = internetpay;
	}

	public String getInternetchinapay() {
		return internetchinapay;
	}

	public void setInternetchinapay(String internetchinapay) {
		this.internetchinapay = internetchinapay;
	}

	public String getCurrentIntegra() {
		return currentIntegra;
	}

	public void setCurrentIntegra(String currentIntegra) {
		this.currentIntegra = currentIntegra;
	}

	public String getLastIntegra() {
		return lastIntegra;
	}

	public void setLastIntegra(String lastIntegra) {
		this.lastIntegra = lastIntegra;
	}

	public String getUsingIntegra() {
		return usingIntegra;
	}

	public void setUsingIntegra(String usingIntegra) {
		this.usingIntegra = usingIntegra;
	}

	public String getRiseIntegra() {
		return riseIntegra;
	}

	public void setRiseIntegra(String riseIntegra) {
		this.riseIntegra = riseIntegra;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		return "TelecomCustomerThemResult [name=" + name + ", postcode=" + postcode + ", address=" + address
				+ ", paydate=" + paydate + ", paynum=" + paynum + ", bussoptional=" + bussoptional + ", bussrent="
				+ bussrent + ", internetpay=" + internetpay + ", internetchinapay=" + internetchinapay
				+ ", currentIntegra=" + currentIntegra + ", lastIntegra=" + lastIntegra + ", usingIntegra="
				+ usingIntegra + ", riseIntegra=" + riseIntegra + ", userid=" + userid + ", taskid=" + taskid + ", md5="
				+ md5 + "]";
	}
}
