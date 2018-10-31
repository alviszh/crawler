package com.microservice.dao.entity.crawler.insurance.ganzhou;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
/**
 * 赣州社保养老保险
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_ganzhou_persion")
public class InsuranceGanZhouPersion extends IdEntity {
	
	private String username;	//姓名
	private String state;	//缴费状态
	private String idnum;	//身份证号
	private String paymonth;	//缴费总月数
	private String companyname;	//企业名称
	private String area;	//缴费地区
	private String companyamount;	//缴费总额
	private String taskid;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCompanyamount() {
		return companyamount;
	}
	public void setCompanyamount(String companyamount) {
		this.companyamount = companyamount;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceGanZhouPersion [username=" + username + ", state=" + state + ", idnum=" + idnum + ", paymonth="
				+ paymonth + ", companyname=" + companyname + ", area=" + area + ", companyamount=" + companyamount
				+ ", taskid=" + taskid + "]";
	}
    
}
