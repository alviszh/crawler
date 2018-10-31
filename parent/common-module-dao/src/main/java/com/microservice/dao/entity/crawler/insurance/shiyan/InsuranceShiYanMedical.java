package com.microservice.dao.entity.crawler.insurance.shiyan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 *
 */
@Entity
@Table(name="insurance_shiyan_medical")
public class InsuranceShiYanMedical extends IdEntity {

	private String name; // 姓名
	private String num; //社会保障号(身份证号)
	private String medicalCardNum; //原医保卡号
	private String yearEndKnotsBalance; //年终结转余额
	private String thisWeekAccountBalance; //本周一账户余额
	private String taskid;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getMedicalCardNum() {
		return medicalCardNum;
	}
	public void setMedicalCardNum(String medicalCardNum) {
		this.medicalCardNum = medicalCardNum;
	}
	public String getYearEndKnotsBalance() {
		return yearEndKnotsBalance;
	}
	public void setYearEndKnotsBalance(String yearEndKnotsBalance) {
		this.yearEndKnotsBalance = yearEndKnotsBalance;
	}
	public String getThisWeekAccountBalance() {
		return thisWeekAccountBalance;
	}
	public void setThisWeekAccountBalance(String thisWeekAccountBalance) {
		this.thisWeekAccountBalance = thisWeekAccountBalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
}
