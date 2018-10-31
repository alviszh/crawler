package com.microservice.dao.entity.crawler.mobile;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntityAndCookie; 
import javax.persistence.Index;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "task_mobile" ,indexes = {@Index(name = "index_task_mobile_taskid", columnList = "taskid")})
public class TaskMobile extends IdEntityAndCookie implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7601637293254927953L;

	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String phase;//当前步骤
	
	private String phase_status;//步骤状态
	
	private String description;
	
	private String carrier;//运营商
	
	private String owner;//数据所属人
	
	private String phonenum;//手机号
	
//	private Integer progress;//爬虫进度
	
	private Boolean finished;//爬虫任务是否全部完成
	
	private Integer error_code; //错误代码  StatusCodeRec 枚举类
	
	private String error_message; //错误信息 StatusCodeRec 枚举类

	//@JsonBackReference
	private BasicUser basicUser; //用户基本表
	
	private Date updateTime = new Date();
	 
	private String nexturl;
	
	private Integer trianNum;			//登录失败重试次数
	
	private Integer userMsgStatus; // 用户信息状态
	
	private Integer accountMsgStatus; // 账户信息状态
	
	private Integer callRecordStatus; // 通话详单状态
	
	private Integer smsRecordStatus; // 短信记录状态
	
	private Integer businessMsgStatus; // 业务信息状态
	
	private Integer payMsgStatus; // 缴费信息状态
	
	private Integer integralMsgStatus; // 积分信息状态
	
	private Integer familyMsgStatus; // 亲情号信息状态
	
	private String testhtml; //测试
	
	private String province;//归属地省份
	
	private String city; //归属地市
	
	private String areacode; //归属地市代码  eg:北京 010 
	
	private Date etltime;
	
	private String etlStatus;
	
	private Date reportTime;
	
	private String reportStatus;

	private Date pushtime; //推送前置规则的时间
	
	private String key; //唯一标识
	
	private String environmentId;
	
	
	public Date getEtltime() {
		return etltime;
	}

	public void setEtltime(Date etltime) {
		this.etltime = etltime;
	}

	public String getTesthtml() {
		return testhtml;
	}

	public void setTesthtml(String testhtml) {
		this.testhtml = testhtml;
	}

	public Integer getUserMsgStatus() {
		return userMsgStatus;
	}

	public void setUserMsgStatus(Integer userMsgStatus) {
		this.userMsgStatus = userMsgStatus;
	}


	public Integer getAccountMsgStatus() {
		return accountMsgStatus;
	}

	public void setAccountMsgStatus(Integer accountMsgStatus) {
		this.accountMsgStatus = accountMsgStatus;
	}


	public Integer getCallRecordStatus() {
		return callRecordStatus;
	}

	public void setCallRecordStatus(Integer callRecordStatus) {
		this.callRecordStatus = callRecordStatus;
	}
	
	public Integer getSmsRecordStatus() {
		return smsRecordStatus;
	}

	public void setSmsRecordStatus(Integer smsRecordStatus) {
		this.smsRecordStatus = smsRecordStatus;
	}

	public Integer getBusinessMsgStatus() {
		return businessMsgStatus;
	}

	public void setBusinessMsgStatus(Integer businessMsgStatus) {
		this.businessMsgStatus = businessMsgStatus;
	}


	public Integer getPayMsgStatus() {
		return payMsgStatus;
	}

	public void setPayMsgStatus(Integer payMsgStatus) {
		this.payMsgStatus = payMsgStatus;
	}


	public Integer getIntegralMsgStatus() {
		return integralMsgStatus;
	}

	public void setIntegralMsgStatus(Integer integralMsgStatus) {
		this.integralMsgStatus = integralMsgStatus;
	}

	public Integer getFamilyMsgStatus() {
		return familyMsgStatus;
	}

	public void setFamilyMsgStatus(Integer familyMsgStatus) {
		this.familyMsgStatus = familyMsgStatus;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	//@JsonIgnore	
	public Integer getTrianNum() {
		return trianNum;
	}

	public void setTrianNum(Integer trianNum) {
		this.trianNum = trianNum;
	}

	public String getError_message() {
		return error_message;
	}
	
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}	
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getPhase_status() {
		return phase_status;
	}

	public void setPhase_status(String phase_status) {
		this.phase_status = phase_status;
	}

	public String getDescription() {
		return description;
	}

	public void cookieString(String description) {
		this.description = description;
	}
	
	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}
	
	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	
	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	
	//@JsonIgnore
	@Column(columnDefinition="text")
	public String getNexturl() {
		return nexturl;
	}

	public void setNexturl(String nexturl) {
		this.nexturl = nexturl;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="basicUser_id")
	public BasicUser getBasicUser() {
		return basicUser;
	}

	public void setBasicUser(BasicUser basicUser) {
		this.basicUser = basicUser;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	
	@Column(name = "task_owner")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	
	@Column(columnDefinition="text")
	public String getReportStatus() {
		return reportStatus;
	}

	
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	public Date getPushtime() {
		return pushtime;
	}

	public void setPushtime(Date pushtime) {
		this.pushtime = pushtime;
	}
	
	@Column(columnDefinition="text")
	public String getEtlStatus() {
		return etlStatus;
	}

	public void setEtlStatus(String etlStatus) {
		this.etlStatus = etlStatus;
	}
	
	@Column(name = "task_key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}

	@Override
	public String toString() {
		return "TaskMobile [taskid=" + taskid + ", phase=" + phase + ", phase_status=" + phase_status + ", description="
				+ description + ", carrier=" + carrier + ", owner=" + owner + ", phonenum=" + phonenum + ", finished="
				+ finished + ", error_code=" + error_code + ", error_message=" + error_message + ", basicUser="
				+ basicUser + ", updateTime=" + updateTime + ", nexturl=" + nexturl + ", trianNum=" + trianNum
				+ ", userMsgStatus=" + userMsgStatus + ", accountMsgStatus=" + accountMsgStatus + ", callRecordStatus="
				+ callRecordStatus + ", smsRecordStatus=" + smsRecordStatus + ", businessMsgStatus=" + businessMsgStatus
				+ ", payMsgStatus=" + payMsgStatus + ", integralMsgStatus=" + integralMsgStatus + ", familyMsgStatus="
				+ familyMsgStatus + ", testhtml=" + testhtml + ", province=" + province + ", city=" + city
				+ ", areacode=" + areacode + ", etltime=" + etltime + ", etlStatus=" + etlStatus + ", reportTime="
				+ reportTime + ", reportStatus=" + reportStatus + ", pushtime=" + pushtime + "]";
	}

	
}
