package com.microservice.dao.entity.crawler.bank.abcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="abcchina_userinfo",indexes = {@Index(name = "index_abcchina_userinfo_taskid", columnList = "taskid")})
public class AbcChinaUserInfo extends IdEntity{
	private static final long serialVersionUID = -7225639204374657354L;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	/** 客户号 */
	@Column(name="khh")
	private String khh;
	/** 客户姓名 */
	@Column(name="khxm")
	private String khxm;
	/** 证件类型 */
	@Column(name="zjlx")
	private String zjlx;
	/** 证件号 */
	@Column(name="zjh")
	private String zjh;
	/** 证件有效期起始日期 */
	@Column(name="zjyxqqsrq")
	private String zjyxqqsrq;
	/** 证件有效期结束日期 */
	@Column(name="zjyxqjsrq")
	private String zjyxqjsrq;
	/** 网银注册行 */
	@Column(name="wyzch")
	private String wyzch;
	/** 出生日期 */
	@Column(name="csrq")
	private String csrq;
	/** 国籍 */
	@Column(name="gj")
	private String gj;
	/** 移动电话 */
	@Column(name="yddh")
	private String yddh;
	/** Email */
	@Column(name="email")
	private String email;
	/** 单位名称 */
	@Column(name="dwmc")
	private String dwmc;
	/** 单位电话 */
	@Column(name="dwdh")
	private String dwdh;
	/** 单位邮编 */
	@Column(name="dwyb")
	private String dwyb;
	/** 家庭电话 */
	@Column(name="jtdh")
	private String jtdh;
	/** 家庭邮编 */
	@Column(name="jtyb")
	private String jtyb;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getKhh() {
		return khh;
	}
	public void setKhh(String khh) {
		this.khh = khh;
	}
	public String getKhxm() {
		return khxm;
	}
	public void setKhxm(String khxm) {
		this.khxm = khxm;
	}
	public String getZjlx() {
		return zjlx;
	}
	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}
	public String getZjh() {
		return zjh;
	}
	public void setZjh(String zjh) {
		this.zjh = zjh;
	}
	public String getZjyxqqsrq() {
		return zjyxqqsrq;
	}
	public void setZjyxqqsrq(String zjyxqqsrq) {
		this.zjyxqqsrq = zjyxqqsrq;
	}
	public String getZjyxqjsrq() {
		return zjyxqjsrq;
	}
	public void setZjyxqjsrq(String zjyxqjsrq) {
		this.zjyxqjsrq = zjyxqjsrq;
	}
	public String getWyzch() {
		return wyzch;
	}
	public void setWyzch(String wyzch) {
		this.wyzch = wyzch;
	}
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	public String getGj() {
		return gj;
	}
	public void setGj(String gj) {
		this.gj = gj;
	}
	public String getYddh() {
		return yddh;
	}
	public void setYddh(String yddh) {
		this.yddh = yddh;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getDwdh() {
		return dwdh;
	}
	public void setDwdh(String dwdh) {
		this.dwdh = dwdh;
	}
	public String getDwyb() {
		return dwyb;
	}
	public void setDwyb(String dwyb) {
		this.dwyb = dwyb;
	}
	public String getJtdh() {
		return jtdh;
	}
	public void setJtdh(String jtdh) {
		this.jtdh = jtdh;
	}
	public String getJtyb() {
		return jtyb;
	}
	public void setJtyb(String jtyb) {
		this.jtyb = jtyb;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
