package com.microservice.dao.entity.crawler.insurance.sz.xinjiang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_xinjiang_info")
public class InsuranceXinJiangInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**姓名 */
	@Column(name="name")
	private String name;
	
	/** 参保险种 */
	@Column(name="cbxz")
	private String cbxz;
	
	/** 缴费状态 */
	@Column(name="jfzt")
	private String jfzt;
	
	/** 最大缴费期 */
	@Column(name="zdjfq")
	private String zdjfq;
	
	/** 经办机构名称 */
	@Column(name="jbjgmc")
	private String jbjgmc;
	
	/** 身份证号码 */
	@Column(name="cardid")
	private String cardid;

	public String getTaskid() {
		return taskid;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
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

	public String getCbxz() {
		return cbxz;
	}

	public void setCbxz(String cbxz) {
		this.cbxz = cbxz;
	}

	public String getJfzt() {
		return jfzt;
	}

	public void setJfzt(String jfzt) {
		this.jfzt = jfzt;
	}

	public String getZdjfq() {
		return zdjfq;
	}

	public void setZdjfq(String zdjfq) {
		this.zdjfq = zdjfq;
	}

	public String getJbjgmc() {
		return jbjgmc;
	}

	public void setJbjgmc(String jbjgmc) {
		this.jbjgmc = jbjgmc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}