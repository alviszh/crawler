package com.microservice.dao.entity.crawler.insurance.dalian;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_dalian_pension",indexes = {@Index(name = "index_insurance_dalian_pension_taskid", columnList = "taskid")})
public class InsuranceDaLianPension extends IdEntity{
	private String unitName;                    //单位名称
	private String year;                        //年度
	private String beginMonth;                  //起始月份
	private String terMonth;                    //终止月份
	private String base;                        //缴费基数
	private String unitRatio;                   //缴费比例单位
	private String personalRatio;               //缴费比例个人
	private String personalAmount;              //缴费金额个人
	private String type;                        //缴费类型
	private String sign;                        //缴费标记
	private String mode;                        //缴费方式
	private String payMonth;					//缴费时间
	private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceDaLianPension [unitName=" + unitName + ", year=" + year
				+ ", beginMonth=" + beginMonth+ ", terMonth=" + terMonth+ ", base=" + base
				+ ", unitRatio=" + unitRatio+ ", personalRatio=" + personalRatio+ ", personalAmount=" + personalAmount
				+ ", type=" + type+ ", sign=" + sign
				+ ", mode=" + mode+ ", payMonth=" + payMonth+ ", taskid=" + taskid + "]";
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

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getUnitRatio() {
		return unitRatio;
	}

	public void setUnitRatio(String unitRatio) {
		this.unitRatio = unitRatio;
	}

	public String getPersonalRatio() {
		return personalRatio;
	}

	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}

	public String getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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
