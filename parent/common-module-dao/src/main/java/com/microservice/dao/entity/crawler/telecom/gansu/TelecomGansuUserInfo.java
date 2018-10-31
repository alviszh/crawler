package com.microservice.dao.entity.crawler.telecom.gansu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGansu_userinfo",indexes = {@Index(name = "index_telecomGansu_userinfo_taskid", columnList = "taskid")}) 
public class TelecomGansuUserInfo extends IdEntity{
	private String name;//姓名
	
	private String phone;//电话
	
	private String cardType;//证件类型
	
	private String cardNum;//证件号
	
	private String lineNum;//关联电话
	
	private String postalcode;//邮编
	
	private String addr;//地址

	private String taskid;
	
	private String accountMoney;//余额

	private String custName;
	
	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getMembershipLevel() {
		return membershipLevel;
	}

	public void setMembershipLevel(String membershipLevel) {
		this.membershipLevel = membershipLevel;
	}

	public String getGrowthpoint() {
		return growthpoint;
	}

	public void setGrowthpoint(String growthpoint) {
		this.growthpoint = growthpoint;
	}

	private String membershipLevel;//星级
	
	private String growthpoint;//成长值

	@Override
	public String toString() {
		return "TelecomGansuUserInfo [name=" + name + ", phone=" + phone + ", cardType=" + cardType + ", cardNum="
				+ cardNum + ", lineNum=" + lineNum + ", postalcode=" + postalcode + ", addr=" + addr + ", taskid="
				+ taskid + ", accountMoney=" + accountMoney + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccountMoney() {
		return accountMoney;
	}

	public void setAccountMoney(String accountMoney) {
		this.accountMoney = accountMoney;
	}

	
	
	
}
