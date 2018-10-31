package com.microservice.dao.entity.crawler.housing.baoshan;


import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description: 宝山市公积金个人基本信息
 * @author: zmy
 * @date:
 */
@Entity
@Table(name="housing_baoshan_userinfo")
public class HousingBaoShanUserInfo extends IdEntity implements Serializable {

    private String taskid;
    /*个人基本信息*/
    //	个人姓名
    private String accName;
    //	身份证号
    private String certiNum;
    //  婚姻状况
    private String maritalStatus;
    //  职业
    private String profession;
    //  手机号码
    private String phoneNumber;
    //电子邮件
    private String email;
    //短信功能开通状态
    private String isSmsOpen;
    //个人开户日期
    private String perOpenData;
    //个人社保账户
    private String perInsurNum;
    //是否已认证
    private String isAuth;
    //家庭住址
    private String homeAddress;

    /*个人账户信息*/
    //	个人账号
    private String accnum;
    //个人状态
    private String indiaccstate;
    //	单位账号
    private String unitAccnum;
    //	账户机构
    private String accGanization;
    //	单位名称
    private String unitaccname;
    //	余额
    private String balance;
    //冻结余额
    private String freezeBalance;
    //定期余额
    private String regularBalance;
    //活期余额
    private String dueBalance;
    //	缴存基数
    private String payBaseNum;
    //月缴额
    private String payAmountMonth;
    //	单位缴存比例
    private String unitProp;
    //	个人缴存比例
    private String indiProp;
    //缴至年月
    private String stopPayDate;
    //最后提取日
    private String lastExtractDate;
    //累计提取金额
    private String extractTotal;
    //提取次数
    private String extractCount;
    //贷款次数
    private String loansCount;
    //不良信用次数
    private String badCreditCount;


    /*个人托收信息*/
    //银行代码
    private String bankCode;
    //银行账号
    private String bankAccount;
    //托收银行账户名称
    private String emittingBankAccName;
    //托收日
    private String emittingDate;
    //提取收款是否签约
    private String extGatheringIsSign;
    //个人托收是否签约
    private String perEmittingIsSign;

    /*缴存月数*/
    //连续汇缴月
    private String rePayMonths;
    //累计汇缴月
    private String payMonths;

    public String getExtractTotal() {
        return extractTotal;
    }

    public void setExtractTotal(String extractTotal) {
        this.extractTotal = extractTotal;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getCertiNum() {
        return certiNum;
    }

    public void setCertiNum(String certiNum) {
        this.certiNum = certiNum;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsSmsOpen() {
        return isSmsOpen;
    }

    public void setIsSmsOpen(String isSmsOpen) {
        this.isSmsOpen = isSmsOpen;
    }

    public String getPerOpenData() {
        return perOpenData;
    }

    public void setPerOpenData(String perOpenData) {
        this.perOpenData = perOpenData;
    }

    public String getPerInsurNum() {
        return perInsurNum;
    }

    public void setPerInsurNum(String perInsurNum) {
        this.perInsurNum = perInsurNum;
    }

    public String getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getAccnum() {
        return accnum;
    }

    public void setAccnum(String accnum) {
        this.accnum = accnum;
    }

    public String getUnitAccnum() {
        return unitAccnum;
    }

    public void setUnitAccnum(String unitAccnum) {
        this.unitAccnum = unitAccnum;
    }

    public String getAccGanization() {
        return accGanization;
    }

    public void setAccGanization(String accGanization) {
        this.accGanization = accGanization;
    }

    public String getUnitaccname() {
        return unitaccname;
    }

    public void setUnitaccname(String unitaccname) {
        this.unitaccname = unitaccname;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(String freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public String getRegularBalance() {
        return regularBalance;
    }

    public void setRegularBalance(String regularBalance) {
        this.regularBalance = regularBalance;
    }

    public String getDueBalance() {
        return dueBalance;
    }

    public void setDueBalance(String dueBalance) {
        this.dueBalance = dueBalance;
    }

    public String getPayBaseNum() {
        return payBaseNum;
    }

    public void setPayBaseNum(String payBaseNum) {
        this.payBaseNum = payBaseNum;
    }

    public String getPayAmountMonth() {
        return payAmountMonth;
    }

    public void setPayAmountMonth(String payAmountMonth) {
        this.payAmountMonth = payAmountMonth;
    }

    public String getUnitProp() {
        return unitProp;
    }

    public void setUnitProp(String unitProp) {
        this.unitProp = unitProp;
    }

    public String getIndiProp() {
        return indiProp;
    }

    public void setIndiProp(String indiProp) {
        this.indiProp = indiProp;
    }

    public String getStopPayDate() {
        return stopPayDate;
    }

    public void setStopPayDate(String stopPayDate) {
        this.stopPayDate = stopPayDate;
    }

    public String getLastExtractDate() {
        return lastExtractDate;
    }

    public void setLastExtractDate(String lastExtractDate) {
        this.lastExtractDate = lastExtractDate;
    }

    public String getExtractCount() {
        return extractCount;
    }

    public void setExtractCount(String extractCount) {
        this.extractCount = extractCount;
    }

    public String getLoansCount() {
        return loansCount;
    }

    public void setLoansCount(String loansCount) {
        this.loansCount = loansCount;
    }

    public String getBadCreditCount() {
        return badCreditCount;
    }

    public void setBadCreditCount(String badCreditCount) {
        this.badCreditCount = badCreditCount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getEmittingBankAccName() {
        return emittingBankAccName;
    }

    public void setEmittingBankAccName(String emittingBankAccName) {
        this.emittingBankAccName = emittingBankAccName;
    }

    public String getEmittingDate() {
        return emittingDate;
    }

    public void setEmittingDate(String emittingDate) {
        this.emittingDate = emittingDate;
    }

    public String getExtGatheringIsSign() {
        return extGatheringIsSign;
    }

    public void setExtGatheringIsSign(String extGatheringIsSign) {
        this.extGatheringIsSign = extGatheringIsSign;
    }

    public String getPerEmittingIsSign() {
        return perEmittingIsSign;
    }

    public void setPerEmittingIsSign(String perEmittingIsSign) {
        this.perEmittingIsSign = perEmittingIsSign;
    }

    public String getRePayMonths() {
        return rePayMonths;
    }

    public void setRePayMonths(String rePayMonths) {
        this.rePayMonths = rePayMonths;
    }

    public String getPayMonths() {
        return payMonths;
    }

    public HousingBaoShanUserInfo() {
    }
    public void setPayMonths(String payMonths) {
        this.payMonths = payMonths;
    }

    public String getIndiaccstate() {
        return indiaccstate;
    }

    public void setIndiaccstate(String indiaccstate) {
        this.indiaccstate = indiaccstate;
    }

    public HousingBaoShanUserInfo(String taskid, String accName, String certiNum, String maritalStatus, String profession, String phoneNumber, String email, String isSmsOpen, String perOpenData, String perInsurNum, String isAuth, String homeAddress, String accnum, String indiaccstate, String unitAccnum, String accGanization, String unitaccname, String balance, String freezeBalance, String regularBalance, String dueBalance, String payBaseNum, String payAmountMonth, String unitProp, String indiProp, String stopPayDate, String lastExtractDate, String extractTotal, String extractCount, String loansCount, String badCreditCount, String bankCode, String bankAccount, String emittingBankAccName, String emittingDate, String extGatheringIsSign, String perEmittingIsSign, String rePayMonths, String payMonths) {
        this.taskid = taskid;
        this.accName = accName;
        this.certiNum = certiNum;
        this.maritalStatus = maritalStatus;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isSmsOpen = isSmsOpen;
        this.perOpenData = perOpenData;
        this.perInsurNum = perInsurNum;
        this.isAuth = isAuth;
        this.homeAddress = homeAddress;
        this.accnum = accnum;
        this.indiaccstate = indiaccstate;
        this.unitAccnum = unitAccnum;
        this.accGanization = accGanization;
        this.unitaccname = unitaccname;
        this.balance = balance;
        this.freezeBalance = freezeBalance;
        this.regularBalance = regularBalance;
        this.dueBalance = dueBalance;
        this.payBaseNum = payBaseNum;
        this.payAmountMonth = payAmountMonth;
        this.unitProp = unitProp;
        this.indiProp = indiProp;
        this.stopPayDate = stopPayDate;
        this.lastExtractDate = lastExtractDate;
        this.extractTotal = extractTotal;
        this.extractCount = extractCount;
        this.loansCount = loansCount;
        this.badCreditCount = badCreditCount;
        this.bankCode = bankCode;
        this.bankAccount = bankAccount;
        this.emittingBankAccName = emittingBankAccName;
        this.emittingDate = emittingDate;
        this.extGatheringIsSign = extGatheringIsSign;
        this.perEmittingIsSign = perEmittingIsSign;
        this.rePayMonths = rePayMonths;
        this.payMonths = payMonths;
    }
}