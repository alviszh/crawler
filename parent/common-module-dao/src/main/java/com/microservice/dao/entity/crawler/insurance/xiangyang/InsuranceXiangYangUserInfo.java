package com.microservice.dao.entity.crawler.insurance.xiangyang;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xiangyang_userinfo")
public class InsuranceXiangYangUserInfo extends IdEntity{
	private String taskid;//uuid 前端通过uuid访问状态结果	
	private String num;//社保证号码
	private String name;//姓名
	private String idcard;//身份证号码
	private String sex;//性别
	private String qyyl;//企业养老保险
	private String jgsyyl;//机关事业养老保险
	private String yiliao;//基本医疗保险
	private String zyyiliao;//基本医疗保险(仅住院)
	private String gongshang;//工伤保险
	private String shengyu;//生育保险
	private String shiye;//失业保险
	private String gwyylbz;//公务员医疗补助保险
	private String dejz;//大额救助保险
	private String lxgbyl;//离休干部医疗保险
	private String dbdxyl;//低保对象医疗保险
	private String nmgzh;//农民工综合保险
	private String dw_name;//单位名称
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getQyyl() {
		return qyyl;
	}
	public void setQyyl(String qyyl) {
		this.qyyl = qyyl;
	}
	public String getJgsyyl() {
		return jgsyyl;
	}
	public void setJgsyyl(String jgsyyl) {
		this.jgsyyl = jgsyyl;
	}
	public String getYiliao() {
		return yiliao;
	}
	public void setYiliao(String yiliao) {
		this.yiliao = yiliao;
	}
	public String getZyyiliao() {
		return zyyiliao;
	}
	public void setZyyiliao(String zyyiliao) {
		this.zyyiliao = zyyiliao;
	}
	public String getGongshang() {
		return gongshang;
	}
	public void setGongshang(String gongshang) {
		this.gongshang = gongshang;
	}
	public String getShengyu() {
		return shengyu;
	}
	public void setShengyu(String shengyu) {
		this.shengyu = shengyu;
	}
	public String getShiye() {
		return shiye;
	}
	public void setShiye(String shiye) {
		this.shiye = shiye;
	}
	public String getGwyylbz() {
		return gwyylbz;
	}
	public void setGwyylbz(String gwyylbz) {
		this.gwyylbz = gwyylbz;
	}
	public String getDejz() {
		return dejz;
	}
	public void setDejz(String dejz) {
		this.dejz = dejz;
	}
	public String getLxgbyl() {
		return lxgbyl;
	}
	public void setLxgbyl(String lxgbyl) {
		this.lxgbyl = lxgbyl;
	}
	public String getDbdxyl() {
		return dbdxyl;
	}
	public void setDbdxyl(String dbdxyl) {
		this.dbdxyl = dbdxyl;
	}
	public String getNmgzh() {
		return nmgzh;
	}
	public void setNmgzh(String nmgzh) {
		this.nmgzh = nmgzh;
	}
	public String getDw_name() {
		return dw_name;
	}
	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}
	public InsuranceXiangYangUserInfo(String taskid, String num, String name, String idcard, String sex, String qyyl,
			String jgsyyl, String yiliao, String zyyiliao, String gongshang, String shengyu, String shiye,
			String gwyylbz, String dejz, String lxgbyl, String dbdxyl, String nmgzh, String dw_name) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.name = name;
		this.idcard = idcard;
		this.sex = sex;
		this.qyyl = qyyl;
		this.jgsyyl = jgsyyl;
		this.yiliao = yiliao;
		this.zyyiliao = zyyiliao;
		this.gongshang = gongshang;
		this.shengyu = shengyu;
		this.shiye = shiye;
		this.gwyylbz = gwyylbz;
		this.dejz = dejz;
		this.lxgbyl = lxgbyl;
		this.dbdxyl = dbdxyl;
		this.nmgzh = nmgzh;
		this.dw_name = dw_name;
	}
	public InsuranceXiangYangUserInfo() {
		super();
	}
	
	
}
