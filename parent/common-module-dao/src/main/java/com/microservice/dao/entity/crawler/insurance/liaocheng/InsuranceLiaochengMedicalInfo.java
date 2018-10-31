package com.microservice.dao.entity.crawler.insurance.liaocheng;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;


/**
 * 聊城个人医疗保险缴费记录查询结果
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_liaocheng_medicalinfo" ,indexes = {@Index(name = "index_insurance_liaocheng_medicalinfo_taskid", columnList = "taskid")})
public class InsuranceLiaochengMedicalInfo extends IdEntity{
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	/**
	 * 个人编号
	 */
	private String personal_number;	
	/**
	 * 月缴费基数
	 */
	private String pay_base;
	/**
	 * 月划入账户金额
	 */
	private String month_money;	
	/**
	 * 累积划入账户金额
	 */
	private String sum_money;
	/**
	 * 缴费年月
	 */
	private String yearmoney_pay;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPersonal_number() {
		return personal_number;
	}
	public void setPersonal_number(String personal_number) {
		this.personal_number = personal_number;
	}
	public String getPay_base() {
		return pay_base;
	}
	public void setPay_base(String pay_base) {
		this.pay_base = pay_base;
	}
	public String getMonth_money() {
		return month_money;
	}
	public void setMonth_money(String month_money) {
		this.month_money = month_money;
	}
	public String getSum_money() {
		return sum_money;
	}
	public void setSum_money(String sum_money) {
		this.sum_money = sum_money;
	}
	public String getYearmoney_pay() {
		return yearmoney_pay;
	}
	public void setYearmoney_pay(String yearmoney_pay) {
		this.yearmoney_pay = yearmoney_pay;
	}
	public InsuranceLiaochengMedicalInfo(String taskid, String personal_number, String pay_base, String month_money,
			String sum_money, String yearmoney_pay) {
		super();
		this.taskid = taskid;
		this.personal_number = personal_number;
		this.pay_base = pay_base;
		this.month_money = month_money;
		this.sum_money = sum_money;
		this.yearmoney_pay = yearmoney_pay;
	}
	public InsuranceLiaochengMedicalInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
}
