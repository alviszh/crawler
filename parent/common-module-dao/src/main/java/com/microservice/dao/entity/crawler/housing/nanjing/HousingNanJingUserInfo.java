package com.microservice.dao.entity.crawler.housing.nanjing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月26日 下午2:43:28 
 */
@Entity
@Table(name="housing_nanjing_userinfo",indexes = {@Index(name = "index_housing_nanjing_userinfo_taskid", columnList = "taskid")})
public class HousingNanJingUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -5901921334712806791L;
	private String taskid;
//	个人姓名
	private String accname;
//	个人账号
	private String accnum;
//	产品代码
	private String prodcode;   //代号1 是公积金 ,2是补贴——由于我们查的信息是公积金信息，故此处直接存储的值是：公积金
//	证件号码
	private String certinum;
//	单位名称
	private String unitaccname;
//	单位账号
	private String unitaccnum;
//	联名卡号
	private String cardnocsp;
//	短信标识
	private String smsflag;
//	联系电话
	private String linkphone;
//	开户日期
	private String opnaccdate;
//	账户状态
	private String indiaccstate;
//	月缴存额
	private String monthcharge;
//	余额
	private String balance;
//	最后汇缴月
	private String lastpaymonth;
//	个人比例 (返回的数据是小数)
	private String indiprop;
//	单位比例
	private String unitprop;
//	单位开户日期
	private String unitopnaccdate;
//	单位账户状态
	private String unitaccstate;
//	开户网点名称
	private String opnaccnet;
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
	public String getProdcode() {
		return prodcode;
	}
	public void setProdcode(String prodcode) {
		this.prodcode = prodcode;
	}
	public String getCertinum() {
		return certinum;
	}
	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}
	public String getUnitaccname() {
		return unitaccname;
	}
	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getCardnocsp() {
		return cardnocsp;
	}
	public void setCardnocsp(String cardnocsp) {
		this.cardnocsp = cardnocsp;
	}
	public String getSmsflag() {
		return smsflag;
	}
	public void setSmsflag(String smsflag) {
		this.smsflag = smsflag;
	}
	public String getLinkphone() {
		return linkphone;
	}
	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}
	public String getOpnaccdate() {
		return opnaccdate;
	}
	public void setOpnaccdate(String opnaccdate) {
		this.opnaccdate = opnaccdate;
	}
	public String getIndiaccstate() {
		return indiaccstate;
	}
	public void setIndiaccstate(String indiaccstate) {
		this.indiaccstate = indiaccstate;
	}
	public String getMonthcharge() {
		return monthcharge;
	}
	public void setMonthcharge(String monthcharge) {
		this.monthcharge = monthcharge;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getLastpaymonth() {
		return lastpaymonth;
	}
	public void setLastpaymonth(String lastpaymonth) {
		this.lastpaymonth = lastpaymonth;
	}
	public String getIndiprop() {
		return indiprop;
	}
	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}
	public String getUnitprop() {
		return unitprop;
	}
	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}
	public String getUnitopnaccdate() {
		return unitopnaccdate;
	}
	public void setUnitopnaccdate(String unitopnaccdate) {
		this.unitopnaccdate = unitopnaccdate;
	}
	public String getUnitaccstate() {
		return unitaccstate;
	}
	public void setUnitaccstate(String unitaccstate) {
		this.unitaccstate = unitaccstate;
	}
	public String getOpnaccnet() {
		return opnaccnet;
	}
	public void setOpnaccnet(String opnaccnet) {
		this.opnaccnet = opnaccnet;
	}
	public HousingNanJingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingNanJingUserInfo(String taskid, String accname, String accnum, String prodcode, String certinum,
			String unitaccname, String unitaccnum, String cardnocsp, String smsflag, String linkphone,
			String opnaccdate, String indiaccstate, String monthcharge, String balance, String lastpaymonth,
			String indiprop, String unitprop, String unitopnaccdate, String unitaccstate, String opnaccnet) {
		super();
		this.taskid = taskid;
		this.accname = accname;
		this.accnum = accnum;
		this.prodcode = prodcode;
		this.certinum = certinum;
		this.unitaccname = unitaccname;
		this.unitaccnum = unitaccnum;
		this.cardnocsp = cardnocsp;
		this.smsflag = smsflag;
		this.linkphone = linkphone;
		this.opnaccdate = opnaccdate;
		this.indiaccstate = indiaccstate;
		this.monthcharge = monthcharge;
		this.balance = balance;
		this.lastpaymonth = lastpaymonth;
		this.indiprop = indiprop;
		this.unitprop = unitprop;
		this.unitopnaccdate = unitopnaccdate;
		this.unitaccstate = unitaccstate;
		this.opnaccnet = opnaccnet;
	}
}
