package com.microservice.dao.entity.crawler.telecom.hunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 湖南电信基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_hunan_userinfo",indexes = {@Index(name = "index_telecom_hunan_userinfo_taskid", columnList = "taskid")})
public class TelecomHunanUserInfo  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	/**姓名*/   
	@Column(name="name")
	private String name;
	

	/**产品名称*/   
	@Column(name="proname")
	private String proname;

	/**账户状态*/   
	@Column(name="accountstatus")
	private String accountstatus;
	
	/**当前可用积分*/ 
	@Column(name="use_integral")
	private String useintegral ;
	
	/**倍增积分*/ 
	@Column(name="double_integral")
	private String doubleintegral ;
	
	/**促销积分*/ 
	@Column(name="sales_integral")
	private String salesintegral ;
	
	/**历史积分*/ 
	@Column(name="history_integral")
	private String historyintegral ;
	
	/**即将到期积分*/ 
	@Column(name="expire_integral")
	private String expireintegral ;
	
///////////////////////////////////////////////费用查询-余额变动
	/**余额*/  
	@Column(name="commonremaining")
	private String commonremaining ;

	/**电话号码*/   
	@Column(name="phone")
	private String phone ;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}


	public String getUseintegral() {
		return useintegral;
	}

	public void setUseintegral(String useintegral) {
		this.useintegral = useintegral;
	}

	public String getDoubleintegral() {
		return doubleintegral;
	}

	public void setDoubleintegral(String doubleintegral) {
		this.doubleintegral = doubleintegral;
	}

	public String getSalesintegral() {
		return salesintegral;
	}

	public void setSalesintegral(String salesintegral) {
		this.salesintegral = salesintegral;
	}

	public String getHistoryintegral() {
		return historyintegral;
	}

	public void setHistoryintegral(String historyintegral) {
		this.historyintegral = historyintegral;
	}

	public String getExpireintegral() {
		return expireintegral;
	}

	public void setExpireintegral(String expireintegral) {
		this.expireintegral = expireintegral;
	}

	public String getCommonremaining() {
		return commonremaining;
	}

	public void setCommonremaining(String commonremaining) {
		this.commonremaining = commonremaining;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
}
