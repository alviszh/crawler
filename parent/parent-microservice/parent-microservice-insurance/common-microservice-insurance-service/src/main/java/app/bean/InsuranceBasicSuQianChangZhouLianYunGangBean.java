/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Auto-generated: 2018-03-02 15:40:21
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

public class InsuranceBasicSuQianChangZhouLianYunGangBean{

	private String account;//未知
	private String name;//姓名
	private String email;//邮箱
	private String mobile;//电话
	private String idNumber;//身份证号码
	private List<String> associatedCompanys;
	private List<AssociatedPersons> associatedPersons;
	private List<String> roles;

	private String userType;
	private String headImgUrl;
	private boolean hasRealNameAuth;
	private String organization;
	private String mainCompanyNumber;
	
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

	@JsonBackReference
	@Transient
	public List<String> getAssociatedCompanys() {
		return associatedCompanys;
	}

	public void setAssociatedCompanys(List<String> associatedCompanys) {
		this.associatedCompanys = associatedCompanys;
	}

	@JsonBackReference
	@Transient
	public List<AssociatedPersons> getAssociatedPersons() {
		return associatedPersons;
	}

	public void setAssociatedPersons(List<AssociatedPersons> associatedPersons) {
		this.associatedPersons = associatedPersons;
	}

	@JsonBackReference
	@Transient
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "InsuranceBasicSuQianBean [account=" + account + ", name=" + name + ", email=" + email + ", mobile="
				+ mobile + ", idNumber=" + idNumber + ", associatedCompanys=" + associatedCompanys
				+ ", associatedPersons=" + associatedPersons + ", roles=" + roles + ", userType=" + userType
				+ ", headImgUrl=" + headImgUrl + ", hasRealNameAuth=" + hasRealNameAuth + ", organization="
				+ organization + ", mainCompanyNumber=" + mainCompanyNumber + "]";
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

}