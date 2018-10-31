package com.microservice.dao.entity.crawler.housing.liangshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="housing_liangshan_userinfo",indexes = {@Index(name = "index_housing_liangshan_userinfo_taskid", columnList = "taskid")})
public class HousingLiangShanUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4523112843732781341L;
	private String taskid;
//	单位名称
	private String unitname;
//	缴止年月
	private String paytoyearmonth;
//	月缴款合计
	private String monthtotalpay;
//	借贷关系
	private String creditrelation;
//	贷款余额
	private String loanbalance;
/////////////从两个链接中获取的上下两部分内容//////////////////////
//	个人账号
	private String accnum;
//	姓名
	private String name;
//	性别
	private String gender;
//	固定电话号码
	private String fixphone;
//	手机号码
	private String phonenum;
//	证件类型
	private String idtype;
//	证件号码
	private String idnum;
//	出生年月
	private String birthday;
//	婚姻状况
	private String marriage;
//	职业
	private String profession;
//	职称
	private String professionalrank;
//	职务
	private String post;
//	学历
	private String education;
//	邮政编码
	private String postalcode;
//	家庭住址
	private String homeaddress;
//	家庭月收入
	private String homemonthincome;
//	个人缴存基数
	private String chargebasenum;
//	个人账户状态
	private String accstatus;
//	开户日期
	private String opendate;
//	个人账户余额
	private String balance;
//	个人账户上年结转余额
	private String lastyearbalance;
//	个人账户当年归集余额
	private String thisyeardownbalance;
//	个人月缴存额
	private String permonthcharge;
//	单位月缴存额
	private String unitmonthcharge;
//	销户日期
	private String accountcanceldate;
//	销户原因
	private String accountcancelreason;
//	个人存款账户号码
	private String persavingaccnum;
//	个人存款账户开户银行名称
	private String persavingbank;
//	个人存款账户开户银行代码
	private String persavingbankcode;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getPaytoyearmonth() {
		return paytoyearmonth;
	}
	public void setPaytoyearmonth(String paytoyearmonth) {
		this.paytoyearmonth = paytoyearmonth;
	}
	public String getMonthtotalpay() {
		return monthtotalpay;
	}
	public void setMonthtotalpay(String monthtotalpay) {
		this.monthtotalpay = monthtotalpay;
	}
	public String getCreditrelation() {
		return creditrelation;
	}
	public void setCreditrelation(String creditrelation) {
		this.creditrelation = creditrelation;
	}
	public String getLoanbalance() {
		return loanbalance;
	}
	public void setLoanbalance(String loanbalance) {
		this.loanbalance = loanbalance;
	}
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getFixphone() {
		return fixphone;
	}
	public void setFixphone(String fixphone) {
		this.fixphone = fixphone;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	
	public String getProfessionalrank() {
		return professionalrank;
	}
	public void setProfessionalrank(String professionalrank) {
		this.professionalrank = professionalrank;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getHomeaddress() {
		return homeaddress;
	}
	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}
	public String getHomemonthincome() {
		return homemonthincome;
	}
	public void setHomemonthincome(String homemonthincome) {
		this.homemonthincome = homemonthincome;
	}
	public String getChargebasenum() {
		return chargebasenum;
	}
	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}
	public String getAccstatus() {
		return accstatus;
	}
	public void setAccstatus(String accstatus) {
		this.accstatus = accstatus;
	}
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getLastyearbalance() {
		return lastyearbalance;
	}
	public void setLastyearbalance(String lastyearbalance) {
		this.lastyearbalance = lastyearbalance;
	}
	public String getThisyeardownbalance() {
		return thisyeardownbalance;
	}
	public void setThisyeardownbalance(String thisyeardownbalance) {
		this.thisyeardownbalance = thisyeardownbalance;
	}
	public String getPermonthcharge() {
		return permonthcharge;
	}
	public void setPermonthcharge(String permonthcharge) {
		this.permonthcharge = permonthcharge;
	}
	public String getUnitmonthcharge() {
		return unitmonthcharge;
	}
	public void setUnitmonthcharge(String unitmonthcharge) {
		this.unitmonthcharge = unitmonthcharge;
	}
	public String getAccountcanceldate() {
		return accountcanceldate;
	}
	public void setAccountcanceldate(String accountcanceldate) {
		this.accountcanceldate = accountcanceldate;
	}
	public String getAccountcancelreason() {
		return accountcancelreason;
	}
	public void setAccountcancelreason(String accountcancelreason) {
		this.accountcancelreason = accountcancelreason;
	}
	public String getPersavingaccnum() {
		return persavingaccnum;
	}
	public void setPersavingaccnum(String persavingaccnum) {
		this.persavingaccnum = persavingaccnum;
	}
	public String getPersavingbank() {
		return persavingbank;
	}
	public void setPersavingbank(String persavingbank) {
		this.persavingbank = persavingbank;
	}
	public String getPersavingbankcode() {
		return persavingbankcode;
	}
	public void setPersavingbankcode(String persavingbankcode) {
		this.persavingbankcode = persavingbankcode;
	}
	public HousingLiangShanUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingLiangShanUserInfo(String taskid, String unitname, String paytoyearmonth, String monthtotalpay,
			String creditrelation, String loanbalance, String accnum, String name, String gender, String fixphone,
			String phonenum, String idtype, String idnum, String birthday, String marriage, String profession,
			String professionalrank, String post, String education, String postalcode, String homeaddress,
			String homemonthincome, String chargebasenum, String accstatus, String opendate, String balance,
			String lastyearbalance, String thisyeardownbalance, String permonthcharge, String unitmonthcharge,
			String accountcanceldate, String accountcancelreason, String persavingaccnum, String persavingbank,
			String persavingbankcode) {
		super();
		this.taskid = taskid;
		this.unitname = unitname;
		this.paytoyearmonth = paytoyearmonth;
		this.monthtotalpay = monthtotalpay;
		this.creditrelation = creditrelation;
		this.loanbalance = loanbalance;
		this.accnum = accnum;
		this.name = name;
		this.gender = gender;
		this.fixphone = fixphone;
		this.phonenum = phonenum;
		this.idtype = idtype;
		this.idnum = idnum;
		this.birthday = birthday;
		this.marriage = marriage;
		this.profession = profession;
		this.professionalrank = professionalrank;
		this.post = post;
		this.education = education;
		this.postalcode = postalcode;
		this.homeaddress = homeaddress;
		this.homemonthincome = homemonthincome;
		this.chargebasenum = chargebasenum;
		this.accstatus = accstatus;
		this.opendate = opendate;
		this.balance = balance;
		this.lastyearbalance = lastyearbalance;
		this.thisyeardownbalance = thisyeardownbalance;
		this.permonthcharge = permonthcharge;
		this.unitmonthcharge = unitmonthcharge;
		this.accountcanceldate = accountcanceldate;
		this.accountcancelreason = accountcancelreason;
		this.persavingaccnum = persavingaccnum;
		this.persavingbank = persavingbank;
		this.persavingbankcode = persavingbankcode;
	}
	
}
