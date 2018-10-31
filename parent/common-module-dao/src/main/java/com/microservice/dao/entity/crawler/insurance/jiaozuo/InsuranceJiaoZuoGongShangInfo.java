package com.microservice.dao.entity.crawler.insurance.jiaozuo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_jiaozuo_gongshanginfo",indexes = {@Index(name = "index_insurance_jiaozuo_gongshanginfo_taskid", columnList = "taskid")})
public class InsuranceJiaoZuoGongShangInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5273473947864101643L;
	private String category;//保险类型
	private String unit;//单位名称
	private String time;//缴费年月
	private String base;//缴费基数
	private String sign;//缴费标志
	private String unitPay;//单位缴费
	private String personalPay;//个人缴费
	private String total;//缴费金额
	private String payType;//缴费类型
	private String taskid;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public InsuranceJiaoZuoGongShangInfo(String category, String unit, String time, String base, String sign,
			String unitPay, String personalPay, String total, String payType, String taskid) {
		super();
		this.category = category;
		this.unit = unit;
		this.time = time;
		this.base = base;
		this.sign = sign;
		this.unitPay = unitPay;
		this.personalPay = personalPay;
		this.total = total;
		this.payType = payType;
		this.taskid = taskid;
	}
	public InsuranceJiaoZuoGongShangInfo() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceJiaoZuoGongShangInfo [category=" + category + ", unit=" + unit + ", time=" + time + ", base="
				+ base + ", sign=" + sign + ", unitPay=" + unitPay + ", personalPay=" + personalPay + ", total=" + total
				+ ", payType=" + payType + ", taskid=" + taskid + "]";
	}
	
	
	
}
