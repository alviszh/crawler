package com.microservice.dao.entity.crawler.insurance.sz.sichuan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_sichuan_information")
public class InsuranceSZSichuanInformation extends IdEntity{
	
	private String taskid;
	
	@Column(name="insured_status")
	private String AAC008;					//参保状态
	
	private String AAC007;					//未知字段（大部分为空）	

	@Column(name="end_date")
	private String AAE031;					//终止日期
	
	@Column(name="pay_wage")
	private String AAC040;					//缴费工资
	
	@Column(name="pay_stauts")
	private String AAC031;					//缴费状态
	
	private String AAB001;					//未知字段（例：AAB001=1000000481）
	
	@Column(name="idnum")
	private String AAC002;					//身份证号
	
	@Column(name="personne_type")
	private String YAC505;					//人员类型（例：YAC505=城镇普通人员）
	
	@Column(name="insured_type")
	private String AAE140;					//参保险种
	
	@Column(name="company_name")
	private String AAB044;					//单位名称
	
	@Column(name="start_date")
	private String AAC049;					//初次参保时间

	public String getTaskid() {
		return taskid;
	}
	
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public String getAAC008() {
		return AAC008;
	}
	
	@Override
	public String toString() {
		return "InsuranceSZSichuanInformation [taskid=" + taskid + ", AAC008=" + AAC008 + ", AAC007=" + AAC007
				+ ", AAE031=" + AAE031 + ", AAC040=" + AAC040 + ", AAC031=" + AAC031 + ", AAB001=" + AAB001
				+ ", AAC002=" + AAC002 + ", YAC505=" + YAC505 + ", AAE140=" + AAE140 + ", AAB044=" + AAB044
				+ ", AAC049=" + AAC049 + "]";
	}

	public void setAAC008(String aAC008) {
		AAC008 = aAC008;
	}
	
	public String getAAC007() {
		return AAC007;
	}
	
	public void setAAC007(String aAC007) {
		AAC007 = aAC007;
	}
	
	public String getAAE031() {
		return AAE031;
	}
	
	public void setAAE031(String aAE031) {
		AAE031 = aAE031;
	}
	
	public String getAAC040() {
		return AAC040;
	}
	
	public void setAAC040(String aAC040) {
		AAC040 = aAC040;
	}
	
	public String getAAC031() {
		return AAC031;
	}
	
	public void setAAC031(String aAC031) {
		AAC031 = aAC031;
	}
	
	public String getAAB001() {
		return AAB001;
	}
	
	public void setAAB001(String aAB001) {
		AAB001 = aAB001;
	}
	
	public String getAAC002() {
		return AAC002;
	}
	
	public void setAAC002(String aAC002) {
		AAC002 = aAC002;
	}
	
	public String getYAC505() {
		return YAC505;
	}
	
	public void setYAC505(String yAC505) {
		YAC505 = yAC505;
	}
	
	public String getAAE140() {
		return AAE140;
	}
	
	public void setAAE140(String aAE140) {
		if(aAE140.equals("110")){
			AAE140 = "职工基本养老保险";
		}else if(aAE140.equals("210")){
			AAE140 = "失业保险";
		}else if(aAE140.equals("310")){
			AAE140 = "职工基本医疗保险";
		}else if(aAE140.equals("410")){
			AAE140 = "工伤保险";
		}else{
			AAE140 = aAE140;			
		}
			
	}
	
	public String getAAB044() {
		return AAB044;
	}
	
	public void setAAB044(String aAB044) {
		AAB044 = aAB044;
	}
	
	public String getAAC049() {
		return AAC049;
	}
	
	public void setAAC049(String aAC049) {
		AAC049 = aAC049;
	}
}
