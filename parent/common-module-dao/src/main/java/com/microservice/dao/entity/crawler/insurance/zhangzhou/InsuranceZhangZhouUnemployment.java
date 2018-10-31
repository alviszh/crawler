package com.microservice.dao.entity.crawler.insurance.zhangzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhangzhou_unemployment",indexes = {@Index(name = "index_insurance_zhangzhou_unemployment_taskid", columnList = "taskid")})
public class InsuranceZhangZhouUnemployment extends IdEntity{
	
	private String startDate;//起始年月
	private String endDate;//截止年月
	private String monthSum;//可发放总月数
	private String month;//已发放月数
	private String number;//就失业登记证号
	private String flag;//有效标记
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceZhangZhouUnemployment [startDate=" + startDate + ", endDate=" + endDate + ", monthSum="
				+ monthSum + ", month=" + month + ", number=" + number + ", flag=" + flag + ", taskid=" + taskid + "]";
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMonthSum() {
		return monthSum;
	}
	public void setMonthSum(String monthSum) {
		this.monthSum = monthSum;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
