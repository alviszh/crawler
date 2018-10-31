package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_baseinfo",indexes = {@Index(name = "index_pro_mobile_report_baseinfo_taskid", columnList = "taskId")})
public class ProMobileReportBaseinfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String userName;
	private String idNum;
	private String basicUserName;
	private String basicIdNum;
	private String address;
	private String contactNum;
	private String phoneNum;
	private String curBalance;
	private String packageName;
	private String email;
	private String netInDate;
	private String netInDuration;
	private String points;
	private String postcode;
	private String birthday;
	private String province;
	private String city;
	private String education;
	private String hobby;
	private String occupation;
	private String gender;
	private String carrier;
	private String qq;
	private String fax;
	private String weibo;
	private String nick_name;
	private String cusLevel;
	private String cusStatus;
	private String certificateType;
	private String cusType;
	private String realName;
	private String sourceName;
	private String dataType;
	private String reportTime;
	private String idnumStatus;
	private String emailStatus;
	private String addressStatus;
	private String idnumCarrier;
	private String nameCarrier;
	private String callrecordStatus;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getBasicUserName() {
		return basicUserName;
	}
	public void setBasicUserName(String basicUserName) {
		this.basicUserName = basicUserName;
	}
	public String getBasicIdNum() {
		return basicIdNum;
	}
	public void setBasicIdNum(String basicIdNum) {
		this.basicIdNum = basicIdNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNum() {
		return contactNum;
	}
	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getCurBalance() {
		return curBalance;
	}
	public void setCurBalance(String curBalance) {
		this.curBalance = curBalance;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNetInDate() {
		return netInDate;
	}
	public void setNetInDate(String netInDate) {
		this.netInDate = netInDate;
	}
	public String getNetInDuration() {
		return netInDuration;
	}
	public void setNetInDuration(String netInDuration) {
		this.netInDuration = netInDuration;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getCusLevel() {
		return cusLevel;
	}
	public void setCusLevel(String cusLevel) {
		this.cusLevel = cusLevel;
	}
	public String getCusStatus() {
		return cusStatus;
	}
	public void setCusStatus(String cusStatus) {
		this.cusStatus = cusStatus;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getIdnumStatus() {
		return idnumStatus;
	}
	public void setIdnumStatus(String idnumStatus) {
		this.idnumStatus = idnumStatus;
	}
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	public String getAddressStatus() {
		return addressStatus;
	}
	public void setAddressStatus(String addressStatus) {
		this.addressStatus = addressStatus;
	}
	public String getIdnumCarrier() {
		return idnumCarrier;
	}
	public void setIdnumCarrier(String idnumCarrier) {
		this.idnumCarrier = idnumCarrier;
	}
	public String getNameCarrier() {
		return nameCarrier;
	}
	public void setNameCarrier(String nameCarrier) {
		this.nameCarrier = nameCarrier;
	}
	public String getCallrecordStatus() {
		return callrecordStatus;
	}
	public void setCallrecordStatus(String callrecordStatus) {
		this.callrecordStatus = callrecordStatus;
	}
	@Override
	public String toString() {
		return "ProMobileReportBaseinfo [taskId=" + taskId + ", userName=" + userName + ", idNum=" + idNum
				+ ", basicUserName=" + basicUserName + ", basicIdNum=" + basicIdNum + ", address=" + address
				+ ", contactNum=" + contactNum + ", phoneNum=" + phoneNum + ", curBalance=" + curBalance
				+ ", packageName=" + packageName + ", email=" + email + ", netInDate=" + netInDate + ", netInDuration="
				+ netInDuration + ", points=" + points + ", postcode=" + postcode + ", birthday=" + birthday
				+ ", province=" + province + ", city=" + city + ", education=" + education + ", hobby=" + hobby
				+ ", occupation=" + occupation + ", gender=" + gender + ", carrier=" + carrier + ", qq=" + qq + ", fax="
				+ fax + ", weibo=" + weibo + ", nick_name=" + nick_name + ", cusLevel=" + cusLevel + ", cusStatus="
				+ cusStatus + ", certificateType=" + certificateType + ", cusType=" + cusType + ", realName=" + realName
				+ ", sourceName=" + sourceName + ", dataType=" + dataType + ", reportTime=" + reportTime
				+ ", idnumStatus=" + idnumStatus + ", emailStatus=" + emailStatus + ", addressStatus=" + addressStatus
				+ ", idnumCarrier=" + idnumCarrier + ", nameCarrier=" + nameCarrier + ", callrecordStatus="
				+ callrecordStatus + "]";
	}
	
}
