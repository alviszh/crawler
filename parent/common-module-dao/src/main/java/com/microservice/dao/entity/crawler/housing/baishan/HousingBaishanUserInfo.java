package com.microservice.dao.entity.crawler.housing.baishan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 白山公积金用户信息
 */
@Entity
@Table(name="housing_baishan_userinfo")
public class HousingBaishanUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;

	private String username;// 姓名
	private String companyNum;// 	单位账号
	private String idnum;// 身份证号
	private String accountNum;// 职工账号
	private String companyName;// 所在单位
	private String staffName;// 所属办事处
	private String openTime;//开户日期
	private String accountState;//前状态
	private String payBase;// 月缴基数
	private String payAmount;//	月缴金额
	private String payRatio;//	个人/单位月缴比例
	private String suppleRatio;// 	补充缴存率
	private String taskid;
	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getPayBase() {
		return payBase;
	}

	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayRatio() {
		return payRatio;
	}

	public void setPayRatio(String payRatio) {
		this.payRatio = payRatio;
	}

	public String getSuppleRatio() {
		return suppleRatio;
	}

	public void setSuppleRatio(String suppleRatio) {
		this.suppleRatio = suppleRatio;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	@Override
	public String toString() {
		return "HousingBaishanUserInfo [username=" + username + ", companyNum=" + companyNum + ", idnum=" + idnum
				+ ", accountNum=" + accountNum + ", companyName=" + companyName + ", staffName=" + staffName
				+ ", openTime=" + openTime + ", accountState=" + accountState + ", payBase=" + payBase + ", payAmount="
				+ payAmount + ", payRatio=" + payRatio + ", suppleRatio=" + suppleRatio + ", taskid=" + taskid + "]";
	}

	public HousingBaishanUserInfo(String username, String companyNum, String idnum, String accountNum,
			String companyName, String staffName, String openTime, String accountState, String payBase,
			String payAmount, String payRatio, String suppleRatio, String taskid) {
		super();
		this.username = username;
		this.companyNum = companyNum;
		this.idnum = idnum;
		this.accountNum = accountNum;
		this.companyName = companyName;
		this.staffName = staffName;
		this.openTime = openTime;
		this.accountState = accountState;
		this.payBase = payBase;
		this.payAmount = payAmount;
		this.payRatio = payRatio;
		this.suppleRatio = suppleRatio;
		this.taskid = taskid;
	}

	
	public HousingBaishanUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
