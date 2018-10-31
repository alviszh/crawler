package com.microservice.dao.entity.crawler.housing.leshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_leshan_userinfo",indexes = {@Index(name = "index_housing_leshan_userinfo_taskid", columnList = "taskid")})
public class HousingLeShanUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String taskid;
	
	private String name;//姓名
	
	private String idcard;//身份证
	
	private String num;//个人帐号
	
	private String tel;//手机号码
	
	private String dwname;//单位名称
	
	private String dwnum;//单位帐号
	
	private String payglb;//缴存管理部
	
	private String paybank;//缴存银行
	
	private String paycardinal;//缴存基数
	
	private String grpayratio;//个人缴存比例
	
	private String dwpayratio;//单位缴存比例
	
	private String cardinalnum;//工资基数
	
	private String grmonth_remit;//个人月汇缴额
	
	private String dwmonth_remit;//单位月汇缴额
	
	private String hjmonth_remit;//合计月汇缴额
	
	private String balance;//缴存余额
	
	private String state;//账户状态
	
	private String khdate;//开户日期
	
	private String paydate;//缴至年月
	
	private String bank;//绑定银行
	
	private String cardnum;//绑定银行卡号

	

	public String getPaycardinal() {
		return paycardinal;
	}

	public void setPaycardinal(String paycardinal) {
		this.paycardinal = paycardinal;
	}

	public HousingLeShanUserInfo() {
		super();
	}

	public HousingLeShanUserInfo(String taskid, String name, String idcard, String num, String tel, String dwname,
			String dwnum, String payglb, String paybank, String paycardinal, String grpayratio, String dwpayratio,
			String cardinalnum, String grmonth_remit, String dwmonth_remit, String hjmonth_remit, String balance,
			String state, String khdate, String paydate, String bank, String cardnum) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idcard = idcard;
		this.num = num;
		this.tel = tel;
		this.dwname = dwname;
		this.dwnum = dwnum;
		this.payglb = payglb;
		this.paybank = paybank;
		this.paycardinal = paycardinal;
		this.grpayratio = grpayratio;
		this.dwpayratio = dwpayratio;
		this.cardinalnum = cardinalnum;
		this.grmonth_remit = grmonth_remit;
		this.dwmonth_remit = dwmonth_remit;
		this.hjmonth_remit = hjmonth_remit;
		this.balance = balance;
		this.state = state;
		this.khdate = khdate;
		this.paydate = paydate;
		this.bank = bank;
		this.cardnum = cardnum;
	}

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

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDwname() {
		return dwname;
	}

	public void setDwname(String dwname) {
		this.dwname = dwname;
	}

	public String getDwnum() {
		return dwnum;
	}

	public void setDwnum(String dwnum) {
		this.dwnum = dwnum;
	}

	public String getPayglb() {
		return payglb;
	}

	public void setPayglb(String payglb) {
		this.payglb = payglb;
	}

	public String getPaybank() {
		return paybank;
	}

	public void setPaybank(String paybank) {
		this.paybank = paybank;
	}

	public String getGrpayratio() {
		return grpayratio;
	}

	public void setGrpayratio(String grpayratio) {
		this.grpayratio = grpayratio;
	}

	public String getDwpayratio() {
		return dwpayratio;
	}

	public void setDwpayratio(String dwpayratio) {
		this.dwpayratio = dwpayratio;
	}

	public String getCardinalnum() {
		return cardinalnum;
	}

	public void setCardinalnum(String cardinalnum) {
		this.cardinalnum = cardinalnum;
	}

	public String getGrmonth_remit() {
		return grmonth_remit;
	}

	public void setGrmonth_remit(String grmonth_remit) {
		this.grmonth_remit = grmonth_remit;
	}

	public String getDwmonth_remit() {
		return dwmonth_remit;
	}

	public void setDwmonth_remit(String dwmonth_remit) {
		this.dwmonth_remit = dwmonth_remit;
	}

	public String getHjmonth_remit() {
		return hjmonth_remit;
	}

	public void setHjmonth_remit(String hjmonth_remit) {
		this.hjmonth_remit = hjmonth_remit;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getKhdate() {
		return khdate;
	}

	public void setKhdate(String khdate) {
		this.khdate = khdate;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	@Override
	public String toString() {
		return "HousingLeShanUserInfo [taskid=" + taskid + ", name=" + name + ", idcard=" + idcard + ", num=" + num
				+ ", tel=" + tel + ", dwname=" + dwname + ", dwnum=" + dwnum + ", payglb=" + payglb + ", paybank="
				+ paybank + ", grpayratio=" + grpayratio + ", dwpayratio=" + dwpayratio + ", cardinalnum=" + cardinalnum
				+ ", grmonth_remit=" + grmonth_remit + ", dwmonth_remit=" + dwmonth_remit + ", hjmonth_remit="
				+ hjmonth_remit + ", balance=" + balance + ", state=" + state + ", khdate=" + khdate + ", paydate="
				+ paydate + ", bank=" + bank + ", cardnum=" + cardnum + "]";
	}
	
	

}
