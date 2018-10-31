package com.microservice.dao.entity.crawler.insurance.xiangyang;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xiangyang_yiliao")
public class InsuranceXiangYangYiLiao extends IdEntity{

	private String taskid;
	private String time;//时间
	private String type;//险种
	private String category;//缴费类别
	private String paymentbase;//缴费基数
	private String paymoney;//应缴金额
	private String dw_name;//单位名称
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPaymentbase() {
		return paymentbase;
	}
	public void setPaymentbase(String paymentbase) {
		this.paymentbase = paymentbase;
	}
	public String getPaymoney() {
		return paymoney;
	}
	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}
	public String getDw_name() {
		return dw_name;
	}
	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}
	public InsuranceXiangYangYiLiao(String taskid, String time, String type, String category, String paymentbase,
			String paymoney, String dw_name) {
		super();
		this.taskid = taskid;
		this.time = time;
		this.type = type;
		this.category = category;
		this.paymentbase = paymentbase;
		this.paymoney = paymoney;
		this.dw_name = dw_name;
	}
	public InsuranceXiangYangYiLiao() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceXiangYangYangLao [taskid=" + taskid + ", time=" + time + ", type=" + type + ", category="
				+ category + ", paymentbase=" + paymentbase + ", paymoney=" + paymoney + ", dw_name=" + dw_name + "]";
	}
	
	
}
