package com.microservice.dao.entity.crawler.cmcc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * @author zz
 *	移动短信信息表
 */
@Entity
@Table(name="cmcc_smsmsg_result",indexes = {@Index(name = "index_cmcc_smsmsg_result_taskId", columnList = "taskId")})
public class CmccSMSMsgResult extends IdEntity{
	
	@Column(name="task_id")
	private String taskId;
	private String anotherNm;				//发送对象号码
	private String busiName;				//业务名称 例：国内短信费
	private String commFee;					//产生费用
	private String commMode;				//发送状态
	private String commPlac;				//发送地点
	private String infoType;				//发送方式
	private String meal;
	private String startTime;				//发送时间
	private String startYear;				//发送年份
	private String mobileNum;
	


	@Override
	public String toString() {
		return "CmccSMSMsgResult [ taskId=" + taskId + ", anotherNm=" + anotherNm + ", busiName="
				+ busiName + ", commFee=" + commFee + ", commMode=" + commMode + ", commPlac=" + commPlac
				+ ", infoType=" + infoType + ", meal=" + meal + ", startTime=" + startTime + "]";
	}
	
	public String getMobileNum() {
		return mobileNum;
	}
	
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getStartYear() {
		return startYear;
	}
	
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getAnotherNm() {
		return anotherNm;
	}
	public void setAnotherNm(String anotherNm) {
		this.anotherNm = anotherNm;
	}
	public String getBusiName() {
		return busiName;
	}
	public void setBusiName(String busiName) {
		this.busiName = busiName;
	}
	public String getCommFee() {
		return commFee;
	}
	public void setCommFee(String commFee) {
		this.commFee = commFee;
	}
	public String getCommMode() {
		return commMode;
	}
	public void setCommMode(String commMode) {
		this.commMode = commMode;
	}
	public String getCommPlac() {
		return commPlac;
	}
	public void setCommPlac(String commPlac) {
		this.commPlac = commPlac;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getMeal() {
		return meal;
	}
	public void setMeal(String meal) {
		this.meal = meal;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	

}
