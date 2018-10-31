package com.microservice.dao.entity.crawler.e_commerce.etl.taobao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="alipay_userinfo_taobao") //交易明细
public class AlipayUserinfoTaoBao extends IdEntity implements Serializable {

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String alipayAccountName;
	private String taobaoAccountName;
	private String idnumberValidityTerm;
	private String registerDate;
	private String isRealnameAuth;
	private String isEmberGuarantee;
	private String isPhone;
	private String securityLevel;
	private String accountBalance;
	private String yuebaoIncome;
	private String isIdnumber;
	private String yuebaoBalance;
	private String checklaterCreditlimit;
	private String historySumIncome;

	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAlipayAccountName() {
		return alipayAccountName;
	}

	public void setAlipayAccountName(String alipayAccountName) {
		this.alipayAccountName = alipayAccountName;
	}

	public String getTaobaoAccountName() {
		return taobaoAccountName;
	}

	public void setTaobaoAccountName(String taobaoAccountName) {
		this.taobaoAccountName = taobaoAccountName;
	}

	public String getIdnumberValidityTerm() {
		return idnumberValidityTerm;
	}

	public void setIdnumberValidityTerm(String idnumberValidityTerm) {
		this.idnumberValidityTerm = idnumberValidityTerm;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getIsRealnameAuth() {
		return isRealnameAuth;
	}

	public void setIsRealnameAuth(String isRealnameAuth) {
		this.isRealnameAuth = isRealnameAuth;
	}

	public String getIsEmberGuarantee() {
		return isEmberGuarantee;
	}

	public void setIsEmberGuarantee(String isEmberGuarantee) {
		this.isEmberGuarantee = isEmberGuarantee;
	}

	public String getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}

	public String getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getYuebaoIncome() {
		return yuebaoIncome;
	}

	public void setYuebaoIncome(String yuebaoIncome) {
		this.yuebaoIncome = yuebaoIncome;
	}

	public String getIsIdnumber() {
		return isIdnumber;
	}

	public void setIsIdnumber(String isIdnumber) {
		this.isIdnumber = isIdnumber;
	}

	public String getYuebaoBalance() {
		return yuebaoBalance;
	}

	public void setYuebaoBalance(String yuebaoBalance) {
		this.yuebaoBalance = yuebaoBalance;
	}

	public String getChecklaterCreditlimit() {
		return checklaterCreditlimit;
	}

	public void setChecklaterCreditlimit(String checklaterCreditlimit) {
		this.checklaterCreditlimit = checklaterCreditlimit;
	}

	public String getHistorySumIncome() {
		return historySumIncome;
	}

	public void setHistorySumIncome(String historySumIncome) {
		this.historySumIncome = historySumIncome;
	}
	
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "AlipayUserinfoTaoBao [taskId=" + taskId + ", alipayAccountName=" + alipayAccountName
				+ ", taobaoAccountName=" + taobaoAccountName + ", idnumberValidityTerm=" + idnumberValidityTerm
				+ ", registerDate=" + registerDate + ", isRealnameAuth=" + isRealnameAuth + ", isEmberGuarantee="
				+ isEmberGuarantee + ", isPhone=" + isPhone + ", securityLevel=" + securityLevel + ", accountBalance="
				+ accountBalance + ", yuebaoIncome=" + yuebaoIncome + ", isIdnumber=" + isIdnumber + ", yuebaoBalance="
				+ yuebaoBalance + ", checklaterCreditlimit=" + checklaterCreditlimit + ", historySumIncome="
				+ historySumIncome + ", resource=" + resource + "]";
	}
	
	

}
