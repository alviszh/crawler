package com.microservice.dao.entity.crawler.housing.shijiazhuang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:43:28 
 */
@Entity
@Table(name="housing_shijiazhuang_userinfo",indexes = {@Index(name = "index_housing_shijiazhuang_userinfo_taskid", columnList = "taskid")})
public class HousingShiJiaZhuangUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 7667455662058760759L;
	private String taskid;
//	个人姓名
	private String accname;
//	个人账号
	private String accnum;
//	身份证号
	private String certinum;
//	开户日期
	private String openaccountdate;
//	公积金余额
	private String balance;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitaccname;
//	缴存基数
	private String chargebasenum;
//	月缴存额
	private String chargemonth;
//	缴至年月
	private String chargetodate;
	//状态码
	private String statuscode;
//	状态（根据状态码判断：有四种类：正常、封存、空账、销户）
	private String status;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getCertinum() {
		return certinum;
	}
	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}
	public String getOpenaccountdate() {
		return openaccountdate;
	}
	public void setOpenaccountdate(String openaccountdate) {
		this.openaccountdate = openaccountdate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getUnitaccname() {
		return unitaccname;
	}
	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}
	public String getChargebasenum() {
		return chargebasenum;
	}
	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}
	public String getChargemonth() {
		return chargemonth;
	}
	public void setChargemonth(String chargemonth) {
		this.chargemonth = chargemonth;
	}
	public String getChargetodate() {
		return chargetodate;
	}
	public void setChargetodate(String chargetodate) {
		this.chargetodate = chargetodate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public HousingShiJiaZhuangUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
	public HousingShiJiaZhuangUserInfo(String taskid, String accname, String accnum, String certinum,
			String openaccountdate, String balance, String unitaccnum, String unitaccname, String chargebasenum,
			String chargemonth, String chargetodate, String statuscode, String status) {
		super();
		this.taskid = taskid;
		this.accname = accname;
		this.accnum = accnum;
		this.certinum = certinum;
		this.openaccountdate = openaccountdate;
		this.balance = balance;
		this.unitaccnum = unitaccnum;
		this.unitaccname = unitaccname;
		this.chargebasenum = chargebasenum;
		this.chargemonth = chargemonth;
		this.chargetodate = chargetodate;
		this.statuscode = statuscode;
		this.status = status;
	}
	@Override
	public String toString() {
		return "HousingShiJiaZhuangUserInfo [taskid=" + taskid + ", accname=" + accname + ", accnum=" + accnum
				+ ", certinum=" + certinum + ", openaccountdate=" + openaccountdate + ", balance=" + balance
				+ ", unitaccnum=" + unitaccnum + ", unitaccname=" + unitaccname + ", chargebasenum=" + chargebasenum
				+ ", chargemonth=" + chargemonth + ", chargetodate=" + chargetodate + ", statuscode=" + statuscode
				+ ", status=" + status + "]";
	}
	
}
