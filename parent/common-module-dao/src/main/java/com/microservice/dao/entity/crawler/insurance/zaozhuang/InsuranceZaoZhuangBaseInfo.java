package com.microservice.dao.entity.crawler.insurance.zaozhuang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zaozhuang_baseinfo")
public class InsuranceZaoZhuangBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 身份证号 */
	@Column(name="card_id")
	private String cardId;
	/** 姓名 */
	@Column(name="name")
	private String name;
	/** 性别 */
	@Column(name="sex")
	private String sex;
	/** 出生日期 */
	@Column(name="birth_date")
	private String birthDate;
	/** 户口性质*/
	@Column(name="hkxz")
	private String hkxz;
	/**文化程度*/
	@Column(name="culture")
	private String culture;
	/**通讯住址*/
	@Column(name="communicationaddr")
	private String communicationaddr;
	/**家庭住址*/
	@Column(name="homeaddr")
	private String homeaddr;
	/**民族*/
	@Column(name="nation")
	private String nation;
	/**固定电话*/
	@Column(name="registerphone")
	private String registerphone;
	/**邮政编码*/
	@Column(name="postalservice")
	private String postalservice;
	/**手机号码*/
	@Column(name="phone")
	private String phone;
	
	
	
	
	/**姓名*/
	@Column(name="xingming")
	private String xingming;
	/**身份证号*/
	@Column(name="sfzh")
	private String sfzh;
	/**性别*/
	@Column(name="xingbie")
	private String xingbie;
	/**联系电话*/
	@Column(name="lxdh")
	private String lxdh;
	
	
	
	
	
	public String getXingming() {
		return xingming;
	}
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public String getXingbie() {
		return xingbie;
	}
	public void setXingbie(String xingbie) {
		this.xingbie = xingbie;
	}
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
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
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getHkxz() {
		return hkxz;
	}
	public void setHkxz(String hkxz) {
		this.hkxz = hkxz;
	}
	public String getCulture() {
		return culture;
	}
	public void setCulture(String culture) {
		this.culture = culture;
	}
	public String getCommunicationaddr() {
		return communicationaddr;
	}
	public void setCommunicationaddr(String communicationaddr) {
		this.communicationaddr = communicationaddr;
	}
	public String getHomeaddr() {
		return homeaddr;
	}
	public void setHomeaddr(String homeaddr) {
		this.homeaddr = homeaddr;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getRegisterphone() {
		return registerphone;
	}
	public void setRegisterphone(String registerphone) {
		this.registerphone = registerphone;
	}
	public String getPostalservice() {
		return postalservice;
	}
	public void setPostalservice(String postalservice) {
		this.postalservice = postalservice;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}