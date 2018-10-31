package com.microservice.dao.entity.crawler.insurance.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description: 青岛各类保险缴纳的公共信息  年度缴费基数
 * @author sln
 * @date 2017年8月9日
 */
@Entity
@Table(name = "insurance_qingdao_paygeneral",indexes = {@Index(name = "index_insurance_qingdao_paygeneral_taskid", columnList = "taskid")})
public class InsuranceQingdaoPayGeneral extends IdEntity implements Serializable{
	private static final long serialVersionUID = 8657192249815052286L;
	private String taskid;
//	开始时间
	private String startdate;
//	结束时间
	private String enddate;
//	单位编号
	private String compnum;
//	单位名称
	private String compname;
//	工资 
	private String salary;   
//	养老缴费基数
	private String pensionbasenum;
//	医疗缴费基数
	private String medicalbasenum;
//	失业缴费基数
	private String unemploymentbasenum;
//	工伤缴费基数
	private String injurybasenum;
//	生育缴费基数
	private String bearbasenum;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getCompnum() {
		return compnum;
	}
	public void setCompnum(String compnum) {
		this.compnum = compnum;
	}
	public String getCompname() {
		return compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getPensionbasenum() {
		return pensionbasenum;
	}
	public void setPensionbasenum(String pensionbasenum) {
		this.pensionbasenum = pensionbasenum;
	}
	public String getMedicalbasenum() {
		return medicalbasenum;
	}
	public void setMedicalbasenum(String medicalbasenum) {
		this.medicalbasenum = medicalbasenum;
	}
	public String getUnemploymentbasenum() {
		return unemploymentbasenum;
	}
	public void setUnemploymentbasenum(String unemploymentbasenum) {
		this.unemploymentbasenum = unemploymentbasenum;
	}
	public String getInjurybasenum() {
		return injurybasenum;
	}
	public void setInjurybasenum(String injurybasenum) {
		this.injurybasenum = injurybasenum;
	}
	public String getBearbasenum() {
		return bearbasenum;
	}
	public void setBearbasenum(String bearbasenum) {
		this.bearbasenum = bearbasenum;
	}
	public InsuranceQingdaoPayGeneral(String taskid, String startdate, String enddate, String compnum, String compname,
			String salary, String pensionbasenum, String medicalbasenum, String unemploymentbasenum,
			String injurybasenum, String bearbasenum) {
		super();
		this.taskid = taskid;
		this.startdate = startdate;
		this.enddate = enddate;
		this.compnum = compnum;
		this.compname = compname;
		this.salary = salary;
		this.pensionbasenum = pensionbasenum;
		this.medicalbasenum = medicalbasenum;
		this.unemploymentbasenum = unemploymentbasenum;
		this.injurybasenum = injurybasenum;
		this.bearbasenum = bearbasenum;
	}
	public InsuranceQingdaoPayGeneral() {
		super();
	}
	
}

