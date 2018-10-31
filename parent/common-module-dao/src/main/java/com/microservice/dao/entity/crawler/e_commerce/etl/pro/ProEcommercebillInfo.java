package com.microservice.dao.entity.crawler.e_commerce.etl.pro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_ecommerce_bill_info",indexes = {@Index(name = "index_pro_ecommerce_bill_info_taskid", columnList = "taskId")})
public class ProEcommercebillInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String amount;
	private String goodsName;
	private String sellerName;
	private String address;
	private String consignee;
	private String telnum;
	private String tradeTime;
	private String tradeStatus;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	@Column(columnDefinition="text")
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getTelnum() {
		return telnum;
	}
	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	@Override
	public String toString() {
		return "ProEcommercebillInfo [taskId=" + taskId + ", resource=" + resource + ", amount=" + amount
				+ ", goodsName=" + goodsName + ", sellerName=" + sellerName + ", address=" + address + ", consignee="
				+ consignee + ", telnum=" + telnum + ", tradeTime=" + tradeTime + ", tradeStatus=" + tradeStatus + "]";
	}
		
}	
