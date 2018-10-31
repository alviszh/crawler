package com.microservice.dao.entity.crawler.housing.taizhou2;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_taizhou2_userinfo",indexes = {@Index(name = "index_housing_taizhou2_userinfo_taskid", columnList = "taskid")})
public class HousingFundTaiZhou2UserInfo extends IdEntity{

	private String companyNum;//单位账号
	private String comapny;//单位名称
	private String personalNum;//职工账号
	private String name;//职工姓名
	private String IDNum;//身份证
	private String phone;//手机号码
	private String openDate;//开户日期
	private String status;//汇缴状态
	private String addr;//家庭地址
	private String payDate;//汇缴年月
	private String base;//工资基数
	private String comapnyRadio;//单位比例
	private String personalRadio;//职工比例
	private String fundMonth;//公积金月缴额
	private String fundFee;//公积余额
	private String btRadio;//补贴比例
	private String btMonth;//补贴月缴额
	private String btFee;//补贴余额
	private String fee;//余额合计
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundTaiZhou2UserInfo [companyNum=" + companyNum + ", comapny=" + comapny + ", personalNum="
				+ personalNum + ", name=" + name + ", IDNum=" + IDNum + ", phone=" + phone + ", openDate=" + openDate
				+ ", status=" + status + ", addr=" + addr + ", payDate=" + payDate + ", base=" + base
				+ ", comapnyRadio=" + comapnyRadio + ", personalRadio=" + personalRadio + ", fundMonth=" + fundMonth
				+ ", fundFee=" + fundFee + ", btRadio=" + btRadio + ", btMonth=" + btMonth + ", btFee=" + btFee
				+ ", fee=" + fee + ", taskid=" + taskid + "]";
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getComapny() {
		return comapny;
	}
	public void setComapny(String comapny) {
		this.comapny = comapny;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getComapnyRadio() {
		return comapnyRadio;
	}
	public void setComapnyRadio(String comapnyRadio) {
		this.comapnyRadio = comapnyRadio;
	}
	public String getPersonalRadio() {
		return personalRadio;
	}
	public void setPersonalRadio(String personalRadio) {
		this.personalRadio = personalRadio;
	}
	public String getFundMonth() {
		return fundMonth;
	}
	public void setFundMonth(String fundMonth) {
		this.fundMonth = fundMonth;
	}
	public String getFundFee() {
		return fundFee;
	}
	public void setFundFee(String fundFee) {
		this.fundFee = fundFee;
	}
	public String getBtRadio() {
		return btRadio;
	}
	public void setBtRadio(String btRadio) {
		this.btRadio = btRadio;
	}
	public String getBtMonth() {
		return btMonth;
	}
	public void setBtMonth(String btMonth) {
		this.btMonth = btMonth;
	}
	public String getBtFee() {
		return btFee;
	}
	public void setBtFee(String btFee) {
		this.btFee = btFee;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
