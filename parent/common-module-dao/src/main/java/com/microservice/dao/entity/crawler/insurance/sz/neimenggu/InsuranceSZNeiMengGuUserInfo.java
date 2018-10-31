package com.microservice.dao.entity.crawler.insurance.sz.neimenggu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 由于关于用户信息响应回来的数据中有一部分是code代号，
 * 在调研的时候并没有找到，故按照如下注释中的方式处理
 * 
 * 关于民族和性别，按照其他网站的方式来
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_sz_neimenggu_userinfo",indexes = {@Index(name = "index_insurance_sz_neimenggu_userinfo_taskid", columnList = "taskid")})
public class InsuranceSZNeiMengGuUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	姓名
	private String name;
//	性别
	private String gender;
//	民族
	private String nation;
//	身份证号码
	private String idnum;
//	身份证有效期
	private String idvalidate;
//	是否长期  (从返回的代码中无法看出邮箱的激活状态，故暂时不要这个字段)
//	private String islongterm;
//	国籍   （根据响应回来的代号——中国，否则写其他国籍）
	private String nationality;
//	职业（截图关于职业只显示了数字8，响应的数据也是，在调研的时候并没有找到相关的含义，故决定暂时不要这个字段）
//	private String profession;
//	移动电话
	private String mobilephone;
//	联系电话
	private String contactnum;
//	工作电话
	private String workphone;
//	电子邮箱
	private String email;
//	激活状态   (从返回的代码中无法看出邮箱的激活状态，故暂时不要这个字段)
//	private String activatedstate;
//	自治区（省）（根据响应回来的代号——内蒙古自治区，否则就是其他）
	private String municipality;
//	盟市（市）（关于盟市，由于没有找到相关信息，故决定根据百度的11个盟市写code码及其含义）
	private String unioncity;
//	详细地址
	private String detailaddr;
//	邮政编码
	private String postalcode;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getIdvalidate() {
		return idvalidate;
	}
	public void setIdvalidate(String idvalidate) {
		this.idvalidate = idvalidate;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getContactnum() {
		return contactnum;
	}
	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}
	public String getWorkphone() {
		return workphone;
	}
	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMunicipality() {
		return municipality;
	}
	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}
	public String getUnioncity() {
		return unioncity;
	}
	public void setUnioncity(String unioncity) {
		this.unioncity = unioncity;
	}
	public String getDetailaddr() {
		return detailaddr;
	}
	public void setDetailaddr(String detailaddr) {
		this.detailaddr = detailaddr;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public InsuranceSZNeiMengGuUserInfo() {
		super();
	}
}
