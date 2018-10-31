package com.microservice.dao.entity.crawler.bank.cmbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 招商银行信用卡个人信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "cmbchina_creditcard_userinfo" ,indexes = {@Index(name = "index_cmbchina_creditcard_userinfo_taskid", columnList = "taskid")})
public class CmbChinaCreditCardUserInfo extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8763264875623438865L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 信用额度--------人民币
	 */
	private String trxyedRMB;
	/**
	 * 信用额度--------美元
	 */
	private String trxyedDollar;

	/**
	 * 可用额度--------人民币
	 */
	private String trkyedRMB;
	/**
	 * 可用额度--------美元
	 */
	private String trkyedDollar;

	/**
	 * 预借现金可用额度--------人民币
	 */
	private String tryjxjedRMB;
	/**
	 * 预借现金可用额度--------美元
	 */
	private String tryjxjedDollar;
	/**
	 * 每月账单日
	 */
	private String lmyzd;
	/**
	 * 本期到期还款日
	 */
	private String lbqdqhk;

	/**
	 * 卡号
	 */
	private String idNum;
	/**
	 * 主卡/附属卡
	 */
	private String cardType;
	/**
	 * 联名卡别
	 */
	private String cardStyle;
	/**
	 * 持卡人姓名
	 */
	private String name;
	/**
	 * 开卡标志
	 */
	private String cardState;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTrxyedRMB() {
		return trxyedRMB;
	}
	public void setTrxyedRMB(String trxyedRMB) {
		this.trxyedRMB = trxyedRMB;
	}
	public String getTrxyedDollar() {
		return trxyedDollar;
	}
	public void setTrxyedDollar(String trxyedDollar) {
		this.trxyedDollar = trxyedDollar;
	}
	public String getTrkyedRMB() {
		return trkyedRMB;
	}
	public void setTrkyedRMB(String trkyedRMB) {
		this.trkyedRMB = trkyedRMB;
	}
	public String getTrkyedDollar() {
		return trkyedDollar;
	}
	public void setTrkyedDollar(String trkyedDollar) {
		this.trkyedDollar = trkyedDollar;
	}
	public String getTryjxjedRMB() {
		return tryjxjedRMB;
	}
	public void setTryjxjedRMB(String tryjxjedRMB) {
		this.tryjxjedRMB = tryjxjedRMB;
	}
	public String getTryjxjedDollar() {
		return tryjxjedDollar;
	}
	public void setTryjxjedDollar(String tryjxjedDollar) {
		this.tryjxjedDollar = tryjxjedDollar;
	}
	public String getLmyzd() {
		return lmyzd;
	}
	public void setLmyzd(String lmyzd) {
		this.lmyzd = lmyzd;
	}
	public String getLbqdqhk() {
		return lbqdqhk;
	}
	public void setLbqdqhk(String lbqdqhk) {
		this.lbqdqhk = lbqdqhk;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardStyle() {
		return cardStyle;
	}
	public void setCardStyle(String cardStyle) {
		this.cardStyle = cardStyle;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardState() {
		return cardState;
	}
	public void setCardState(String cardState) {
		this.cardState = cardState;
	}
	public CmbChinaCreditCardUserInfo(String taskid, String trxyedRMB, String trxyedDollar, String trkyedRMB,
			String trkyedDollar, String tryjxjedRMB, String tryjxjedDollar, String lmyzd, String lbqdqhk, String idNum,
			String cardType, String cardStyle, String name, String cardState) {
		super();
		this.taskid = taskid;
		this.trxyedRMB = trxyedRMB;
		this.trxyedDollar = trxyedDollar;
		this.trkyedRMB = trkyedRMB;
		this.trkyedDollar = trkyedDollar;
		this.tryjxjedRMB = tryjxjedRMB;
		this.tryjxjedDollar = tryjxjedDollar;
		this.lmyzd = lmyzd;
		this.lbqdqhk = lbqdqhk;
		this.idNum = idNum;
		this.cardType = cardType;
		this.cardStyle = cardStyle;
		this.name = name;
		this.cardState = cardState;
	}
	public CmbChinaCreditCardUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
