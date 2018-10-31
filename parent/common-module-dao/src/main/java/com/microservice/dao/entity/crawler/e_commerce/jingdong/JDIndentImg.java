/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name = "e_commerce_jd_indent_img",indexes = {@Index(name = "index_e_commerce_jd_indent_img_taskid", columnList = "taskid")})
public class JDIndentImg extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;//商品名称
	
    private String state;
    private String color;
    private String snCode;
    private String wareUrl;
    private String num;
    private String imgPath;//图片url
    private String productId;//商品id   订单图片关联id
    private String categoryString;
    private String secondHandNameAndUrl;
    private String price;
    private String yb;
    private String jiFen;
    private String stock;
    private String cardKey;
    private String discountPrice;
    private String stockName;
    private String singleShouldPrice;
    private String wareType;
    private String jingDouNum;
    private String cid;
    private String isShowHuiShouJiuJiLink;
    private String mainProductId;
    private String showSellForMoneyLink;
    private String cxlFlag;
    
	private String taskid; //唯一标识

	
    
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSnCode() {
		return snCode;
	}
	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}
	public String getWareUrl() {
		return wareUrl;
	}
	public void setWareUrl(String wareUrl) {
		this.wareUrl = wareUrl;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCategoryString() {
		return categoryString;
	}
	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}
	public String getSecondHandNameAndUrl() {
		return secondHandNameAndUrl;
	}
	public void setSecondHandNameAndUrl(String secondHandNameAndUrl) {
		this.secondHandNameAndUrl = secondHandNameAndUrl;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getYb() {
		return yb;
	}
	public void setYb(String yb) {
		this.yb = yb;
	}
	public String getJiFen() {
		return jiFen;
	}
	public void setJiFen(String jiFen) {
		this.jiFen = jiFen;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getCardKey() {
		return cardKey;
	}
	public void setCardKey(String cardKey) {
		this.cardKey = cardKey;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getSingleShouldPrice() {
		return singleShouldPrice;
	}
	public void setSingleShouldPrice(String singleShouldPrice) {
		this.singleShouldPrice = singleShouldPrice;
	}
	public String getWareType() {
		return wareType;
	}
	public void setWareType(String wareType) {
		this.wareType = wareType;
	}
	public String getJingDouNum() {
		return jingDouNum;
	}
	public void setJingDouNum(String jingDouNum) {
		this.jingDouNum = jingDouNum;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getIsShowHuiShouJiuJiLink() {
		return isShowHuiShouJiuJiLink;
	}
	public void setIsShowHuiShouJiuJiLink(String isShowHuiShouJiuJiLink) {
		this.isShowHuiShouJiuJiLink = isShowHuiShouJiuJiLink;
	}
	public String getMainProductId() {
		return mainProductId;
	}
	public void setMainProductId(String mainProductId) {
		this.mainProductId = mainProductId;
	}
	public String getShowSellForMoneyLink() {
		return showSellForMoneyLink;
	}
	public void setShowSellForMoneyLink(String showSellForMoneyLink) {
		this.showSellForMoneyLink = showSellForMoneyLink;
	}
	public String getCxlFlag() {
		return cxlFlag;
	}
	public void setCxlFlag(String cxlFlag) {
		this.cxlFlag = cxlFlag;
	}
	@Override
	public String toString() {
		return "JDIndentImg [name=" + name + ", state=" + state + ", color=" + color + ", snCode=" + snCode
				+ ", wareUrl=" + wareUrl + ", num=" + num + ", imgPath=" + imgPath + ", productId=" + productId
				+ ", categoryString=" + categoryString + ", secondHandNameAndUrl=" + secondHandNameAndUrl + ", price="
				+ price + ", yb=" + yb + ", jiFen=" + jiFen + ", stock=" + stock + ", cardKey=" + cardKey
				+ ", discountPrice=" + discountPrice + ", stockName=" + stockName + ", singleShouldPrice="
				+ singleShouldPrice + ", wareType=" + wareType + ", jingDouNum=" + jingDouNum + ", cid=" + cid
				+ ", isShowHuiShouJiuJiLink=" + isShowHuiShouJiuJiLink + ", mainProductId=" + mainProductId
				+ ", showSellForMoneyLink=" + showSellForMoneyLink + ", cxlFlag=" + cxlFlag + ", taskid=" + taskid
				+ "]";
	}
  
    
    

}