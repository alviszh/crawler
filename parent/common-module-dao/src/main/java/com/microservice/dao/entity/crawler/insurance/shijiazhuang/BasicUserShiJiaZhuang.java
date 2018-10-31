package com.microservice.dao.entity.crawler.insurance.shijiazhuang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;
/**
 * 
 * @Description: 用户基本信息实体
 * @author sln
 * @date 2017年7月27日
 */
@Entity
@Table(name = "insurance_shijiazhuang_basicuser",indexes = {@Index(name = "index_insurance_shijiazhuang_basicuser_taskid", columnList = "taskid")})
public class BasicUserShiJiaZhuang extends IdEntity implements Serializable {

	private static final long serialVersionUID = 645050369714256288L;
	private String taskid; 
	private String basicuser_name;  //姓名
	private String basicuser_idnum; //身份证号
	private String basicuser_social_insur_num;  //社会保障号  （这个网站用的是身份证号）
	private String basicuser_nation;  //民族
	private String basicuser_gender; //性别
	private String basicuser_card_num;  //卡号	
	private String basicuser_mobile_phone_num;   //手机号
	private String basicuser_fix_phone_num;  //座机号
	private String basicuser_live_place; //居住地
	private String basicuser_postalode; //  居住地的邮政编码	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getBasicuser_name() {
		return basicuser_name;
	}
	public void setBasicuser_name(String basicuser_name) {
		this.basicuser_name = basicuser_name;
	}
	public String getBasicuser_idnum() {
		return basicuser_idnum;
	}
	public void setBasicuser_idnum(String basicuser_idnum) {
		this.basicuser_idnum = basicuser_idnum;
	}
	public String getBasicuser_social_insur_num() {
		return basicuser_social_insur_num;
	}
	public void setBasicuser_social_insur_num(String basicuser_social_insur_num) {
		this.basicuser_social_insur_num = basicuser_social_insur_num;
	}
	public String getBasicuser_nation() {
		return basicuser_nation;
	}
	public void setBasicuser_nation(String basicuser_nation) {
		this.basicuser_nation = basicuser_nation;
	}
	public String getBasicuser_gender() {
		return basicuser_gender;
	}
	public void setBasicuser_gender(String basicuser_gender) {
		this.basicuser_gender = basicuser_gender;
	}
	public String getBasicuser_card_num() {
		return basicuser_card_num;
	}
	public void setBasicuser_card_num(String basicuser_card_num) {
		this.basicuser_card_num = basicuser_card_num;
	}
	public String getBasicuser_mobile_phone_num() {
		return basicuser_mobile_phone_num;
	}
	public void setBasicuser_mobile_phone_num(String basicuser_mobile_phone_num) {
		this.basicuser_mobile_phone_num = basicuser_mobile_phone_num;
	}
	public String getBasicuser_fix_phone_num() {
		return basicuser_fix_phone_num;
	}
	public void setBasicuser_fix_phone_num(String basicuser_fix_phone_num) {
		this.basicuser_fix_phone_num = basicuser_fix_phone_num;
	}
	public String getBasicuser_live_place() {
		return basicuser_live_place;
	}
	public void setBasicuser_live_place(String basicuser_live_place) {
		this.basicuser_live_place = basicuser_live_place;
	}
	public String getBasicuser_postalode() {
		return basicuser_postalode;
	}
	public void setBasicuser_postalode(String basicuser_postalode) {
		this.basicuser_postalode = basicuser_postalode;
	}
	@Override
	public String toString() {
		return "BasicUserShiJiaZhuang [taskid=" + taskid + ", basicuser_name=" + basicuser_name + ", basicuser_idnum="
				+ basicuser_idnum + ", basicuser_social_insur_num=" + basicuser_social_insur_num + ", basicuser_nation="
				+ basicuser_nation + ", basicuser_gender=" + basicuser_gender + ", basicuser_card_num="
				+ basicuser_card_num + ", basicuser_mobile_phone_num=" + basicuser_mobile_phone_num
				+ ", basicuser_fix_phone_num=" + basicuser_fix_phone_num + ", basicuser_live_place="
				+ basicuser_live_place + ", basicuser_postalode=" + basicuser_postalode + "]";
	}
	public BasicUserShiJiaZhuang(String taskid, String basicuser_name, String basicuser_idnum,
			String basicuser_social_insur_num, String basicuser_nation, String basicuser_gender,
			String basicuser_card_num, String basicuser_mobile_phone_num, String basicuser_fix_phone_num,
			String basicuser_live_place, String basicuser_postalode) {
		super();
		this.taskid = taskid;
		this.basicuser_name = basicuser_name;
		this.basicuser_idnum = basicuser_idnum;
		this.basicuser_social_insur_num = basicuser_social_insur_num;
		this.basicuser_nation = basicuser_nation;
		this.basicuser_gender = basicuser_gender;
		this.basicuser_card_num = basicuser_card_num;
		this.basicuser_mobile_phone_num = basicuser_mobile_phone_num;
		this.basicuser_fix_phone_num = basicuser_fix_phone_num;
		this.basicuser_live_place = basicuser_live_place;
		this.basicuser_postalode = basicuser_postalode;
	}
	public BasicUserShiJiaZhuang() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
