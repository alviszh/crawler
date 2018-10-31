package com.microservice.dao.entity.crawler.insurance.nanning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_nanning_medical",indexes = {@Index(name = "index_insurance_nanning_medical_taskid", columnList = "taskid")}) 
public class InsuranceNanNingMedical extends IdEntity{

	private String WEB_V_AC20_OAE001;//记录编号
	private String WEB_V_AC20_AAC001;//个人编号1
	private String WEB_V_AC20_AAB004;//单位名称1
	private String  WEB_V_AC20_AAE002;//台帐年月
	private String  WEB_V_AC20_AAE003;//费款所属期1
	private String  WEB_V_AC20_AAE140;//险种类型1
	private String  WEB_V_AC20_AAE210;//款项1
	private String  WEB_V_AC20_AAE216;//基金来源
	private String  WEB_V_AC20_AAE143;//缴费类型
	private String  WEB_V_AC20_AAC150;//缴费基数
	private String  WEB_V_AC20_AAC123;//本期应缴
	private String  WEB_V_AC20_AAC130;//划入个人帐户金额1
	private String  WEB_V_AC20_AAE114;//缴费标志
	private String  WEB_V_AC20_AAE037;//到帐日期
	private String  WEB_V_AC20_AAE125;//划帐标志
	private String  WEB_V_AC20_AAE061;//补退流水号
	private String  WEB_V_AC20_AAE063;//征集通知流水号
	private String  WEB_V_AC20_AAE067;//基金配置流水号
	private String  WEB_V_AC20_AAC008;//社会保险状态
	
	private String  WEB_V_AC20_AAB001;//单位编号1
	private String  WEB_V_AC20_AAA040;//缴费比例编号
	private String  WEB_V_AC20_OAE300;//操作序号
	private String  WEB_V_AC20_OAE301;//前次操作序号
	private String  WEB_V_AC20_AAB324;//统筹区号码
	private String  WEB_V_AC20_OAE100;//数据有效标志
	
	private String taskid;

	public String getWEB_V_AC20_OAE001() {
		return WEB_V_AC20_OAE001;
	}

	public void setWEB_V_AC20_OAE001(String wEB_V_AC20_OAE001) {
		WEB_V_AC20_OAE001 = wEB_V_AC20_OAE001;
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

	public String getWEB_V_AC20_AAE002() {
		return WEB_V_AC20_AAE002;
	}

	public void setWEB_V_AC20_AAE002(String wEB_V_AC20_AAE002) {
		WEB_V_AC20_AAE002 = wEB_V_AC20_AAE002;
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

	public String getWEB_V_AC20_AAE210() {
		return WEB_V_AC20_AAE210;
	}

	public void setWEB_V_AC20_AAE210(String wEB_V_AC20_AAE210) {
		WEB_V_AC20_AAE210 = wEB_V_AC20_AAE210;
	}

	public String getWEB_V_AC20_AAE216() {
		return WEB_V_AC20_AAE216;
	}

	public void setWEB_V_AC20_AAE216(String wEB_V_AC20_AAE216) {
		WEB_V_AC20_AAE216 = wEB_V_AC20_AAE216;
	}

	public String getWEB_V_AC20_AAE143() {
		return WEB_V_AC20_AAE143;
	}

	public void setWEB_V_AC20_AAE143(String wEB_V_AC20_AAE143) {
		WEB_V_AC20_AAE143 = wEB_V_AC20_AAE143;
	}

	public String getWEB_V_AC20_AAC150() {
		return WEB_V_AC20_AAC150;
	}

	public void setWEB_V_AC20_AAC150(String wEB_V_AC20_AAC150) {
		WEB_V_AC20_AAC150 = wEB_V_AC20_AAC150;
	}

	public String getWEB_V_AC20_AAC123() {
		return WEB_V_AC20_AAC123;
	}

	public void setWEB_V_AC20_AAC123(String wEB_V_AC20_AAC123) {
		WEB_V_AC20_AAC123 = wEB_V_AC20_AAC123;
	}

	public String getWEB_V_AC20_AAC130() {
		return WEB_V_AC20_AAC130;
	}

	public void setWEB_V_AC20_AAC130(String wEB_V_AC20_AAC130) {
		WEB_V_AC20_AAC130 = wEB_V_AC20_AAC130;
	}

	public String getWEB_V_AC20_AAE114() {
		return WEB_V_AC20_AAE114;
	}

	public void setWEB_V_AC20_AAE114(String wEB_V_AC20_AAE114) {
		WEB_V_AC20_AAE114 = wEB_V_AC20_AAE114;
	}

	public String getWEB_V_AC20_AAE037() {
		return WEB_V_AC20_AAE037;
	}

	public void setWEB_V_AC20_AAE037(String wEB_V_AC20_AAE037) {
		WEB_V_AC20_AAE037 = wEB_V_AC20_AAE037;
	}

	public String getWEB_V_AC20_AAE125() {
		return WEB_V_AC20_AAE125;
	}

	public void setWEB_V_AC20_AAE125(String wEB_V_AC20_AAE125) {
		WEB_V_AC20_AAE125 = wEB_V_AC20_AAE125;
	}

	public String getWEB_V_AC20_AAE061() {
		return WEB_V_AC20_AAE061;
	}

	public void setWEB_V_AC20_AAE061(String wEB_V_AC20_AAE061) {
		WEB_V_AC20_AAE061 = wEB_V_AC20_AAE061;
	}

	public String getWEB_V_AC20_AAE063() {
		return WEB_V_AC20_AAE063;
	}

	public void setWEB_V_AC20_AAE063(String wEB_V_AC20_AAE063) {
		WEB_V_AC20_AAE063 = wEB_V_AC20_AAE063;
	}

	public String getWEB_V_AC20_AAE067() {
		return WEB_V_AC20_AAE067;
	}

	public void setWEB_V_AC20_AAE067(String wEB_V_AC20_AAE067) {
		WEB_V_AC20_AAE067 = wEB_V_AC20_AAE067;
	}

	public String getWEB_V_AC20_AAC008() {
		return WEB_V_AC20_AAC008;
	}

	public void setWEB_V_AC20_AAC008(String wEB_V_AC20_AAC008) {
		WEB_V_AC20_AAC008 = wEB_V_AC20_AAC008;
	}

	public String getWEB_V_AC20_AAB001() {
		return WEB_V_AC20_AAB001;
	}

	public void setWEB_V_AC20_AAB001(String wEB_V_AC20_AAB001) {
		WEB_V_AC20_AAB001 = wEB_V_AC20_AAB001;
	}

	public String getWEB_V_AC20_AAA040() {
		return WEB_V_AC20_AAA040;
	}

	public void setWEB_V_AC20_AAA040(String wEB_V_AC20_AAA040) {
		WEB_V_AC20_AAA040 = wEB_V_AC20_AAA040;
	}

	public String getWEB_V_AC20_OAE300() {
		return WEB_V_AC20_OAE300;
	}

	public void setWEB_V_AC20_OAE300(String wEB_V_AC20_OAE300) {
		WEB_V_AC20_OAE300 = wEB_V_AC20_OAE300;
	}

	public String getWEB_V_AC20_OAE301() {
		return WEB_V_AC20_OAE301;
	}

	public void setWEB_V_AC20_OAE301(String wEB_V_AC20_OAE301) {
		WEB_V_AC20_OAE301 = wEB_V_AC20_OAE301;
	}

	public String getWEB_V_AC20_AAB324() {
		return WEB_V_AC20_AAB324;
	}

	public void setWEB_V_AC20_AAB324(String wEB_V_AC20_AAB324) {
		WEB_V_AC20_AAB324 = wEB_V_AC20_AAB324;
	}

	public String getWEB_V_AC20_OAE100() {
		return WEB_V_AC20_OAE100;
	}

	public void setWEB_V_AC20_OAE100(String wEB_V_AC20_OAE100) {
		WEB_V_AC20_OAE100 = wEB_V_AC20_OAE100;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "InsuranceNanNingMedical [WEB_V_AC20_OAE001=" + WEB_V_AC20_OAE001 + ", WEB_V_AC20_AAC001="
				+ WEB_V_AC20_AAC001 + ", WEB_V_AC20_AAB004=" + WEB_V_AC20_AAB004 + ", WEB_V_AC20_AAE002="
				+ WEB_V_AC20_AAE002 + ", WEB_V_AC20_AAE003=" + WEB_V_AC20_AAE003 + ", WEB_V_AC20_AAE140="
				+ WEB_V_AC20_AAE140 + ", WEB_V_AC20_AAE210=" + WEB_V_AC20_AAE210 + ", WEB_V_AC20_AAE216="
				+ WEB_V_AC20_AAE216 + ", WEB_V_AC20_AAE143=" + WEB_V_AC20_AAE143 + ", WEB_V_AC20_AAC150="
				+ WEB_V_AC20_AAC150 + ", WEB_V_AC20_AAC123=" + WEB_V_AC20_AAC123 + ", WEB_V_AC20_AAC130="
				+ WEB_V_AC20_AAC130 + ", WEB_V_AC20_AAE114=" + WEB_V_AC20_AAE114 + ", WEB_V_AC20_AAE037="
				+ WEB_V_AC20_AAE037 + ", WEB_V_AC20_AAE125=" + WEB_V_AC20_AAE125 + ", WEB_V_AC20_AAE061="
				+ WEB_V_AC20_AAE061 + ", WEB_V_AC20_AAE063=" + WEB_V_AC20_AAE063 + ", WEB_V_AC20_AAE067="
				+ WEB_V_AC20_AAE067 + ", WEB_V_AC20_AAC008=" + WEB_V_AC20_AAC008 + ", WEB_V_AC20_AAB001="
				+ WEB_V_AC20_AAB001 + ", WEB_V_AC20_AAA040=" + WEB_V_AC20_AAA040 + ", WEB_V_AC20_OAE300="
				+ WEB_V_AC20_OAE300 + ", WEB_V_AC20_OAE301=" + WEB_V_AC20_OAE301 + ", WEB_V_AC20_AAB324="
				+ WEB_V_AC20_AAB324 + ", WEB_V_AC20_OAE100=" + WEB_V_AC20_OAE100 + ", taskid=" + taskid + "]";
	}
	
	
	
	
}
