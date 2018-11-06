package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Index;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

/*
 * 手机账户信息 
 */
@Entity
@Table(name = "mobile_account_info")
//@Table(name="mobile_account_info",indexes = {@Index(name = "index_mobile_account_info_taskid", columnList = "taskId")})
public class MobileAccountInfo extends IdEntity implements Serializable {

	private static final long serialVersionUID = 5133365472766002696L;

	//@Column(name = "task_id")
	private String taskId; // 唯一标识
	private String accountStatus; // 账户状态
	private String netInDate; // 入网日期
	private String netInDuration; // 入网时间
	private String realameInfo; // 实名制信息
	private String accountLevel; // 账户等级
	private String levelEnddate; // 等级到期日
	private String points; // 账户积分
	private String accountFrozenBalance; // 账户冻结余额
	private String telephoneNumber; // 用户手机号

	@JsonBackReference
	private String resource; // 溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getNetInDate() {
		return netInDate;
	}

	public void setNetInDate(String netInDate) {
		this.netInDate = netInDate;
	}

	public String getNetInDuration() {
		return netInDuration;
	}

	public void setNetInDuration(String netInDuration) {
		this.netInDuration = netInDuration;
	}

	public String getRealameInfo() {
		return realameInfo;
	}

	public void setRealameInfo(String realameInfo) {
		this.realameInfo = realameInfo;
	}

	public String getAccountLevel() {
		return accountLevel;
	}

	public void setAccountLevel(String accountLevel) {
		this.accountLevel = accountLevel;
	}

	public String getLevelEnddate() {
		return levelEnddate;
	}

	public void setLevelEnddate(String levelEnddate) {
		this.levelEnddate = levelEnddate;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getAccountFrozenBalance() {
		return accountFrozenBalance;
	}

	public void setAccountFrozenBalance(String accountFrozenBalance) {
		this.accountFrozenBalance = accountFrozenBalance;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Column(columnDefinition = "text")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "MobileAccountInfo [taskId=" + taskId + ", accountStatus=" + accountStatus + ", netInDate=" + netInDate
				+ ", netInDuration=" + netInDuration + ", realameInfo=" + realameInfo + ", accountLevel=" + accountLevel
				+ ", levelEnddate=" + levelEnddate + ", points=" + points + ", accountFrozenBalance="
				+ accountFrozenBalance + ", telephoneNumber=" + telephoneNumber + ", resource=" + resource + "]";
	}

}
