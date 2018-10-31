package com.microservice.dao.entity.crawler.insurance.sz.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_jilin_userinfo",indexes = {@Index(name = "index_insurance_sz_jilin_userinfo_taskid", columnList = "taskid")})
public class InsuranceSZJiLinUserInfo extends IdEntity{

	private String taskid;
	private String name;
	private String sex;
	private String idcard;
	private String birth;//出生日期
	private String identity;//身份
	private String employment;//用工形式
	private String state;//人员状态
	private String peasant_worker_identification;//农民工标识
	private String dw_num;
	private String dw_name;
	private String creation_time;//养老建账时间
	private String join_time;//参工时间
	private String yanglao_insurance_time;//养老参保时间
	private String shiye_insurance_time;//失业参保时间
	private String yanglao_insurance_state;//养老参保状态
	private String shiye_insurance_state;//失业参保状态
	private String yanglao_pay_state;//养老缴费状态
	private String shiye_pay_state;//失业缴费状态
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getEmployment() {
		return employment;
	}
	public void setEmployment(String employment) {
		this.employment = employment;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPeasant_worker_identification() {
		return peasant_worker_identification;
	}
	public void setPeasant_worker_identification(String peasant_worker_identification) {
		this.peasant_worker_identification = peasant_worker_identification;
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
	public String getCreation_time() {
		return creation_time;
	}
	public void setCreation_time(String creation_time) {
		this.creation_time = creation_time;
	}
	public String getJoin_time() {
		return join_time;
	}
	public void setJoin_time(String join_time) {
		this.join_time = join_time;
	}
	public String getYanglao_insurance_time() {
		return yanglao_insurance_time;
	}
	public void setYanglao_insurance_time(String yanglao_insurance_time) {
		this.yanglao_insurance_time = yanglao_insurance_time;
	}
	public String getShiye_insurance_time() {
		return shiye_insurance_time;
	}
	public void setShiye_insurance_time(String shiye_insurance_time) {
		this.shiye_insurance_time = shiye_insurance_time;
	}
	public String getYanglao_insurance_state() {
		return yanglao_insurance_state;
	}
	public void setYanglao_insurance_state(String yanglao_insurance_state) {
		this.yanglao_insurance_state = yanglao_insurance_state;
	}
	public String getShiye_insurance_state() {
		return shiye_insurance_state;
	}
	public void setShiye_insurance_state(String shiye_insurance_state) {
		this.shiye_insurance_state = shiye_insurance_state;
	}
	public String getYanglao_pay_state() {
		return yanglao_pay_state;
	}
	public void setYanglao_pay_state(String yanglao_pay_state) {
		this.yanglao_pay_state = yanglao_pay_state;
	}
	public String getShiye_pay_state() {
		return shiye_pay_state;
	}
	public void setShiye_pay_state(String shiye_pay_state) {
		this.shiye_pay_state = shiye_pay_state;
	}
	public InsuranceSZJiLinUserInfo(String taskid, String name, String sex, String idcard, String birth,
			String identity, String employment, String state, String peasant_worker_identification, String dw_num,
			String dw_name, String creation_time, String join_time, String yanglao_insurance_time,
			String shiye_insurance_time, String yanglao_insurance_state, String shiye_insurance_state,
			String yanglao_pay_state, String shiye_pay_state) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.sex = sex;
		this.idcard = idcard;
		this.birth = birth;
		this.identity = identity;
		this.employment = employment;
		this.state = state;
		this.peasant_worker_identification = peasant_worker_identification;
		this.dw_num = dw_num;
		this.dw_name = dw_name;
		this.creation_time = creation_time;
		this.join_time = join_time;
		this.yanglao_insurance_time = yanglao_insurance_time;
		this.shiye_insurance_time = shiye_insurance_time;
		this.yanglao_insurance_state = yanglao_insurance_state;
		this.shiye_insurance_state = shiye_insurance_state;
		this.yanglao_pay_state = yanglao_pay_state;
		this.shiye_pay_state = shiye_pay_state;
	}
	public InsuranceSZJiLinUserInfo() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceBaiShanUserInfo [taskid=" + taskid + ", name=" + name + ", sex=" + sex + ", idcard=" + idcard
				+ ", birth=" + birth + ", identity=" + identity + ", employment=" + employment + ", state=" + state
				+ ", peasant_worker_identification=" + peasant_worker_identification + ", dw_num=" + dw_num
				+ ", dw_name=" + dw_name + ", creation_time=" + creation_time + ", join_time=" + join_time
				+ ", yanglao_insurance_time=" + yanglao_insurance_time + ", shiye_insurance_time="
				+ shiye_insurance_time + ", yanglao_insurance_state=" + yanglao_insurance_state
				+ ", shiye_insurance_state=" + shiye_insurance_state + ", yanglao_pay_state=" + yanglao_pay_state
				+ ", shiye_pay_state=" + shiye_pay_state + "]";
	}
	
	
	
}
