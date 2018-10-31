package com.microservice.dao.entity.crawler.insurance.sz.zhejiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_zhejiang_userinfo",indexes = {@Index(name = "index_insurance_sz_zhejiang_userinfo_taskid", columnList = "taskid")})
public class InsuranceSZZheJiangUserInfo extends IdEntity{

	private String taskid;
	private String name;
	private String insured_state;//参保状态
	private String dw_name;
	private String insurance_num;//社会保障号
	private String lastyear_pay_month;//至上年末实际缴费月数
	private String date;//年度
	private String lastyear_tally;//上年末记账金额
	private String interest;//上年末记账利息
	private String lastyear_account;//截至上年末个人账户累计储存额
	private String year_account;//本年末个人账户累计存储额
	private String year_tally;//本年末记账金额
	private String num_name;//账户名
	private String tel;//手机号
	private String email;//邮箱
	private String security;//安全等级
	private String sex;//性别
	private String nation;//民族
	private String passport;//护照
	public InsuranceSZZheJiangUserInfo(String taskid, String name, String insured_state, String dw_name,
			String insurance_num, String lastyear_pay_month, String date, String lastyear_tally, String interest,
			String lastyear_account, String year_account, String year_tally, String num_name, String tel, String email,
			String security, String sex, String nation, String passport) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.insured_state = insured_state;
		this.dw_name = dw_name;
		this.insurance_num = insurance_num;
		this.lastyear_pay_month = lastyear_pay_month;
		this.date = date;
		this.lastyear_tally = lastyear_tally;
		this.interest = interest;
		this.lastyear_account = lastyear_account;
		this.year_account = year_account;
		this.year_tally = year_tally;
		this.num_name = num_name;
		this.tel = tel;
		this.email = email;
		this.security = security;
		this.sex = sex;
		this.nation = nation;
		this.passport = passport;
	}
	public InsuranceSZZheJiangUserInfo() {
		super();
	}
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
	public String getInsured_state() {
		return insured_state;
	}
	public void setInsured_state(String insured_state) {
		this.insured_state = insured_state;
	}
	public String getDw_name() {
		return dw_name;
	}
	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}
	public String getInsurance_num() {
		return insurance_num;
	}
	public void setInsurance_num(String insurance_num) {
		this.insurance_num = insurance_num;
	}
	public String getLastyear_pay_month() {
		return lastyear_pay_month;
	}
	public void setLastyear_pay_month(String lastyear_pay_month) {
		this.lastyear_pay_month = lastyear_pay_month;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLastyear_tally() {
		return lastyear_tally;
	}
	public void setLastyear_tally(String lastyear_tally) {
		this.lastyear_tally = lastyear_tally;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getLastyear_account() {
		return lastyear_account;
	}
	public void setLastyear_account(String lastyear_account) {
		this.lastyear_account = lastyear_account;
	}
	public String getYear_account() {
		return year_account;
	}
	public void setYear_account(String year_account) {
		this.year_account = year_account;
	}
	public String getYear_tally() {
		return year_tally;
	}
	public void setYear_tally(String year_tally) {
		this.year_tally = year_tally;
	}
	public String getNum_name() {
		return num_name;
	}
	public void setNum_name(String num_name) {
		this.num_name = num_name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSecurity() {
		return security;
	}
	public void setSecurity(String security) {
		this.security = security;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPassport() {
		return passport;
	}
	public void setPassport(String passport) {
		this.passport = passport;
	}
	@Override
	public String toString() {
		return "InsuranceSZZheJiangUserInfo [taskid=" + taskid + ", name=" + name + ", insured_state=" + insured_state
				+ ", dw_name=" + dw_name + ", insurance_num=" + insurance_num + ", lastyear_pay_month="
				+ lastyear_pay_month + ", date=" + date + ", lastyear_tally=" + lastyear_tally + ", interest="
				+ interest + ", lastyear_account=" + lastyear_account + ", year_account=" + year_account
				+ ", year_tally=" + year_tally + ", num_name=" + num_name + ", tel=" + tel + ", email=" + email
				+ ", security=" + security + ", sex=" + sex + ", nation=" + nation + ", passport=" + passport + "]";
	}
	
	
	
}
