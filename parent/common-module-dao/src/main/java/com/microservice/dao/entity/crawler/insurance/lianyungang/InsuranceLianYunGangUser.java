package com.microservice.dao.entity.crawler.insurance.lianyungang;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 
 * 项目名称：common-microservice-insurance-lianyungang 类名称：InsuranceLianYunGangPay
 * 类描述： 创建人：hyx 创建时间：2018年3月21日 下午2:24:36
 * 
 * @version
 */
@Entity
@Table(name = "insurance_lianyungang_user",indexes = {@Index(name = "index_insurance_lianyungang_user_taskid", columnList = "taskid")})
public class InsuranceLianYunGangUser extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String account;// 社保卡号
	private String name;// 姓名
	private String email;// 邮箱
	private String mobile;// 电话
	private String idNumber;// 身份证号码

	private String userType;//性别
	private String headImgUrl;
	private boolean hasRealNameAuth;
	private String organization;
	private String mainCompanyNumber;
	
	private String taskid;
	
	

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public boolean isHasRealNameAuth() {
		return hasRealNameAuth;
	}

	public void setHasRealNameAuth(boolean hasRealNameAuth) {
		this.hasRealNameAuth = hasRealNameAuth;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getMainCompanyNumber() {
		return mainCompanyNumber;
	}

	public void setMainCompanyNumber(String mainCompanyNumber) {
		this.mainCompanyNumber = mainCompanyNumber;
	}

	@Override
	public String toString() {
		return "InsuranceLianYunGangUser [account=" + account + ", name=" + name + ", email=" + email + ", mobile="
				+ mobile + ", idNumber=" + idNumber + ", userType=" + userType + ", headImgUrl=" + headImgUrl
				+ ", hasRealNameAuth=" + hasRealNameAuth + ", organization=" + organization + ", mainCompanyNumber="
				+ mainCompanyNumber + ", taskid=" + taskid + "]";
	}

}
