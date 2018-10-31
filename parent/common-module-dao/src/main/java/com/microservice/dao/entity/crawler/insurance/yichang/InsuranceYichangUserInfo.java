package com.microservice.dao.entity.crawler.insurance.yichang;

import java.io.Serializable;

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
@Table(name = "insurance_yichang_userinfo" ,indexes = {@Index(name = "index_insurance_yichang_userinfo_taskid", columnList = "taskid")})
public class InsuranceYichangUserInfo extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6209732617640277915L;

	private String taskid;

	/**
	 * 单位名称
	 */
	private String dw_name;

	/**
	 * 公民身份证号码
	 */
	private String idcard;

	/**
	 * 个人编号
	 */
	private String gr_num;

	/**
	 * 姓　　名
	 */
	private String name;

	/**
	 * 性　　别
	 */
	private String sex;

	/**
	 * 出生日期
	 */
	private String birthday;

	/**
	 * 人员状态
	 */
	private String state;

	/**
	 * 社会保障卡卡号
	 */
	private String insurance_num;

	/**
	 * 医疗待遇类别
	 */
	private String medical_category;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDw_name() {
		return dw_name;
	}

	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getGr_num() {
		return gr_num;
	}

	public void setGr_num(String gr_num) {
		this.gr_num = gr_num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getInsurance_num() {
		return insurance_num;
	}

	public void setInsurance_num(String insurance_num) {
		this.insurance_num = insurance_num;
	}

	public String getMedical_category() {
		return medical_category;
	}

	public void setMedical_category(String medical_category) {
		this.medical_category = medical_category;
	}

	public InsuranceYichangUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InsuranceYichangUserInfo(String taskid, String dw_name, String idcard, String gr_num, String name,
			String sex, String birthday, String state, String insurance_num, String medical_category) {
		super();
		this.taskid = taskid;
		this.dw_name = dw_name;
		this.idcard = idcard;
		this.gr_num = gr_num;
		this.name = name;
		this.sex = sex;
		this.birthday = birthday;
		this.state = state;
		this.insurance_num = insurance_num;
		this.medical_category = medical_category;
	}
	
}
