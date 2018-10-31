package com.microservice.dao.entity.crawler.insurance.shangqiu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 历年缴费基数信息(一年可能出现多个缴费基数)
 * @author: sln 
 * @date: 2018年1月9日 下午3:41:00 
 */
@Entity
@Table(name = "insurance_shangqiu_yearpayinfo",indexes = {@Index(name = "index_insurance_shangqiu_yearpayinfo_taskid", columnList = "taskid")})
public class InsuranceShangQiuYearPayInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -2470134020952987471L;
	private String taskid;
	//年份
	private String year;
	//年缴费基数
	private String yearbasenum;
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
	public String getYearbasenum() {
		return yearbasenum;
	}
	public void setYearbasenum(String yearbasenum) {
		this.yearbasenum = yearbasenum;
	}
}
