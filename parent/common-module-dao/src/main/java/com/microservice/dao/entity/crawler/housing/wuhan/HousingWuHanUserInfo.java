package com.microservice.dao.entity.crawler.housing.wuhan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_wuhan_userinfo",indexes = {@Index(name = "index_housing_wuhan_userinfo_taskid", columnList = "taskid")})
public class HousingWuHanUserInfo extends IdEntity implements Serializable {

	private String taskid;
	
	private Integer userid;
	
	private String UnitName;//存缴单位
	
	private String SalaryNum;//缴存基数
	
	private String UserName ;//姓名
	
	private String LastPayMouth;//上笔存缴日期
	
	private String AcctState;//状态
	
	private String UnitAcctNo;//单位帐号
	
	private String Balance;//余额
	
	private String MonthPaySum;//月缴额
	
	private String AcctNo;//公积金帐号
	
	private String _MCHTimestamp;//数据截至日期

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUnitName() {
		return UnitName;
	}

	public void setUnitName(String unitName) {
		UnitName = unitName;
	}

	public String getSalaryNum() {
		return SalaryNum;
	}

	public void setSalaryNum(String salaryNum) {
		SalaryNum = salaryNum;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getLastPayMouth() {
		return LastPayMouth;
	}

	public void setLastPayMouth(String lastPayMouth) {
		LastPayMouth = lastPayMouth;
	}

	public String getAcctState() {
		return AcctState;
	}

	public void setAcctState(String acctState) {
		AcctState = acctState;
	}

	public String getUnitAcctNo() {
		return UnitAcctNo;
	}

	public void setUnitAcctNo(String unitAcctNo) {
		UnitAcctNo = unitAcctNo;
	}

	public String getBalance() {
		return Balance;
	}

	public void setBalance(String balance) {
		Balance = balance;
	}

	public String getMonthPaySum() {
		return MonthPaySum;
	}

	public void setMonthPaySum(String monthPaySum) {
		MonthPaySum = monthPaySum;
	}

	public String getAcctNo() {
		return AcctNo;
	}

	public void setAcctNo(String acctNo) {
		AcctNo = acctNo;
	}

	public String get_MCHTimestamp() {
		return _MCHTimestamp;
	}

	public void set_MCHTimestamp(String _MCHTimestamp) {
		this._MCHTimestamp = _MCHTimestamp;
	}

	@Override
	public String toString() {
		return "HousingWuHanUserInfo [taskid=" + taskid + ", userid=" + userid + ", UnitName=" + UnitName
				+ ", SalaryNum=" + SalaryNum + ", UserName=" + UserName + ", LastPayMouth=" + LastPayMouth
				+ ", AcctState=" + AcctState + ", UnitAcctNo=" + UnitAcctNo + ", Balance=" + Balance + ", MonthPaySum="
				+ MonthPaySum + ", AcctNo=" + AcctNo + ", _MCHTimestamp=" + _MCHTimestamp + "]";
	}

	public HousingWuHanUserInfo(String taskid, Integer userid, String unitName, String salaryNum, String userName,
			String lastPayMouth, String acctState, String unitAcctNo, String balance, String monthPaySum, String acctNo,
			String _MCHTimestamp) {
		super();
		this.taskid = taskid;
		this.userid = userid;
		UnitName = unitName;
		SalaryNum = salaryNum;
		UserName = userName;
		LastPayMouth = lastPayMouth;
		AcctState = acctState;
		UnitAcctNo = unitAcctNo;
		Balance = balance;
		MonthPaySum = monthPaySum;
		AcctNo = acctNo;
		this._MCHTimestamp = _MCHTimestamp;
	}

	public HousingWuHanUserInfo() {
		super();
	}
	
	
	
	
}
