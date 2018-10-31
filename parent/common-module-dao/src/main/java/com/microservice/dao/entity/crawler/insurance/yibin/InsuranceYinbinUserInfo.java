package com.microservice.dao.entity.crawler.insurance.yibin;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
/**
 * 宜宾社保个人信息
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_yibin_userinfo")
public class InsuranceYinbinUserInfo extends IdEntity {
	private String useraccount; // 个人编号
	private String username; // 姓名
	private String sex; // 性别
	private String idnum; // 身份证
	private String birthdate; // 出生日期
	private String firstdate; //参工时间
	private String taskid; // 任务ID
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getFirstdate() {
		return firstdate;
	}
	public void setFirstdate(String firstdate) {
		this.firstdate = firstdate;
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceYinbinUserInfo [useraccount=" + useraccount + ", username=" + username + ", sex=" + sex
				+ ", idnum=" + idnum + ", birthdate=" + birthdate + ", firstdate=" + firstdate + ", taskid=" + taskid
				+ "]";
	}
	public InsuranceYinbinUserInfo() {
		super();
	}
	public InsuranceYinbinUserInfo(String useraccount, String username, String sex, String idnum, String birthdate,
			String firstdate, String taskid) {
		super();
		this.useraccount = useraccount;
		this.username = username;
		this.sex = sex;
		this.idnum = idnum;
		this.birthdate = birthdate;
		this.firstdate = firstdate;
		this.taskid = taskid;
	}
}
