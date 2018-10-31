package com.microservice.dao.entity.crawler.wechat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.CookieJson;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "task_wechat",indexes = {@Index(name = "index_task_wechat_taskid", columnList = "taskid")})
public class TaskWeChat extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7601637293254927953L;
	
	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String phase;//当前步骤
	
	private String phase_status;//步骤状态
	
	private String cookies;
	
	private String description;
	
	private String loginType;		//登录类型  
	
	private Boolean finished;//爬虫任务是否全部完成
	
	private Integer error_code; //错误代码  StatusCodeRec 枚举类
	
	private String error_message; //错误信息 StatusCodeRec 枚举类


	@JsonBackReference
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
	
	public String getLoginType() {
		return loginType;
	}
	
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
	@Column(columnDefinition="text")
	public String getTesthtml() {
		return testhtml;
	}

	public void setTesthtml(String testhtml) {
		this.testhtml = testhtml;
	}

	public Date getEtltime() {
		return etltime;
	}

	public void setEtltime(Date etltime) {
		this.etltime = etltime;
	}

	public WebClient getClient(String cookieStr){
		if(StringUtils.isBlank(cookieStr)){
			throw new RuntimeException("cookie is null !");
		}else{
			Set<Cookie> cookies = transferJsonToSet(cookieStr);
			WebClient webClient = null;
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.setRefreshHandler(new ThreadedRefreshHandler());
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setPrintContentOnFailingStatusCode(false);
			webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setTimeout(20000); // 15->60
			webClient.waitForBackgroundJavaScript(50000); // 5s
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setUseInsecureSSL(true); //
			
			for(Cookie cookie:cookies){
				webClient.getCookieManager().addCookie(cookie);
			}
			return webClient;
		}
		
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

	@Column(columnDefinition="text")
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

	
	

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	/**
	 * @Des 将json转为Set<Cookie>
	 * @param json
	 * @return
	 */
	public Set<Cookie> transferJsonToSet(String json) {

		Set<Cookie> set = new HashSet<Cookie>();
		Set<CookieJson> cookiesJsonSet = new Gson().fromJson(json, new TypeToken<Set<CookieJson>>() {
		}.getType());
		for (CookieJson cookieJson : cookiesJsonSet) {
			Cookie cookie = new Cookie(cookieJson.getDomain(), cookieJson.getKey(), cookieJson.getValue());
			set.add(cookie);
		}
		
		return set;

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


	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="basicUserWechat_id")
	public BasicUserWechat getBasicUserWechat() {
		return basicUserWechat;
	}

	public void setBasicUserWechat(BasicUserWechat basicUserWechat) {
		this.basicUserWechat = basicUserWechat;
	}

	public Integer getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(Integer groupStatus) {
		this.groupStatus = groupStatus;
	}

	
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

	@Column(columnDefinition="text")
	public String getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}
	@Column(columnDefinition="text")
	public String getQrUrl() {
		return qrUrl;
	}

	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}

	

}
