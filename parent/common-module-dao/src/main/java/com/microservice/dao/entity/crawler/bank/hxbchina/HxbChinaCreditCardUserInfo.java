package com.microservice.dao.entity.crawler.bank.hxbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="hxbchina_creditcard_userinfo",indexes = {@Index(name = "index_hxbchina_creditcard_userinfo_taskid", columnList = "taskid")})
public class HxbChinaCreditCardUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 4742551477839146611L;
	private String taskid;
//	持卡人姓名
	private String cardOwnerName;
//	信用卡卡号
	private String cardNo;
//	信用额度
	private String creditLimit;
//	可用额度
	private String  availableLimit;
//	消费是否使用密码
	private String usePwdFlag;
//	账户别名
	private String accOtherName;
//	卡片凸字姓名
	private String cardConvexWordName;
//	卡片有效期
	private String cardValidDate;
//	主附卡标志
	private String mainFlag;
//	卡片种类描述
	private String cardType;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCardOwnerName() {
		return cardOwnerName;
	}
	public void setCardOwnerName(String cardOwnerName) {
		this.cardOwnerName = cardOwnerName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getAvailableLimit() {
		return availableLimit;
	}
	public void setAvailableLimit(String availableLimit) {
		this.availableLimit = availableLimit;
	}
	public String getUsePwdFlag() {
		return usePwdFlag;
	}
	public void setUsePwdFlag(String usePwdFlag) {
		this.usePwdFlag = usePwdFlag;
	}
	public String getAccOtherName() {
		return accOtherName;
	}
	public void setAccOtherName(String accOtherName) {
		this.accOtherName = accOtherName;
	}
	public String getCardConvexWordName() {
		return cardConvexWordName;
	}
	public void setCardConvexWordName(String cardConvexWordName) {
		this.cardConvexWordName = cardConvexWordName;
	}
	public String getCardValidDate() {
		return cardValidDate;
	}
	public void setCardValidDate(String cardValidDate) {
		this.cardValidDate = cardValidDate;
	}
	public String getMainFlag() {
		return mainFlag;
	}
	public void setMainFlag(String mainFlag) {
		this.mainFlag = mainFlag;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public HxbChinaCreditCardUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HxbChinaCreditCardUserInfo(String taskid, String cardOwnerName, String cardNo, String creditLimit,
			String availableLimit, String usePwdFlag, String accOtherName, String cardConvexWordName,
			String cardValidDate, String mainFlag, String cardType) {
		super();
		this.taskid = taskid;
		this.cardOwnerName = cardOwnerName;
		this.cardNo = cardNo;
		this.creditLimit = creditLimit;
		this.availableLimit = availableLimit;
		this.usePwdFlag = usePwdFlag;
		this.accOtherName = accOtherName;
		this.cardConvexWordName = cardConvexWordName;
		this.cardValidDate = cardValidDate;
		this.mainFlag = mainFlag;
		this.cardType = cardType;
	}
}
