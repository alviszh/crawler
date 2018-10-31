package com.microservice.dao.entity.crawler.housing.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 单位信息
 * @author: sln 
 * @date: 2017年10月25日 上午11:42:39 
 */
@Entity
@Table(name="housing_qingdao_compinfo",indexes = {@Index(name = "index_housing_qingdao_compinfo_taskid", columnList = "taskid")})
public class HousingQingDaoCompInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4468553150310541791L;
	private String taskid;
//	单位编号
	private String compnum;
//	单位名称
	private String compname;
//	单位地址
	private String compaddress;
//	经办部门
	private String managedept;
//	所在市区
	private String downtownarea;
//	成立日期
	private String setupdate;
//	组织机构代码
	private String unitcode;
//	单位性质
	private String compproperty;
//	营业执照编号
	private String licensenum;
//	法人资格
	private String legalpersonality;
//	法人代表
	private String corporaterepresentative;
//	发薪日
	private String payday;
//	主管单位
	private String competentorganization;
//	单位传真
	private String compfax;
//	单位邮编
	private String compostcode;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCompnum() {
		return compnum;
	}
	public void setCompnum(String compnum) {
		this.compnum = compnum;
	}
	public String getCompname() {
		return compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getCompaddress() {
		return compaddress;
	}
	public void setCompaddress(String compaddress) {
		this.compaddress = compaddress;
	}
	public String getManagedept() {
		return managedept;
	}
	public void setManagedept(String managedept) {
		this.managedept = managedept;
	}
	public String getDowntownarea() {
		return downtownarea;
	}
	public void setDowntownarea(String downtownarea) {
		this.downtownarea = downtownarea;
	}
	public String getSetupdate() {
		return setupdate;
	}
	public void setSetupdate(String setupdate) {
		this.setupdate = setupdate;
	}
	public String getUnitcode() {
		return unitcode;
	}
	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}
	public String getCompproperty() {
		return compproperty;
	}
	public void setCompproperty(String compproperty) {
		this.compproperty = compproperty;
	}
	public String getLicensenum() {
		return licensenum;
	}
	public void setLicensenum(String licensenum) {
		this.licensenum = licensenum;
	}
	public String getLegalpersonality() {
		return legalpersonality;
	}
	public void setLegalpersonality(String legalpersonality) {
		this.legalpersonality = legalpersonality;
	}
	public String getCorporaterepresentative() {
		return corporaterepresentative;
	}
	public void setCorporaterepresentative(String corporaterepresentative) {
		this.corporaterepresentative = corporaterepresentative;
	}
	public String getPayday() {
		return payday;
	}
	public void setPayday(String payday) {
		this.payday = payday;
	}
	public String getCompetentorganization() {
		return competentorganization;
	}
	public void setCompetentorganization(String competentorganization) {
		this.competentorganization = competentorganization;
	}
	public String getCompfax() {
		return compfax;
	}
	public void setCompfax(String compfax) {
		this.compfax = compfax;
	}
	public String getCompostcode() {
		return compostcode;
	}
	public void setCompostcode(String compostcode) {
		this.compostcode = compostcode;
	}
	public HousingQingDaoCompInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingQingDaoCompInfo(String taskid, String compnum, String compname, String compaddress, String managedept,
			String downtownarea, String setupdate, String unitcode, String compproperty, String licensenum,
			String legalpersonality, String corporaterepresentative, String payday, String competentorganization,
			String compfax, String compostcode) {
		super();
		this.taskid = taskid;
		this.compnum = compnum;
		this.compname = compname;
		this.compaddress = compaddress;
		this.managedept = managedept;
		this.downtownarea = downtownarea;
		this.setupdate = setupdate;
		this.unitcode = unitcode;
		this.compproperty = compproperty;
		this.licensenum = licensenum;
		this.legalpersonality = legalpersonality;
		this.corporaterepresentative = corporaterepresentative;
		this.payday = payday;
		this.competentorganization = competentorganization;
		this.compfax = compfax;
		this.compostcode = compostcode;
	}
	
}
