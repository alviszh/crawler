package com.crawler.pbccrc.json;

import java.io.Serializable;

/**
 * @author meidi
 * @date 2017年12月28日
 * 强制执行记录 
 */
public class EnforcedRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2463473984918063852L;
	
	
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

	@Override
	public String toString() {
		return "EnforcedRecord [court=" + court + ", caseNum=" + caseNum + ", caseType=" + caseType + ", closedMode="
				+ closedMode + ", filingTime=" + filingTime + ", caseStatus=" + caseStatus + ", applyExecuteTarget="
				+ applyExecuteTarget + ", executedTarget=" + executedTarget + ", applyExecuteTargetMoney="
				+ applyExecuteTargetMoney + ", executedTargetMoney=" + executedTargetMoney + ", closedTime="
				+ closedTime + "]";
	}
	
	
	
	
	
	
	

}
