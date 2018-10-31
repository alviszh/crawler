/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-28 16:15:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocchina_cebitcard_customerinfo",indexes = {@Index(name = "index_bocchina_cebitcard_customerinfo_taskid", columnList = "taskid")})
public class  BocchinaCebitCardCrcdCustomerInfo extends IdEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cardNo;//信用卡卡号
    private String zipCode;// 100020 推测为邮政编码
    private String address1;// 地址
    private String address2;//地址
    private String address3;//地址
    private String surname;//用户姓名
    private String name;//“”
    private String title;//女士 头衔
    private String billDate;//账单日期
    private String repayDate;//到期还款日
    private String currencyCode1;// "001"
    private String curTermBalance1;// 3144 欠款
    private String lowestRepayAmount1;//315 最低还款额
    private String currencyCode2;//null
    private String curTermBalance2;//null
    private String lowestRepayAmount2;//null
    private String creditcardId;//9621275509531271 信用卡在银行内部编号
    private String curTermBalanceflag1;//0 
    private String curTermBalanceflag2;//null
    
    private String crcdBillInfoListString;
    
    @Column(columnDefinition="text")    
	public String getCrcdBillInfoListString() {
		return crcdBillInfoListString;
	}

	public void setCrcdBillInfoListString(String crcdBillInfoListString) {
		this.crcdBillInfoListString = crcdBillInfoListString;
	}
	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

    
    public void setCardNo(String cardNo) {
         this.cardNo = cardNo;
     }
     public String getCardNo() {
         return cardNo;
     }

    public void setZipCode(String zipCode) {
         this.zipCode = zipCode;
     }
     public String getZipCode() {
         return zipCode;
     }

    public void setAddress1(String address1) {
         this.address1 = address1;
     }
     public String getAddress1() {
         return address1;
     }

    public void setAddress2(String address2) {
         this.address2 = address2;
     }
     public String getAddress2() {
         return address2;
     }

    public void setAddress3(String address3) {
         this.address3 = address3;
     }
     public String getAddress3() {
         return address3;
     }

    public void setSurname(String surname) {
         this.surname = surname;
     }
     public String getSurname() {
         return surname;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setBillDate(String billDate) {
         this.billDate = billDate;
     }
     public String getBillDate() {
         return billDate;
     }

    public void setRepayDate(String repayDate) {
         this.repayDate = repayDate;
     }
     public String getRepayDate() {
         return repayDate;
     }

    public void setCurrencyCode1(String currencyCode1) {
         this.currencyCode1 = currencyCode1;
     }
     public String getCurrencyCode1() {
         return currencyCode1;
     }

    public void setCurTermBalance1(String curTermBalance1) {
         this.curTermBalance1 = curTermBalance1;
     }
     public String getCurTermBalance1() {
         return curTermBalance1;
     }

    public void setLowestRepayAmount1(String lowestRepayAmount1) {
         this.lowestRepayAmount1 = lowestRepayAmount1;
     }
     public String getLowestRepayAmount1() {
         return lowestRepayAmount1;
     }

    public void setCurrencyCode2(String currencyCode2) {
         this.currencyCode2 = currencyCode2;
     }
     public String getCurrencyCode2() {
         return currencyCode2;
     }

    public void setCurTermBalance2(String curTermBalance2) {
         this.curTermBalance2 = curTermBalance2;
     }
     public String getCurTermBalance2() {
         return curTermBalance2;
     }

    public void setLowestRepayAmount2(String lowestRepayAmount2) {
         this.lowestRepayAmount2 = lowestRepayAmount2;
     }
     public String getLowestRepayAmount2() {
         return lowestRepayAmount2;
     }

    public void setCreditcardId(String creditcardId) {
         this.creditcardId = creditcardId;
     }
     public String getCreditcardId() {
         return creditcardId;
     }

    public void setCurTermBalanceflag1(String curTermBalanceflag1) {
         this.curTermBalanceflag1 = curTermBalanceflag1;
     }
     public String getCurTermBalanceflag1() {
         return curTermBalanceflag1;
     }

    public void setCurTermBalanceflag2(String curTermBalanceflag2) {
         this.curTermBalanceflag2 = curTermBalanceflag2;
     }
     public String getCurTermBalanceflag2() {
         return curTermBalanceflag2;
     }

     
}