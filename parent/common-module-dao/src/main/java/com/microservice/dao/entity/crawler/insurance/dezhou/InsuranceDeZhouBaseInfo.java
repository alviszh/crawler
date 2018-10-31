package com.microservice.dao.entity.crawler.insurance.dezhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 德州社保 参保基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_dezhou_baseinfo")
public class InsuranceDeZhouBaseInfo extends IdEntity implements Serializable{
	
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

	/** 婚姻状况*/
	@Column(name="maritalstatus")
	private String maritalstatus;

	/**通讯住址*/
	@Column(name="communicationaddr")
	private String communicationaddr;
	
	/**家庭住址*/
	@Column(name="homeaddr")
	private String homeaddr;
	
	/**民族*/
	@Column(name="nation")
	private String nation;

	/**联系人*/
	@Column(name="linkman")
	private String linkman;

	/**联系电话*/
	@Column(name="callphone")
	private String callphone;
	
	/**行政职务*/
	@Column(name="administrative")
	private String administrative;
	
	/**邮政编码*/
	@Column(name="postalservice")
	private String postalservice;
	
	/**户口所在地*/
	@Column(name="permanentaddr")
	private String permanentaddr;

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

	public String getMaritalstatus() {
		return maritalstatus;
	}

	public void setMaritalstatus(String maritalstatus) {
		this.maritalstatus = maritalstatus;
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

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getCallphone() {
		return callphone;
	}

	public void setCallphone(String callphone) {
		this.callphone = callphone;
	}

	public String getAdministrative() {
		return administrative;
	}

	public void setAdministrative(String administrative) {
		this.administrative = administrative;
	}

	public String getPostalservice() {
		return postalservice;
	}

	public void setPostalservice(String postalservice) {
		this.postalservice = postalservice;
	}

	public String getPermanentaddr() {
		return permanentaddr;
	}

	public void setPermanentaddr(String permanentaddr) {
		this.permanentaddr = permanentaddr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}