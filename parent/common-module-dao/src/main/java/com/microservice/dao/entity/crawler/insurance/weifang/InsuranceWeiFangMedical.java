package com.microservice.dao.entity.crawler.insurance.weifang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 潍坊医保
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_weifang_medical",indexes = {@Index(name = "index_insurance_weifang_medical_taskid", columnList = "taskid")})
public class InsuranceWeiFangMedical extends IdEntity {
	private String rnum;	//序号
	private String beginMonth;	//起始年月
	private String endMonth;	//终止年月
	private String payPersonBase;	//个人缴费基数
	private String payCompanyBase;	//单位缴费基数
	private String payPersonAmount;	//单位缴费金额
	private String payCompanyAmount;	//个人缴费金额
	private String taskid;
	public String getRnum() {
		return rnum;
	}
	public void setRnum(String rnum) {
		this.rnum = rnum;
	}
	public String getBeginMonth() {
		return beginMonth;
	}
	public void setBeginMonth(String beginMonth) {
		this.beginMonth = beginMonth;
	}
	public String getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}
	public String getPayPersonBase() {
		return payPersonBase;
	}
	public void setPayPersonBase(String payPersonBase) {
		this.payPersonBase = payPersonBase;
	}
	public String getPayCompanyBase() {
		return payCompanyBase;
	}
	public void setPayCompanyBase(String payCompanyBase) {
		this.payCompanyBase = payCompanyBase;
	}
	public String getPayPersonAmount() {
		return payPersonAmount;
	}
	public void setPayPersonAmount(String payPersonAmount) {
		this.payPersonAmount = payPersonAmount;
	}
	public String getPayCompanyAmount() {
		return payCompanyAmount;
	}
	public void setPayCompanyAmount(String payCompanyAmount) {
		this.payCompanyAmount = payCompanyAmount;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceWeiFangMedical [rnum=" + rnum + ", beginMonth=" + beginMonth + ", endMonth=" + endMonth
				+ ", payPersonBase=" + payPersonBase + ", payCompanyBase=" + payCompanyBase + ", payPersonAmount="
				+ payPersonAmount + ", payCompanyAmount=" + payCompanyAmount + ", taskid=" + taskid + "]";
	}
}
