package com.microservice.dao.entity.crawler.housing.handan;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 公积金-邯郸-用户信息
 * @author zz
 *
 */

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_handan_userinfo")
public class HousingHandanUserinfo extends IdEntity{
	
	private String taskid;
	private String bal;							//个人账户余额
	private String indiprop;					//个人比例
	private String accname;						//姓名
	private String indiaccstate;				//个人账户状态
	private String unitaccnum;					//未知字段
	private String indipayamt;					//个人月缴存额
	private String accnum;						//个人账号
	private String unitpayamt;					//单位月缴存额
	private String unitaccname;					//单位名称
	private String opnaccdate;					//开户日期
	private String indipaysum;					//月缴存额
	private String certinum;					//证件号码
	private String basenum;						//个人缴存基数
	private String lpaym;						//缴至年月
	private String unitprop;					//单位比例
	private String recode;						//请求结果返回状态，与数据无关
	
	public String getRecode() {
		return recode;
	}
	public void setRecode(String recode) {
		this.recode = recode;
	}
	
	@Override
	public String toString() {
		return "HousingHandanUserinfo [taskid=" + taskid + ", bal=" + bal + ", indiprop=" + indiprop + ", accname="
				+ accname + ", indiaccstate=" + indiaccstate + ", unitaccnum=" + unitaccnum + ", indipayamt="
				+ indipayamt + ", accnum=" + accnum + ", unitpayamt=" + unitpayamt + ", unitaccname=" + unitaccname
				+ ", opnaccdate=" + opnaccdate + ", indipaysum=" + indipaysum + ", certinum=" + certinum + ", basenum="
				+ basenum + ", lpaym=" + lpaym + ", unitprop=" + unitprop + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getBal() {
		return bal;
	}
	public void setBal(String bal) {
		this.bal = bal;
	}
	public String getIndiprop() {
		return indiprop;
	}
	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public String getIndiaccstate() {
		return indiaccstate;
	}
	public void setIndiaccstate(String indiaccstate) {
		this.indiaccstate = indiaccstate;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getIndipayamt() {
		return indipayamt;
	}
	public void setIndipayamt(String indipayamt) {
		this.indipayamt = indipayamt;
	}
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getUnitpayamt() {
		return unitpayamt;
	}
	public void setUnitpayamt(String unitpayamt) {
		this.unitpayamt = unitpayamt;
	}
	public String getUnitaccname() {
		return unitaccname;
	}
	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}
	public String getOpnaccdate() {
		return opnaccdate;
	}
	public void setOpnaccdate(String opnaccdate) {
		this.opnaccdate = opnaccdate;
	}
	public String getIndipaysum() {
		return indipaysum;
	}
	public void setIndipaysum(String indipaysum) {
		this.indipaysum = indipaysum;
	}
	public String getCertinum() {
		return certinum;
	}
	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}
	public String getBasenum() {
		return basenum;
	}
	public void setBasenum(String basenum) {
		this.basenum = basenum;
	}
	public String getLpaym() {
		return lpaym;
	}
	public void setLpaym(String lpaym) {
		this.lpaym = lpaym;
	}
	public String getUnitprop() {
		return unitprop;
	}
	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}
	
	

}
