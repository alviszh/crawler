/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.housing.yvlin;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-01-10 15:24:0
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_yvlin_userinfo")
public class HousingBasicUserData extends IdEntity implements Serializable{

	private String loancontrstate;
	private String famaddr;
	private String filename;
	private String _PAGEID;
	private String bal; //余额
	private String NoteMsg;
	private String _PROCID;
	private String _SENDOPERID;
	private String TranSeq;
	private String basenum; //缴存基数
	private String _TYPE;
	private String indiaccstate; //个人状态
	private String calintyear;
	private String temp__rownum;
	private String dpinstance;
	private String dpdrawcheattimes;
	private String accname1; //姓名
	private String accname2; //姓名
	private String dpbadcretimes;
	private Date MTimeStamp;
	private String _ACCNAME; //姓名
	private String instcode;
	private String TranChannel;
	private String counts;
	private String times; //不良信用次数
	private String contramt; 
	private String bfcalintbal;
	private String dploantimes; //贷款次数
	private String occupation; //职业
	private String _LOGIP;
	private String isSamePer;
	private String AuthFlag;
	private String _PORCNAME;
	private String keepintaccu;
	private String TellCode;
	private Date opnaccdate; //开户日期
	private String accnum; //开户账号
	private String sex;
	private String keepbal; // 定期余额
	private String unitaccnum2;
	private String accnum1;
	private String accnum2;
	private String url;
	private String intamt;
	private String _WITHKEY;
	private String ChannelSeq;
	private Date birthday;
	private String certitype;
	private String BrcCode;
	private String drawtimes; //提取次数
	private String _ACCNUM;
	private String dac;
	private String payendym; //缴至年月
	private String afcalintbal;
	private String unitaccname; //单位名称
	private Date lastdrawdate; //最后提取日
	private Date contrenddate;
	private String accname;
	private String handset; //手机号码
	private String ChkCode;
	private String certitype3;
	private Date _SENDDATE;
	private String BusiSeq;
	private Date TranDate;
	private String email; // 电子邮件
	private String certinum; //证件号码
	private String unitaccname1; //单位名称
	private String _UNITACCNAME; //单位名称
	private String custid;
	private String monpaysum; //月缴额
	private String _DEPUTYIDCARDNUM;
	private String certinum3;
	private String TranIP;
	private String certinum4;
	private Date _SENDTIME;
	private String uuid;
	private String RspCode;
	private String stpayamt;
	private String _IS;
	private String indiprop; //个人缴存比例
	private String marstatus; //婚姻状况
	private String cardno; //联名卡卡号
	private String increintaccu;
	private String RspMsg;
	private String _BRANCHKIND;
	private Date CURRENT_SYSTEM_DATE;
	private String TranCode;
	private String _ISCROP;
	private String frzamt; //冻结金额
	private String increbal; //活期余额
	private String dplncheattimes;
	private Date STimeStamp;
	private String _RW;
	private String unitprop; //单位缴存比例
	private String unitaccnum; //单位账号
	private String loancontrnum;
	private String freeuse2;
	private String dpdrawamt; //累计提取金额
	private String freeuse4;
	private String AuthCode1;
	private String AuthCode2;
	private String AuthCode3;
	
	private String taskid;
	
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getLoancontrstate() {
		return loancontrstate;
	}
	public void setLoancontrstate(String loancontrstate) {
		this.loancontrstate = loancontrstate;
	}
	public String getFamaddr() {
		return famaddr;
	}
	public void setFamaddr(String famaddr) {
		this.famaddr = famaddr;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String get_PAGEID() {
		return _PAGEID;
	}
	public void set_PAGEID(String _PAGEID) {
		this._PAGEID = _PAGEID;
	}
	public String getBal() {
		return bal;
	}
	public void setBal(String bal) {
		this.bal = bal;
	}
	public String getNoteMsg() {
		return NoteMsg;
	}
	public void setNoteMsg(String noteMsg) {
		NoteMsg = noteMsg;
	}
	public String get_PROCID() {
		return _PROCID;
	}
	public void set_PROCID(String _PROCID) {
		this._PROCID = _PROCID;
	}
	public String get_SENDOPERID() {
		return _SENDOPERID;
	}
	public void set_SENDOPERID(String _SENDOPERID) {
		this._SENDOPERID = _SENDOPERID;
	}
	public String getTranSeq() {
		return TranSeq;
	}
	public void setTranSeq(String tranSeq) {
		TranSeq = tranSeq;
	}
	public String getBasenum() {
		return basenum;
	}
	public void setBasenum(String basenum) {
		this.basenum = basenum;
	}
	public String get_TYPE() {
		return _TYPE;
	}
	public void set_TYPE(String _TYPE) {
		this._TYPE = _TYPE;
	}
	public String getIndiaccstate() {
		return indiaccstate;
	}
	public void setIndiaccstate(String indiaccstate) {
		this.indiaccstate = indiaccstate;
	}
	public String getCalintyear() {
		return calintyear;
	}
	public void setCalintyear(String calintyear) {
		this.calintyear = calintyear;
	}
	public String getTemp__rownum() {
		return temp__rownum;
	}
	public void setTemp__rownum(String temp__rownum) {
		this.temp__rownum = temp__rownum;
	}
	public String getDpinstance() {
		return dpinstance;
	}
	public void setDpinstance(String dpinstance) {
		this.dpinstance = dpinstance;
	}
	public String getDpdrawcheattimes() {
		return dpdrawcheattimes;
	}
	public void setDpdrawcheattimes(String dpdrawcheattimes) {
		this.dpdrawcheattimes = dpdrawcheattimes;
	}
	public String getAccname1() {
		return accname1;
	}
	public void setAccname1(String accname1) {
		this.accname1 = accname1;
	}
	public String getAccname2() {
		return accname2;
	}
	public void setAccname2(String accname2) {
		this.accname2 = accname2;
	}
	public String getDpbadcretimes() {
		return dpbadcretimes;
	}
	public void setDpbadcretimes(String dpbadcretimes) {
		this.dpbadcretimes = dpbadcretimes;
	}
	public Date getMTimeStamp() {
		return MTimeStamp;
	}
	public void setMTimeStamp(Date mTimeStamp) {
		MTimeStamp = mTimeStamp;
	}
	public String get_ACCNAME() {
		return _ACCNAME;
	}
	public void set_ACCNAME(String _ACCNAME) {
		this._ACCNAME = _ACCNAME;
	}
	public String getInstcode() {
		return instcode;
	}
	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}
	public String getTranChannel() {
		return TranChannel;
	}
	public void setTranChannel(String tranChannel) {
		TranChannel = tranChannel;
	}
	public String getCounts() {
		return counts;
	}
	public void setCounts(String counts) {
		this.counts = counts;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getContramt() {
		return contramt;
	}
	public void setContramt(String contramt) {
		this.contramt = contramt;
	}
	public String getBfcalintbal() {
		return bfcalintbal;
	}
	public void setBfcalintbal(String bfcalintbal) {
		this.bfcalintbal = bfcalintbal;
	}
	public String getDploantimes() {
		return dploantimes;
	}
	public void setDploantimes(String dploantimes) {
		this.dploantimes = dploantimes;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String get_LOGIP() {
		return _LOGIP;
	}
	public void set_LOGIP(String _LOGIP) {
		this._LOGIP = _LOGIP;
	}
	public String getIsSamePer() {
		return isSamePer;
	}
	public void setIsSamePer(String isSamePer) {
		this.isSamePer = isSamePer;
	}
	public String getAuthFlag() {
		return AuthFlag;
	}
	public void setAuthFlag(String authFlag) {
		AuthFlag = authFlag;
	}
	public String get_PORCNAME() {
		return _PORCNAME;
	}
	public void set_PORCNAME(String _PORCNAME) {
		this._PORCNAME = _PORCNAME;
	}
	public String getKeepintaccu() {
		return keepintaccu;
	}
	public void setKeepintaccu(String keepintaccu) {
		this.keepintaccu = keepintaccu;
	}
	public String getTellCode() {
		return TellCode;
	}
	public void setTellCode(String tellCode) {
		TellCode = tellCode;
	}
	public Date getOpnaccdate() {
		return opnaccdate;
	}
	public void setOpnaccdate(Date opnaccdate) {
		this.opnaccdate = opnaccdate;
	}
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getKeepbal() {
		return keepbal;
	}
	public void setKeepbal(String keepbal) {
		this.keepbal = keepbal;
	}
	public String getUnitaccnum2() {
		return unitaccnum2;
	}
	public void setUnitaccnum2(String unitaccnum2) {
		this.unitaccnum2 = unitaccnum2;
	}
	public String getAccnum1() {
		return accnum1;
	}
	public void setAccnum1(String accnum1) {
		this.accnum1 = accnum1;
	}
	public String getAccnum2() {
		return accnum2;
	}
	public void setAccnum2(String accnum2) {
		this.accnum2 = accnum2;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIntamt() {
		return intamt;
	}
	public void setIntamt(String intamt) {
		this.intamt = intamt;
	}
	public String get_WITHKEY() {
		return _WITHKEY;
	}
	public void set_WITHKEY(String _WITHKEY) {
		this._WITHKEY = _WITHKEY;
	}
	public String getChannelSeq() {
		return ChannelSeq;
	}
	public void setChannelSeq(String channelSeq) {
		ChannelSeq = channelSeq;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getCertitype() {
		return certitype;
	}
	public void setCertitype(String certitype) {
		this.certitype = certitype;
	}
	public String getBrcCode() {
		return BrcCode;
	}
	public void setBrcCode(String brcCode) {
		BrcCode = brcCode;
	}
	public String getDrawtimes() {
		return drawtimes;
	}
	public void setDrawtimes(String drawtimes) {
		this.drawtimes = drawtimes;
	}
	public String get_ACCNUM() {
		return _ACCNUM;
	}
	public void set_ACCNUM(String _ACCNUM) {
		this._ACCNUM = _ACCNUM;
	}
	public String getDac() {
		return dac;
	}
	public void setDac(String dac) {
		this.dac = dac;
	}
	public String getPayendym() {
		return payendym;
	}
	public void setPayendym(String payendym) {
		this.payendym = payendym;
	}
	public String getAfcalintbal() {
		return afcalintbal;
	}
	public void setAfcalintbal(String afcalintbal) {
		this.afcalintbal = afcalintbal;
	}
	public String getUnitaccname() {
		return unitaccname;
	}
	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}
	public Date getLastdrawdate() {
		return lastdrawdate;
	}
	public void setLastdrawdate(Date lastdrawdate) {
		this.lastdrawdate = lastdrawdate;
	}
	public Date getContrenddate() {
		return contrenddate;
	}
	public void setContrenddate(Date contrenddate) {
		this.contrenddate = contrenddate;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public String getHandset() {
		return handset;
	}
	public void setHandset(String handset) {
		this.handset = handset;
	}
	public String getChkCode() {
		return ChkCode;
	}
	public void setChkCode(String chkCode) {
		ChkCode = chkCode;
	}
	public String getCertitype3() {
		return certitype3;
	}
	public void setCertitype3(String certitype3) {
		this.certitype3 = certitype3;
	}
	public Date get_SENDDATE() {
		return _SENDDATE;
	}
	public void set_SENDDATE(Date _SENDDATE) {
		this._SENDDATE = _SENDDATE;
	}
	public String getBusiSeq() {
		return BusiSeq;
	}
	public void setBusiSeq(String busiSeq) {
		BusiSeq = busiSeq;
	}
	public Date getTranDate() {
		return TranDate;
	}
	public void setTranDate(Date tranDate) {
		TranDate = tranDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCertinum() {
		return certinum;
	}
	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}
	public String getUnitaccname1() {
		return unitaccname1;
	}
	public void setUnitaccname1(String unitaccname1) {
		this.unitaccname1 = unitaccname1;
	}
	public String get_UNITACCNAME() {
		return _UNITACCNAME;
	}
	public void set_UNITACCNAME(String _UNITACCNAME) {
		this._UNITACCNAME = _UNITACCNAME;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getMonpaysum() {
		return monpaysum;
	}
	public void setMonpaysum(String monpaysum) {
		this.monpaysum = monpaysum;
	}
	public String get_DEPUTYIDCARDNUM() {
		return _DEPUTYIDCARDNUM;
	}
	public void set_DEPUTYIDCARDNUM(String _DEPUTYIDCARDNUM) {
		this._DEPUTYIDCARDNUM = _DEPUTYIDCARDNUM;
	}
	public String getCertinum3() {
		return certinum3;
	}
	public void setCertinum3(String certinum3) {
		this.certinum3 = certinum3;
	}
	public String getTranIP() {
		return TranIP;
	}
	public void setTranIP(String tranIP) {
		TranIP = tranIP;
	}
	public String getCertinum4() {
		return certinum4;
	}
	public void setCertinum4(String certinum4) {
		this.certinum4 = certinum4;
	}
	public Date get_SENDTIME() {
		return _SENDTIME;
	}
	public void set_SENDTIME(Date _SENDTIME) {
		this._SENDTIME = _SENDTIME;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getRspCode() {
		return RspCode;
	}
	public void setRspCode(String rspCode) {
		RspCode = rspCode;
	}
	public String getStpayamt() {
		return stpayamt;
	}
	public void setStpayamt(String stpayamt) {
		this.stpayamt = stpayamt;
	}
	public String get_IS() {
		return _IS;
	}
	public void set_IS(String _IS) {
		this._IS = _IS;
	}
	public String getIndiprop() {
		return indiprop;
	}
	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}
	public String getMarstatus() {
		return marstatus;
	}
	public void setMarstatus(String marstatus) {
		this.marstatus = marstatus;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getIncreintaccu() {
		return increintaccu;
	}
	public void setIncreintaccu(String increintaccu) {
		this.increintaccu = increintaccu;
	}
	public String getRspMsg() {
		return RspMsg;
	}
	public void setRspMsg(String rspMsg) {
		RspMsg = rspMsg;
	}
	public String get_BRANCHKIND() {
		return _BRANCHKIND;
	}
	public void set_BRANCHKIND(String _BRANCHKIND) {
		this._BRANCHKIND = _BRANCHKIND;
	}
	public Date getCURRENT_SYSTEM_DATE() {
		return CURRENT_SYSTEM_DATE;
	}
	public void setCURRENT_SYSTEM_DATE(Date cURRENT_SYSTEM_DATE) {
		CURRENT_SYSTEM_DATE = cURRENT_SYSTEM_DATE;
	}
	public String getTranCode() {
		return TranCode;
	}
	public void setTranCode(String tranCode) {
		TranCode = tranCode;
	}
	public String get_ISCROP() {
		return _ISCROP;
	}
	public void set_ISCROP(String _ISCROP) {
		this._ISCROP = _ISCROP;
	}
	public String getFrzamt() {
		return frzamt;
	}
	public void setFrzamt(String frzamt) {
		this.frzamt = frzamt;
	}
	public String getIncrebal() {
		return increbal;
	}
	public void setIncrebal(String increbal) {
		this.increbal = increbal;
	}
	public String getDplncheattimes() {
		return dplncheattimes;
	}
	public void setDplncheattimes(String dplncheattimes) {
		this.dplncheattimes = dplncheattimes;
	}
	public Date getSTimeStamp() {
		return STimeStamp;
	}
	public void setSTimeStamp(Date sTimeStamp) {
		STimeStamp = sTimeStamp;
	}
	public String get_RW() {
		return _RW;
	}
	public void set_RW(String _RW) {
		this._RW = _RW;
	}
	public String getUnitprop() {
		return unitprop;
	}
	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getLoancontrnum() {
		return loancontrnum;
	}
	public void setLoancontrnum(String loancontrnum) {
		this.loancontrnum = loancontrnum;
	}
	public String getFreeuse2() {
		return freeuse2;
	}
	public void setFreeuse2(String freeuse2) {
		this.freeuse2 = freeuse2;
	}
	public String getDpdrawamt() {
		return dpdrawamt;
	}
	public void setDpdrawamt(String dpdrawamt) {
		this.dpdrawamt = dpdrawamt;
	}
	public String getFreeuse4() {
		return freeuse4;
	}
	public void setFreeuse4(String freeuse4) {
		this.freeuse4 = freeuse4;
	}
	public String getAuthCode1() {
		return AuthCode1;
	}
	public void setAuthCode1(String authCode1) {
		AuthCode1 = authCode1;
	}
	public String getAuthCode2() {
		return AuthCode2;
	}
	public void setAuthCode2(String authCode2) {
		AuthCode2 = authCode2;
	}
	public String getAuthCode3() {
		return AuthCode3;
	}
	public void setAuthCode3(String authCode3) {
		AuthCode3 = authCode3;
	}
	@Override
	public String toString() {
		return "HousingBasicUserData [loancontrstate=" + loancontrstate + ", famaddr=" + famaddr + ", filename="
				+ filename + ", _PAGEID=" + _PAGEID + ", bal=" + bal + ", NoteMsg=" + NoteMsg + ", _PROCID=" + _PROCID
				+ ", _SENDOPERID=" + _SENDOPERID + ", TranSeq=" + TranSeq + ", basenum=" + basenum + ", _TYPE=" + _TYPE
				+ ", indiaccstate=" + indiaccstate + ", calintyear=" + calintyear + ", temp__rownum=" + temp__rownum
				+ ", dpinstance=" + dpinstance + ", dpdrawcheattimes=" + dpdrawcheattimes + ", accname1=" + accname1
				+ ", accname2=" + accname2 + ", dpbadcretimes=" + dpbadcretimes + ", MTimeStamp=" + MTimeStamp
				+ ", _ACCNAME=" + _ACCNAME + ", instcode=" + instcode + ", TranChannel=" + TranChannel + ", counts="
				+ counts + ", times=" + times + ", contramt=" + contramt + ", bfcalintbal=" + bfcalintbal
				+ ", dploantimes=" + dploantimes + ", occupation=" + occupation + ", _LOGIP=" + _LOGIP + ", isSamePer="
				+ isSamePer + ", AuthFlag=" + AuthFlag + ", _PORCNAME=" + _PORCNAME + ", keepintaccu=" + keepintaccu
				+ ", TellCode=" + TellCode + ", opnaccdate=" + opnaccdate + ", accnum=" + accnum + ", sex=" + sex
				+ ", keepbal=" + keepbal + ", unitaccnum2=" + unitaccnum2 + ", accnum1=" + accnum1 + ", accnum2="
				+ accnum2 + ", url=" + url + ", intamt=" + intamt + ", _WITHKEY=" + _WITHKEY + ", ChannelSeq="
				+ ChannelSeq + ", birthday=" + birthday + ", certitype=" + certitype + ", BrcCode=" + BrcCode
				+ ", drawtimes=" + drawtimes + ", _ACCNUM=" + _ACCNUM + ", dac=" + dac + ", payendym=" + payendym
				+ ", afcalintbal=" + afcalintbal + ", unitaccname=" + unitaccname + ", lastdrawdate=" + lastdrawdate
				+ ", contrenddate=" + contrenddate + ", accname=" + accname + ", handset=" + handset + ", ChkCode="
				+ ChkCode + ", certitype3=" + certitype3 + ", _SENDDATE=" + _SENDDATE + ", BusiSeq=" + BusiSeq
				+ ", TranDate=" + TranDate + ", email=" + email + ", certinum=" + certinum + ", unitaccname1="
				+ unitaccname1 + ", _UNITACCNAME=" + _UNITACCNAME + ", custid=" + custid + ", monpaysum=" + monpaysum
				+ ", _DEPUTYIDCARDNUM=" + _DEPUTYIDCARDNUM + ", certinum3=" + certinum3 + ", TranIP=" + TranIP
				+ ", certinum4=" + certinum4 + ", _SENDTIME=" + _SENDTIME + ", uuid=" + uuid + ", RspCode=" + RspCode
				+ ", stpayamt=" + stpayamt + ", _IS=" + _IS + ", indiprop=" + indiprop + ", marstatus=" + marstatus
				+ ", cardno=" + cardno + ", increintaccu=" + increintaccu + ", RspMsg=" + RspMsg + ", _BRANCHKIND="
				+ _BRANCHKIND + ", CURRENT_SYSTEM_DATE=" + CURRENT_SYSTEM_DATE + ", TranCode=" + TranCode + ", _ISCROP="
				+ _ISCROP + ", frzamt=" + frzamt + ", increbal=" + increbal + ", dplncheattimes=" + dplncheattimes
				+ ", STimeStamp=" + STimeStamp + ", _RW=" + _RW + ", unitprop=" + unitprop + ", unitaccnum="
				+ unitaccnum + ", loancontrnum=" + loancontrnum + ", freeuse2=" + freeuse2 + ", dpdrawamt=" + dpdrawamt
				+ ", freeuse4=" + freeuse4 + ", AuthCode1=" + AuthCode1 + ", AuthCode2=" + AuthCode2 + ", AuthCode3="
				+ AuthCode3 + ", taskid=" + taskid + "]";
	}

	
}