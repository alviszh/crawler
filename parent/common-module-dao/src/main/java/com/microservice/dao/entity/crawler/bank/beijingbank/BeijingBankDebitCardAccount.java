package com.microservice.dao.entity.crawler.bank.beijingbank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 北京银行储蓄卡-账户信息(活期、整存整取)
 * Created by zmy on 2018/3/9.
 */
@Entity
@Table(name="beijingbank_debitcard_account")
public class BeijingBankDebitCardAccount extends IdEntity{
    private String taskid;
    private String acctNo;  //账/卡号
    private String bankName;    //开户行
    private String pointsAccount;     //分账户
    private String currency;        //币种
    private String productName;     //产品名称
    private String depositTerm;     //存期
    private String dueDate;         //到期日
    private String strikeRate;      //执行利率
    private String balances ;       //存款余额
    private String canUseBalance;   //可用余额
    private String username;        //户名
    private String baseRate;       //基准利率
    private String openDate;        //开户日期
    private String currencyType;    //汇钞标志
    private String accountStatus;   //账户状态
    private String medicare;        //已关联医保存折
    private String withhold;        //代发代扣
    private String archivedWay;    //转存方式

    @JsonIgnore
    private String href; //保存账户信息-查看详情的href值（请求参数）
    @Transient
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPointsAccount() {
        return pointsAccount;
    }

    public void setPointsAccount(String pointsAccount) {
        this.pointsAccount = pointsAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDepositTerm() {
        return depositTerm;
    }

    public void setDepositTerm(String depositTerm) {
        this.depositTerm = depositTerm;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(String strikeRate) {
        this.strikeRate = strikeRate;
    }

    public String getBalances() {
        return balances;
    }

    public void setBalances(String balances) {
        this.balances = balances;
    }

    public String getCanUseBalance() {
        return canUseBalance;
    }

    public void setCanUseBalance(String canUseBalance) {
        this.canUseBalance = canUseBalance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(String baseRate) {
        this.baseRate = baseRate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getMedicare() {
        return medicare;
    }

    public void setMedicare(String medicare) {
        this.medicare = medicare;
    }

    public String getWithhold() {
        return withhold;
    }

    public void setWithhold(String withhold) {
        this.withhold = withhold;
    }

    public String getArchivedWay() {
        return archivedWay;
    }

    public void setArchivedWay(String archivedWay) {
        this.archivedWay = archivedWay;
    }

    @Override
    public String toString() {
        return "BeijingBankDebitCardAccount{" +
                "taskid='" + taskid + '\'' +
                ", acctNo='" + acctNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", pointsAccount='" + pointsAccount + '\'' +
                ", currency='" + currency + '\'' +
                ", productName='" + productName + '\'' +
                ", depositTerm='" + depositTerm + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", strikeRate='" + strikeRate + '\'' +
                ", balances='" + balances + '\'' +
                ", canUseBalance='" + canUseBalance + '\'' +
                ", username='" + username + '\'' +
                ", baseRate='" + baseRate + '\'' +
                ", openDate='" + openDate + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", medicare='" + medicare + '\'' +
                ", withhold='" + withhold + '\'' +
                ", archivedWay='" + archivedWay + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
