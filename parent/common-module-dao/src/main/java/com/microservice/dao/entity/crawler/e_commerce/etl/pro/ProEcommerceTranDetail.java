package com.microservice.dao.entity.crawler.e_commerce.etl.pro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_ecommerce_tran_detail",indexes = {@Index(name = "index_pro_ecommerce_tran_detail_taskid", columnList = "taskId")})
public class ProEcommerceTranDetail extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String amount;
	private String tradeStatus;
	private String sellerName;
	private String tradeTime;
	private String tradeComment;
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
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getTradeComment() {
		return tradeComment;
	}
	public void setTradeComment(String tradeComment) {
		this.tradeComment = tradeComment;
	}
	@Override
	public String toString() {
		return "ProEcommerceTranDetail [taskId=" + taskId + ", resource=" + resource + ", amount=" + amount
				+ ", tradeStatus=" + tradeStatus + ", sellerName=" + sellerName + ", tradeTime=" + tradeTime
				+ ", tradeComment=" + tradeComment + "]";
	}
	
	
}	
