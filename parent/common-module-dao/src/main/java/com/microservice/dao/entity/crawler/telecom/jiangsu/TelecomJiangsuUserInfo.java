package com.microservice.dao.entity.crawler.telecom.jiangsu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_jiangsu_userinfo" ,indexes = {@Index(name = "index_telecom_jiangsu_userinfo_taskid", columnList = "taskid")})
public class TelecomJiangsuUserInfo extends IdEntity {

	/**
	 * taskid
	 */
	private String taskid;
	/**
	 * 登录帐号
	 */
	private String productId;
	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * 所在地区 
	 */
	private String areaCode;
	/**
	 * 客户姓名
	 */
	private String customer_name;
	/**
	 * 证件类型
	 */
	private String document_type;
	/**
	 * 证件号码
	 */
	private String document_code;
	/**
	 * 通讯地址
	 */
	private String userAddress;
	/**
	 * 通讯编码
	 */
	private String zipCode;
	/**
	 * 联系人
	 */
	private String contactPerson;
	/**
	 * 联系电话
	 */
	private String contactPhone;
	/**
	 * 电子邮件
	 */
	private String email;
	/**
	 * 余额
	 */
	private String blance;

	/**
	 * 积分
	 */
	private String scores;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getDocument_type() {
		return document_type;
	}

	public void setDocument_type(String document_type) {
		this.document_type = document_type;
	}

	public String getDocument_code() {
		return document_code;
	}

	public void setDocument_code(String document_code) {
		this.document_code = document_code;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBlance() {
		return blance;
	}

	public void setBlance(String blance) {
		this.blance = blance;
	}

	public String getScores() {
		return scores;
	}

	public void setScores(String scores) {
		this.scores = scores;
	}
	

	public TelecomJiangsuUserInfo(String taskid, String productId, String userName, String areaCode,
			String customer_name, String document_type, String document_code, String userAddress, String zipCode,
			String contactPerson, String contactPhone, String email, String blance, String scores) {
		super();
		this.taskid = taskid;
		this.productId = productId;
		this.userName = userName;
		this.areaCode = areaCode;
		this.customer_name = customer_name;
		this.document_type = document_type;
		this.document_code = document_code;
		this.userAddress = userAddress;
		this.zipCode = zipCode;
		this.contactPerson = contactPerson;
		this.contactPhone = contactPhone;
		this.email = email;
		this.blance = blance;
		this.scores = scores;
	}

	public TelecomJiangsuUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	


}