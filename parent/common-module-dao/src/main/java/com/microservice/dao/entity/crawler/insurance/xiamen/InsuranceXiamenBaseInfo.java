package com.microservice.dao.entity.crawler.insurance.xiamen;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 深圳社保 参保基本信息
 * @author rongshengxu
 *
 */
@Entity
@Table(name="insurance_xiamen_baseinfo",indexes = {@Index(name = "index_insurance_xiamen_baseinfo_taskid", columnList = "taskId")})
public class InsuranceXiamenBaseInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = -7225639204374657354L;

	/** 登录名 */
	@Column(name="login_name")
	private String loginName;

	/** 爬取批次号 */
	@Column(name="task_id")
	private String taskId;

	/** 姓名 */
	@Column(name="name")
	private String name;

	/**保险号*/
	@Column(name = "insurance_no")
	private String insuranceNo;

	/**社会保障卡卡号*/
	@Column(name = "shbz_card_no")
	private String shbzCardNo;

	/**社会保障卡状态*/
	@Column(name = "shbz_card_status")
	private String shbzCardStatus;

	/**单位名称*/
	@Column(name = "company_name")
	private String companyName;

	/**单位编号*/
	@Column(name = "company_no")
	private String companyNo;

	/**单位类型*/
	@Column(name = "company_type")
	private String companyType;

	/**个人身份*/
	@Column(name = "identity")
	private String identity;

	/**工作状态*/
	@Column(name = "work_status")
	private String workStatus;

	/**养老保险参保日期*/
	@Column(name = "aged_insurance_canbao_date")
	private String agedInsuranceCanbaodate;
	/**养老缴费工资*/
	@Column(name = "aged_insurance_base")
	private String agedInsurancePayBase;
	/**医疗参保日期*/
	@Column(name = "medical_insurance_canbao_date")
	private String medicalInsuranceCanbaodate;
	/**医疗缴费工资*/
	@Column(name = "medical_insurance_pay_base")
	private String medicalInsurancePayBase;
	/**工伤参保日期*/
	@Column(name = "injury_insurance_canbao_date")
	private String injuryInsuranceCanbaodate;
	/**工伤缴费工资*/
	@Column(name = "injury_insurance_pay_base")
	private String injuryInsurancePayBase;
	/**失业参保日期*/
	@Column(name = "unemployment_insurance_canbao_date")
	private String unemploymentInsuranceCanbaodate;
	/**失业缴费工资*/
	@Column(name = "unemployment_insurance_pay_base")
	private String unemploymentInsurancePayBase;
	/**生育参保日期*/
	@Column(name = "birth_insurance_canbao_date")
	private String birthInsuranceCanbaodate;
	/**生育缴费工资*/
	@Column(name = "birth_insurance_pay_base")
	private String birthInsurancePayBase;



	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInsuranceNo() {
		return insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public String getShbzCardNo() {
		return shbzCardNo;
	}

	public void setShbzCardNo(String shbzCardNo) {
		this.shbzCardNo = shbzCardNo;
	}

	public String getShbzCardStatus() {
		return shbzCardStatus;
	}

	public void setShbzCardStatus(String shbzCardStatus) {
		this.shbzCardStatus = shbzCardStatus;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	public String getAgedInsuranceCanbaodate() {
		return agedInsuranceCanbaodate;
	}

	public void setAgedInsuranceCanbaodate(String agedInsuranceCanbaodate) {
		this.agedInsuranceCanbaodate = agedInsuranceCanbaodate;
	}

	public String getAgedInsurancePayBase() {
		return agedInsurancePayBase;
	}

	public void setAgedInsurancePayBase(String agedInsurancePayBase) {
		this.agedInsurancePayBase = agedInsurancePayBase;
	}

	public String getMedicalInsuranceCanbaodate() {
		return medicalInsuranceCanbaodate;
	}

	public void setMedicalInsuranceCanbaodate(String medicalInsuranceCanbaodate) {
		this.medicalInsuranceCanbaodate = medicalInsuranceCanbaodate;
	}

	public String getMedicalInsurancePayBase() {
		return medicalInsurancePayBase;
	}

	public void setMedicalInsurancePayBase(String medicalInsurancePayBase) {
		this.medicalInsurancePayBase = medicalInsurancePayBase;
	}

	public String getInjuryInsuranceCanbaodate() {
		return injuryInsuranceCanbaodate;
	}

	public void setInjuryInsuranceCanbaodate(String injuryInsuranceCanbaodate) {
		this.injuryInsuranceCanbaodate = injuryInsuranceCanbaodate;
	}

	public String getInjuryInsurancePayBase() {
		return injuryInsurancePayBase;
	}

	public void setInjuryInsurancePayBase(String injuryInsurancePayBase) {
		this.injuryInsurancePayBase = injuryInsurancePayBase;
	}

	public String getUnemploymentInsuranceCanbaodate() {
		return unemploymentInsuranceCanbaodate;
	}

	public void setUnemploymentInsuranceCanbaodate(String unemploymentInsuranceCanbaodate) {
		this.unemploymentInsuranceCanbaodate = unemploymentInsuranceCanbaodate;
	}

	public String getUnemploymentInsurancePayBase() {
		return unemploymentInsurancePayBase;
	}

	public void setUnemploymentInsurancePayBase(String unemploymentInsurancePayBase) {
		this.unemploymentInsurancePayBase = unemploymentInsurancePayBase;
	}

	public String getBirthInsuranceCanbaodate() {
		return birthInsuranceCanbaodate;
	}

	public void setBirthInsuranceCanbaodate(String birthInsuranceCanbaodate) {
		this.birthInsuranceCanbaodate = birthInsuranceCanbaodate;
	}

	public String getBirthInsurancePayBase() {
		return birthInsurancePayBase;
	}

	public void setBirthInsurancePayBase(String birthInsurancePayBase) {
		this.birthInsurancePayBase = birthInsurancePayBase;
	}

}
