package com.microservice.dao.entity.crawler.housing.weifang;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_weifang_userinfo")
public class HousingWeiFangUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String staffName;						//职工姓名
	private String idNum;							//身份证号码
	private String staffNum;						//职工账号
	private String companyName;						//单位名称
	private String payBase;							//缴存基数
	private String personalPercent;					//个人存缴比例
	private String companyPercent;					//单位存缴比例
	private String sumPercent;						//合计存缴比例
	private String monthPay;						//月汇缴额
	private String balance;							//余额
	private String lastPayDate;						//最新汇缴年月
	private String state;							//账号状态
	private String openDate;						//开户日期
	private String department;						//所在管理部
	private String phoneNum;						//手机号码
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getStaffNum() {
		return staffNum;
	}
	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getPersonalPercent() {
		return personalPercent;
	}
	public void setPersonalPercent(String personalPercent) {
		this.personalPercent = personalPercent;
	}
	public String getCompanyPercent() {
		return companyPercent;
	}
	public void setCompanyPercent(String companyPercent) {
		this.companyPercent = companyPercent;
	}
	public String getSumPercent() {
		return sumPercent;
	}
	public void setSumPercent(String sumPercent) {
		this.sumPercent = sumPercent;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getLastPayDate() {
		return lastPayDate;
	}
	public void setLastPayDate(String lastPayDate) {
		this.lastPayDate = lastPayDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	@Override
	public String toString() {
		return "HousingWeiFangUserinfo [taskid=" + taskid + ", staffName=" + staffName + ", idNum=" + idNum
				+ ", staffNum=" + staffNum + ", companyName=" + companyName + ", payBase=" + payBase
				+ ", personalPercent=" + personalPercent + ", companyPercent=" + companyPercent + ", sumPercent="
				+ sumPercent + ", monthPay=" + monthPay + ", balance=" + balance + ", lastPayDate=" + lastPayDate
				+ ", state=" + state + ", openDate=" + openDate + ", department=" + department + ", phoneNum="
				+ phoneNum + "]";
	}
	

}
