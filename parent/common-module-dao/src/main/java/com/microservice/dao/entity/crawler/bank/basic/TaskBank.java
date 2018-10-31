package com.microservice.dao.entity.crawler.bank.basic;

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

/**
 * 银行task表
 * @author zz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="task_bank")
public class TaskBank extends IdEntityAndCookie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	@JsonBackReference
	private BasicUserBank basicUserBank; //用户基本表
	
	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String phase;//当前步骤
	
	private String phase_status;//步骤状态
	
	private String description;
	
	private Boolean finished;//爬虫任务是否全部完成
	
	private Integer error_code; //错误代码  StatusCodeRec 枚举类
	
	private String error_message; //错误信息 StatusCodeRec 枚举类
	
	private Date etltime;
	
	private String param;		//爬取中需要的参数
	
	private String cardType;		//银行卡类型 1.信用卡 2.储蓄卡
	 
	private String loginType;		//登录类型  1.卡号 CARDNUM 2.用户名  USERNAME 3.身份证  IDNUM
	
	private String loginName;		//登录名
	
	private String bankType;		//银行类型
	
	private String crawlerHost;		//爬虫节点的IP或hostname
	
	private String crawlerPort;		//爬虫节点的端口
	
	private String webdriverHandle;		//seleunim的 webdriverhander ID，用于登录完后存储当前页面的webdriverhander id，在登陆完毕下一步需要短信验证码的情况下能继续使用这个webdriverhander（也必须继续使用这个webdriverhander）
	
	private String testhtml;		//
	
	private Integer userinfoStatus;	//用户信息状态
	
	private Integer transflowStatus;	//流水信息状态
	
	private String owner;			//所属公司，例：天曦、汇城、汇金

	private String question;		//渤海银行返回的问题
	
	private String parent_time;		//纳入父级表时间
	
	private String report_time;		//报告处理时间
	
	private String report_status;	//报告处理状态
	
	private String etl_status;	//报告处理状态
	
	
	//opendata项目中用到的，不然opendata项目中调不到bank-etl项目中的接口
	private String environmentId;	//environmentId
	
	
	
	public String getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}
	

	@Column(name = "task_owner")
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getWebdriverHandle() {
		return webdriverHandle;
	}

	public void setWebdriverHandle(String webdriverHandle) {
		this.webdriverHandle = webdriverHandle;
	}

	public String getTesthtml() {
		return testhtml;
	}

	public void setTesthtml(String testhtml) {
		this.testhtml = testhtml;
	}

	public String getCrawlerHost() {
		return crawlerHost;
	}

	public void setCrawlerHost(String crawlerHost) {
		this.crawlerHost = crawlerHost;
	}

	public String getCrawlerPort() {
		return crawlerPort;
	}

	public void setCrawlerPort(String crawlerPort) {
		this.crawlerPort = crawlerPort;
	}

	public String getBankType() {
		return bankType;
	}
	
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	
	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="basicUserBank_id")
	public BasicUserBank getBasicUserBank() {
		return basicUserBank;
	}

	public void setBasicUserBank(BasicUserBank basicUserBank) {
		this.basicUserBank = basicUserBank;
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

	public void setDescription(String description) {
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

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public Date getEtltime() {
		return etltime;
	}

	public void setEtltime(Date etltime) {
		this.etltime = etltime;
	}

	@Column(columnDefinition="text")
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public Integer getUserinfoStatus() {
		return userinfoStatus;
	}

	public void setUserinfoStatus(Integer userinfoStatus) {
		this.userinfoStatus = userinfoStatus;
	}

	public Integer getTransflowStatus() {
		return transflowStatus;
	}

	public void setTransflowStatus(Integer transflowStatus) {
		this.transflowStatus = transflowStatus;
	}
	
	public String getParent_time() {
		return parent_time;
	}

	public void setParent_time(String parent_time) {
		this.parent_time = parent_time;
	}

	public String getReport_time() {
		return report_time;
	}

	public void setReport_time(String report_time) {
		this.report_time = report_time;
	}

	@Column(columnDefinition="text")
	public String getReport_status() {
		return report_status;
	}

	public void setReport_status(String report_status) {
		this.report_status = report_status;
	}

	public String getEtl_status() {
		return etl_status;
	}

	public void setEtl_status(String etl_status) {
		this.etl_status = etl_status;
	}

	@Override
	public String toString() {
		return "TaskBank [basicUserBank=" + basicUserBank + ", taskid=" + taskid + ", phase=" + phase
				+ ", phase_status=" + phase_status + ", description=" + description + ", finished=" + finished
				+ ", error_code=" + error_code + ", error_message=" + error_message + ", etltime=" + etltime
				+ ", param=" + param + ", cardType=" + cardType + ", loginType=" + loginType + ", loginName="
				+ loginName + ", bankType=" + bankType + ", crawlerHost=" + crawlerHost + ", crawlerPort=" + crawlerPort
				+ ", webdriverHandle=" + webdriverHandle + ", testhtml=" + testhtml + ", userinfoStatus="
				+ userinfoStatus + ", transflowStatus=" + transflowStatus + ", owner=" + owner + ", question="
				+ question + ", parent_time=" + parent_time + ", report_time=" + report_time + ", report_status="
				+ report_status + ", etl_status=" + etl_status + ", environmentId=" + environmentId + "]";
	}

}
