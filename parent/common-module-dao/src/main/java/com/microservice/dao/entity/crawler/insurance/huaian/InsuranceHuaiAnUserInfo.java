package com.microservice.dao.entity.crawler.insurance.huaian;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_huaian_userinfo",indexes = {@Index(name = "index_insurance_huaian_userinfo_taskid", columnList = "taskid")}) 
public class InsuranceHuaiAnUserInfo extends IdEntity{

	private String num;//个人编号
	
	private String name;//姓名
	private String idNum;//卡号
	
	private String sex;//性别
	
	private String birth;//出生日期
	
	private String idCard;//身份证号
	private String company;//单位名称
	private String type;//单位类型
	private String tc;//统筹区
	private String floor;//医疗人员待遇类别
	
	private String datea;//参加工作日期
	
	private String endDatea;//离退休日期
	private String phone;//联系电话
	private String addr;//通讯地址
	private String taskid;//
	@Override
	public String toString() {
		return "InsuranceHuaiAnUserInfo [num=" + num + ", name=" + name + ", idNum=" + idNum + ", sex=" + sex
				+ ", birth=" + birth + ", idCard=" + idCard + ", company=" + company + ", type=" + type + ", tc=" + tc
				+ ", floor=" + floor + ", datea=" + datea + ", endDatea=" + endDatea + ", phone=" + phone + ", addr="
				+ addr + ", taskid=" + taskid + "]";
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getEndDatea() {
		return endDatea;
	}
	public void setEndDatea(String endDatea) {
		this.endDatea = endDatea;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
