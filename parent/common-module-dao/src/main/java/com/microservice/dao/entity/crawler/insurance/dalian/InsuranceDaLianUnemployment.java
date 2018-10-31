package com.microservice.dao.entity.crawler.insurance.dalian;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_dalian_unemployment",indexes = {@Index(name = "index_insurance_dalian_unemployment_taskid", columnList = "taskid")})
public class InsuranceDaLianUnemployment extends IdEntity{
	private String unitName;                    //单位名称
	private String year;                        //年度
	private String beginMonth;                  //缴费开始月份
	private String terMonth;                    //缴费结止月份
	private String personalbase;                //个人缴费基数
	private String unitbase;                    //单位缴费基数
	private String personalRatio;               //个人缴费比例
	private String unitRatio;                   //单位缴费比例
	private String personalAmount;              //个人缴费金额
	private String unitAmount;                  //单位缴费金额
	private String totalAmount;                 //缴费总金额
	private String numOfMonths;                 //缴费月数累计
	private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceDaLianUnemployment [unitName=" + unitName + ", year=" + year
				+ ", beginMonth=" + beginMonth+ ", terMonth=" + terMonth+ ", personalbase=" + personalbase
				+ ", unitbase=" + unitbase+ ", personalRatio=" + personalRatio+ ", unitRatio=" + unitRatio
				+ ", personalAmount=" + personalAmount+ ", unitAmount=" + unitAmount+ ", totalAmount=" + totalAmount
				+ ", numOfMonths=" + numOfMonths+ ", taskid=" + taskid + "]";
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getBeginMonth() {
		return beginMonth;
	}

	public void setBeginMonth(String beginMonth) {
		this.beginMonth = beginMonth;
	}

	public String getTerMonth() {
		return terMonth;
	}

	public void setTerMonth(String terMonth) {
		this.terMonth = terMonth;
	}

	public String getPersonalbase() {
		return personalbase;
	}

	public void setPersonalbase(String personalbase) {
		this.personalbase = personalbase;
	}

	public String getUnitbase() {
		return unitbase;
	}

	public void setUnitbase(String unitbase) {
		this.unitbase = unitbase;
	}

	public String getPersonalRatio() {
		return personalRatio;
	}

	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}

	public String getUnitRatio() {
		return unitRatio;
	}

	public void setUnitRatio(String unitRatio) {
		this.unitRatio = unitRatio;
	}

	public String getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}

	public String getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(String unitAmount) {
		this.unitAmount = unitAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getNumOfMonths() {
		return numOfMonths;
	}

	public void setNumOfMonths(String numOfMonths) {
		this.numOfMonths = numOfMonths;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	

}
