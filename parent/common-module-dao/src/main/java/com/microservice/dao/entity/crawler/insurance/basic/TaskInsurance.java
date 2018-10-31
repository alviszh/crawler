package com.microservice.dao.entity.crawler.insurance.basic;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
@Table(name = "task_insurance")
public class TaskInsurance extends IdEntity implements Serializable {

	private static final long serialVersionUID = -7601637293254927953L;

	private String taskid;// uuid 前端通过uuid访问状态结果

	private String phase;// 当前步骤

	private String phase_status;// 步骤状态

	private String cookies;

	private String description;

	private String city; // 城市

	private String loginType; // 登录类型

	private Boolean finished;// 爬虫任务是否全部完成

	private Integer error_code; // 错误代码 StatusCodeRec 枚举类

	private String error_message; // 错误信息 StatusCodeRec 枚举类

	
	private BasicUserInsurance basicUserInsurance; // 用户基本表

	private Date updateTime = new Date();

	private Integer yanglaoStatus;

	private Integer shiyeStatus;

	private Integer gongshangStatus;

	private Integer shengyuStatus;

	private Integer yiliaoStatus;

	/** 深圳社保爬取必要参数 */
	private String pid;

	private Integer userInfoStatus;

	private Date etltime;

	private String testhtml;

	private String crawlerHost; // 爬虫节点的IP或hostname

	private String crawlerPort; // 爬虫节点的端口

	private String webdriverHandle; // seleunim的 webdriverhander
									// ID，用于登录完后存储当前页面的webdriverhander
									// id，在登陆完毕下一步需要短信验证码的情况下能继续使用这个webdriverhander（也必须继续使用这个webdriverhander）

	private String owner; // 所属公司，例：天曦、汇城、汇金
	private String report_time; // 报告处理时间
	private String report_status; // 报告处理状态
	// opendata项目中用到的，不然opendata项目中调不到bank-etl项目中的接口
	private String environmentId; // environmentId

	@Column(name = "task_owner")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getReport_time() {
		return report_time;
	}

	public void setReport_time(String report_time) {
		this.report_time = report_time;
	}

	public String getReport_status() {
		return report_status;
	}

	public void setReport_status(String report_status) {
		this.report_status = report_status;
	}

	public String getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	@Column(columnDefinition = "text")
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

	public WebClient getClient(String cookieStr) {
		if (StringUtils.isBlank(cookieStr)) {
			throw new RuntimeException("cookie is null !");
		} else {
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

			for (Cookie cookie : cookies) {
				webClient.getCookieManager().addCookie(cookie);
			}
			return webClient;
		}

	}

	public Integer getUserInfoStatus() {
		return userInfoStatus;
	}

	public void setUserInfoStatus(Integer userInfoStatus) {
		this.userInfoStatus = userInfoStatus;
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

	@Column(columnDefinition = "text")
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "basicUserInsurance_id")
	public BasicUserInsurance getBasicUserInsurance() {
		return basicUserInsurance;
	}

	public void setBasicUserInsurance(BasicUserInsurance basicUserInsurance) {
		this.basicUserInsurance = basicUserInsurance;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getYanglaoStatus() {
		return yanglaoStatus;
	}

	public void setYanglaoStatus(Integer yanglaoStatus) {
		this.yanglaoStatus = yanglaoStatus;
	}

	public Integer getShiyeStatus() {
		return shiyeStatus;
	}

	public void setShiyeStatus(Integer shiyeStatus) {
		this.shiyeStatus = shiyeStatus;
	}

	public Integer getGongshangStatus() {
		return gongshangStatus;
	}

	public void setGongshangStatus(Integer gongshangStatus) {
		this.gongshangStatus = gongshangStatus;
	}

	public Integer getShengyuStatus() {
		return shengyuStatus;
	}

	public void setShengyuStatus(Integer shengyuStatus) {
		this.shengyuStatus = shengyuStatus;
	}

	public Integer getYiliaoStatus() {
		return yiliaoStatus;
	}

	public void setYiliaoStatus(Integer yiliaoStatus) {
		this.yiliaoStatus = yiliaoStatus;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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

	@Override
	public String toString() {
		return "TaskInsurance [taskid=" + taskid + ", phase=" + phase + ", phase_status=" + phase_status + ", cookies="
				+ cookies + ", description=" + description + ", city=" + city + ", loginType=" + loginType
				+ ", finished=" + finished + ", error_code=" + error_code + ", error_message=" + error_message
				+ ", basicUserInsurance=" + basicUserInsurance + ", updateTime=" + updateTime + ", yanglaoStatus="
				+ yanglaoStatus + ", shiyeStatus=" + shiyeStatus + ", gongshangStatus=" + gongshangStatus
				+ ", shengyuStatus=" + shengyuStatus + ", yiliaoStatus=" + yiliaoStatus + ", pid=" + pid
				+ ", userInfoStatus=" + userInfoStatus + ", etltime=" + etltime + ", testhtml=" + testhtml
				+ ", crawlerHost=" + crawlerHost + ", crawlerPort=" + crawlerPort + ", webdriverHandle="
				+ webdriverHandle + "]";
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

}
