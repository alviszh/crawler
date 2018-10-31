package com.microservice.dao.entity.crawler.housing.shenyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_shenyang_userinfo",indexes = {@Index(name = "index_housing_shenyang_userinfo_taskid", columnList = "taskid")})
public class HousingShenYangUserInfo extends IdEntity implements Serializable{

	private String name;//姓名
	private String personal;//个人账号
	private String IDCard;//身份证号
	private String dayFee;//查询日余额
	private String cardNum;//磁卡卡号
	private String regularFee;//定期余额
	private String saveStatus;//缴存状态
	private String currentFee;//活期余额
	private String saveDate;//缴存年月
	private String getMoney;//本年提取额
	private String companyRatio;//单位缴存比例
	private String saveBase;//缴存基数
	private String personalRatio;//个人缴存比例
	private String monthSave;//月缴存额
	private String taskid;
	@Override
	public String toString() {
		return "HousingShenYangUserInfo [name=" + name + ", personal=" + personal + ", IDCard=" + IDCard + ", dayFee="
				+ dayFee + ", cardNum=" + cardNum + ", regularFee=" + regularFee + ", saveStatus=" + saveStatus
				+ ", currentFee=" + currentFee + ", saveDate=" + saveDate + ", getMoney=" + getMoney + ", companyRatio="
				+ companyRatio + ", saveBase=" + saveBase + ", personalRatio=" + personalRatio + ", monthSave="
				+ monthSave + ", taskid=" + taskid + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getIDCard() {
		return IDCard;
	}
	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}
	public String getDayFee() {
		return dayFee;
	}
	public void setDayFee(String dayFee) {
		this.dayFee = dayFee;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getRegularFee() {
		return regularFee;
	}
	public void setRegularFee(String regularFee) {
		this.regularFee = regularFee;
	}
	public String getSaveStatus() {
		return saveStatus;
	}
	public void setSaveStatus(String saveStatus) {
		this.saveStatus = saveStatus;
	}
	public String getCurrentFee() {
		return currentFee;
	}
	public void setCurrentFee(String currentFee) {
		this.currentFee = currentFee;
	}
	public String getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(String saveDate) {
		this.saveDate = saveDate;
	}
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}
	public String getCompanyRatio() {
		return companyRatio;
	}
	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}
	public String getSaveBase() {
		return saveBase;
	}
	public void setSaveBase(String saveBase) {
		this.saveBase = saveBase;
	}
	public String getPersonalRatio() {
		return personalRatio;
	}
	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}
	public String getMonthSave() {
		return monthSave;
	}
	public void setMonthSave(String monthSave) {
		this.monthSave = monthSave;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
