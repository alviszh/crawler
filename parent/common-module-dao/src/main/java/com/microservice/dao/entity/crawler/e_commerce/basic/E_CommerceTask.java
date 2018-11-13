package com.microservice.dao.entity.crawler.e_commerce.basic;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_task",indexes = {@Index(name = "index_e_commerce_task_taskid", columnList = "taskid")})
public class E_CommerceTask extends IdEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonBackReference
    private E_commerceBasicUser basicUser; //用户基本表

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
    private Integer orderInfoStatus; //订单信息
    private Integer addressInfoStatus; //地址信息
    private Integer bankCardInfoStatus;//银行卡信息

    private Integer alipayPaymentInfoStatus;   //支付宝支付信息
    private Integer alipayInfoStatus;//支付宝基本信息


    private Integer btPrivilegeInfoStatus; //白条信息 和小白信用分
    private Integer renzhengInfoStatus;//认证信息

    private String webdriverHandle;//selenium 的window hander
    
    private String baseCode;//二维码的base64码
    private String qrUrl;//二维码解析后的地址
    
    private String owner;//数据所属人
	
	private String taskKey; //唯一标识

	//opendata项目中用到的，不然opendata项目中调不到bank-etl项目中的接口
	private String environmentId;	//environmentId
    private String report_time;		//报告处理时间
	
	private String report_status;	//报告处理状态	
	
	private String etlStatus;		//etl处理状态


	@Column(name = "task_owner")
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
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

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="basicUser_id")
	public E_commerceBasicUser getBasicUser() {
		return basicUser;
	}

	public void setBasicUser(E_commerceBasicUser basicUser) {
		this.basicUser = basicUser;
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
    @Column(columnDefinition="text")
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
	

	public String getEtlStatus() {
		return etlStatus;
	}

	public void setEtlStatus(String etlStatus) {
		this.etlStatus = etlStatus;
	}

	@Override
	public String toString() {
		return "E_CommerceTask [basicUser=" + basicUser + ", taskid=" + taskid + ", phase=" + phase + ", phase_status="
				+ phase_status + ", description=" + description + ", finished=" + finished + ", error_code="
				+ error_code + ", error_message=" + error_message + ", etltime=" + etltime + ", param=" + param
				+ ", loginName=" + loginName + ", verificationPhone=" + verificationPhone + ", websiteType="
				+ websiteType + ", crawlerHost=" + crawlerHost + ", crawlerPort=" + crawlerPort + ", testhtml="
				+ testhtml + ", userinfoStatus=" + userinfoStatus + ", orderInfoStatus=" + orderInfoStatus
				+ ", addressInfoStatus=" + addressInfoStatus + ", bankCardInfoStatus=" + bankCardInfoStatus
				+ ", alipayPaymentInfoStatus=" + alipayPaymentInfoStatus + ", alipayInfoStatus=" + alipayInfoStatus
				+ ", btPrivilegeInfoStatus=" + btPrivilegeInfoStatus + ", renzhengInfoStatus=" + renzhengInfoStatus
				+ ", webdriverHandle=" + webdriverHandle + ", baseCode=" + baseCode + ", qrUrl=" + qrUrl + ", owner="
				+ owner + ", taskKey=" + taskKey + ", environmentId=" + environmentId + ", report_time=" + report_time
				+ ", report_status=" + report_status + ", etlStatus=" + etlStatus + "]";
	}


}