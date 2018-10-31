package com.microservice.dao.entity.crawler.insurance.guiyang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 贵阳社保 机关事业养老信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_guiyang_jiguan_yanglao_info")
public class InsuranceGuiyangJiGuanYanglaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 登录名 */
	@Column(name="login_name")
	private String loginName;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 险种 */
	@Column(name="name")
	private String name;
	
	/** 期号 */
	@Column(name="yearMonth")
	private String yearMonth;
	
	/** 缴费基数*/
	@Column(name="pay_cardinal")
	private String payCardinal;
	
	/** 个人交 */
	@Column(name="personal_pay")
	private String personalPay;
	
	/** 单位交 */
	@Column(name="company_pay")
	private String companyPay;
	
	/**单位编号*/
	@Column(name="unit_no")
	private String unitNo;
	
	/**单位名称*/
	@Column(name="unit_name")
	private String unitName;


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

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getPayCardinal() {
		return payCardinal;
	}

	public void setPayCardinal(String payCardinal) {
		this.payCardinal = payCardinal;
	}

	public String getPersonalPay() {
		return personalPay;
	}

	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}

	public String getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	
	
	
	
}