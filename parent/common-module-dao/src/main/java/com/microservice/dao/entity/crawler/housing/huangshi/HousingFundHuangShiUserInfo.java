package com.microservice.dao.entity.crawler.housing.huangshi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_huangshi_userinfo")
public class HousingFundHuangShiUserInfo extends IdEntity{
	private String name;//姓名
	private String birth;//出生日期
	private String sex;//性别
	private String cardType;//证件类型
	private String cardNum;//证件号码
	private String phone;//手机号码
	private String num;//固定电话
	private String code;//邮政编码
	private String familyMoney;//家庭收入
	private String addr;//家庭地址
	private String marry;//婚姻
	private String loan;//贷款
	private String accountNum;//账号
	private String status;//账户状态
	private String fee;//余额
	private String openDate;//开户日期
	private String company;//单位名称
	private String companyNum;//单位账号
	private String ratio;//缴存比例
	private String base;//个人缴存基数
	private String monthPay;//月缴存额
	private String bank;//个人存款开户银行名称
	private String personalNum;//个人存款账户号码
	private String taskid;//
	@Override
	public String toString() {
		return "HousingFundHuangShiUserInfo [name=" + name + ", birth=" + birth + ", sex=" + sex + ", cardType="
				+ cardType + ", cardNum=" + cardNum + ", phone=" + phone + ", num=" + num + ", code=" + code
				+ ", familyMoney=" + familyMoney + ", addr=" + addr + ", marry=" + marry + ", loan=" + loan
				+ ", accountNum=" + accountNum + ", status=" + status + ", fee=" + fee + ", openDate=" + openDate
				+ ", company=" + company + ", companyNum=" + companyNum + ", ratio=" + ratio + ", base=" + base
				+ ", monthPay=" + monthPay + ", bank=" + bank + ", personalNum=" + personalNum + ", taskid=" + taskid
				+ "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFamilyMoney() {
		return familyMoney;
	}
	public void setFamilyMoney(String familyMoney) {
		this.familyMoney = familyMoney;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getMarry() {
		return marry;
	}
	public void setMarry(String marry) {
		this.marry = marry;
	}
	public String getLoan() {
		return loan;
	}
	public void setLoan(String loan) {
		this.loan = loan;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
