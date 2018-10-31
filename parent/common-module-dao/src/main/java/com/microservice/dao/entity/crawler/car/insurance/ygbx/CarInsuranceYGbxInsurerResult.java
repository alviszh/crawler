package com.microservice.dao.entity.crawler.car.insurance.ygbx;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntityAndCookie;

/**
 * 车险-阳光车险-保险人信息及合同表
 * @author zz
 *
 */
@Entity
@Table(name="car_insurance_ygbx_insurer_result")
public class CarInsuranceYGbxInsurerResult extends IdEntityAndCookie implements Serializable {
	
	private static final long serialVersionUID = -7601637293254927953L;
	
	private String taskid;							//uuid 前端通过uuid访问状态结果
	
	private String acknowledgementDate;				//收款确认时间
	
	private String generatePolicyDate;				//生成保单时间
	
	private String electronPolicyPipelineNo;		//电子保单流水号
	
	private String electronPolicyPipelineDate;		//电子保单生成时间
	
	private String confirmationCode;				//确认码
	
	private String contractDisputeSolveType;		//保险合同争议解决方式
	
	private String insuranceDate;					//保险时间
	
	private String insuranceCompanyName;			//保险公司名称
	
	private String insuranceCompanySite;			//保险公司网址
	
	private String insuranceCompanyCode;			//保险公司邮政编码
	
	private String insuranceCompanyPhone;			//保险公司联系电话
	
	private String insuranceCompanyAddress;			//保险公司地址
	
	private String signingDate;						//签单日期
	
	private String underwriter;						//核保人
	
	private String touching;						//核单
	
	private String handle;							//经办
	
	private String nationalUnifiedCallService;		//全国统一客户服务和客户维权电话
	
	private String ygbxInsuranceCallService;		//阳光保险电话车险
	
	private String ygbxSite;						//阳光网上车险
	
	private String specialAgreement;				//特别约定
	
	private String importantNote;					//重要提示
	
	
	public CarInsuranceYGbxInsurerResult(String taskid, String acknowledgementDate, String generatePolicyDate,
			String electronPolicyPipelineNo, String electronPolicyPipelineDate, String confirmationCode,
			String contractDisputeSolveType, String insuranceDate, String insuranceCompanyName,
			String insuranceCompanySite, String insuranceCompanyCode, String insuranceCompanyPhone,
			String insuranceCompanyAddress, String signingDate, String underwriter, String touching, String handle,
			String nationalUnifiedCallService, String ygbxInsuranceCallService, String ygbxSite,
			String specialAgreement, String importantNote) {
		super();
		this.taskid = taskid;
		this.acknowledgementDate = acknowledgementDate;
		this.generatePolicyDate = generatePolicyDate;
		this.electronPolicyPipelineNo = electronPolicyPipelineNo;
		this.electronPolicyPipelineDate = electronPolicyPipelineDate;
		this.confirmationCode = confirmationCode;
		this.contractDisputeSolveType = contractDisputeSolveType;
		this.insuranceDate = insuranceDate;
		this.insuranceCompanyName = insuranceCompanyName;
		this.insuranceCompanySite = insuranceCompanySite;
		this.insuranceCompanyCode = insuranceCompanyCode;
		this.insuranceCompanyPhone = insuranceCompanyPhone;
		this.insuranceCompanyAddress = insuranceCompanyAddress;
		this.signingDate = signingDate;
		this.underwriter = underwriter;
		this.touching = touching;
		this.handle = handle;
		this.nationalUnifiedCallService = nationalUnifiedCallService;
		this.ygbxInsuranceCallService = ygbxInsuranceCallService;
		this.ygbxSite = ygbxSite;
		this.specialAgreement = specialAgreement;
		this.importantNote = importantNote;
	}

	
	@Override
	public String toString() {
		return "CarInsuranceYGbxInsurerResult [taskid=" + taskid + ", acknowledgementDate=" + acknowledgementDate
				+ ", generatePolicyDate=" + generatePolicyDate + ", electronPolicyPipelineNo="
				+ electronPolicyPipelineNo + ", electronPolicyPipelineDate=" + electronPolicyPipelineDate
				+ ", confirmationCode=" + confirmationCode + ", contractDisputeSolveType=" + contractDisputeSolveType
				+ ", insuranceDate=" + insuranceDate + ", insuranceCompanyName=" + insuranceCompanyName
				+ ", insuranceCompanySite=" + insuranceCompanySite + ", insuranceCompanyCode=" + insuranceCompanyCode
				+ ", insuranceCompanyPhone=" + insuranceCompanyPhone + ", insuranceCompanyAddress="
				+ insuranceCompanyAddress + ", signingDate=" + signingDate + ", underwriter=" + underwriter
				+ ", touching=" + touching + ", handle=" + handle + ", nationalUnifiedCallService="
				+ nationalUnifiedCallService + ", ygbxInsuranceCallService=" + ygbxInsuranceCallService + ", ygbxSite="
				+ ygbxSite + ", specialAgreement=" + specialAgreement + ", importantNote=" + importantNote + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAcknowledgementDate() {
		return acknowledgementDate;
	}

	public void setAcknowledgementDate(String acknowledgementDate) {
		this.acknowledgementDate = acknowledgementDate;
	}

	public String getGeneratePolicyDate() {
		return generatePolicyDate;
	}

	public void setGeneratePolicyDate(String generatePolicyDate) {
		this.generatePolicyDate = generatePolicyDate;
	}

	public String getElectronPolicyPipelineNo() {
		return electronPolicyPipelineNo;
	}

	public void setElectronPolicyPipelineNo(String electronPolicyPipelineNo) {
		this.electronPolicyPipelineNo = electronPolicyPipelineNo;
	}

	public String getElectronPolicyPipelineDate() {
		return electronPolicyPipelineDate;
	}

	public void setElectronPolicyPipelineDate(String electronPolicyPipelineDate) {
		this.electronPolicyPipelineDate = electronPolicyPipelineDate;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public String getContractDisputeSolveType() {
		return contractDisputeSolveType;
	}

	public void setContractDisputeSolveType(String contractDisputeSolveType) {
		this.contractDisputeSolveType = contractDisputeSolveType;
	}

	public String getInsuranceDate() {
		return insuranceDate;
	}

	public void setInsuranceDate(String insuranceDate) {
		this.insuranceDate = insuranceDate;
	}

	public String getInsuranceCompanyName() {
		return insuranceCompanyName;
	}

	public void setInsuranceCompanyName(String insuranceCompanyName) {
		this.insuranceCompanyName = insuranceCompanyName;
	}

	public String getInsuranceCompanySite() {
		return insuranceCompanySite;
	}

	public void setInsuranceCompanySite(String insuranceCompanySite) {
		this.insuranceCompanySite = insuranceCompanySite;
	}

	public String getInsuranceCompanyCode() {
		return insuranceCompanyCode;
	}

	public void setInsuranceCompanyCode(String insuranceCompanyCode) {
		this.insuranceCompanyCode = insuranceCompanyCode;
	}

	public String getInsuranceCompanyPhone() {
		return insuranceCompanyPhone;
	}

	public void setInsuranceCompanyPhone(String insuranceCompanyPhone) {
		this.insuranceCompanyPhone = insuranceCompanyPhone;
	}

	public String getInsuranceCompanyAddress() {
		return insuranceCompanyAddress;
	}

	public void setInsuranceCompanyAddress(String insuranceCompanyAddress) {
		this.insuranceCompanyAddress = insuranceCompanyAddress;
	}

	public String getSigningDate() {
		return signingDate;
	}

	public void setSigningDate(String signingDate) {
		this.signingDate = signingDate;
	}

	public String getUnderwriter() {
		return underwriter;
	}

	public void setUnderwriter(String underwriter) {
		this.underwriter = underwriter;
	}

	public String getTouching() {
		return touching;
	}

	public void setTouching(String touching) {
		this.touching = touching;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getNationalUnifiedCallService() {
		return nationalUnifiedCallService;
	}

	public void setNationalUnifiedCallService(String nationalUnifiedCallService) {
		this.nationalUnifiedCallService = nationalUnifiedCallService;
	}

	public String getYgbxInsuranceCallService() {
		return ygbxInsuranceCallService;
	}

	public void setYgbxInsuranceCallService(String ygbxInsuranceCallService) {
		this.ygbxInsuranceCallService = ygbxInsuranceCallService;
	}

	public String getYgbxSite() {
		return ygbxSite;
	}

	public void setYgbxSite(String ygbxSite) {
		this.ygbxSite = ygbxSite;
	}

	@Column(columnDefinition="text")
	public String getSpecialAgreement() {
		return specialAgreement;
	}

	public void setSpecialAgreement(String specialAgreement) {
		this.specialAgreement = specialAgreement;
	}

	@Column(columnDefinition="text")
	public String getImportantNote() {
		return importantNote;
	}

	public void setImportantNote(String importantNote) {
		this.importantNote = importantNote;
	}


}
