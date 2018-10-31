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
@Table(name = "e_commerce_taobao_order_info")
public class TaobaoOrderInfo extends IdEntity implements Serializable {
    private String taskid;
    private String orderDate;
    private String orderNo;
    private String shopsName;
    private String goodsName;//商品名称
    private String goodsDescription;//商品描述
    private Double goodsOriginalPrice;//商品原价
    private Double goodsTransactionPrice;//商品成交价
    private int goodsCount;//商品个数
    private Double orderTotalOriginalPrice;//订单总原价
    private Double orderTotalTransactionPrice;//订单总成交价
    private Double freight;//运费
    private String OrderTradingStatus;//订单交易状态
    private String receiver;//收货人
    private String paymentMethod;//支付方式
    private String receivAddress;//收货地址
    private String postNum;//邮编
    private String telNum;//固定电话
    private String phoneNum;//手机号
    private String email;
    private String orderType;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getEmail() {
        return email;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceivAddress() {
        return receivAddress;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPostNum() {
        return postNum;
    }

    public void setReceivAddress(String receivAddress) {
        this.receivAddress = receivAddress;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShopsName() {
        return shopsName;
    }

    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public Double getGoodsOriginalPrice() {
        return goodsOriginalPrice;
    }

    public void setGoodsOriginalPrice(Double goodsOriginalPrice) {
        this.goodsOriginalPrice = goodsOriginalPrice;
    }

    public Double getGoodsTransactionPrice() {
        return goodsTransactionPrice;
    }

    public void setGoodsTransactionPrice(Double goodsTransactionPrice) {
        this.goodsTransactionPrice = goodsTransactionPrice;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Double getOrderTotalOriginalPrice() {
        return orderTotalOriginalPrice;
    }

    public void setOrderTotalOriginalPrice(Double orderTotalOriginalPrice) {
        this.orderTotalOriginalPrice = orderTotalOriginalPrice;
    }

    public Double getOrderTotalTransactionPrice() {
        return orderTotalTransactionPrice;
    }

    public void setOrderTotalTransactionPrice(Double orderTotalTransactionPrice) {
        this.orderTotalTransactionPrice = orderTotalTransactionPrice;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public String getOrderTradingStatus() {
        return OrderTradingStatus;
    }

    public void setOrderTradingStatus(String orderTradingStatus) {
        OrderTradingStatus = orderTradingStatus;
    }


    public TaobaoOrderInfo copy(TaobaoOrderInfo tempBean) throws IllegalAccessException, NoSuchFieldException {
        Class<TaobaoOrderInfo> clazz = (Class<TaobaoOrderInfo>) tempBean.getClass();
        Field[] fieldArray = clazz.getDeclaredFields();
        for (Field field : fieldArray) {
            Object value = field.get(tempBean);
            if (value != null) {
                String fieldName = field.getName();
                Field thisField = this.getClass().getDeclaredField(fieldName);
                thisField.setAccessible(true);
                thisField.set(this, value);
            }
        }
        return this;
    }
}
