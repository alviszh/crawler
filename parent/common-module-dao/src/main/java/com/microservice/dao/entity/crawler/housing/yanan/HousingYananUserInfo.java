package com.microservice.dao.entity.crawler.housing.yanan;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_yanan_userinfo")
public class HousingYananUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 职工姓名
	 */
	private String username;
	/**
	 * 银行账号
	 */
	private String bankNum;
	/**
	 * 身份证号
	 */
	private String idNum;
	/**
	 * 职工账号
	 */
	private String staffAccount;
	/**
	 * 所在单位
	 */
	private String company;
	/**
	 * 所属办事处
	 */
	private String office;
	/**
	 * 开户日期
	 */
	private String openingDate;
	/**
	 * 当前状态
	 */
	private String state;
	/**
	 * 月缴基数
	 */
	private String basemny;
	/**
	 * 缴存比例-----个人/单位
	 */
	private String proportion;
	/**
	 * 月缴金额
	 */
	private String monthlyPay;
	/**
	 * 缴存比例-----补贴额
	 */
	private String supplyAmount;
	/**
	 * 上年余额
	 */
	private String yearBalance;
	/**
	 * 本年补缴
	 */
	private String yearPay;
	/**
	 * 本年缴交
	 */
	private String yearPayable;
	/**
	 * 本年支取
	 */
	private String yearDraw;
	/**
	 * 本年利息
	 */
	private String yearInterest;
	/**
	 * 本年转入
	 */
	private String yearInto;
	/**
	 * 实存总额
	 */
	private String totalAmount;
	/**
	 * 本金余额
	 */
	private String balance;	
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getStaffAccount() {
		return staffAccount;
	}
	public void setStaffAccount(String staffAccount) {
		this.staffAccount = staffAccount;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getOpeningDate() {
		return openingDate;
	}
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBasemny() {
		return basemny;
	}
	public void setBasemny(String basemny) {
		this.basemny = basemny;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getMonthlyPay() {
		return monthlyPay;
	}
	public void setMonthlyPay(String monthlyPay) {
		this.monthlyPay = monthlyPay;
	}
	public String getYearBalance() {
		return yearBalance;
	}
	public void setYearBalance(String yearBalance) {
		this.yearBalance = yearBalance;
	}
	
	public String getYearPay() {
		return yearPay;
	}
	public void setYearPay(String yearPay) {
		this.yearPay = yearPay;
	}
	
	public String getYearDraw() {
		return yearDraw;
	}
	public void setYearDraw(String yearDraw) {
		this.yearDraw = yearDraw;
	}
	public String getYearPayable() {
		return yearPayable;
	}
	public void setYearPayable(String yearPayable) {
		this.yearPayable = yearPayable;
	}
	
	public String getYearInterest() {
		return yearInterest;
	}
	public void setYearInterest(String yearInterest) {
		this.yearInterest = yearInterest;
	}
	public String getYearInto() {
		return yearInto;
	}
	public void setYearInto(String yearInto) {
		this.yearInto = yearInto;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public String getSupplyAmount() {
		return supplyAmount;
	}
	public void setSupplyAmount(String supplyAmount) {
		this.supplyAmount = supplyAmount;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Override
	public String toString() {
		return "HousingYananUserInfo [taskid=" + taskid + ", username=" + username + ", bankNum=" + bankNum + ", idNum="
				+ idNum + ", staffAccount=" + staffAccount + ", company=" + company + ", office=" + office
				+ ", openingDate=" + openingDate + ", state=" + state + ", basemny=" + basemny + ", proportion="
				+ proportion + ", monthlyPay=" + monthlyPay + ", supplyAmount=" + supplyAmount + ", yearBalance="
				+ yearBalance + ", yearPay=" + yearPay + ", yearPayable=" + yearPayable + ", yearDraw=" + yearDraw
				+ ", yearInterest=" + yearInterest + ", yearInto=" + yearInto + ", totalAmount=" + totalAmount
				+ ", balance=" + balance + "]";
	}
	
	public HousingYananUserInfo(String taskid, String username, String bankNum, String idNum, String staffAccount,
			String company, String office, String openingDate, String state, String basemny, String proportion,
			String monthlyPay, String supplyAmount, String yearBalance, String yearPay, String yearPayable,
			String yearDraw, String yearInterest, String yearInto, String totalAmount, String balance) {
		super();
		this.taskid = taskid;
		this.username = username;
		this.bankNum = bankNum;
		this.idNum = idNum;
		this.staffAccount = staffAccount;
		this.company = company;
		this.office = office;
		this.openingDate = openingDate;
		this.state = state;
		this.basemny = basemny;
		this.proportion = proportion;
		this.monthlyPay = monthlyPay;
		this.supplyAmount = supplyAmount;
		this.yearBalance = yearBalance;
		this.yearPay = yearPay;
		this.yearPayable = yearPayable;
		this.yearDraw = yearDraw;
		this.yearInterest = yearInterest;
		this.yearInto = yearInto;
		this.totalAmount = totalAmount;
		this.balance = balance;
	}
	public HousingYananUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
