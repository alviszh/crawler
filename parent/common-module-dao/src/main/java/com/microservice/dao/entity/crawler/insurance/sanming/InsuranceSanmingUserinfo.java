package com.microservice.dao.entity.crawler.insurance.sanming;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @author zz
 * @create 2018-3-14
 * @Desc 社保-三明市 用户信息表
 */
@Entity
@Table(name = "insurance_sanming_userinfo")
public class InsuranceSanmingUserinfo extends IdEntity {
	
    private String taskid;
    private String idnum;					//身份证号
    private String name;					//姓名
    private String companyCode;				//单位编码
    private String companyName;				//单位名称
    private String status;					//工作状态
    private String phone;					//联系电话
    private String address;					//通讯地址
    private String postcode;				//邮政编码
    private String birthday;				//出生年月
    private String wordType;				//用工形式
    private String workTime;				//参加工作时间
    private String pensionMonth;			//参加养老保险年月
    private String payMonth;				//建账前累计缴费月数
    private String beginMonth;				//建立个人账户年月
    private String payMonthEnd;				//建帐后累计缴费月数
    private String payBaseTotal;			//缴费总基数
    private String accountTotalMoney;		//个人账户总金额
    private String myNum;					//个人编号
    
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getWordType() {
		return wordType;
	}
	public void setWordType(String wordType) {
		this.wordType = wordType;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public String getPensionMonth() {
		return pensionMonth;
	}
	public void setPensionMonth(String pensionMonth) {
		this.pensionMonth = pensionMonth;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getBeginMonth() {
		return beginMonth;
	}
	public void setBeginMonth(String beginMonth) {
		this.beginMonth = beginMonth;
	}
	public String getPayMonthEnd() {
		return payMonthEnd;
	}
	public void setPayMonthEnd(String payMonthEnd) {
		this.payMonthEnd = payMonthEnd;
	}
	public String getPayBaseTotal() {
		return payBaseTotal;
	}
	public void setPayBaseTotal(String payBaseTotal) {
		this.payBaseTotal = payBaseTotal;
	}
	public String getAccountTotalMoney() {
		return accountTotalMoney;
	}
	public void setAccountTotalMoney(String accountTotalMoney) {
		this.accountTotalMoney = accountTotalMoney;
	}
	public String getMyNum() {
		return myNum;
	}
	public void setMyNum(String myNum) {
		this.myNum = myNum;
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
