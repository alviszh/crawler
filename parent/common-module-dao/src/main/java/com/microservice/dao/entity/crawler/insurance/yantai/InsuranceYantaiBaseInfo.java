package com.microservice.dao.entity.crawler.insurance.yantai;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 烟台社保 参保基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_yantai_baseinfo")
public class InsuranceYantaiBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 身份证 */
	@Column(name="cardId")
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
	
	/** 民族 */
	@Column(name="nation")
	private String nation;
	
	/** 单位编号 */
	@Column(name="dwbh")
	private String dwbh;
	
	/** 单位名称 */
	@Column(name="dwmc")
	private String dwmc;
	
	/** 职工类别 */
	@Column(name="zglb")
	private String zglb;
	
	/** 投保类别 */
	@Column(name="tblb")
	private String tblb;
	
	/** 通讯地址 */
	@Column(name="communicationaddr")
	private String communicationaddr;
	
	/** 联系人 */
	@Column(name="linkman")
	private String linkman;
	
	/** 手机号码 */
	@Column(name="phone")
	private String phone;
	
	/** 离退休日期 */
	@Column(name="ltxrq")
	private String ltxrq;
	
	/** 离退休类别 */
	@Column(name="ltxlb")
	private String ltxlb;

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

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getDwbh() {
		return dwbh;
	}

	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getZglb() {
		return zglb;
	}

	public void setZglb(String zglb) {
		this.zglb = zglb;
	}

	public String getTblb() {
		return tblb;
	}

	public void setTblb(String tblb) {
		this.tblb = tblb;
	}

	public String getCommunicationaddr() {
		return communicationaddr;
	}

	public void setCommunicationaddr(String communicationaddr) {
		this.communicationaddr = communicationaddr;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLtxrq() {
		return ltxrq;
	}

	public void setLtxrq(String ltxrq) {
		this.ltxrq = ltxrq;
	}

	public String getLtxlb() {
		return ltxlb;
	}

	public void setLtxlb(String ltxlb) {
		this.ltxlb = ltxlb;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
}