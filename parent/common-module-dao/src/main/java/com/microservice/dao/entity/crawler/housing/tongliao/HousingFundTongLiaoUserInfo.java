package com.microservice.dao.entity.crawler.housing.tongliao;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_tongliao_userinfo",indexes = {@Index(name = "index_housing_tongliao_userinfo_taskid", columnList = "taskid")})
public class HousingFundTongLiaoUserInfo extends IdEntity{

	private String taskid;
	
	private String name;//姓名
	
	private String birthday;//出生年月
	
	private String sex;//性别
	
	private String cardType;//证件类型
	
	private String IdcardNum;//证件号码
	
	private String phone;//手机
	
	private String num;//固定电话
	
	private String code;//邮政编码
	
	private String getMoney;//家庭月收入
	
	private String addr;//家庭地址
	
	private String marry;//婚姻状况
	
	private String loan;//贷款情况
	
	private String cardNum;//账户账号
	
	private String cardStatus;//账户状态
	
	private String fee;//账户余额
	
	private String openDate;//开户日期
	
	private String company;//单位名称
	
	private String payRatio;//缴存比例
	private String payBase;//个人缴存基数
	
	private String monthPay;//月缴存额
	
	private String bank;//开户银行
	private String setNum;//个人存款账户号码
	@Override
	public String toString() {
		return "HousingFundTongLiaoUserInfo [taskid=" + taskid + ", name=" + name + ", birthday=" + birthday + ", sex="
				+ sex + ", cardType=" + cardType + ", IdcardNum=" + IdcardNum + ", phone=" + phone + ", num=" + num
				+ ", code=" + code + ", getMoney=" + getMoney + ", addr=" + addr + ", marry=" + marry + ", loan=" + loan
				+ ", cardNum=" + cardNum + ", cardStatus=" + cardStatus + ", fee=" + fee + ", openDate=" + openDate
				+ ", company=" + company + ", payRatio=" + payRatio + ", payBase=" + payBase + ", monthPay=" + monthPay
				+ ", bank=" + bank + ", setNum=" + setNum + "]";
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
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
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
	public String getIdcardNum() {
		return IdcardNum;
	}
	public void setIdcardNum(String idcardNum) {
		IdcardNum = idcardNum;
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
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
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
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
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
	public String getPayRatio() {
		return payRatio;
	}
	public void setPayRatio(String payRatio) {
		this.payRatio = payRatio;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
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
	public String getSetNum() {
		return setNum;
	}
	public void setSetNum(String setNum) {
		this.setNum = setNum;
	}
	
	
}
