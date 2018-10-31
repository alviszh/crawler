package com.microservice.dao.entity.crawler.insurance.liuzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_liuzhou_pension",indexes = {@Index(name = "index_insurance_liuzhou_pension_taskid", columnList = "taskid")})
public class InsuranceLiuZhouPension extends IdEntity{
	private String number;						    //个人编号
	private String name;				            //姓名
	private String idNum;							//身份证号
	private String type;                            //险种类型
	private String year;                            //台账年月
	private String payMonth;                        //费款所属期
	private String base;                            //缴费基数
	private String unitAmount;                      //单位应缴
	private String personalAmount;                  //个人应缴
	private String subtotal;                        //小计
	private String sign;                            //缴费标志
    private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceLiuZhouPension [number=" + number + ", name=" + name
				+ ", idNum=" + idNum+ ", type=" + type+ ", year=" + year
				+ ", payMonth=" + payMonth+ ", base=" + base+ ", unitAmount=" + unitAmount
				+ ", personalAmount=" + personalAmount+ ", subtotal=" + subtotal+ ", sign=" + sign
				+ ", taskid=" + taskid + "]";
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(String unitAmount) {
		this.unitAmount = unitAmount;
	}

	public String getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
