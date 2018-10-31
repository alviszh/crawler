package com.microservice.dao.entity.crawler.insurance.zhongshan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhongshan_medicalcare")
public class InsuranceZhongShanMedicalCare extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 身份证号 */
	@Column(name="card_id")
	private String cardId;
	
	/**社保卡号 */
	@Column(name="social_security_number")
	private String socialSecurityNumber;

	/** 统筹年度 */
	@Column(name="overall_planning_year")
	private String overallPlanningYear;
	
	/** 医保个帐总金额*/
	@Column(name="account_total")
	private String accountTotal;
	/**卡内余额*/
	@Column(name="card_balance")
	private String cardBalance;
	/**未圈存金额*/
	@Column(name="no_quancun_amount")
	private String noQuancunAmount;
	/**本年度统筹限额(社区门诊)*/
	@Column(name="overall_balance_community")
	private String overallBalanceCommunity;
	/**本年度统筹余额(社区门诊)*/
	@Column(name="planning_balance_community")
	private String planningBalanceCommunity;
	/**已选定社区卫生站*/
	@Column(name="community_health_station")
	private String communityHealthStation;
	/**本年度统筹限额（基本医疗统筹）*/
	@Column(name="overall_balance_basic")
	private String overallBalanceBasic;
	/**基本医疗统筹余额*/
	@Column(name="planning_balance_basic")
	private String planningBalanceBasic;
	/**本年度统筹限额（补充医疗）*/
	@Column(name="overall_balance_supplement")
	private String overallBalanceSupplement;
	/**其中：特病门诊统筹限额*/
	@Column(name="especially_overall_balance")
	private String especiallyOverallBalance;
	/**本年度统筹余额(补充医疗)*/
	@Column(name="planning_balance_supplement")
	private String planningBalanceSupplement;
	/**其中：特病门诊统筹余额*/
	@Column(name="especially_planning_balance")
	private String especiallyPlanningBalance;
	/**本年度住院自付累计*/
	@Column(name="hospitalization_total")
	private String hospitalizationTotal;
	/**本年度特殊病种门诊累计*/
	@Column(name="hospitalization_especially_total")
	private String hospitalizationEspeciallyTotal;
	
	
	
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
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}
	public String getOverallPlanningYear() {
		return overallPlanningYear;
	}
	public void setOverallPlanningYear(String overallPlanningYear) {
		this.overallPlanningYear = overallPlanningYear;
	}
	public String getAccountTotal() {
		return accountTotal;
	}
	public void setAccountTotal(String accountTotal) {
		this.accountTotal = accountTotal;
	}
	public String getCardBalance() {
		return cardBalance;
	}
	public void setCardBalance(String cardBalance) {
		this.cardBalance = cardBalance;
	}
	public String getNoQuancunAmount() {
		return noQuancunAmount;
	}
	public void setNoQuancunAmount(String noQuancunAmount) {
		this.noQuancunAmount = noQuancunAmount;
	}
	public String getOverallBalanceCommunity() {
		return overallBalanceCommunity;
	}
	public void setOverallBalanceCommunity(String overallBalanceCommunity) {
		this.overallBalanceCommunity = overallBalanceCommunity;
	}
	public String getPlanningBalanceCommunity() {
		return planningBalanceCommunity;
	}
	public void setPlanningBalanceCommunity(String planningBalanceCommunity) {
		this.planningBalanceCommunity = planningBalanceCommunity;
	}
	public String getCommunityHealthStation() {
		return communityHealthStation;
	}
	public void setCommunityHealthStation(String communityHealthStation) {
		this.communityHealthStation = communityHealthStation;
	}
	public String getOverallBalanceBasic() {
		return overallBalanceBasic;
	}
	public void setOverallBalanceBasic(String overallBalanceBasic) {
		this.overallBalanceBasic = overallBalanceBasic;
	}
	public String getPlanningBalanceBasic() {
		return planningBalanceBasic;
	}
	public void setPlanningBalanceBasic(String planningBalanceBasic) {
		this.planningBalanceBasic = planningBalanceBasic;
	}
	public String getOverallBalanceSupplement() {
		return overallBalanceSupplement;
	}
	public void setOverallBalanceSupplement(String overallBalanceSupplement) {
		this.overallBalanceSupplement = overallBalanceSupplement;
	}
	public String getEspeciallyOverallBalance() {
		return especiallyOverallBalance;
	}
	public void setEspeciallyOverallBalance(String especiallyOverallBalance) {
		this.especiallyOverallBalance = especiallyOverallBalance;
	}
	public String getPlanningBalanceSupplement() {
		return planningBalanceSupplement;
	}
	public void setPlanningBalanceSupplement(String planningBalanceSupplement) {
		this.planningBalanceSupplement = planningBalanceSupplement;
	}
	
	public String getEspeciallyPlanningBalance() {
		return especiallyPlanningBalance;
	}
	public void setEspeciallyPlanningBalance(String especiallyPlanningBalance) {
		this.especiallyPlanningBalance = especiallyPlanningBalance;
	}
	public String getHospitalizationTotal() {
		return hospitalizationTotal;
	}
	public void setHospitalizationTotal(String hospitalizationTotal) {
		this.hospitalizationTotal = hospitalizationTotal;
	}
	public String getHospitalizationEspeciallyTotal() {
		return hospitalizationEspeciallyTotal;
	}
	public void setHospitalizationEspeciallyTotal(String hospitalizationEspeciallyTotal) {
		this.hospitalizationEspeciallyTotal = hospitalizationEspeciallyTotal;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}