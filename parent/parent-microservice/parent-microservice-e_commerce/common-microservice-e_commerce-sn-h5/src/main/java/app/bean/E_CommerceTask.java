package app.bean;
import java.io.Serializable;
import java.util.Date;

public class E_CommerceTask implements Serializable {


    private String taskid;//uuid 前端通过uuid访问状态结果

    private String phase;//当前步骤

    private String phase_status;//步骤状态

    private String description;

    private Boolean finished;//爬虫任务是否全部完成

    private Integer error_code; //错误代码  StatusCodeRec 枚举类

    private String error_message; //错误信息 StatusCodeRec 枚举类

    private Date etltime;

    private String param;        //爬取中需要的参数

    private String loginName;        //登录名

    private String verificationPhone; //短信验证手机

    private String websiteType;        //电商网站类型类型

    private String crawlerHost;        //爬虫节点的IP或hostname

    private String crawlerPort;        //爬虫节点的端口

    private String testhtml;        //

    private Integer userinfoStatus;    //用户信息状态

    private Integer alipayInfoStatus;//支付宝基本信息
    private Integer orderInfoStatus; //订单信息
    private Integer alipayPaymentInfoStatus;   //支付宝支付信息
    private Integer addressInfoStatus; //地址信息
    private Integer bankCardInfoStatus;//银行卡信息
    private Integer btPrivilegeInfoStatus; //白条信息 和小白信用分
    private Integer renzhengInfoStatus;//认证信息





    private String webdriverHandle;//selenium 的window hander

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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
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

    public String getTesthtml() {
        return testhtml;
    }

    public void setTesthtml(String testhtml) {
        this.testhtml = testhtml;
    }

    public Integer getUserinfoStatus() {
        return userinfoStatus;
    }

    public void setUserinfoStatus(Integer userinfoStatus) {
        this.userinfoStatus = userinfoStatus;
    }

    public Integer getAlipayInfoStatus() {
        return alipayInfoStatus;
    }

    public void setAlipayInfoStatus(Integer alipayInfoStatus) {
        this.alipayInfoStatus = alipayInfoStatus;
    }

    public Integer getOrderInfoStatus() {
        return orderInfoStatus;
    }

    public void setOrderInfoStatus(Integer orderInfoStatus) {
        this.orderInfoStatus = orderInfoStatus;
    }

    public Integer getAlipayPaymentInfoStatus() {
        return alipayPaymentInfoStatus;
    }

    public void setAlipayPaymentInfoStatus(Integer alipayPaymentInfoStatus) {
        this.alipayPaymentInfoStatus = alipayPaymentInfoStatus;
    }

    public Integer getAddressInfoStatus() {
        return addressInfoStatus;
    }

    public void setAddressInfoStatus(Integer addressInfoStatus) {
        this.addressInfoStatus = addressInfoStatus;
    }

    public Integer getBankCardInfoStatus() {
        return bankCardInfoStatus;
    }

    public void setBankCardInfoStatus(Integer bankCardInfoStatus) {
        this.bankCardInfoStatus = bankCardInfoStatus;
    }

    public String getWebdriverHandle() {
        return webdriverHandle;
    }

    public void setWebdriverHandle(String webdriverHandle) {
        this.webdriverHandle = webdriverHandle;
    }

    public String getVerificationPhone() {
        return verificationPhone;
    }

    public void setVerificationPhone(String verificationPhone) {
        this.verificationPhone = verificationPhone;
    }

    public Integer getBtPrivilegeInfoStatus() {
     return btPrivilegeInfoStatus;
    }

    public void setBtPrivilegeInfoStatus(Integer btPrivilegeInfoStatus) {
        this.btPrivilegeInfoStatus = btPrivilegeInfoStatus;
    }
    public Integer getRenzhengInfoStatus() {
         return renzhengInfoStatus;
    }

    public void setRenzhengInfoStatus(Integer renzhengInfoStatus) {
        this.renzhengInfoStatus = renzhengInfoStatus;
    }


    @Override
    public String toString() {
        return "TaskBank{" +
                "taskid='" + taskid + '\'' +
                ", phase='" + phase + '\'' +
                ", phase_status='" + phase_status + '\'' +
                ", description='" + description + '\'' +
                ", finished=" + finished +
                ", error_code=" + error_code +
                ", error_message='" + error_message + '\'' +
                ", etltime =" + etltime +
                ", param ='" + param + '\'' +
                ", loginName='" + loginName + '\'' +
                ", verificationPhone='" + verificationPhone + '\'' +
                ", websiteType='" + websiteType + '\'' +
                ", crawlerHost='" + crawlerHost+ '\'' +
                ", crawlerPort='" + crawlerPort+ '\'' +
                ", testhtml='" + testhtml+ '\'' +
                ", userinfoStatus='" + userinfoStatus+ '\'' +
                ", orderInfoStatus='" + orderInfoStatus+ '\'' +
                ", addressInfoStatus='" + addressInfoStatus+ '\'' +
                ", bankCardInfoStatus='" + bankCardInfoStatus+ '\'' +
                ", alipayPaymentInfoStatus='" + alipayPaymentInfoStatus+ '\'' +
                ", alipayInfoStatus='" + alipayInfoStatus+ '\'' +
                ", webdriverHandle='" + webdriverHandle+ '\'' +
                ", btPrivilegeInfoStatus='" + btPrivilegeInfoStatus+ '\'' +
                ", renzhengInfoStatus='" + renzhengInfoStatus+ '\'' +
                '}';
    }
}

