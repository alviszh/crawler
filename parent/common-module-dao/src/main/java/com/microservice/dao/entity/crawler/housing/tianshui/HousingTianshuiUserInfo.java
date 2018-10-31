package com.microservice.dao.entity.crawler.housing.tianshui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 * @author tz
 *
 */
@Entity
@Table(name = "housing_tianshui_userinfo" ,indexes = {@Index(name = "index_housing_tianshui_userinfo_taskid", columnList = "taskid")})
public class HousingTianshuiUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 个人编号
	 */
	private String percode;

	/**
	 * 个人姓名
	 */
	private String pername;

	/**
	 * 证件类型
	 * {"id":"01","name":"身份证"},
	 * {"id":"02","name":"军官证"},
	 * {"id":"03","name":"护照"},
	 * {"id":"04","name":"外籍"},
	 * {"id":"05","name":"警官证"},
	 * {"id":"06","name":"士兵证"},
	 * {"id":"07","name":"港澳台身份证"},
	 * {"id":"98","name":"数据迁移无"},
	 * {"id":"99","name":"其他"}
	 */
	private String codetype;

	/**
	 * 证件号码
	 */
	private String codeno;

	/**
	 * 性别
	 * {"id":"0","name":"未知的性别"},
	 * {"id":"1","name":"男性"},
	 * {"id":"2","name":"女性"},
	 * {"id":"9","name":"未说明的性别"}
	 */
	private String sex;

	/**
	 * 出生日期
	 */
	private String birthday;

	/**
	 * 个人邮箱
	 */
	private String email;

	/**
	 * 移动电话
	 */
	private String phone;

	/**
	 * 民族
	 * {"id":"01","name":"汉族"},
	 * {"id":"99","name":"其他"}
	 */
	private String nation;
	/**
	 * 国籍
	 * {"id":"01","name":"中国"},
	 * {"id":"99","name":"其他"}
	 */
	private String country;
	/**
	 * 文化程度
	 * {"id":"10","name":"研究生教育"},
	 * {"id":"20","name":"大学本科教育"},
	 * {"id":"30","name":"大学专科教育"},
	 * {"id":"40","name":"中等职业教育"},
	 * {"id":"50","name":"高中以下"},
	 * {"id":"60","name":"普通高级中学教育"},
	 * {"id":"90","name":"其他"}
	 */
	private String edulev;
	/**
	 * 婚姻状况
	 * {"id":"10","name":"未婚"},
	 * {"id":"20","name":"已婚"},
	 * {"id":"21","name":"初婚"},
	 * {"id":"22","name":"再婚"},
	 * {"id":"23","name":"复婚"},
	 * {"id":"30","name":"丧偶"},
	 * {"id":"40","name":"离婚"},
	 * {"id":"90","name":"未说明的婚姻情况"}
	 */
	private String marstate;
	/**
	 * 邮政编码
	 */
	private String postcode;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPercode() {
		return percode;
	}
	public void setPercode(String percode) {
		this.percode = percode;
	}
	public String getPername() {
		return pername;
	}
	public void setPername(String pername) {
		this.pername = pername;
	}
	public String getCodetype() {
		return codetype;
	}
	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}
	public String getCodeno() {
		return codeno;
	}
	public void setCodeno(String codeno) {
		this.codeno = codeno;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEdulev() {
		return edulev;
	}
	public void setEdulev(String edulev) {
		this.edulev = edulev;
	}
	public String getMarstate() {
		return marstate;
	}
	public void setMarstate(String marstate) {
		this.marstate = marstate;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public HousingTianshuiUserInfo(String taskid, String percode, String pername, String codetype, String codeno,
			String sex, String birthday, String email, String phone, String nation, String country, String edulev,
			String marstate, String postcode) {
		super();
		this.taskid = taskid;
		this.percode = percode;
		this.pername = pername;
		this.codetype = codetype;
		this.codeno = codeno;
		this.sex = sex;
		this.birthday = birthday;
		this.email = email;
		this.phone = phone;
		this.nation = nation;
		this.country = country;
		this.edulev = edulev;
		this.marstate = marstate;
		this.postcode = postcode;
	}
	public HousingTianshuiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
