package com.microservice.dao.entity.crawler.insurance.guiyang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 贵阳社保 参保基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_guiyang_baseinfo")
public class InsuranceGuiyangBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 登录名 */
	@Column(name="login_name")
	private String loginName;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 身份证 */
	@Column(name="cardid")
	private String cardid;
	
	/** 性别 */
	@Column(name="sex")
	private String sex;
	
	
	/** 出生日期 */
	@Column(name="birth_date")
	private String birthdate;
	
	/** 参工日期 */
	@Column(name="cangong_date")
	private String cangongdate;
	
	/** 户口性质 */
	@Column(name="hkxz")
	private String hkxz;
	
	/** 户口所在地 */
	@Column(name="hkaddr")
	private String hkaddr;
	
	/** 人员状态 */
	@Column(name="status")
	private String status;
	
	/** 邮政编码*/
	@Column(name="yzbm")
	private String yzbm;
	
	/** 联系地址*/
	@Column(name="linkaddr")
	private String linkaddr;
	
	/** 个人身份*/
	@Column(name="grsf")
	private String grsf;
	
	/** 用工形式*/
	@Column(name="ygxs")
	private String ygxs;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

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

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getCangongdate() {
		return cangongdate;
	}

	public void setCangongdate(String cangongdate) {
		this.cangongdate = cangongdate;
	}

	public String getHkxz() {
		return hkxz;
	}

	public void setHkxz(String hkxz) {
		this.hkxz = hkxz;
	}

	public String getHkaddr() {
		return hkaddr;
	}

	public void setHkaddr(String hkaddr) {
		this.hkaddr = hkaddr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getYzbm() {
		return yzbm;
	}

	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}

	public String getLinkaddr() {
		return linkaddr;
	}

	public void setLinkaddr(String linkaddr) {
		this.linkaddr = linkaddr;
	}

	public String getGrsf() {
		return grsf;
	}

	public void setGrsf(String grsf) {
		this.grsf = grsf;
	}

	public String getYgxs() {
		return ygxs;
	}

	public void setYgxs(String ygxs) {
		this.ygxs = ygxs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}