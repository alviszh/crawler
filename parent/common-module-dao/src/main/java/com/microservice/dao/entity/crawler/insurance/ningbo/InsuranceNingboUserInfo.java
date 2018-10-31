package com.microservice.dao.entity.crawler.insurance.ningbo;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 个人信息
 * @author yl
 *
 */
@Entity
@Table(name="insurance_ningbo_userinfo",indexes = {@Index(name = "index_insurance_ningbo_userinfo_taskid", columnList = "taskid")}) 
public class InsuranceNingboUserInfo extends IdEntity{

	private String taskid;							//uuid 前端通过uuid访问状态结果
	private String unit;                            //单位
	private String name;	                            //姓名
    private String sex;                              //性别
    private String cardNum;                             //证件号
    private String national;                              //国籍
    private String insuranceNum;                            //社保卡号
    private String cardStatus;                             //卡状态
    private String cardMoney;                            //银行卡号
    private String cardDate;                            //发卡日期
    private String phoneNum;                            //手机号码
    private String landlineNum;                            //固定号码
    private String addr;                            //常驻地址
    private String postalcode;                            //邮政编码
	private String canbaoStatus;                         //参保状态
	@Override
	public String toString() {
		return "InsuranceNingboUserInfo [taskid=" + taskid + ", unit=" + unit + ", name=" + name + ", sex=" + sex
				+ ", cardNum=" + cardNum + ", national=" + national + ", insuranceNum=" + insuranceNum + ", cardStatus="
				+ cardStatus + ", cardMoney=" + cardMoney + ", cardDate=" + cardDate + ", phoneNum=" + phoneNum
				+ ", landlineNum=" + landlineNum + ", addr=" + addr + ", postalcode=" + postalcode + ", canbaoStatus="
				+ canbaoStatus + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getNational() {
		return national;
	}
	public void setNational(String national) {
		this.national = national;
	}
	public String getInsuranceNum() {
		return insuranceNum;
	}
	public void setInsuranceNum(String insuranceNum) {
		this.insuranceNum = insuranceNum;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getCardMoney() {
		return cardMoney;
	}
	public void setCardMoney(String cardMoney) {
		this.cardMoney = cardMoney;
	}
	public String getCardDate() {
		return cardDate;
	}
	public void setCardDate(String cardDate) {
		this.cardDate = cardDate;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getLandlineNum() {
		return landlineNum;
	}
	public void setLandlineNum(String landlineNum) {
		this.landlineNum = landlineNum;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getCanbaoStatus() {
		return canbaoStatus;
	}
	public void setCanbaoStatus(String canbaoStatus) {
		this.canbaoStatus = canbaoStatus;
	}
	public InsuranceNingboUserInfo() {
		super();
	}
	public InsuranceNingboUserInfo(String taskid, String unit, String name, String sex, String cardNum, String national,
			String insuranceNum, String cardStatus, String cardMoney, String cardDate, String phoneNum,
			String landlineNum, String addr, String postalcode, String canbaoStatus) {
		super();
		this.taskid = taskid;
		this.unit = unit;
		this.name = name;
		this.sex = sex;
		this.cardNum = cardNum;
		this.national = national;
		this.insuranceNum = insuranceNum;
		this.cardStatus = cardStatus;
		this.cardMoney = cardMoney;
		this.cardDate = cardDate;
		this.phoneNum = phoneNum;
		this.landlineNum = landlineNum;
		this.addr = addr;
		this.postalcode = postalcode;
		this.canbaoStatus = canbaoStatus;
	}
	
	
	
	
}
