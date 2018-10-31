package com.microservice.dao.entity.crawler.bank.spdb;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="spdb_debitcard_userinfo")
public class SpdbDebitCardUserInfo extends IdEntity implements Serializable {
    private static final long serialVersionUID = -1243666112431058141L;

    private String taskid;
    private String customerNo;        //客户号
    private String customerName;        //客户姓名
    private String address;        //地址
    private String postalCode;        //邮政编码
    private String telephoneNo;        //联系手机号
    private String fixedTel;        //固定电话
    private String rsrvNo;        //备用手机号

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getFixedTel() {
        return fixedTel;
    }

    public void setFixedTel(String fixedTel) {
        this.fixedTel = fixedTel;
    }

    public String getRsrvNo() {
        return rsrvNo;
    }

    public void setRsrvNo(String rsrvNo) {
        this.rsrvNo = rsrvNo;
    }

    @Override
    public String toString() {
        return "SpdbDebitCardUserInfo{" +
                "taskid='" + taskid + '\'' +
                ", customerNo='" + customerNo + '\'' +
                ", customerName='" + customerName + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", telephoneNo='" + telephoneNo + '\'' +
                ", fixedTel='" + fixedTel + '\'' +
                ", rsrvNo='" + rsrvNo + '\'' +
                '}';
    }
}
