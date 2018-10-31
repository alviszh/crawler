package com.microservice.dao.entity.crawler.xuexin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 学历信息
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "xuexin_education_info" ,indexes = {@Index(name = "index_xuexin_education_info_taskid", columnList = "taskid")})
public class XuexinEducationInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private String gender;

	/**
	 * 出生日期
	 */
	private String birthday;
	/**
	 * 入学日期
	 */
	private String entrance_date;
	/**
	 * 毕（结）业日期
	 */
	private String graduation_date;
	/**
	 * 学校名称
	 */
	private String school_name;
	/**
	 * 专业
	 */
	private String major;
	/**
	 * 学历类别
	 */
	private String education_category;
	/**
	 * 学制
	 */
	private String length_schooling;
	/**
	 * 学习形式
	 */
	private String study_form;
	/**
	 * 层次
	 */
	private String level;
	/**
	 * 毕（结）业
	 */
	private String graduation;
	/**
	 * 校（院）长姓名
	 */
	private String dean;
	/**
	 * 证书编号
	 */
	private String certificate_num;
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
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEntrance_date() {
		return entrance_date;
	}
	public void setEntrance_date(String entrance_date) {
		this.entrance_date = entrance_date;
	}
	public String getGraduation_date() {
		return graduation_date;
	}
	public void setGraduation_date(String graduation_date) {
		this.graduation_date = graduation_date;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getEducation_category() {
		return education_category;
	}
	public void setEducation_category(String education_category) {
		this.education_category = education_category;
	}
	public String getLength_schooling() {
		return length_schooling;
	}
	public void setLength_schooling(String length_schooling) {
		this.length_schooling = length_schooling;
	}
	public String getStudy_form() {
		return study_form;
	}
	public void setStudy_form(String study_form) {
		this.study_form = study_form;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getGraduation() {
		return graduation;
	}
	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}
	public String getDean() {
		return dean;
	}
	public void setDean(String dean) {
		this.dean = dean;
	}
	public String getCertificate_num() {
		return certificate_num;
	}
	public void setCertificate_num(String certificate_num) {
		this.certificate_num = certificate_num;
	}
	public XuexinEducationInfo(String taskid, String name, String gender, String birthday, String entrance_date,
			String graduation_date, String school_name, String major, String education_category,
			String length_schooling, String study_form, String level, String graduation, String dean,
			String certificate_num) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.entrance_date = entrance_date;
		this.graduation_date = graduation_date;
		this.school_name = school_name;
		this.major = major;
		this.education_category = education_category;
		this.length_schooling = length_schooling;
		this.study_form = study_form;
		this.level = level;
		this.graduation = graduation;
		this.dean = dean;
		this.certificate_num = certificate_num;
	}
	public XuexinEducationInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
