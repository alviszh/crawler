package com.microservice.dao.entity.crawler.housing.shangrao;

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
@Table(name = "housing_shangrao_userinfo")
public class HousingShangRaoUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 职工姓名
	 */
	private String username;
	/**
	 *联名卡卡号
	 */
	private String cardNum;
	/**
	 * 身份证号
	 */
	private String idNum;
	/**
	 * 职工公积金编号
	 */
	private String fundNum;
	/**
	 * 所在单位
	 */
	private String companyName;
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
	 * 上年余额
	 */
	private String yearBalance;
	/**
	 * 单位月缴额
	 */
	private String companyMonthlyPay;
	/**
	 * 本年补缴
	 */
	private String yearBackupPay;
	/**
	 * 个人月缴额
	 */
	private String psnMonthlyPay;
	/**
	 * 本年支取
	 */
	private String yearDraw;
	/**
	 * 财配月缴额
	 */
	private String financeMonthlyPay;
	/**
	 * 本年利息
	 */
	private String yearInterest;
	/**
	 * 本年缴交
	 */
	private String yearPay;
	/**
	 * 公积金余额
	 */
	private String balance;

	/**
	 * 本年转入
	 */
	private String yearInto;
	/**
	 * 应缴年月
	 */
	private String paymonth;	
	
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
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getFundNum() {
		return fundNum;
	}
	public void setFundNum(String fundNum) {
		this.fundNum = fundNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getCompanyMonthlyPay() {
		return companyMonthlyPay;
	}
	public void setCompanyMonthlyPay(String companyMonthlyPay) {
		this.companyMonthlyPay = companyMonthlyPay;
	}
	public String getYearBackupPay() {
		return yearBackupPay;
	}
	public void setYearBackupPay(String yearBackupPay) {
		this.yearBackupPay = yearBackupPay;
	}
	public String getPsnMonthlyPay() {
		return psnMonthlyPay;
	}
	public void setPsnMonthlyPay(String psnMonthlyPay) {
		this.psnMonthlyPay = psnMonthlyPay;
	}
	public String getYearDraw() {
		return yearDraw;
	}
	public void setYearDraw(String yearDraw) {
		this.yearDraw = yearDraw;
	}
	public String getFinanceMonthlyPay() {
		return financeMonthlyPay;
	}
	public void setFinanceMonthlyPay(String financeMonthlyPay) {
		this.financeMonthlyPay = financeMonthlyPay;
	}
	public String getYearInterest() {
		return yearInterest;
	}
	public void setYearInterest(String yearInterest) {
		this.yearInterest = yearInterest;
	}
	public String getYearPay() {
		return yearPay;
	}
	public void setYearPay(String yearPay) {
		this.yearPay = yearPay;
	}
	
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getYearInto() {
		return yearInto;
	}
	public void setYearInto(String yearInto) {
		this.yearInto = yearInto;
	}
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	
	public HousingShangRaoUserInfo(String taskid, String username, String cardNum, String idNum, String fundNum,
			String companyName, String office, String openingDate, String state, String basemny, String proportion,
			String monthlyPay, String yearBalance, String companyMonthlyPay, String yearBackupPay, String psnMonthlyPay,
			String yearDraw, String financeMonthlyPay, String yearInterest, String yearPay,String balance, String yearInto,
			String paymonth) {
		super();
		this.taskid = taskid;
		this.username = username;
		this.cardNum = cardNum;
		this.idNum = idNum;
		this.fundNum = fundNum;
		this.companyName = companyName;
		this.office = office;
		this.openingDate = openingDate;
		this.state = state;
		this.basemny = basemny;
		this.proportion = proportion;
		this.monthlyPay = monthlyPay;
		this.yearBalance = yearBalance;
		this.companyMonthlyPay = companyMonthlyPay;
		this.yearBackupPay = yearBackupPay;
		this.psnMonthlyPay = psnMonthlyPay;
		this.yearDraw = yearDraw;
		this.financeMonthlyPay = financeMonthlyPay;
		this.yearInterest = yearInterest;
		this.yearPay = yearPay;
		this.balance=balance;
		this.yearInto = yearInto;
		this.paymonth = paymonth;
	}
	public HousingShangRaoUserInfo() {
		super();
	}
}
