package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 公共记录 -- 强制执行记录
 */
@Entity
@Table(name="public_enforced_record")
public class PublicEnforcedRecord extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -2654434377758185855L;

	private String mapping_id;  //uuid 唯一标识
	private String report_no;   //人行征信报告编号
	private String type;  //1.欠税记录 2.民事判决记录 3.强制执行记录 4.行政处罚记录 5.电信欠费记录

	private String court;//执行法院
	
	private String caseNum;//案号   eg   （2013）甬慈执民字第6021号
	
	private String caseType;//执行案由
	
	private String closedMode;//结案方式
	
	private String filingTime;//立案时间
	
	private String caseStatus;//案件状态
	
	private String applyExecuteTarget;//申请执行标的
	
	private String executedTarget;//已执行标的
	
	private String applyExecuteTargetMoney;//申请执行标的金额
	
	private String executedTargetMoney;//已执行标的金额
	
	private String closedTime;//结案时间

	public String getCourt() {
		return court;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getClosedMode() {
		return closedMode;
	}

	public void setClosedMode(String closedMode) {
		this.closedMode = closedMode;
	}

	public String getFilingTime() {
		return filingTime;
	}

	public void setFilingTime(String filingTime) {
		this.filingTime = filingTime;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getApplyExecuteTarget() {
		return applyExecuteTarget;
	}

	public void setApplyExecuteTarget(String applyExecuteTarget) {
		this.applyExecuteTarget = applyExecuteTarget;
	}

	public String getExecutedTarget() {
		return executedTarget;
	}

	public void setExecutedTarget(String executedTarget) {
		this.executedTarget = executedTarget;
	}

	public String getApplyExecuteTargetMoney() {
		return applyExecuteTargetMoney;
	}

	public void setApplyExecuteTargetMoney(String applyExecuteTargetMoney) {
		this.applyExecuteTargetMoney = applyExecuteTargetMoney;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExecutedTargetMoney() {
		return executedTargetMoney;
	}

	public void setExecutedTargetMoney(String executedTargetMoney) {
		this.executedTargetMoney = executedTargetMoney;
	}

	public String getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(String closedTime) {
		this.closedTime = closedTime;
	}

	public String getMapping_id() {
		return mapping_id;
	}

	public void setMapping_id(String mapping_id) {
		this.mapping_id = mapping_id;
	}

	public String getReport_no() {
		return report_no;
	}

	public void setReport_no(String report_no) {
		this.report_no = report_no;
	}

	@Override
	public String toString() {
		return "PublicEnforcedRecord{" +
				"mapping_id='" + mapping_id + '\'' +
				", report_no='" + report_no + '\'' +
				", type='" + type + '\'' +
				", court='" + court + '\'' +
				", caseNum='" + caseNum + '\'' +
				", caseType='" + caseType + '\'' +
				", closedMode='" + closedMode + '\'' +
				", filingTime='" + filingTime + '\'' +
				", caseStatus='" + caseStatus + '\'' +
				", applyExecuteTarget='" + applyExecuteTarget + '\'' +
				", executedTarget='" + executedTarget + '\'' +
				", applyExecuteTargetMoney='" + applyExecuteTargetMoney + '\'' +
				", executedTargetMoney='" + executedTargetMoney + '\'' +
				", closedTime='" + closedTime + '\'' +
				'}';
	}
}
