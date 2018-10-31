package com.microservice.dao.entity.crawler.insurance.xingtai;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xingtai_injury",indexes = {@Index(name = "index_insurance_xingtai_injury_taskid", columnList = "taskid")})
public class InsuranceXingTaiInjury extends IdEntity{
	private String year;                            //缴费年月
	private String paymentType;				        //缴费类别
	private String base;                            //缴费月基数
	private String sign;                            //缴费标记
	private String payMonth;                        //到账日期
	
    private String taskid;
    
    @Override
	public String toString() {
		return "InsuranceXingTaiInjury [year=" + year + ", paymentType=" + paymentType
				+ ", base=" + base+ ", sign=" + sign+ ", payMonth=" + payMonth
				+ ", taskid=" + taskid + "]";
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

}
