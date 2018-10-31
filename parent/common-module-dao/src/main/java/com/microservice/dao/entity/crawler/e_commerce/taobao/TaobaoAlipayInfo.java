package com.microservice.dao.entity.crawler.e_commerce.taobao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_taobao_alipay_info")
public class TaobaoAlipayInfo extends IdEntity implements Serializable {
    private String taskid;
    private String account;//账户名
    private String userName;
    private String accountBalance;//账户余额
    private String yu_e_bao;//余额宝
    private String yu_e_baoAccumulatedEarnings;//余额宝累计收益
    private String huabeiAvailableCredit;//花呗可用额度
    private String huabeiTotalCredit;//花呗总额度
    private String lastLoginTime;//上次登录时间
    private String realName;
    private String idNum;//身份证号
    private String email;
    private String phone;
    private String taobaoMemberName;//淘宝会员名
    private String registerTime;//注册时间
    private String memberGuarantee;//会员保障
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getYu_e_bao() {
		return yu_e_bao;
	}
	public void setYu_e_bao(String yu_e_bao) {
		this.yu_e_bao = yu_e_bao;
	}
	public String getYu_e_baoAccumulatedEarnings() {
		return yu_e_baoAccumulatedEarnings;
	}
	public void setYu_e_baoAccumulatedEarnings(String yu_e_baoAccumulatedEarnings) {
		this.yu_e_baoAccumulatedEarnings = yu_e_baoAccumulatedEarnings;
	}
	public String getHuabeiAvailableCredit() {
		return huabeiAvailableCredit;
	}
	public void setHuabeiAvailableCredit(String huabeiAvailableCredit) {
		this.huabeiAvailableCredit = huabeiAvailableCredit;
	}
	public String getHuabeiTotalCredit() {
		return huabeiTotalCredit;
	}
	public void setHuabeiTotalCredit(String huabeiTotalCredit) {
		this.huabeiTotalCredit = huabeiTotalCredit;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTaobaoMemberName() {
		return taobaoMemberName;
	}
	public void setTaobaoMemberName(String taobaoMemberName) {
		this.taobaoMemberName = taobaoMemberName;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public String getMemberGuarantee() {
		return memberGuarantee;
	}
	public void setMemberGuarantee(String memberGuarantee) {
		this.memberGuarantee = memberGuarantee;
	}
	
	@Override
	public String toString() {
		return "TaobaoAlipayInfo [taskid=" + taskid + ", account=" + account + ", userName=" + userName
				+ ", accountBalance=" + accountBalance + ", yu_e_bao=" + yu_e_bao + ", yu_e_baoAccumulatedEarnings="
				+ yu_e_baoAccumulatedEarnings + ", huabeiAvailableCredit=" + huabeiAvailableCredit
				+ ", huabeiTotalCredit=" + huabeiTotalCredit + ", lastLoginTime=" + lastLoginTime + ", realName="
				+ realName + ", idNum=" + idNum + ", email=" + email + ", phone=" + phone + ", taobaoMemberName="
				+ taobaoMemberName + ", registerTime=" + registerTime + ", memberGuarantee=" + memberGuarantee + "]";
	}

}
