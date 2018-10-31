package com.microservice.dao.entity.crawler.insurance.sz.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_jilin_shiyeinfo",indexes = {@Index(name = "index_insurance_sz_jilin_shiyeinfo_taskid", columnList = "taskid")})
public class InsuranceSZJiLinShiYeInfo extends IdEntity{

	private String taskid;
	
	private String name;
	
	private String idcard;
	
	private String fee_period;//费款所属期
	
	private String insurance_type;//险种类型
	
	private String pay_type;//缴费类型
	
	private String pay_base;//缴费基数
	
	private String per_paymoney;//个人缴费金额
	
	private String per_paysign;//个人缴费标志
	
	private String per_paydatetime;//个人缴费到账日期
	
	private String dw_nummoney;//单位缴费划账户金额
	
	private String dw_crossmoney;//单位缴费划统筹金额
	
	private String dw_paysnig;//单位缴费标志
	
	private String dw_datetime;//单位缴费到账日期
	
	private String month;//缴费月数
	
	private String dw_num;//缴费单位编号
	
	private String dw_name;//缴费单位名称

	public InsuranceSZJiLinShiYeInfo(String taskid, String name, String idcard, String fee_period,
			String insurance_type, String pay_type, String pay_base, String per_paymoney, String per_paysign,
			String per_paydatetime, String dw_nummoney, String dw_crossmoney, String dw_paysnig, String dw_datetime,
			String month, String dw_num, String dw_name) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idcard = idcard;
		this.fee_period = fee_period;
		this.insurance_type = insurance_type;
		this.pay_type = pay_type;
		this.pay_base = pay_base;
		this.per_paymoney = per_paymoney;
		this.per_paysign = per_paysign;
		this.per_paydatetime = per_paydatetime;
		this.dw_nummoney = dw_nummoney;
		this.dw_crossmoney = dw_crossmoney;
		this.dw_paysnig = dw_paysnig;
		this.dw_datetime = dw_datetime;
		this.month = month;
		this.dw_num = dw_num;
		this.dw_name = dw_name;
	}

	public InsuranceSZJiLinShiYeInfo() {
		super();
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getFee_period() {
		return fee_period;
	}

	public void setFee_period(String fee_period) {
		this.fee_period = fee_period;
	}

	public String getInsurance_type() {
		return insurance_type;
	}

	public void setInsurance_type(String insurance_type) {
		this.insurance_type = insurance_type;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getPay_base() {
		return pay_base;
	}

	public void setPay_base(String pay_base) {
		this.pay_base = pay_base;
	}

	public String getPer_paymoney() {
		return per_paymoney;
	}

	public void setPer_paymoney(String per_paymoney) {
		this.per_paymoney = per_paymoney;
	}

	public String getPer_paysign() {
		return per_paysign;
	}

	public void setPer_paysign(String per_paysign) {
		this.per_paysign = per_paysign;
	}

	public String getPer_paydatetime() {
		return per_paydatetime;
	}

	public void setPer_paydatetime(String per_paydatetime) {
		this.per_paydatetime = per_paydatetime;
	}

	public String getDw_nummoney() {
		return dw_nummoney;
	}

	public void setDw_nummoney(String dw_nummoney) {
		this.dw_nummoney = dw_nummoney;
	}

	public String getDw_crossmoney() {
		return dw_crossmoney;
	}

	public void setDw_crossmoney(String dw_crossmoney) {
		this.dw_crossmoney = dw_crossmoney;
	}

	public String getDw_paysnig() {
		return dw_paysnig;
	}

	public void setDw_paysnig(String dw_paysnig) {
		this.dw_paysnig = dw_paysnig;
	}

	public String getDw_datetime() {
		return dw_datetime;
	}

	public void setDw_datetime(String dw_datetime) {
		this.dw_datetime = dw_datetime;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDw_num() {
		return dw_num;
	}

	public void setDw_num(String dw_num) {
		this.dw_num = dw_num;
	}

	public String getDw_name() {
		return dw_name;
	}

	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}

	@Override
	public String toString() {
		return "InsuranceSZJiLinYaoLaoInfo [taskid=" + taskid + ", name=" + name + ", idcard=" + idcard
				+ ", fee_period=" + fee_period + ", insurance_type=" + insurance_type + ", pay_type=" + pay_type
				+ ", pay_base=" + pay_base + ", per_paymoney=" + per_paymoney + ", per_paysign=" + per_paysign
				+ ", per_paydatetime=" + per_paydatetime + ", dw_nummoney=" + dw_nummoney + ", dw_crossmoney="
				+ dw_crossmoney + ", dw_paysnig=" + dw_paysnig + ", dw_datetime=" + dw_datetime + ", month=" + month
				+ ", dw_num=" + dw_num + ", dw_name=" + dw_name + "]";
	}
	
	
}
