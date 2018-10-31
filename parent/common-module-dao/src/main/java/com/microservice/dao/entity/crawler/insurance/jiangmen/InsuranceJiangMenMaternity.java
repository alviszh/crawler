package com.microservice.dao.entity.crawler.insurance.jiangmen;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_jiangmen_maternity",indexes = {@Index(name = "index_insurance_jiangmen_maternity_taskid", columnList = "taskid")})
public class InsuranceJiangMenMaternity extends IdEntity{
	private String type;              //缴费记录类型
	private String bureauName;        //局名
	private String num;               //单位参保号
	private String company;           //单位名称
	private String startMonth;        //开始年月
	private String endMonth;          //终止年月
	private String monthCount;        //月数
	private String unitPay;           //单位缴纳
	private String personalPay;       //个人缴纳
	private String base;              //缴费工资
	
	private String taskid;
	
	@Override
   	public String toString() {
   		return "InsuranceJiangMenMaternity [type=" + type+ ", bureauName=" + bureauName+ ", num=" + num
   				+ ", company=" + company+ ", startMonth=" + startMonth+ ", endMonth=" + endMonth
   				+ ", monthCount=" + monthCount+ ", unitPay=" + unitPay+ ", personalPay=" + personalPay
   				+ ", base=" + base+ ", taskid=" + taskid + "]";
   	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBureauName() {
		return bureauName;
	}

	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public String getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	public String getMonthCount() {
		return monthCount;
	}

	public void setMonthCount(String monthCount) {
		this.monthCount = monthCount;
	}

	public String getUnitPay() {
		return unitPay;
	}

	public void setUnitPay(String unitPay) {
		this.unitPay = unitPay;
	}

	public String getPersonalPay() {
		return personalPay;
	}

	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

}
