package com.microservice.dao.entity.crawler.housing.jilin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 吉林市公积金个人基本信息
 * @author: sln 
 * @date: 
 */
@Entity
@Table(name="housing_jilin_userinfo",indexes = {@Index(name = "index_housing_jilin_userinfo_taskid", columnList = "taskid")})
public class HousingJiLinUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4523112843732781341L;
	private String taskid;
	//=============改版前================
//	个人账号
	private String accnum;
//	姓名
	private String accname;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitaccname;
//	身份证号
	private String certinum;
//	最后汇缴月
	private String lastpaydate;
//	个人公积金比例
	private String accmulperprop;
//	单位公积金比例
	private String accmulunitprop;
//	公积金比例合计
	private String prop;
//	缴存基数
	private String basenumber;
//	卡号
	private String cardno;
//	余额
	private String balance;
//	账户状态
	private String peraccstate;
//	冻结状态
	private String perfrozenflag;
//	开户日期
	private String opendate;
//	启用日期
	private String beginpaydate;
//	月汇缴合计
	private String monpaysum;
//	联系电话
	private String relphone;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
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
	public String getCertinum() {
		return certinum;
	}
	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}
	public String getLastpaydate() {
		return lastpaydate;
	}
	public void setLastpaydate(String lastpaydate) {
		this.lastpaydate = lastpaydate;
	}
	public String getAccmulperprop() {
		return accmulperprop;
	}
	public void setAccmulperprop(String accmulperprop) {
		this.accmulperprop = accmulperprop;
	}
	public String getAccmulunitprop() {
		return accmulunitprop;
	}
	public void setAccmulunitprop(String accmulunitprop) {
		this.accmulunitprop = accmulunitprop;
	}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public String getBasenumber() {
		return basenumber;
	}
	public void setBasenumber(String basenumber) {
		this.basenumber = basenumber;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPeraccstate() {
		return peraccstate;
	}
	public void setPeraccstate(String peraccstate) {
		this.peraccstate = peraccstate;
	}
	public String getPerfrozenflag() {
		return perfrozenflag;
	}
	public void setPerfrozenflag(String perfrozenflag) {
		this.perfrozenflag = perfrozenflag;
	}
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getBeginpaydate() {
		return beginpaydate;
	}
	public void setBeginpaydate(String beginpaydate) {
		this.beginpaydate = beginpaydate;
	}
	public String getMonpaysum() {
		return monpaysum;
	}
	public void setMonpaysum(String monpaysum) {
		this.monpaysum = monpaysum;
	}
	public String getRelphone() {
		return relphone;
	}
	public void setRelphone(String relphone) {
		this.relphone = relphone;
	}
	public HousingJiLinUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingJiLinUserInfo(String taskid, String accnum, String accname, String unitaccnum, String unitaccname,
			String certinum, String lastpaydate, String accmulperprop, String accmulunitprop, String prop,
			String basenumber, String cardno, String balance, String peraccstate, String perfrozenflag, String opendate,
			String beginpaydate, String monpaysum, String relphone) {
		super();
		this.taskid = taskid;
		this.accnum = accnum;
		this.accname = accname;
		this.unitaccnum = unitaccnum;
		this.unitaccname = unitaccname;
		this.certinum = certinum;
		this.lastpaydate = lastpaydate;
		this.accmulperprop = accmulperprop;
		this.accmulunitprop = accmulunitprop;
		this.prop = prop;
		this.basenumber = basenumber;
		this.cardno = cardno;
		this.balance = balance;
		this.peraccstate = peraccstate;
		this.perfrozenflag = perfrozenflag;
		this.opendate = opendate;
		this.beginpaydate = beginpaydate;
		this.monpaysum = monpaysum;
		this.relphone = relphone;
	}
	
	
//=============改版后====考虑到etl，此处依旧保留之前的表结构============
/*//	个人账号
	private String accnum;
//	姓名
	private String accname;
//	单位名称
	private String unitaccname;
//	身份证号
	private String certinum;
//	最后汇缴月
	private String lastpaydate;
//	缴存基数
	private String basenumber;
//	余额
	private String balance;
//	月汇缴合计
	private String monpaysum;*/
//=============改版后====考虑到etl，此处依旧保留之前的表结构============
	
}
