package com.microservice.dao.entity.crawler.housing.sz.anhui;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "housing_sz_anhui_userinfo")
public class HousingSZAnhuiUserInfo extends IdEntity implements Serializable {
    private static final long serialVersionUID = 2537702838749974022L;
    private String taskid;
    private String companyCode; //单位代码
    private String companyName; //单位名称
    private String companyBankNum;  //单位银行账号
    private String idNum;   //身份证号
    private String startPayMonth;   //起缴月份
    private String payableMonth;    //月应缴额
    private String companyDepositRatio; //单位缴存比例
    private String intrustBank; //委托银行
    private String perCode; //个人代码
    private String perName; //个人姓名
    private String perBankNum;  //个人银行账号
    private String balance; //余额
    private String endPayMonth; //缴至月份
    private String accountStatus;   //账户状态
    private String perDepositRatio;//个人缴存比例

    public HousingSZAnhuiUserInfo() {
    }

    public HousingSZAnhuiUserInfo(String taskid, String companyCode, String companyName, String companyBankNum, String idNum, String startPayMonth, String payableMonth, String companyDepositRatio, String intrustBank, String perCode, String perName, String perBankNum, String balance, String endPayMonth, String accountStatus, String perDepositRatio) {
        this.taskid = taskid;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.companyBankNum = companyBankNum;
        this.idNum = idNum;
        this.startPayMonth = startPayMonth;
        this.payableMonth = payableMonth;
        this.companyDepositRatio = companyDepositRatio;
        this.intrustBank = intrustBank;
        this.perCode = perCode;
        this.perName = perName;
        this.perBankNum = perBankNum;
        this.balance = balance;
        this.endPayMonth = endPayMonth;
        this.accountStatus = accountStatus;
        this.perDepositRatio = perDepositRatio;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyBankNum() {
        return companyBankNum;
    }

    public void setCompanyBankNum(String companyBankNum) {
        this.companyBankNum = companyBankNum;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getStartPayMonth() {
        return startPayMonth;
    }

    public void setStartPayMonth(String startPayMonth) {
        this.startPayMonth = startPayMonth;
    }

    public String getPayableMonth() {
        return payableMonth;
    }

    public void setPayableMonth(String payableMonth) {
        this.payableMonth = payableMonth;
    }

    public String getCompanyDepositRatio() {
        return companyDepositRatio;
    }

    public void setCompanyDepositRatio(String companyDepositRatio) {
        this.companyDepositRatio = companyDepositRatio;
    }

    public String getIntrustBank() {
        return intrustBank;
    }

    public void setIntrustBank(String intrustBank) {
        this.intrustBank = intrustBank;
    }

    public String getPerCode() {
        return perCode;
    }

    public void setPerCode(String perCode) {
        this.perCode = perCode;
    }

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public String getPerBankNum() {
        return perBankNum;
    }

    public void setPerBankNum(String perBankNum) {
        this.perBankNum = perBankNum;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEndPayMonth() {
        return endPayMonth;
    }

    public void setEndPayMonth(String endPayMonth) {
        this.endPayMonth = endPayMonth;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPerDepositRatio() {
        return perDepositRatio;
    }

    public void setPerDepositRatio(String perDepositRatio) {
        this.perDepositRatio = perDepositRatio;
    }
}
