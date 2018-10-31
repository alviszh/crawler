package com.microservice.dao.entity.crawler.housing.sz.hunan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_sz_hunan_userinfo" ,indexes = {@Index(name = "index_housing_sz_hunan_userinfo_taskid", columnList = "taskid")})
public class HousingSZHunanUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 职工姓名
	 */
	private String name;
	/**
	 * 银行账号
	 */
	private String bank;
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
	private String yearPay;
	/**
	 * 个人月缴额
	 */
	private String psnMonthlyPay;
	/**
	 * 本年支取
	 */
	private String yearDraw;
	/**
	 * 本年缴交
	 */
	private String yearPayable;
	/**
	 * 本年利息
	 */
	private String interest;
	/**
	 * 本年转入
	 */
	private String yearInto;
	/**
	 * 公积金余额
	 */
	private String balance;
	/**
	 * 缴至年月
	 */
	private String yearMonth;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
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
	public String getCompanyMonthlyPay() {
		return companyMonthlyPay;
	}
	public void setCompanyMonthlyPay(String companyMonthlyPay) {
		this.companyMonthlyPay = companyMonthlyPay;
	}
	public String getYearPay() {
		return yearPay;
	}
	public void setYearPay(String yearPay) {
		this.yearPay = yearPay;
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
	public String getYearPayable() {
		return yearPayable;
	}
	public void setYearPayable(String yearPayable) {
		this.yearPayable = yearPayable;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
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
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public HousingSZHunanUserInfo(String taskid, String name, String bank, String idNum, String staffAccount,
			String company, String office, String openingDate, String state, String basemny, String proportion,
			String monthlyPay, String yearBalance, String companyMonthlyPay, String yearPay, String psnMonthlyPay,
			String yearDraw, String yearPayable, String interest, String yearInto, String balance, String yearMonth) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.bank = bank;
		this.idNum = idNum;
		this.staffAccount = staffAccount;
		this.company = company;
		this.office = office;
		this.openingDate = openingDate;
		this.state = state;
		this.basemny = basemny;
		this.proportion = proportion;
		this.monthlyPay = monthlyPay;
		this.yearBalance = yearBalance;
		this.companyMonthlyPay = companyMonthlyPay;
		this.yearPay = yearPay;
		this.psnMonthlyPay = psnMonthlyPay;
		this.yearDraw = yearDraw;
		this.yearPayable = yearPayable;
		this.interest = interest;
		this.yearInto = yearInto;
		this.balance = balance;
		this.yearMonth = yearMonth;
	}
	public HousingSZHunanUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
