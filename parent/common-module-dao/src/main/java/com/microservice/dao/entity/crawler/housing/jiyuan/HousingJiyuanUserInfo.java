package com.microservice.dao.entity.crawler.housing.jiyuan;

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
@Table(name = "housing_jiyuan_userinfo" ,indexes = {@Index(name = "index_housing_jiyuan_userinfo_taskid", columnList = "taskid")})
public class HousingJiyuanUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 管理部
	 */
	private String management;

	/**
	 * 单位编号
	 */
	private String company_num;

	/**
	 * 个人编号
	 */
	private String personal_num;

	/**
	 * 个人姓名
	 */
	private String name;

	/**
	 * 单位名称
	 */
	private String company_name;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 身份证号
	 */
	private String idnumber;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 工资额
	 */
	private String wages;
	
	/**
	 * 缴至月份
	 */
	private String to_pay_month;
	/**
	 * 单位月缴额
	 */
	private String company_month_pay;
	/**
	 * 个人月缴额
	 */
	private String personal_month_pay;
	/**
	 * 月缴额
	 */
	private String month_pay;
	/**
	 * 当前余额
	 */
	private String balance;
	/**
	 * 开户日期
	 */
	private String opening_date;
	/**
	 * 个人状态
	 */
	private String state;
	/**
	 * 个人贷款担保标记
	 */
	private String personal_loan_guarantee_mark;
	/**
	 * 截止时间
	 */
	private String deadline;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getManagement() {
		return management;
	}
	public void setManagement(String management) {
		this.management = management;
	}
	public String getCompany_num() {
		return company_num;
	}
	public void setCompany_num(String company_num) {
		this.company_num = company_num;
	}
	public String getPersonal_num() {
		return personal_num;
	}
	public void setPersonal_num(String personal_num) {
		this.personal_num = personal_num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWages() {
		return wages;
	}
	public void setWages(String wages) {
		this.wages = wages;
	}
	public String getTo_pay_month() {
		return to_pay_month;
	}
	public void setTo_pay_month(String to_pay_month) {
		this.to_pay_month = to_pay_month;
	}
	public String getCompany_month_pay() {
		return company_month_pay;
	}
	public void setCompany_month_pay(String company_month_pay) {
		this.company_month_pay = company_month_pay;
	}
	public String getPersonal_month_pay() {
		return personal_month_pay;
	}
	public void setPersonal_month_pay(String personal_month_pay) {
		this.personal_month_pay = personal_month_pay;
	}
	public String getMonth_pay() {
		return month_pay;
	}
	public void setMonth_pay(String month_pay) {
		this.month_pay = month_pay;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getOpening_date() {
		return opening_date;
	}
	public void setOpening_date(String opening_date) {
		this.opening_date = opening_date;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPersonal_loan_guarantee_mark() {
		return personal_loan_guarantee_mark;
	}
	public void setPersonal_loan_guarantee_mark(String personal_loan_guarantee_mark) {
		this.personal_loan_guarantee_mark = personal_loan_guarantee_mark;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public HousingJiyuanUserInfo(String taskid, String management, String company_num, String personal_num, String name,
			String company_name, String sex, String idnumber, String phone, String wages, String to_pay_month,
			String company_month_pay, String personal_month_pay, String month_pay, String balance, String opening_date,
			String state, String personal_loan_guarantee_mark, String deadline) {
		super();
		this.taskid = taskid;
		this.management = management;
		this.company_num = company_num;
		this.personal_num = personal_num;
		this.name = name;
		this.company_name = company_name;
		this.sex = sex;
		this.idnumber = idnumber;
		this.phone = phone;
		this.wages = wages;
		this.to_pay_month = to_pay_month;
		this.company_month_pay = company_month_pay;
		this.personal_month_pay = personal_month_pay;
		this.month_pay = month_pay;
		this.balance = balance;
		this.opening_date = opening_date;
		this.state = state;
		this.personal_loan_guarantee_mark = personal_loan_guarantee_mark;
		this.deadline = deadline;
	}
	public HousingJiyuanUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
