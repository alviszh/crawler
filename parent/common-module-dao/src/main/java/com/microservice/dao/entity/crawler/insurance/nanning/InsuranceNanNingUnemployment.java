package com.microservice.dao.entity.crawler.insurance.nanning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_nanning_unemployment",indexes = {@Index(name = "index_insurance_nanning_unemployment_taskid", columnList = "taskid")}) 
public class InsuranceNanNingUnemployment extends IdEntity{

	private String  WEB_V_AC20_AAB001;//单位编号1
	private String WEB_V_AC20_AAC001;//个人编号1
	private String WEB_V_AC20_AAB004;//单位名称1
	private String  WEB_V_AC20_AAE003;//费款所属期1
	private String  WEB_V_AC20_AAE140;//险种类型1
	private String  WEB_V_AC20_AAC130;//划入个人帐户金额1
	private String  WEB_V_AC20_AAE210;//款项1
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceNanNingEndowment [WEB_V_AC20_AAB001=" + WEB_V_AC20_AAB001 + ", WEB_V_AC20_AAC001="
				+ WEB_V_AC20_AAC001 + ", WEB_V_AC20_AAB004=" + WEB_V_AC20_AAB004 + ", WEB_V_AC20_AAE003="
				+ WEB_V_AC20_AAE003 + ", WEB_V_AC20_AAE140=" + WEB_V_AC20_AAE140 + ", WEB_V_AC20_AAC130="
				+ WEB_V_AC20_AAC130 + ", WEB_V_AC20_AAE210=" + WEB_V_AC20_AAE210 + ", taskid=" + taskid + "]";
	}
	public String getWEB_V_AC20_AAB001() {
		return WEB_V_AC20_AAB001;
	}
	public void setWEB_V_AC20_AAB001(String wEB_V_AC20_AAB001) {
		WEB_V_AC20_AAB001 = wEB_V_AC20_AAB001;
	}
	public String getWEB_V_AC20_AAC001() {
		return WEB_V_AC20_AAC001;
	}
	public void setWEB_V_AC20_AAC001(String wEB_V_AC20_AAC001) {
		WEB_V_AC20_AAC001 = wEB_V_AC20_AAC001;
	}
	public String getWEB_V_AC20_AAB004() {
		return WEB_V_AC20_AAB004;
	}
	public void setWEB_V_AC20_AAB004(String wEB_V_AC20_AAB004) {
		WEB_V_AC20_AAB004 = wEB_V_AC20_AAB004;
	}
	public String getWEB_V_AC20_AAE003() {
		return WEB_V_AC20_AAE003;
	}
	public void setWEB_V_AC20_AAE003(String wEB_V_AC20_AAE003) {
		WEB_V_AC20_AAE003 = wEB_V_AC20_AAE003;
	}
	public String getWEB_V_AC20_AAE140() {
		return WEB_V_AC20_AAE140;
	}
	public void setWEB_V_AC20_AAE140(String wEB_V_AC20_AAE140) {
		WEB_V_AC20_AAE140 = wEB_V_AC20_AAE140;
	}
	public String getWEB_V_AC20_AAC130() {
		return WEB_V_AC20_AAC130;
	}
	public void setWEB_V_AC20_AAC130(String wEB_V_AC20_AAC130) {
		WEB_V_AC20_AAC130 = wEB_V_AC20_AAC130;
	}
	public String getWEB_V_AC20_AAE210() {
		return WEB_V_AC20_AAE210;
	}
	public void setWEB_V_AC20_AAE210(String wEB_V_AC20_AAE210) {
		WEB_V_AC20_AAE210 = wEB_V_AC20_AAE210;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
