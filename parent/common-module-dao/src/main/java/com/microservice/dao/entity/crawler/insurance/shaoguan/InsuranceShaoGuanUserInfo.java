package com.microservice.dao.entity.crawler.insurance.shaoguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_shaoguan_userinfo",indexes = {@Index(name = "index_insurance_shaoguan_userinfo_taskid", columnList = "taskid")})
public class InsuranceShaoGuanUserInfo extends IdEntity{

	private String company;//所属单位
	private String IDNum;//身份证
	private String name;//姓名
	private String status;//人员状态
	private String moneyType;//财政类别
	private String joinDate;//参加工作时间
	private String yongGong;//用工形式
	private String personal;//个人身份
	private String lastThree;//存折号码后三位
	private String sf;//是否领取
	private String netPoint;//农行银网点
	private String phone;//网点联系电话
	private String addr;//网点联系地址
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceShaoGuanUserInfo [company=" + company + ", IDNum=" + IDNum + ", name=" + name + ", status="
				+ status + ", moneyType=" + moneyType + ", joinDate=" + joinDate + ", yongGong=" + yongGong
				+ ", personal=" + personal + ", lastThree=" + lastThree + ", sf=" + sf + ", netPoint=" + netPoint
				+ ", phone=" + phone + ", addr=" + addr + ", taskid=" + taskid + "]";
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getYongGong() {
		return yongGong;
	}
	public void setYongGong(String yongGong) {
		this.yongGong = yongGong;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getLastThree() {
		return lastThree;
	}
	public void setLastThree(String lastThree) {
		this.lastThree = lastThree;
	}
	public String getSf() {
		return sf;
	}
	public void setSf(String sf) {
		this.sf = sf;
	}
	public String getNetPoint() {
		return netPoint;
	}
	public void setNetPoint(String netPoint) {
		this.netPoint = netPoint;
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
