package com.microservice.dao.entity.crawler.housing.yantai;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45
 */
@Entity
@Table(name = "housing_yantai_base")
public class HousingYanTaiBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name = "taskid")
	private String taskid;
	
	// 账户状态
	@Column(name = "status")
	private String status;

	// 单位名称
	@Column(name = "company")
	private String company;

	// 职工姓名
	@Column(name = "name")
	private String name;

	// 证件类型
	@Column(name = "certificate_type")
	private String certificate_type;

	// 缴存基数
	@Column(name = "pay_cardinal")
	private String pay_cardinal;

	// 单位缴存比例
	@Column(name = "company_proportion")
	private String company_proportion;

	// 单位账号
	@Column(name = "company_number")
	private String company_number;

	// 身份证号
	@Column(name = "card")
	private String card;

	// 账户余额
	@Column(name = "yue")
	private String yue;

	// 个人缴存比例
	@Column(name = "person_proportion")
	private String person_proportion;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertificate_type() {
		return certificate_type;
	}

	public void setCertificate_type(String certificate_type) {
		this.certificate_type = certificate_type;
	}

	public String getPay_cardinal() {
		return pay_cardinal;
	}

	public void setPay_cardinal(String pay_cardinal) {
		this.pay_cardinal = pay_cardinal;
	}

	public String getCompany_proportion() {
		return company_proportion;
	}

	public void setCompany_proportion(String company_proportion) {
		this.company_proportion = company_proportion;
	}

	public String getCompany_number() {
		return company_number;
	}

	public void setCompany_number(String company_number) {
		this.company_number = company_number;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getYue() {
		return yue;
	}

	public void setYue(String yue) {
		this.yue = yue;
	}

	public String getPerson_proportion() {
		return person_proportion;
	}

	public void setPerson_proportion(String person_proportion) {
		this.person_proportion = person_proportion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
