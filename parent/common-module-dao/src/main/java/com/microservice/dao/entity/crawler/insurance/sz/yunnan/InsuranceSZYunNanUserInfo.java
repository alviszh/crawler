package com.microservice.dao.entity.crawler.insurance.sz.yunnan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_yunnan_userinfo",indexes = {@Index(name = "index_insurance_sz_yunnan_userinfo_taskid", columnList = "taskid")}) 
public class InsuranceSZYunNanUserInfo extends IdEntity{
	private String personalNum;//个人编号
	
	
	private String taskid;//
	
	private String type;//参保类型
	
	private String name;//姓名
	
	private String sex;//性别
	
	private String birthday;//出生年月
	
	private String yn;//是否公务员
	
	private String medical;//是否医疗照顾人员
	
	private String years;//实足年龄
	
	private String company;//单位名称
	
	private String status;//参保状态
	
	private String fee;//账户余额
	
	private String sum;//财政总收入
	
	private String sumPay;//财政总支出

	@Override
	public String toString() {
		return "InsuranceSZYunNanUserInfo [personalNum=" + personalNum + ", taskid=" + taskid + ", type=" + type
				+ ", name=" + name + ", sex=" + sex + ", birthday=" + birthday + ", yn=" + yn + ", medical=" + medical
				+ ", years=" + years + ", company=" + company + ", status=" + status + ", fee=" + fee + ", sum=" + sum
				+ ", sumPay=" + sumPay + "]";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getYn() {
		return yn;
	}

	public void setYn(String yn) {
		this.yn = yn;
	}

	public String getMedical() {
		return medical;
	}

	public void setMedical(String medical) {
		this.medical = medical;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getSumPay() {
		return sumPay;
	}

	public void setSumPay(String sumPay) {
		this.sumPay = sumPay;
	}
	
	
}
