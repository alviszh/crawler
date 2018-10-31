package com.microservice.dao.entity.crawler.bank.spdb;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 账户信息
 * Created by zmy on 2017/12/4.
 */
@Entity
@Table(name="spdb_debitcard_accountinfo")
public class SpdbDebitCardAccountInfo extends IdEntity implements Serializable {
    private static final long serialVersionUID = -8860888135126382913L;

    private String taskid;
    private String voucherKind;       //储蓄种类
    private String acctNo;       //卡号
    private String currency;    //币种
    private String currencyType;     //钞汇标志
    private String currentBalance;     //活期余额
    private String canUseBalance;   //可用余额
    private String accountStatus;   //账户状态

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVoucherKind() {
        return voucherKind;
    }

    public void setVoucherKind(String voucherKind) {
        this.voucherKind = voucherKind;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getCanUseBalance() {
        return canUseBalance;
    }

    public void setCanUseBalance(String canUseBalance) {
        this.canUseBalance = canUseBalance;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return "SpdbDebitCardAccountInfo{" +
                "taskid='" + taskid + '\'' +
                ", voucherKind='" + voucherKind + '\'' +
                ", acctNo='" + acctNo + '\'' +
                ", currency='" + currency + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", currentBalance='" + currentBalance + '\'' +
                ", canUseBalance='" + canUseBalance + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                '}';
    }
}
