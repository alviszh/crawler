package com.microservice.dao.entity.crawler.bank.basic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bank_code")
public class BankCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankId;
	private String bankName;
	private String bankNameEn;
	private String bankShortnameEn;
	private String bankShortname;
	private String bankSwfitCode;
	private Integer isFlag;			//储蓄卡状态：0.维护中 1.可用
	private Integer creFlag;		//信用卡状态： 0.维护中 1.可用
	
	public Integer getCreFlag() {
		return creFlag;
	}
	public void setCreFlag(Integer creFlag) {
		this.creFlag = creFlag;
	}
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

}
