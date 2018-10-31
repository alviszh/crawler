package com.microservice.dao.entity.crawler.insurance.sz.shanxi3;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi3_pension")
public class InsuranceShanXi3Pension extends IdEntity{
	
	private String taskid;		
	private String year;								//年度
	private String yearPayMonthSum;						//当年缴费月数
	private String payPotency;							//当年实缴缴费基数
	private String payPersonCount;						//当年个人缴划账户
	private String payUnitCount;						//当年单位缴划个人账户
	private String allPayMonthSum;						//截止上年末累计缴费月数
		
	@Override
	public String toString() {
		return "InsuranceShanXi3Pension [taskid=" + taskid + ", year=" + year + ", yearPayMonthSum=" + yearPayMonthSum
				+ ", payPotency=" + payPotency + ", payPersonCount=" + payPersonCount + ", payUnitCount=" + payUnitCount
				+ ", allPayMonthSum=" + allPayMonthSum + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getYearPayMonthSum() {
		return yearPayMonthSum;
	}
	public void setYearPayMonthSum(String yearPayMonthSum) {
		this.yearPayMonthSum = yearPayMonthSum;
	}
	public String getPayPotency() {
		return payPotency;
	}
	public void setPayPotency(String payPotency) {
		this.payPotency = payPotency;
	}
	public String getPayPersonCount() {
		return payPersonCount;
	}
	public void setPayPersonCount(String payPersonCount) {
		this.payPersonCount = payPersonCount;
	}
	public String getPayUnitCount() {
		return payUnitCount;
	}
	public void setPayUnitCount(String payUnitCount) {
		this.payUnitCount = payUnitCount;
	}
	public String getAllPayMonthSum() {
		return allPayMonthSum;
	}
	public void setAllPayMonthSum(String allPayMonthSum) {
		this.allPayMonthSum = allPayMonthSum;
	}

}
