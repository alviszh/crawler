package com.crawler.wechat.json;

import java.io.Serializable;
import java.util.Date;

public class TaskWeChat implements Serializable{
	
	
	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String phase;//当前步骤
	
	private String phase_status;//步骤状态
	
	private String cookies;
	
	private String description;
	
	private String loginType;		//登录类型  
	
	private Boolean finished;//爬虫任务是否全部完成
	
	private Integer error_code; //错误代码  StatusCodeRec 枚举类
	
	private String error_message; //错误信息 StatusCodeRec 枚举类

	private BasicUserWechat basicUserWechat; //用户基本表
	
	private Date updateTime = new Date();
	
	private Integer personalStatus;
	
	private Integer publicAccountStatus;
	
	private Integer groupStatus;
	
	private Date etltime;	
		
	private String testhtml;
	
	private String crawlerHost;		//爬虫节点的IP或hostname
		
	private String crawlerPort;		//爬虫节点的端口
		
	private String webdriverHandle;		//seleunim的 webdriverhander ID，用于登录完后存储当前页面的webdriverhander id，在登陆完毕下一步需要短信验证码的情况下能继续使用这个webdriverhander（也必须继续使用这个webdriverhander）

	private String baseCode;//二维码的base64码
    private String qrUrl;//二维码解析后的地址
	@Override
	public String toString() {
		return "TaskWeChat [taskid=" + taskid + ", phase=" + phase + ", phase_status=" + phase_status + ", cookies="
				+ cookies + ", description=" + description + ", loginType=" + loginType + ", finished=" + finished
				+ ", error_code=" + error_code + ", error_message=" + error_message + ", basicUserWechat="
				+ basicUserWechat + ", updateTime=" + updateTime + ", personalStatus=" + personalStatus
				+ ", publicAccountStatus=" + publicAccountStatus + ", groupStatus=" + groupStatus + ", etltime="
				+ etltime + ", testhtml=" + testhtml + ", crawlerHost=" + crawlerHost + ", crawlerPort=" + crawlerPort
				+ ", webdriverHandle=" + webdriverHandle + ", baseCode=" + baseCode + ", qrUrl=" + qrUrl + "]";
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
	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
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
	public BasicUserWechat getBasicUserWechat() {
		return basicUserWechat;
	}
	public void setBasicUserWechat(BasicUserWechat basicUserWechat) {
		this.basicUserWechat = basicUserWechat;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getPersonalStatus() {
		return personalStatus;
	}
	public void setPersonalStatus(Integer personalStatus) {
		this.personalStatus = personalStatus;
	}
	public Integer getPublicAccountStatus() {
		return publicAccountStatus;
	}
	public void setPublicAccountStatus(Integer publicAccountStatus) {
		this.publicAccountStatus = publicAccountStatus;
	}
	public Integer getGroupStatus() {
		return groupStatus;
	}
	public void setGroupStatus(Integer groupStatus) {
		this.groupStatus = groupStatus;
	}
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
	public String getWebdriverHandle() {
		return webdriverHandle;
	}
	public void setWebdriverHandle(String webdriverHandle) {
		this.webdriverHandle = webdriverHandle;
	}
	public String getBaseCode() {
		return baseCode;
	}
	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}
	public String getQrUrl() {
		return qrUrl;
	}
	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}
    
    
	

}
