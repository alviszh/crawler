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
 * Auto-generated: 2017-10-31 14:58:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Entity
@Table(name="bocchina_debitcard_userinfo",indexes = {@Index(name = "index_bocchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class BocchinaDebitCardUserinfo extends IdEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int accountSeq;//36861666 含义未知
    private String accountType;//119 含义未知
    private String name;//姓名
    @Column(name = "ebankingFfag")
    private String eBankingFlag;//"L" 含义未知
    private boolean isHaveEleCashAcct;//true 含义未知
    private String accountNumber;//银行卡号
    private String taskid;
      
    
	@Override
	public String toString() {
		return "BocchinaDebitCardUserinfo [accountSeq=" + accountSeq + ", accountType=" + accountType + ", name=" + name
				+ ", eBankingFlag=" + eBankingFlag + ", isHaveEleCashAcct=" + isHaveEleCashAcct + ", accountNumber="
				+ accountNumber + ", taskid=" + taskid + "]";
	}
	public String geteBankingFlag() {
		return eBankingFlag;
	}
	public void seteBankingFlag(String eBankingFlag) {
		this.eBankingFlag = eBankingFlag;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public void setHaveEleCashAcct(boolean isHaveEleCashAcct) {
		this.isHaveEleCashAcct = isHaveEleCashAcct;
	}
	public void setAccountSeq(int accountSeq) {
         this.accountSeq = accountSeq;
     }
     public int getAccountSeq() {
         return accountSeq;
     }

    public void setAccountType(String accountType) {
         this.accountType = accountType;
     }
     public String getAccountType() {
         return accountType;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }


    public void setIsHaveEleCashAcct(boolean isHaveEleCashAcct) {
         this.isHaveEleCashAcct = isHaveEleCashAcct;
     }
     public boolean getIsHaveEleCashAcct() {
         return isHaveEleCashAcct;
     }

    public void setAccountNumber(String accountNumber) {
         this.accountNumber = accountNumber;
     }
     public String getAccountNumber() {
         return accountNumber;
     }

}