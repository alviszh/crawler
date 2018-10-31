package com.microservice.dao.entity.crawler.housing.hanzhong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_hanzhong_userinfo" ,indexes = {@Index(name = "index_housing_hanzhong_userinfo_taskid", columnList = "taskid")})
public class HousingHanzhongUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 姓名
	 */
	private String xingming;

	/**
	 * 出生年月
	 */
	private String csny;

	/**
	 * 性别
	 */
	private String xingbie;

	/**
	 * 证件类型
	 */
	private String zjlx;

	/**
	 * 证件号码
	 */
	private String zjhm;

	/**
	 * 手机号码
	 */
	private String sjhm;

	/**
	 * 固定电话号码
	 */
	private String gddhhm;

	/**
	 * 邮政编码
	 */
	private String yzbm;

	/**
	 * 家庭月收入
	 */
	private String jtysr;
	/**
	 * 家庭住址
	 */
	private String jtzz;
	/**
	 * 婚姻状况
	 */
	private String hyzk;
	/**
	 * 贷款情况
	 */
	private String dkqk;
	/**
	 * 账户账号
	 */
	private String grzh;
	/**
	 * 账户状态
	 */
	private String grzhzt;
	/**
	 * 账户余额
	 */
	private String grzhye;
	/**
	 * 开户日期
	 */
	private String djrq;
	/**
	 * 单位名称
	 */
	private String dwmc;
	/**
	 * 缴存比例
	 */
	private String jcbl;
	/**
	 * 个人缴存基数
	 */
	private String grjcjs;
	/**
	 * 月缴存额
	 */
	private String yjce;
	/**
	 * 个人存款账户开户银行名称
	 */
	private String grckzhkhyhmc;
	/**
	 * 个人存款账户号码
	 */
	private String grckzhhm;
	
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getXingming() {
		return xingming;
	}
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getCsny() {
		return csny;
	}
	public void setCsny(String csny) {
		this.csny = csny;
	}
	public String getXingbie() {
		return xingbie;
	}
	public void setXingbie(String xingbie) {
		this.xingbie = xingbie;
	}
	public String getZjlx() {
		return zjlx;
	}
	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}
	public String getZjhm() {
		return zjhm;
	}
	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public String getGddhhm() {
		return gddhhm;
	}
	public void setGddhhm(String gddhhm) {
		this.gddhhm = gddhhm;
	}
	public String getYzbm() {
		return yzbm;
	}
	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}
	public String getJtysr() {
		return jtysr;
	}
	public void setJtysr(String jtysr) {
		this.jtysr = jtysr;
	}
	public String getJtzz() {
		return jtzz;
	}
	public void setJtzz(String jtzz) {
		this.jtzz = jtzz;
	}
	public String getHyzk() {
		return hyzk;
	}
	public void setHyzk(String hyzk) {
		this.hyzk = hyzk;
	}
	public String getDkqk() {
		return dkqk;
	}
	public void setDkqk(String dkqk) {
		this.dkqk = dkqk;
	}
	public String getGrzh() {
		return grzh;
	}
	public void setGrzh(String grzh) {
		this.grzh = grzh;
	}
	public String getGrzhzt() {
		return grzhzt;
	}
	public void setGrzhzt(String grzhzt) {
		this.grzhzt = grzhzt;
	}
	public String getGrzhye() {
		return grzhye;
	}
	public void setGrzhye(String grzhye) {
		this.grzhye = grzhye;
	}
	public String getDjrq() {
		return djrq;
	}
	public void setDjrq(String djrq) {
		this.djrq = djrq;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getJcbl() {
		return jcbl;
	}
	public void setJcbl(String jcbl) {
		this.jcbl = jcbl;
	}
	public String getGrjcjs() {
		return grjcjs;
	}
	public void setGrjcjs(String grjcjs) {
		this.grjcjs = grjcjs;
	}
	public String getYjce() {
		return yjce;
	}
	public void setYjce(String yjce) {
		this.yjce = yjce;
	}
	public String getGrckzhkhyhmc() {
		return grckzhkhyhmc;
	}
	public void setGrckzhkhyhmc(String grckzhkhyhmc) {
		this.grckzhkhyhmc = grckzhkhyhmc;
	}
	public String getGrckzhhm() {
		return grckzhhm;
	}
	public void setGrckzhhm(String grckzhhm) {
		this.grckzhhm = grckzhhm;
	}
	public HousingHanzhongUserInfo(String taskid, String xingming, String csny, String xingbie, String zjlx, String zjhm,
			String sjhm, String gddhhm, String yzbm, String jtysr, String jtzz, String hyzk, String dkqk, String grzh,
			String grzhzt, String grzhye, String djrq, String dwmc, String jcbl, String grjcjs, String yjce,
			String grckzhkhyhmc, String grckzhhm) {
		super();
		this.taskid = taskid;
		this.xingming = xingming;
		this.csny = csny;
		this.xingbie = xingbie;
		this.zjlx = zjlx;
		this.zjhm = zjhm;
		this.sjhm = sjhm;
		this.gddhhm = gddhhm;
		this.yzbm = yzbm;
		this.jtysr = jtysr;
		this.jtzz = jtzz;
		this.hyzk = hyzk;
		this.dkqk = dkqk;
		this.grzh = grzh;
		this.grzhzt = grzhzt;
		this.grzhye = grzhye;
		this.djrq = djrq;
		this.dwmc = dwmc;
		this.jcbl = jcbl;
		this.grjcjs = grjcjs;
		this.yjce = yjce;
		this.grckzhkhyhmc = grckzhkhyhmc;
		this.grckzhhm = grckzhhm;
	}
	public HousingHanzhongUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
