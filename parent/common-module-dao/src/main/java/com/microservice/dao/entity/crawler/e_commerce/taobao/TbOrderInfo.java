package com.microservice.dao.entity.crawler.e_commerce.taobao;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_tb_order_info")
public class TbOrderInfo extends IdEntity implements Serializable {
    private String taskid;
    private String orderId;
    private String orderDate;
    private String orderStatus;
    private String goodsName;//商品名称
    private String goodsPrice;//商品价格
    private String goodsCount;//商品个数
    private String receiver;//收货人
    
    private String seller;//卖家
    private String shopName;//商铺名称
    private String picUrl;//商品图片地址
    
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	@Override
	public String toString() {
		return "TbOrderInfo [taskid=" + taskid + ", orderId=" + orderId + ", orderDate=" + orderDate + ", orderStatus="
				+ orderStatus + ", goodsName=" + goodsName + ", goodsPrice=" + goodsPrice + ", goodsCount=" + goodsCount
				+ ", receiver=" + receiver + ", seller=" + seller + ", shopName=" + shopName + ", picUrl=" + picUrl
				+ "]";
	}

}
