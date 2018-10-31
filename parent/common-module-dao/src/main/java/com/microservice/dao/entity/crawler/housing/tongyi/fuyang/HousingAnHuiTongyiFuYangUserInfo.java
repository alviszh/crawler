package com.microservice.dao.entity.crawler.housing.tongyi.fuyang;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_anhui_tongyi_fuyang_persioninfo",indexes = {@Index(name = "index_housing_anhui_tongyi_fuyang_persioninfo_taskid", columnList = "taskid")})
public class HousingAnHuiTongyiFuYangUserInfo extends IdEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String DWZH; //单位账号
	private String SPZIP; //未知含义
	private String SEX; //性别
	private String HJNY;//最后汇缴年月
	private String ORGNAME;//所属管理部门
	private String LXDH;//未知
	private String YJE;//月汇缴金额
	private String GRZH;//个人账户
	private String SFDK;//是否贷款
	private String SPSBZH;//未知含义
	private String JZNY;//未知含义 
	private String SPZHICHENG;//未知含义 
	private String KHRQ;//开户日期
	private String SJHM;//未知含义 
	private String SCHJNY;//首次汇缴年月
	private String DWBL;//未知含义 
	private String LXDZ;//未知含义 
	private String GRZHYE;//余额
	private String SPMARRIAGE;//未知含义 
	private String DWMC;//单位名称
	private String GRZHZTMC;//汇缴状态
	private String SPDEGREE;//未知含义
	private String SPZHIYE;//未知含义
	private String GRJCJS;//未知含义
	private String SPIDTYPE;//未知含义
	private String YJCE;//未知含义
	private String ZJHM;//证件号码
	private String ZGBL;//未知含义
	private String SPZHIWU;//未知含义
	private String SPTEL;//未知含义
	private String SPHJSZD;//未知含义
	private String XINGMING;//姓名

	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setDWZH(String DWZH) {
		this.DWZH = DWZH;
	}

	public String getDWZH() {
		return DWZH;
	}

	public void setSPZIP(String SPZIP) {
		this.SPZIP = SPZIP;
	}

	public String getSPZIP() {
		return SPZIP;
	}

	public void setSEX(String SEX) {
		this.SEX = SEX;
	}

	public String getSEX() {
		return SEX;
	}

	public void setHJNY(String HJNY) {
		this.HJNY = HJNY;
	}

	public String getHJNY() {
		return HJNY;
	}

	public void setORGNAME(String ORGNAME) {
		this.ORGNAME = ORGNAME;
	}

	public String getORGNAME() {
		return ORGNAME;
	}

	public void setLXDH(String LXDH) {
		this.LXDH = LXDH;
	}

	public String getLXDH() {
		return LXDH;
	}

	public void setYJE(String YJE) {
		this.YJE = YJE;
	}

	public String getYJE() {
		return YJE;
	}

	public void setGRZH(String GRZH) {
		this.GRZH = GRZH;
	}

	public String getGRZH() {
		return GRZH;
	}

	public void setSFDK(String SFDK) {
		this.SFDK = SFDK;
	}

	public String getSFDK() {
		return SFDK;
	}

	public void setSPSBZH(String SPSBZH) {
		this.SPSBZH = SPSBZH;
	}

	public String getSPSBZH() {
		return SPSBZH;
	}

	public void setJZNY(String JZNY) {
		this.JZNY = JZNY;
	}

	public String getJZNY() {
		return JZNY;
	}

	public void setSPZHICHENG(String SPZHICHENG) {
		this.SPZHICHENG = SPZHICHENG;
	}

	public String getSPZHICHENG() {
		return SPZHICHENG;
	}

	public void setKHRQ(String KHRQ) {
		this.KHRQ = KHRQ;
	}

	public String getKHRQ() {
		return KHRQ;
	}

	public void setSJHM(String SJHM) {
		this.SJHM = SJHM;
	}

	public String getSJHM() {
		return SJHM;
	}

	public void setSCHJNY(String SCHJNY) {
		this.SCHJNY = SCHJNY;
	}

	public String getSCHJNY() {
		return SCHJNY;
	}

	public void setDWBL(String DWBL) {
		this.DWBL = DWBL;
	}

	public String getDWBL() {
		return DWBL;
	}

	public void setLXDZ(String LXDZ) {
		this.LXDZ = LXDZ;
	}

	public String getLXDZ() {
		return LXDZ;
	}

	public void setGRZHYE(String GRZHYE) {
		this.GRZHYE = GRZHYE;
	}

	public String getGRZHYE() {
		return GRZHYE;
	}

	public void setSPMARRIAGE(String SPMARRIAGE) {
		this.SPMARRIAGE = SPMARRIAGE;
	}

	public String getSPMARRIAGE() {
		return SPMARRIAGE;
	}

	public void setDWMC(String DWMC) {
		this.DWMC = DWMC;
	}

	public String getDWMC() {
		return DWMC;
	}

	public void setGRZHZTMC(String GRZHZTMC) {
		this.GRZHZTMC = GRZHZTMC;
	}

	public String getGRZHZTMC() {
		return GRZHZTMC;
	}

	public void setSPDEGREE(String SPDEGREE) {
		this.SPDEGREE = SPDEGREE;
	}

	public String getSPDEGREE() {
		return SPDEGREE;
	}

	public void setSPZHIYE(String SPZHIYE) {
		this.SPZHIYE = SPZHIYE;
	}

	public String getSPZHIYE() {
		return SPZHIYE;
	}

	public void setGRJCJS(String GRJCJS) {
		this.GRJCJS = GRJCJS;
	}

	public String getGRJCJS() {
		return GRJCJS;
	}

	public void setSPIDTYPE(String SPIDTYPE) {
		this.SPIDTYPE = SPIDTYPE;
	}

	public String getSPIDTYPE() {
		return SPIDTYPE;
	}

	public void setYJCE(String YJCE) {
		this.YJCE = YJCE;
	}

	public String getYJCE() {
		return YJCE;
	}

	public void setZJHM(String ZJHM) {
		this.ZJHM = ZJHM;
	}

	public String getZJHM() {
		return ZJHM;
	}

	public void setZGBL(String ZGBL) {
		this.ZGBL = ZGBL;
	}

	public String getZGBL() {
		return ZGBL;
	}

	public void setSPZHIWU(String SPZHIWU) {
		this.SPZHIWU = SPZHIWU;
	}

	public String getSPZHIWU() {
		return SPZHIWU;
	}

	public void setSPTEL(String SPTEL) {
		this.SPTEL = SPTEL;
	}

	public String getSPTEL() {
		return SPTEL;
	}

	public void setSPHJSZD(String SPHJSZD) {
		this.SPHJSZD = SPHJSZD;
	}

	public String getSPHJSZD() {
		return SPHJSZD;
	}

	public void setXINGMING(String XINGMING) {
		this.XINGMING = XINGMING;
	}

	public String getXINGMING() {
		return XINGMING;
	}

	@Override
	public String toString() {
		return "HousingAnHuiTongyiFuYangUserInfo [DWZH=" + DWZH + ", SPZIP=" + SPZIP + ", SEX=" + SEX + ", HJNY=" + HJNY
				+ ", ORGNAME=" + ORGNAME + ", LXDH=" + LXDH + ", YJE=" + YJE + ", GRZH=" + GRZH + ", SFDK=" + SFDK
				+ ", SPSBZH=" + SPSBZH + ", JZNY=" + JZNY + ", SPZHICHENG=" + SPZHICHENG + ", KHRQ=" + KHRQ + ", SJHM="
				+ SJHM + ", SCHJNY=" + SCHJNY + ", DWBL=" + DWBL + ", LXDZ=" + LXDZ + ", GRZHYE=" + GRZHYE
				+ ", SPMARRIAGE=" + SPMARRIAGE + ", DWMC=" + DWMC + ", GRZHZTMC=" + GRZHZTMC + ", SPDEGREE=" + SPDEGREE
				+ ", SPZHIYE=" + SPZHIYE + ", GRJCJS=" + GRJCJS + ", SPIDTYPE=" + SPIDTYPE + ", YJCE=" + YJCE
				+ ", ZJHM=" + ZJHM + ", ZGBL=" + ZGBL + ", SPZHIWU=" + SPZHIWU + ", SPTEL=" + SPTEL + ", SPHJSZD="
				+ SPHJSZD + ", XINGMING=" + XINGMING + ", taskid=" + taskid + "]";
	}

}
