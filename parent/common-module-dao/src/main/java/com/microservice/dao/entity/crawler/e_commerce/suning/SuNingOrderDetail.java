package com.microservice.dao.entity.crawler.e_commerce.suning;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_suning_order_detail" ,indexes = {@Index(name = "index_e_commerce_suning_order_detail_taskid", columnList = "taskid")})
public class SuNingOrderDetail extends IdEntity implements Serializable {

    private String taskid;
    private String submitTime;					//订单时间
    private String transStatus;					//订单状态
    private String vendorName;					//商家名称
    private String receiverName;				//收货人
    private String receiverTel;					//收货电话
    private String receiverAddr;					//收货地址
    private String shipModeContent;					//订单配送
    private String productName;					//商品名称
    private String qty;					//商品数量
    private String price;					//商品价格
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverTel() {
		return receiverTel;
	}
	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}
	public String getReceiverAddr() {
		return receiverAddr;
	}
	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}
	public String getShipModeContent() {
		return shipModeContent;
	}
	public void setShipModeContent(String shipModeContent) {
		this.shipModeContent = shipModeContent;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "SuNingOrderDetail [taskid=" + taskid + ", submitTime=" + submitTime + ", transStatus=" + transStatus
				+ ", vendorName=" + vendorName + ", receiverName=" + receiverName + ", receiverTel=" + receiverTel
				+ ", receiverAddr=" + receiverAddr + ", shipModeContent=" + shipModeContent + ", productName="
				+ productName + ", qty=" + qty + ", price=" + price + "]";
	}
    
}
