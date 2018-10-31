package com.microservice.dao.entity.crawler.bank.cmbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:我的信用卡信息
 * @author: sln 
 * @date: 2017年11月14日 下午3:08:35 
 */
@Entity
@Table(name="cmbcchina_creditcard_generalinfo",indexes = {@Index(name = "index_cmbcchina_creditcard_generalinfo_taskid", columnList = "taskid")})
public class CmbcChinaCreditCardGeneralInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -1038179931254822961L;
	private String taskid;
//	持卡人姓名
	private String cardOwner;
//	信用卡号
	private String accountNum;
//	账户类型
	private String accountType;
//	卡片类型
	private String cardTypeDesc;
//	主附卡标志
	private String mainFlag;
//	卡片状态
	private String accountState;
//	开户日期
	private String openDate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCardOwner() {
		return cardOwner;
	}
	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getCardTypeDesc() {
		return cardTypeDesc;
	}
	public void setCardTypeDesc(String cardTypeDesc) {
		this.cardTypeDesc = cardTypeDesc;
	}
	public String getMainFlag() {
		return mainFlag;
	}
	public void setMainFlag(String mainFlag) {
		this.mainFlag = mainFlag;
	}
	public String getAccountState() {
		return accountState;
	}
	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public CmbcChinaCreditCardGeneralInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CmbcChinaCreditCardGeneralInfo(String taskid, String cardOwner, String accountNum, String accountType,
			String cardTypeDesc, String mainFlag, String accountState, String openDate) {
		super();
		this.taskid = taskid;
		this.cardOwner = cardOwner;
		this.accountNum = accountNum;
		this.accountType = accountType;
		this.cardTypeDesc = cardTypeDesc;
		this.mainFlag = mainFlag;
		this.accountState = accountState;
		this.openDate = openDate;
	}
	
}
