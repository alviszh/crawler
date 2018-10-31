package com.microservice.dao.entity.crawler.insurance.luohe;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 用户信息
 * @author Administrator
 *
 */
@Entity
@Table(name = "insurance_luohe_userinfo" ,indexes = {@Index(name = "index_insurance_luohe_userinfo_taskid", columnList = "taskid")})
public class InsuranceLuoheUserInfo extends IdEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3270137427751782013L;

	private String taskid;

	/**
	 * 个人编号
	 */
	private String gr_num;

	/**
	 * 职工姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 民族
	 */
	private String nation;

	/**
	 * 身份证号
	 */
	private String idcard;

	/**
	 * 出生日期
	 */
	private String birthday;

	/**
	 * 参加工作时间
	 */
	private String insuredtime;

	/**
	 * 人员状态
	 */
	private String state;

	/**
	 * 单位编号
	 */
	private String dw_num;
	/**
	 * 单位名称
	 */
	private String dw_name;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getInsuredtime() {
		return insuredtime;
	}
	public void setInsuredtime(String insuredtime) {
		this.insuredtime = insuredtime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDw_num() {
		return dw_num;
	}
	public void setDw_num(String dw_num) {
		this.dw_num = dw_num;
	}
	public String getDw_name() {
		return dw_name;
	}
	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}
	public InsuranceLuoheUserInfo(String taskid, String gr_num, String name, String sex, String nation, String idcard,
			String birthday, String insuredtime, String state, String dw_num, String dw_name) {
		super();
		this.taskid = taskid;
		this.gr_num = gr_num;
		this.name = name;
		this.sex = sex;
		this.nation = nation;
		this.idcard = idcard;
		this.birthday = birthday;
		this.insuredtime = insuredtime;
		this.state = state;
		this.dw_num = dw_num;
		this.dw_name = dw_name;
	}
	public InsuranceLuoheUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
