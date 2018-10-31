package com.microservice.dao.entity.crawler.xuexin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 学籍信息
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "xuexin_school_info" ,indexes = {@Index(name = "index_xuexin_school_info_taskid", columnList = "taskid")})
public class XuexinSchoolInfo extends IdEntity {

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
	 * 民族
	 */
	private String nation;

	/**
	 * 证件号码
	 */
	private String idnumber;

	/**
	 * 学校名称
	 */
	private String school_name;

	/**
	 * 层次
	 */
	private String level;

	/**
	 * 专业
	 */
	private String major;
	
	/**
	 * 学制
	 */
	private String length_schooling;
	/**
	 * 学历类别
	 */
	private String education_category;
	/**
	 * 学习形式
	 */
	private String study_form;
	/**
	 * 分院
	 */
	private String branch;
	/**
	 * 系（所、函授站）
	 */
	private String system_place;
	/**
	 * 班级
	 */
	private String class_place;
	/**
	 * 学号
	 */
	private String study_num;
	/**
	 * 入学日期
	 */
	private String entrance_date;
	/**
	 * 离校日期
	 */
	private String leave_school_date;
	/**
	 * 学籍状态
	 */
	private String status;
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
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getLength_schooling() {
		return length_schooling;
	}
	public void setLength_schooling(String length_schooling) {
		this.length_schooling = length_schooling;
	}
	public String getEducation_category() {
		return education_category;
	}
	public void setEducation_category(String education_category) {
		this.education_category = education_category;
	}
	public String getStudy_form() {
		return study_form;
	}
	public void setStudy_form(String study_form) {
		this.study_form = study_form;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getSystem_place() {
		return system_place;
	}
	public void setSystem_place(String system_place) {
		this.system_place = system_place;
	}
	public String getClass_place() {
		return class_place;
	}
	public void setClass_place(String class_place) {
		this.class_place = class_place;
	}
	public String getStudy_num() {
		return study_num;
	}
	public void setStudy_num(String study_num) {
		this.study_num = study_num;
	}
	public String getEntrance_date() {
		return entrance_date;
	}
	public void setEntrance_date(String entrance_date) {
		this.entrance_date = entrance_date;
	}
	public String getLeave_school_date() {
		return leave_school_date;
	}
	public void setLeave_school_date(String leave_school_date) {
		this.leave_school_date = leave_school_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public XuexinSchoolInfo(String taskid, String name, String gender, String birthday, String nation, String idnumber,
			String school_name, String level, String major, String length_schooling, String education_category,
			String study_form, String branch, String system_place, String class_place, String study_num,
			String entrance_date, String leave_school_date, String status) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.nation = nation;
		this.idnumber = idnumber;
		this.school_name = school_name;
		this.level = level;
		this.major = major;
		this.length_schooling = length_schooling;
		this.education_category = education_category;
		this.study_form = study_form;
		this.branch = branch;
		this.system_place = system_place;
		this.class_place = class_place;
		this.study_num = study_num;
		this.entrance_date = entrance_date;
		this.leave_school_date = leave_school_date;
		this.status = status;
	}
	public XuexinSchoolInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
