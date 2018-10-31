package com.microservice.dao.entity.crawler.housing.eerduosi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_eerduosi_userinfo",indexes = {@Index(name = "index_housing_eerduosi_userinfo_taskid", columnList = "taskid")})
public class HousingEErDuoSiUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -192951429507078904L;
	private String taskid;
//	姓名
	private String name;
//	身份证号
	private String idnum;
//	所属管理部
	private String belongmanagedept;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitname;
//	个人账号
	private String peraccnum;
//	职工状态
	private String employeestatus;
//	职工余额
	private String employeebalance;
//	年提取次数
	private String yearextractimes;
//	年提取金额
	private String yearextractmoney;
//	最后一次提取时间
	private String lastextracttime;
//	是否存在贷款
	private String isloan;
//	是否冻结
	private String isfreeze;
//	是否担保
	private String isassure;
//	担保次数
	private String assuretimes;
//	辅助次数
	private String assistimes;
//	工资基数
	private String wagebase;
//	是否存在担保公司
	private String isexistassureunit;
//	配偶是否存在贷款
	private String isspouseloan;
//	转入单位
	private String transferunit;
//	转出销户日期
	private String cancelaccountdate;
//	单位缴至年月
	private String unitpaytoyear;
//	个人缴至年月
	private String perpaytoyear;
//	财政缴至年月
	private String financepaytoyear;
//	补充缴至年月
	private String supplementpaytoyear;
//	开户日期
	private String opendate;
//	单位缴费
	private String unitcharge;
//	个人缴费
	private String percharge;
//	财政缴费
	private String financecharge;
//	补充缴费
	private String supplementcharge;
//	月缴纳总额
	private String monthtotalcharge;
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
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getBelongmanagedept() {
		return belongmanagedept;
	}
	public void setBelongmanagedept(String belongmanagedept) {
		this.belongmanagedept = belongmanagedept;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getPeraccnum() {
		return peraccnum;
	}
	public void setPeraccnum(String peraccnum) {
		this.peraccnum = peraccnum;
	}
	public String getEmployeestatus() {
		return employeestatus;
	}
	public void setEmployeestatus(String employeestatus) {
		this.employeestatus = employeestatus;
	}
	public String getEmployeebalance() {
		return employeebalance;
	}
	public void setEmployeebalance(String employeebalance) {
		this.employeebalance = employeebalance;
	}
	public String getYearextractimes() {
		return yearextractimes;
	}
	public void setYearextractimes(String yearextractimes) {
		this.yearextractimes = yearextractimes;
	}
	public String getYearextractmoney() {
		return yearextractmoney;
	}
	public void setYearextractmoney(String yearextractmoney) {
		this.yearextractmoney = yearextractmoney;
	}
	public String getLastextracttime() {
		return lastextracttime;
	}
	public void setLastextracttime(String lastextracttime) {
		this.lastextracttime = lastextracttime;
	}
	public String getIsloan() {
		return isloan;
	}
	public void setIsloan(String isloan) {
		this.isloan = isloan;
	}
	public String getIsfreeze() {
		return isfreeze;
	}
	public void setIsfreeze(String isfreeze) {
		this.isfreeze = isfreeze;
	}
	public String getIsassure() {
		return isassure;
	}
	public void setIsassure(String isassure) {
		this.isassure = isassure;
	}
	public String getAssuretimes() {
		return assuretimes;
	}
	public void setAssuretimes(String assuretimes) {
		this.assuretimes = assuretimes;
	}
	public String getAssistimes() {
		return assistimes;
	}
	public void setAssistimes(String assistimes) {
		this.assistimes = assistimes;
	}
	public String getWagebase() {
		return wagebase;
	}
	public void setWagebase(String wagebase) {
		this.wagebase = wagebase;
	}
	public String getIsexistassureunit() {
		return isexistassureunit;
	}
	public void setIsexistassureunit(String isexistassureunit) {
		this.isexistassureunit = isexistassureunit;
	}
	public String getIsspouseloan() {
		return isspouseloan;
	}
	public void setIsspouseloan(String isspouseloan) {
		this.isspouseloan = isspouseloan;
	}
	public String getTransferunit() {
		return transferunit;
	}
	public void setTransferunit(String transferunit) {
		this.transferunit = transferunit;
	}
	public String getCancelaccountdate() {
		return cancelaccountdate;
	}
	public void setCancelaccountdate(String cancelaccountdate) {
		this.cancelaccountdate = cancelaccountdate;
	}
	public String getUnitpaytoyear() {
		return unitpaytoyear;
	}
	public void setUnitpaytoyear(String unitpaytoyear) {
		this.unitpaytoyear = unitpaytoyear;
	}
	public String getPerpaytoyear() {
		return perpaytoyear;
	}
	public void setPerpaytoyear(String perpaytoyear) {
		this.perpaytoyear = perpaytoyear;
	}
	public String getFinancepaytoyear() {
		return financepaytoyear;
	}
	public void setFinancepaytoyear(String financepaytoyear) {
		this.financepaytoyear = financepaytoyear;
	}
	public String getSupplementpaytoyear() {
		return supplementpaytoyear;
	}
	public void setSupplementpaytoyear(String supplementpaytoyear) {
		this.supplementpaytoyear = supplementpaytoyear;
	}
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getUnitcharge() {
		return unitcharge;
	}
	public void setUnitcharge(String unitcharge) {
		this.unitcharge = unitcharge;
	}
	public String getPercharge() {
		return percharge;
	}
	public void setPercharge(String percharge) {
		this.percharge = percharge;
	}
	public String getFinancecharge() {
		return financecharge;
	}
	public void setFinancecharge(String financecharge) {
		this.financecharge = financecharge;
	}
	public String getSupplementcharge() {
		return supplementcharge;
	}
	public void setSupplementcharge(String supplementcharge) {
		this.supplementcharge = supplementcharge;
	}
	public String getMonthtotalcharge() {
		return monthtotalcharge;
	}
	public void setMonthtotalcharge(String monthtotalcharge) {
		this.monthtotalcharge = monthtotalcharge;
	}
	public HousingEErDuoSiUserInfo() {
		super();
	}
	public HousingEErDuoSiUserInfo(String taskid, String name, String idnum, String belongmanagedept, String unitaccnum,
			String unitname, String peraccnum, String employeestatus, String employeebalance, String yearextractimes,
			String yearextractmoney, String lastextracttime, String isloan, String isfreeze, String isassure,
			String assuretimes, String assistimes, String wagebase, String isexistassureunit, String isspouseloan,
			String transferunit, String cancelaccountdate, String unitpaytoyear, String perpaytoyear,
			String financepaytoyear, String supplementpaytoyear, String opendate, String unitcharge, String percharge,
			String financecharge, String supplementcharge, String monthtotalcharge) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idnum = idnum;
		this.belongmanagedept = belongmanagedept;
		this.unitaccnum = unitaccnum;
		this.unitname = unitname;
		this.peraccnum = peraccnum;
		this.employeestatus = employeestatus;
		this.employeebalance = employeebalance;
		this.yearextractimes = yearextractimes;
		this.yearextractmoney = yearextractmoney;
		this.lastextracttime = lastextracttime;
		this.isloan = isloan;
		this.isfreeze = isfreeze;
		this.isassure = isassure;
		this.assuretimes = assuretimes;
		this.assistimes = assistimes;
		this.wagebase = wagebase;
		this.isexistassureunit = isexistassureunit;
		this.isspouseloan = isspouseloan;
		this.transferunit = transferunit;
		this.cancelaccountdate = cancelaccountdate;
		this.unitpaytoyear = unitpaytoyear;
		this.perpaytoyear = perpaytoyear;
		this.financepaytoyear = financepaytoyear;
		this.supplementpaytoyear = supplementpaytoyear;
		this.opendate = opendate;
		this.unitcharge = unitcharge;
		this.percharge = percharge;
		this.financecharge = financecharge;
		this.supplementcharge = supplementcharge;
		this.monthtotalcharge = monthtotalcharge;
	}
	
}
