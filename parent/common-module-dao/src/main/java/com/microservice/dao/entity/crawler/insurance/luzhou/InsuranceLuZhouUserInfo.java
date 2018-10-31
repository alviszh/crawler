package com.microservice.dao.entity.crawler.insurance.luzhou;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_luzhou_userinfo")
public class InsuranceLuZhouUserInfo extends IdEntity{

	private String name;
	private String phone;
	private String IDNum;
	private String company;
	private String canbaoCompany;
	private String endowment;
	private String unemployment;
	private String medical;
	private String addMedical;
	private String injury;
	private String maternity;
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceLuZhouUserInfo [name=" + name + ", phone=" + phone + ", IDNum=" + IDNum + ", company="
				+ company + ", canbaoCompany=" + canbaoCompany + ", endowment=" + endowment + ", unemployment="
				+ unemployment + ", medical=" + medical + ", addMedical=" + addMedical + ", injury=" + injury
				+ ", maternity=" + maternity + ", taskid=" + taskid + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCanbaoCompany() {
		return canbaoCompany;
	}
	public void setCanbaoCompany(String canbaoCompany) {
		this.canbaoCompany = canbaoCompany;
	}
	public String getEndowment() {
		return endowment;
	}
	public void setEndowment(String endowment) {
		this.endowment = endowment;
	}
	public String getUnemployment() {
		return unemployment;
	}
	public void setUnemployment(String unemployment) {
		this.unemployment = unemployment;
	}
	public String getMedical() {
		return medical;
	}
	public void setMedical(String medical) {
		this.medical = medical;
	}
	public String getAddMedical() {
		return addMedical;
	}
	public void setAddMedical(String addMedical) {
		this.addMedical = addMedical;
	}
	public String getInjury() {
		return injury;
	}
	public void setInjury(String injury) {
		this.injury = injury;
	}
	public String getMaternity() {
		return maternity;
	}
	public void setMaternity(String maternity) {
		this.maternity = maternity;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
