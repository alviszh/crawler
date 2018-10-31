package com.microservice.dao.entity.crawler.housing.chengdu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_chengdu_userinfo" ,indexes = {@Index(name = "index_housing_chengdu_userinfo_taskid", columnList = "taskid")})
public class HousingChengduUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 个人账号
	 */
	private String percode;

	/**
	 * 个人姓名
	 */
	private String pername1;

	/**
	 * 证件类型
	 *  "id": "1","name": "居民身份证",
	 *  "id":"11","name":"简阳职工1"
	 *  "id":"12","name":"简阳职工2"
	 *  "id":"13","name":"简阳职工3"
	 *  "id":"2","name":"军官证"
	 *  "id":"3","name":"士兵证"
	 *  "id":"4","name":"护照"
	 *  "id":"5","name":"重复身份证号"
	 *  "id":"6","name":"其他"
	 */
	private String idtype;

	/**
	 * 证件号码
	 */
	private String idcard1;

	/**
	 * 出生日期
	 */
	private String birthday;

	/**
	 * 个人邮箱
	 */
	private String mail;

	/**
	 * 移动电话
	 */
	private String phone1;

	/**
	 * 面签状态
	 * 'id':'1','name':'已面签'
	 * 'id':'2','name':'面签中'
	 * 'id':'0','name':'未面签'
	 */
	private String signflag_desc;

	/**
	 * 发卡银行
	 * "id":"00","name":"中心"
	 * "id":"01","name":"建设银行"
	 * "id":"02","name":"工商银行"
	 * "id":"03","name":"农业银行"
	 * "id":"04","name":"成都银行"
	 * "id":"05","name":"中国银行"
	 * "id":"06","name":"深发展银行"
	 * "id":"08","name":"交通银行"
	 * "id":"10","name":"兴业银行"
	 * "id":"11","name":"浦发银行"
	 * "id":"12","name":"中信银行"
	 * "id":"13","name":"招商银行"
	 * "id":"15","name":"成都农商银行"
	 * "id":"16","name":"民生村镇银行"
	 * "id":"20","name":"民生银行"
	 * "id":"90","name":"网上营业厅"
	 */
	private String banktype;

	/**
	 * 联名卡卡号
	 */
	private String bankcode;
	
	/**
	 * 缴存基数
	 */
	private String basemny;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPercode() {
		return percode;
	}

	public void setPercode(String percode) {
		this.percode = percode;
	}

	public String getPername1() {
		return pername1;
	}

	public void setPername1(String pername1) {
		this.pername1 = pername1;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIdcard1() {
		return idcard1;
	}

	public void setIdcard1(String idcard1) {
		this.idcard1 = idcard1;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getSignflag_desc() {
		return signflag_desc;
	}

	public void setSignflag_desc(String signflag_desc) {
		this.signflag_desc = signflag_desc;
	}

	public String getBanktype() {
		return banktype;
	}

	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getBasemny() {
		return basemny;
	}

	public void setBasemny(String basemny) {
		this.basemny = basemny;
	}

	@Override
	public String toString() {
		return "HousingChengduUserInfo [taskid=" + taskid + ", percode=" + percode + ", pername1=" + pername1
				+ ", idtype=" + idtype + ", idcard1=" + idcard1 + ", birthday=" + birthday + ", mail=" + mail
				+ ", phone1=" + phone1 + ", signflag_desc=" + signflag_desc + ", banktype=" + banktype + ", bankcode="
				+ bankcode + ", basemny=" + basemny + "]";
	}

	public HousingChengduUserInfo(String taskid, String percode, String pername1, String idtype, String idcard1,
			String birthday, String mail, String phone1, String signflag_desc, String banktype, String bankcode,
			String basemny) {
		super();
		this.taskid = taskid;
		this.percode = percode;
		this.pername1 = pername1;
		this.idtype = idtype;
		this.idcard1 = idcard1;
		this.birthday = birthday;
		this.mail = mail;
		this.phone1 = phone1;
		this.signflag_desc = signflag_desc;
		this.banktype = banktype;
		this.bankcode = bankcode;
		this.basemny = basemny;
	}

	public HousingChengduUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
