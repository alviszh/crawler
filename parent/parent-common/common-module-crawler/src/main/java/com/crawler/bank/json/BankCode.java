package com.crawler.bank.json;

import java.io.Serializable;

public class BankCode implements Serializable{

    private Long bankId;
    private String bankName;
    private String bankNameEn;
    private String bankShortnameEn;
    private String bankShortname;
    private String bankSwfitCode;
    private Integer isFlag;			//1.储蓄卡开发完成    2.信用卡开发完成  3.两个类型都开发完成
    private Integer creFlag;
    ;
    public Long getBankId() {
        return bankId;
    }
    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBankNameEn() {
        return bankNameEn;
    }
    public void setBankNameEn(String bankNameEn) {
        this.bankNameEn = bankNameEn;
    }
    public String getBankShortnameEn() {
        return bankShortnameEn;
    }
    public void setBankShortnameEn(String bankShortnameEn) {
        this.bankShortnameEn = bankShortnameEn;
    }
    public String getBankShortname() {
        return bankShortname;
    }
    public void setBankShortname(String bankShortname) {
        this.bankShortname = bankShortname;
    }
    public String getBankSwfitCode() {
        return bankSwfitCode;
    }
    public void setBankSwfitCode(String bankSwfitCode) {
        this.bankSwfitCode = bankSwfitCode;
    }
    public Integer getIsFlag() {
        return isFlag;
    }
    public void setIsFlag(Integer isFlag) {
        this.isFlag = isFlag;
    }
    public Integer getCreFlag() {
        return creFlag;
    }
    public void setCreFlag(Integer creFlag) {
        this.creFlag = creFlag;
    }

    @Override
    public String toString() {
        return "BankCode{" +
                "bankId=" + bankId +
                ", bankName='" + bankName + '\'' +
                ", bankNameEn='" + bankNameEn + '\'' +
                ", bankShortnameEn='" + bankShortnameEn + '\'' +
                ", bankShortname='" + bankShortname + '\'' +
                ", bankSwfitCode=" + bankSwfitCode +
                ", isFlag=" + isFlag +
                ", creFlag=" + creFlag +
                '}';
    }
}
