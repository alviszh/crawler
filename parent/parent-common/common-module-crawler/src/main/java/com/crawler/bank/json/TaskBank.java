package com.crawler.bank.json;

import java.io.Serializable;
import java.util.Date;

public class TaskBank implements Serializable {

    private String taskid;//uuid 前端通过uuid访问状态结果

    private String phase;//当前步骤

    private String phase_status;//步骤状态

    private String cookies;

    private String description;

    private String city;		//城市

    private Boolean finished;//爬虫任务是否全部完成

    private Integer error_code; //错误代码  StatusCodeRec 枚举类

    private String error_message; //错误信息 StatusCodeRec 枚举类

    private Date etltime;

    private BasicUserBank basicUserBank; //用户基本表

    private String param;		//爬取中需要的参数

    private String cardType;		//银行卡类型 1.信用卡 2.储蓄卡

    private String loginType;		//登录类型  1.卡号 CARDNUM 2.用户名  USERNAME 3.身份证  IDNUM

    private String loginName;		//登录名

    private String bankType;		//银行类型

    private String crawlerHost;		//爬虫节点的IP或hostname

    private String crawlerPort;		//爬虫节点的端口

    private String webdriverHandle;		//seleunim的 webdriverhander ID，用于登录完后存储当前页面的webdriverhander id，在登陆完毕下一步需要短信验证码的情况下能继续使用这个webdriverhander（也必须继续使用这个webdriverhander）

    private String testhtml;

    private Integer userinfoStatus; //用户信息

    private Integer transflowStatus; //缴存信息

    private String question;		//渤海银行返回的问题
    
    
    private String createtime;  //为了定时任务邮件，补充添加的     sln

    public String getWebdriverHandle() {
        return webdriverHandle;
    }

    public void setWebdriverHandle(String webdriverHandle) {
        this.webdriverHandle = webdriverHandle;
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

    public Date getEtltime() {
        return etltime;
    }

    public void setEtltime(Date etltime) {
        this.etltime = etltime;
    }


    public BasicUserBank getBasicUserBank() {
        return basicUserBank;
    }

    public void setBasicUserBank(BasicUserBank basicUserBank) {
        this.basicUserBank = basicUserBank;
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

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }


    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getTransflowStatus() { return transflowStatus; }

    public void setTransflowStatus(Integer transflowStatus) {
        this.transflowStatus = transflowStatus;
    }

    public Integer getUserinfoStatus() {
        return userinfoStatus;
    }

    public void setUserinfoStatus(Integer userinfoStatus) {
        this.userinfoStatus = userinfoStatus;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Override
    public String toString() {
        return "TaskBank{" +
                "taskid='" + taskid + '\'' +
                ", phase='" + phase + '\'' +
                ", phase_status='" + phase_status + '\'' +
                ", cookies='" + cookies + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", finished=" + finished +
                ", error_code=" + error_code +
                ", error_message='" + error_message + '\'' +
                ", basicUserBank=" + basicUserBank +
                ", etltime =" + etltime +
                ", param ='" + param + '\'' +
                ", cardType='" + cardType + '\'' +
                ", loginType='" + loginType + '\'' +
                ", loginName='" + loginName + '\'' +
                ", bankType='" + bankType + '\'' +
                ", crawlerHost='" + crawlerHost+ '\'' +
                ", crawlerPort='" + crawlerPort+ '\'' +
                ", testhtml='" + testhtml+ '\'' +
                ", userinfoStatus='" + userinfoStatus+ '\'' +
                ", transflowStatus='" + transflowStatus+ '\'' +
                ", question='" + question+ '\'' +
               '}';
    }
}

