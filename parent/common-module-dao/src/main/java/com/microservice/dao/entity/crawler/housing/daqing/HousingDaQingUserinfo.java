package com.microservice.dao.entity.crawler.housing.daqing;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_daqing_userinfo",indexes = {@Index(name = "index_housing_daqing_userinfo_taskid", columnList = "taskid")})
public class HousingDaQingUserinfo extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalcount;
	private String success;
	private String dwbh;//000000001915 未知含义
	private String dwmc;//公司
	private String grbh;//000000035849 未知含义
	private String grzh;//49801140952 员工号
	private String xingming;//姓名
	private String xmqp;//姓名拼音
	private String xingbie;//性别
	private String gddhhm;//0331715851 固定电话
	private String sjhm;//手机号码
	private String zjlx; //证件类型
	private String zjhm;//证件号码
	private Date csny;//出生年月
	private String hyzk;//婚姻状况
	private String zhiye;//职业
	private String zhichen;//职称
	private String zhiwu;//职务
	private String xueli;//学历
	private String yzbm;//邮政编码
	private String jtzz;//未知
	private String jtysr;//家庭月收入
	private String xmjp;//推测为姓名拼音首字母
	private String cjgzrq;//2014-11-21 00:00:00.0 未知含义
	private String szbm;//未知含义
	private String txdz;//未知含义
	private String poxm;//未知含义
	private String pozjlx;//01 未知含义
	private String pozjhm;//未知含义
	private String zfqk;//未知含义
	private String sfyzfdk;//推测为有无贷款情况
	private String cdgx;////未知含义
	private String sbzh;//未知含义
	private int grysr;//个人缴存基数
	private String xydj;//未知含义
	private String jgbm;//未知含义
	private Date djrq;//2014-11-21 未知含义
	private String djsj;//2014-11-21 00:00:00.0 未知含义
	private int djczyid;//1 //未知含义
	private String djczy;//1 /未知含义
	private String cym;//未知含义
	private String ywm;//未知含义
	private String csd;//未知含义
	private String minzu;//未知含义
	private String shengao;//未知含义 推测为身高
	private String tizhong;//未知含义 推测为体重
	private int zysl;// 0 //未知含义
	private int yysl;// 0 //未知含义
	private String jtdh;//未知含义
	private String hjszd;//未知含义
	private String daszd;//未知含义
	private String zzmm;//未知含义
	private String zjxy;//未知含义
	private String zhuanye;//未知含义 推测为专业
	private String byyx;//未知含义
	private String rzrq;//未知含义 2017-06-28 00:00:00.0
	private String xzz;//未知含义
	private String xzzdh;//未知含义
	private String jzhm;//未知含义
	private String yuzhong;//未知含义
	private String wysp;//未知含义
	private String xuexing;//未知含义 推测为血型
	private String aihao;////未知含义 推测为爱好
	private String techang;//未知含义 推测为特长
	private String mqgzzt;//未知含义
	private String dzxx;//未知含义
	private String dlzt;//未知含义
	private String grlbbm;//未知含义 01
	private String grzhye;//公积金余额
	private String gryjce;//个人缴存金额
	private String dwyjce;//单位缴存金额
	private String grjcjs;//个人缴存基数
	private String grckzhhm;//未知含义
	private String grckzhkhyhmc;//未知含义
	private String grckzhkhyhdm;//未知含义
	private String grckzhhmssyhbm;//未知含义
	private String zhsfdj;//未知含义 0
	private String grzhzt;//未知含义
	private String jzny;//未知含义
	private String dwzh;//未知含义 5130 关键参数
	private String yjce;//月缴存金额
	private String grjcl;//个人缴存比例
	private String dwjcl;//单位缴存金额
	private String jcbl;//单位5.0%,个人5.0%
	private String dkqk;//担保情况
	private String dkcs;//未知含义
	private String ssbmbm;//未知含义
	private String ssbmbm1;//未知含义
	private String jcblmc;//06缴存标准|单位0.0500|个人0.0500"
	private String grckzhkhyhhh;//未知含义
	private String bianzhi;//未知含义 推测为编制
	private String hklx;//未知含义
	private String dqdjje;//未知含义 0.0
	private String hmdzt;//未知含义 否
	private String hjzt;//汇缴状态
	private String bnzq;//未知含义 0
	private String bnjc;//未知含义 730
	private String dbqk;//担保情况
	private String tqrq;//未知含义
	private String tqjehj;//未知含义
	
	private Integer userid;

	private String taskid;


	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getSuccess() {
		return success;
	}

	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}

	public String getDwbh() {
		return dwbh;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}

	public String getGrbh() {
		return grbh;
	}

	public void setGrzh(String grzh) {
		this.grzh = grzh;
	}

	public String getGrzh() {
		return grzh;
	}

	public void setXingming(String xingming) {
		this.xingming = xingming;
	}

	public String getXingming() {
		return xingming;
	}

	public void setXmqp(String xmqp) {
		this.xmqp = xmqp;
	}

	public String getXmqp() {
		return xmqp;
	}

	public void setXingbie(String xingbie) {
		this.xingbie = xingbie;
	}

	public String getXingbie() {
		return xingbie;
	}

	public void setGddhhm(String gddhhm) {
		this.gddhhm = gddhhm;
	}

	public String getGddhhm() {
		return gddhhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setCsny(Date csny) {
		this.csny = csny;
	}

	public Date getCsny() {
		return csny;
	}

	public void setHyzk(String hyzk) {
		this.hyzk = hyzk;
	}

	public String getHyzk() {
		return hyzk;
	}

	public void setZhiye(String zhiye) {
		this.zhiye = zhiye;
	}

	public String getZhiye() {
		return zhiye;
	}

	public void setZhichen(String zhichen) {
		this.zhichen = zhichen;
	}

	public String getZhichen() {
		return zhichen;
	}

	public void setZhiwu(String zhiwu) {
		this.zhiwu = zhiwu;
	}

	public String getZhiwu() {
		return zhiwu;
	}

	public void setXueli(String xueli) {
		this.xueli = xueli;
	}

	public String getXueli() {
		return xueli;
	}

	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}

	public String getYzbm() {
		return yzbm;
	}

	public void setJtzz(String jtzz) {
		this.jtzz = jtzz;
	}

	public String getJtzz() {
		return jtzz;
	}

	public void setJtysr(String jtysr) {
		this.jtysr = jtysr;
	}

	public String getJtysr() {
		return jtysr;
	}

	public void setXmjp(String xmjp) {
		this.xmjp = xmjp;
	}

	public String getXmjp() {
		return xmjp;
	}

	public void setCjgzrq(String cjgzrq) {
		this.cjgzrq = cjgzrq;
	}

	public String getCjgzrq() {
		return cjgzrq;
	}

	public void setSzbm(String szbm) {
		this.szbm = szbm;
	}

	public String getSzbm() {
		return szbm;
	}

	public void setTxdz(String txdz) {
		this.txdz = txdz;
	}

	public String getTxdz() {
		return txdz;
	}

	public void setPoxm(String poxm) {
		this.poxm = poxm;
	}

	public String getPoxm() {
		return poxm;
	}

	public void setPozjlx(String pozjlx) {
		this.pozjlx = pozjlx;
	}

	public String getPozjlx() {
		return pozjlx;
	}

	public void setPozjhm(String pozjhm) {
		this.pozjhm = pozjhm;
	}

	public String getPozjhm() {
		return pozjhm;
	}

	public void setZfqk(String zfqk) {
		this.zfqk = zfqk;
	}

	public String getZfqk() {
		return zfqk;
	}

	public void setSfyzfdk(String sfyzfdk) {
		this.sfyzfdk = sfyzfdk;
	}

	public String getSfyzfdk() {
		return sfyzfdk;
	}

	public void setCdgx(String cdgx) {
		this.cdgx = cdgx;
	}

	public String getCdgx() {
		return cdgx;
	}

	public void setSbzh(String sbzh) {
		this.sbzh = sbzh;
	}

	public String getSbzh() {
		return sbzh;
	}

	public void setGrysr(int grysr) {
		this.grysr = grysr;
	}

	public int getGrysr() {
		return grysr;
	}

	public void setXydj(String xydj) {
		this.xydj = xydj;
	}

	public String getXydj() {
		return xydj;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

	public String getJgbm() {
		return jgbm;
	}

	public void setDjrq(Date djrq) {
		this.djrq = djrq;
	}

	public Date getDjrq() {
		return djrq;
	}

	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}

	public String getDjsj() {
		return djsj;
	}

	public void setDjczyid(int djczyid) {
		this.djczyid = djczyid;
	}

	public int getDjczyid() {
		return djczyid;
	}

	public void setDjczy(String djczy) {
		this.djczy = djczy;
	}

	public String getDjczy() {
		return djczy;
	}

	public void setCym(String cym) {
		this.cym = cym;
	}

	public String getCym() {
		return cym;
	}

	public void setYwm(String ywm) {
		this.ywm = ywm;
	}

	public String getYwm() {
		return ywm;
	}

	public void setCsd(String csd) {
		this.csd = csd;
	}

	public String getCsd() {
		return csd;
	}

	public void setMinzu(String minzu) {
		this.minzu = minzu;
	}

	public String getMinzu() {
		return minzu;
	}

	public void setShengao(String shengao) {
		this.shengao = shengao;
	}

	public String getShengao() {
		return shengao;
	}

	public void setTizhong(String tizhong) {
		this.tizhong = tizhong;
	}

	public String getTizhong() {
		return tizhong;
	}

	public void setZysl(int zysl) {
		this.zysl = zysl;
	}

	public int getZysl() {
		return zysl;
	}

	public void setYysl(int yysl) {
		this.yysl = yysl;
	}

	public int getYysl() {
		return yysl;
	}

	public void setJtdh(String jtdh) {
		this.jtdh = jtdh;
	}

	public String getJtdh() {
		return jtdh;
	}

	public void setHjszd(String hjszd) {
		this.hjszd = hjszd;
	}

	public String getHjszd() {
		return hjszd;
	}

	public void setDaszd(String daszd) {
		this.daszd = daszd;
	}

	public String getDaszd() {
		return daszd;
	}

	public void setZzmm(String zzmm) {
		this.zzmm = zzmm;
	}

	public String getZzmm() {
		return zzmm;
	}

	public void setZjxy(String zjxy) {
		this.zjxy = zjxy;
	}

	public String getZjxy() {
		return zjxy;
	}

	public void setZhuanye(String zhuanye) {
		this.zhuanye = zhuanye;
	}

	public String getZhuanye() {
		return zhuanye;
	}

	public void setByyx(String byyx) {
		this.byyx = byyx;
	}

	public String getByyx() {
		return byyx;
	}

	public void setRzrq(String rzrq) {
		this.rzrq = rzrq;
	}

	public String getRzrq() {
		return rzrq;
	}

	public void setXzz(String xzz) {
		this.xzz = xzz;
	}

	public String getXzz() {
		return xzz;
	}

	public void setXzzdh(String xzzdh) {
		this.xzzdh = xzzdh;
	}

	public String getXzzdh() {
		return xzzdh;
	}

	public void setJzhm(String jzhm) {
		this.jzhm = jzhm;
	}

	public String getJzhm() {
		return jzhm;
	}

	public void setYuzhong(String yuzhong) {
		this.yuzhong = yuzhong;
	}

	public String getYuzhong() {
		return yuzhong;
	}

	public void setWysp(String wysp) {
		this.wysp = wysp;
	}

	public String getWysp() {
		return wysp;
	}

	public void setXuexing(String xuexing) {
		this.xuexing = xuexing;
	}

	public String getXuexing() {
		return xuexing;
	}

	public void setAihao(String aihao) {
		this.aihao = aihao;
	}

	public String getAihao() {
		return aihao;
	}

	public void setTechang(String techang) {
		this.techang = techang;
	}

	public String getTechang() {
		return techang;
	}

	public void setMqgzzt(String mqgzzt) {
		this.mqgzzt = mqgzzt;
	}

	public String getMqgzzt() {
		return mqgzzt;
	}

	public void setDzxx(String dzxx) {
		this.dzxx = dzxx;
	}

	public String getDzxx() {
		return dzxx;
	}

	public void setDlzt(String dlzt) {
		this.dlzt = dlzt;
	}

	public String getDlzt() {
		return dlzt;
	}

	public void setGrlbbm(String grlbbm) {
		this.grlbbm = grlbbm;
	}

	public String getGrlbbm() {
		return grlbbm;
	}

	public void setGrzhye(String grzhye) {
		this.grzhye = grzhye;
	}

	public String getGrzhye() {
		return grzhye;
	}

	public void setGryjce(String gryjce) {
		this.gryjce = gryjce;
	}

	public String getGryjce() {
		return gryjce;
	}

	public void setDwyjce(String dwyjce) {
		this.dwyjce = dwyjce;
	}

	public String getDwyjce() {
		return dwyjce;
	}

	public void setGrjcjs(String grjcjs) {
		this.grjcjs = grjcjs;
	}

	public String getGrjcjs() {
		return grjcjs;
	}

	public void setGrckzhhm(String grckzhhm) {
		this.grckzhhm = grckzhhm;
	}

	public String getGrckzhhm() {
		return grckzhhm;
	}

	public void setGrckzhkhyhmc(String grckzhkhyhmc) {
		this.grckzhkhyhmc = grckzhkhyhmc;
	}

	public String getGrckzhkhyhmc() {
		return grckzhkhyhmc;
	}

	public void setGrckzhkhyhdm(String grckzhkhyhdm) {
		this.grckzhkhyhdm = grckzhkhyhdm;
	}

	public String getGrckzhkhyhdm() {
		return grckzhkhyhdm;
	}

	public void setGrckzhhmssyhbm(String grckzhhmssyhbm) {
		this.grckzhhmssyhbm = grckzhhmssyhbm;
	}

	public String getGrckzhhmssyhbm() {
		return grckzhhmssyhbm;
	}

	public void setZhsfdj(String zhsfdj) {
		this.zhsfdj = zhsfdj;
	}

	public String getZhsfdj() {
		return zhsfdj;
	}

	public void setGrzhzt(String grzhzt) {
		this.grzhzt = grzhzt;
	}

	public String getGrzhzt() {
		return grzhzt;
	}

	public void setJzny(String jzny) {
		this.jzny = jzny;
	}

	public String getJzny() {
		return jzny;
	}

	public void setDwzh(String dwzh) {
		this.dwzh = dwzh;
	}

	public String getDwzh() {
		return dwzh;
	}

	public void setYjce(String yjce) {
		this.yjce = yjce;
	}

	public String getYjce() {
		return yjce;
	}

	public void setGrjcl(String grjcl) {
		this.grjcl = grjcl;
	}

	public String getGrjcl() {
		return grjcl;
	}

	public void setDwjcl(String dwjcl) {
		this.dwjcl = dwjcl;
	}

	public String getDwjcl() {
		return dwjcl;
	}

	public void setJcbl(String jcbl) {
		this.jcbl = jcbl;
	}

	public String getJcbl() {
		return jcbl;
	}

	public void setDkqk(String dkqk) {
		this.dkqk = dkqk;
	}

	public String getDkqk() {
		return dkqk;
	}

	public void setDkcs(String dkcs) {
		this.dkcs = dkcs;
	}

	public String getDkcs() {
		return dkcs;
	}

	public void setSsbmbm(String ssbmbm) {
		this.ssbmbm = ssbmbm;
	}

	public String getSsbmbm() {
		return ssbmbm;
	}

	public void setSsbmbm1(String ssbmbm1) {
		this.ssbmbm1 = ssbmbm1;
	}

	public String getSsbmbm1() {
		return ssbmbm1;
	}

	public void setJcblmc(String jcblmc) {
		this.jcblmc = jcblmc;
	}

	public String getJcblmc() {
		return jcblmc;
	}

	public void setGrckzhkhyhhh(String grckzhkhyhhh) {
		this.grckzhkhyhhh = grckzhkhyhhh;
	}

	public String getGrckzhkhyhhh() {
		return grckzhkhyhhh;
	}

	public void setBianzhi(String bianzhi) {
		this.bianzhi = bianzhi;
	}

	public String getBianzhi() {
		return bianzhi;
	}

	public void setHklx(String hklx) {
		this.hklx = hklx;
	}

	public String getHklx() {
		return hklx;
	}

	public void setDqdjje(String dqdjje) {
		this.dqdjje = dqdjje;
	}

	public String getDqdjje() {
		return dqdjje;
	}

	public void setHmdzt(String hmdzt) {
		this.hmdzt = hmdzt;
	}

	public String getHmdzt() {
		return hmdzt;
	}

	public void setHjzt(String hjzt) {
		this.hjzt = hjzt;
	}

	public String getHjzt() {
		return hjzt;
	}

	public void setBnzq(String bnzq) {
		this.bnzq = bnzq;
	}

	public String getBnzq() {
		return bnzq;
	}

	public void setBnjc(String bnjc) {
		this.bnjc = bnjc;
	}

	public String getBnjc() {
		return bnjc;
	}

	public void setDbqk(String dbqk) {
		this.dbqk = dbqk;
	}

	public String getDbqk() {
		return dbqk;
	}

	public void setTqrq(String tqrq) {
		this.tqrq = tqrq;
	}

	public String getTqrq() {
		return tqrq;
	}

	public void setTqjehj(String tqjehj) {
		this.tqjehj = tqjehj;
	}

	public String getTqjehj() {
		return tqjehj;
	}

	@Override
	public String toString() {
		return "HousingDaQingUserinfo [totalcount=" + totalcount + ", success=" + success + ", dwbh=" + dwbh + ", dwmc="
				+ dwmc + ", grbh=" + grbh + ", grzh=" + grzh + ", xingming=" + xingming + ", xmqp=" + xmqp
				+ ", xingbie=" + xingbie + ", gddhhm=" + gddhhm + ", sjhm=" + sjhm + ", zjlx=" + zjlx + ", zjhm=" + zjhm
				+ ", csny=" + csny + ", hyzk=" + hyzk + ", zhiye=" + zhiye + ", zhichen=" + zhichen + ", zhiwu=" + zhiwu
				+ ", xueli=" + xueli + ", yzbm=" + yzbm + ", jtzz=" + jtzz + ", jtysr=" + jtysr + ", xmjp=" + xmjp
				+ ", cjgzrq=" + cjgzrq + ", szbm=" + szbm + ", txdz=" + txdz + ", poxm=" + poxm + ", pozjlx=" + pozjlx
				+ ", pozjhm=" + pozjhm + ", zfqk=" + zfqk + ", sfyzfdk=" + sfyzfdk + ", cdgx=" + cdgx + ", sbzh=" + sbzh
				+ ", grysr=" + grysr + ", xydj=" + xydj + ", jgbm=" + jgbm + ", djrq=" + djrq + ", djsj=" + djsj
				+ ", djczyid=" + djczyid + ", djczy=" + djczy + ", cym=" + cym + ", ywm=" + ywm + ", csd=" + csd
				+ ", minzu=" + minzu + ", shengao=" + shengao + ", tizhong=" + tizhong + ", zysl=" + zysl + ", yysl="
				+ yysl + ", jtdh=" + jtdh + ", hjszd=" + hjszd + ", daszd=" + daszd + ", zzmm=" + zzmm + ", zjxy="
				+ zjxy + ", zhuanye=" + zhuanye + ", byyx=" + byyx + ", rzrq=" + rzrq + ", xzz=" + xzz + ", xzzdh="
				+ xzzdh + ", jzhm=" + jzhm + ", yuzhong=" + yuzhong + ", wysp=" + wysp + ", xuexing=" + xuexing
				+ ", aihao=" + aihao + ", techang=" + techang + ", mqgzzt=" + mqgzzt + ", dzxx=" + dzxx + ", dlzt="
				+ dlzt + ", grlbbm=" + grlbbm + ", grzhye=" + grzhye + ", gryjce=" + gryjce + ", dwyjce=" + dwyjce
				+ ", grjcjs=" + grjcjs + ", grckzhhm=" + grckzhhm + ", grckzhkhyhmc=" + grckzhkhyhmc + ", grckzhkhyhdm="
				+ grckzhkhyhdm + ", grckzhhmssyhbm=" + grckzhhmssyhbm + ", zhsfdj=" + zhsfdj + ", grzhzt=" + grzhzt
				+ ", jzny=" + jzny + ", dwzh=" + dwzh + ", yjce=" + yjce + ", grjcl=" + grjcl + ", dwjcl=" + dwjcl
				+ ", jcbl=" + jcbl + ", dkqk=" + dkqk + ", dkcs=" + dkcs + ", ssbmbm=" + ssbmbm + ", ssbmbm1=" + ssbmbm1
				+ ", jcblmc=" + jcblmc + ", grckzhkhyhhh=" + grckzhkhyhhh + ", bianzhi=" + bianzhi + ", hklx=" + hklx
				+ ", dqdjje=" + dqdjje + ", hmdzt=" + hmdzt + ", hjzt=" + hjzt + ", bnzq=" + bnzq + ", bnjc=" + bnjc
				+ ", dbqk=" + dbqk + ", tqrq=" + tqrq + ", tqjehj=" + tqjehj + ", userid=" + userid + ", taskid="
				+ taskid + "]";
	}

	
}
