package com.microservice.dao.entity.crawler.insurance.binzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 滨州社保 参保基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_binzhou_baseinfo")
public class InsuranceBinZhouBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 性别 */
	@Column(name="sex")
	private String sex;
	
	/**民族*/
	@Column(name="nation")
	private String nation;
	
	/**文化程度*/
	@Column(name="culture")
	private String culture;
	
	/**行政职务*/
	@Column(name="administrative")
	private String administrative;
	
	/**注册电话*/
	@Column(name="registerphone")
	private String registerphone;
	
	/**家庭住址*/
	@Column(name="homeaddr")
	private String homeaddr;
	
	/**通讯住址*/
	@Column(name="communicationaddr")
	private String communicationaddr;
	
	/**户口所在地*/
	@Column(name="permanentaddr")
	private String permanentaddr;
	
	/**单位名称*/
	@Column(name="unitaddr")
	private String unitaddr;
	
	
	
	/** 身份证号 */
	@Column(name="card_id")
	private String cardId;
	
	/** 出生日期 */
	@Column(name="birth_date")
	private String birthDate;
	
	/** 婚姻状况*/
	@Column(name="maritalstatus")
	private String maritalstatus;
	
	/** 户口性质*/
	@Column(name="hkxz")
	private String hkxz;
	
	/**联系人*/
	@Column(name="linkman")
	private String linkman;
	
	/**邮政编码*/
	@Column(name="postalservice")
	private String postalservice;


	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getCulture() {
		return culture;
	}

	public void setCulture(String culture) {
		this.culture = culture;
	}

	public String getAdministrative() {
		return administrative;
	}

	public void setAdministrative(String administrative) {
		this.administrative = administrative;
	}

	public String getRegisterphone() {
		return registerphone;
	}

	public void setRegisterphone(String registerphone) {
		this.registerphone = registerphone;
	}

	public String getHomeaddr() {
		return homeaddr;
	}

	public void setHomeaddr(String homeaddr) {
		this.homeaddr = homeaddr;
	}

	public String getCommunicationaddr() {
		return communicationaddr;
	}

	public void setCommunicationaddr(String communicationaddr) {
		this.communicationaddr = communicationaddr;
	}

	public String getPermanentaddr() {
		return permanentaddr;
	}

	public void setPermanentaddr(String permanentaddr) {
		this.permanentaddr = permanentaddr;
	}

	public String getUnitaddr() {
		return unitaddr;
	}

	public void setUnitaddr(String unitaddr) {
		this.unitaddr = unitaddr;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getMaritalstatus() {
		return maritalstatus;
	}

	public void setMaritalstatus(String maritalstatus) {
		this.maritalstatus = maritalstatus;
	}

	public String getHkxz() {
		return hkxz;
	}

	public void setHkxz(String hkxz) {
		this.hkxz = hkxz;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPostalservice() {
		return postalservice;
	}

	public void setPostalservice(String postalservice) {
		this.postalservice = postalservice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
}