package com.microservice.dao.entity.crawler.insurance.jiangmen;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_jiangmen_userinfo",indexes = {@Index(name = "index_insurance_jiangmen_userinfo_taskid", columnList = "taskid")})
public class InsuranceJiangMenUserInfo extends IdEntity{

    private String insuredNumber;        //人员参保号
	private String name;                 //姓名
	private String idCard;               //身份证号
	private String idNum;                //IC卡号
	private String num;                  //单位参保号
	private String company;              //单位名称
	private String sex;                  //性别
	private String birth;                //出生日期
	private String addr;                 //通讯地址
	private String postalCode;           //邮政编码
	private String phone;                //联系电话
	private String personnelType;        //医疗人员类别
	private String state;                //人员状态
	private String type;                 //户口类别
	private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceJiangMenUserInfo [insuredNumber=" + insuredNumber + ", name=" + name + ", idCard=" + idCard
				+ ", idNum=" + idNum + ", num=" + num + ", company=" + company
				+ ", sex=" + sex + ", birth=" + birth + ", addr=" + addr
				+ ", postalCode=" + postalCode + ", phone=" + phone + ", personnelType=" + personnelType
				+ ", state=" + state + ", type=" + type + ", taskid=" + taskid + "]";
	}

	public String getInsuredNumber() {
		return insuredNumber;
	}

	public void setInsuredNumber(String insuredNumber) {
		this.insuredNumber = insuredNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPersonnelType() {
		return personnelType;
	}

	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
