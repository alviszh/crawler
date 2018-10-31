package com.microservice.dao.entity.crawler.e_commerce.taobao;


import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_taobao_deliver_address")
public class TaobaoDeliverAddress extends IdEntity implements Serializable {

    private String taskid;

    private String receiver;//收货人

    private String phoneNum;//手机号

    private String telNum;//固定电话号

    private String area;//地区

    private String address;//收货地址

    private String postcode;//邮编

    private int isDefault = 0; //是否为默认地址  0 为false  1为true 
    
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "TaobaoDeliverAddress{" +
                ", taskid='" + taskid + '\'' +
                ", receiver='" + receiver + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", telNum='" + telNum + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
