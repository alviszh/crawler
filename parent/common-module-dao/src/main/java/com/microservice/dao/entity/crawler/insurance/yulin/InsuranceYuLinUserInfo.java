package com.microservice.dao.entity.crawler.insurance.yulin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_yulin_userinfo",indexes = {@Index(name = "index_insurance_yulin_userinfo_taskid", columnList = "taskid")})
public class InsuranceYuLinUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String taskid;
	
	private String name;
	
	private String sex;
	
	private String nation;
	
	private String idcardtype;
	
	private String idcard;
	
	private String num;
	
	private String category_type;
	
	private String administrative_region;
	
	private String domicile_place;
	
	private String monte_carlo_region;
	
	private String postal_code;
	
	private String address;
	
	private String tel;
	
	private String office_phone;
	
	private String fax;
	
	private String brithday;
	
	private String takejobtime;
	
	private String state;
	
	private String txstate;
	
	private String txtime;
	
	private String category;
	
	private String yiliaotime;
	
	private String cardnum;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getIdcardtype() {
		return idcardtype;
	}

	public void setIdcardtype(String idcardtype) {
		this.idcardtype = idcardtype;
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

	public String getCategory_type() {
		return category_type;
	}

	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}

	public String getAdministrative_region() {
		return administrative_region;
	}

	public void setAdministrative_region(String administrative_region) {
		this.administrative_region = administrative_region;
	}

	public String getDomicile_place() {
		return domicile_place;
	}

	public void setDomicile_place(String domicile_place) {
		this.domicile_place = domicile_place;
	}

	public String getMonte_carlo_region() {
		return monte_carlo_region;
	}

	public void setMonte_carlo_region(String monte_carlo_region) {
		this.monte_carlo_region = monte_carlo_region;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getOffice_phone() {
		return office_phone;
	}

	public void setOffice_phone(String office_phone) {
		this.office_phone = office_phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getTakejobtime() {
		return takejobtime;
	}

	public void setTakejobtime(String takejobtime) {
		this.takejobtime = takejobtime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTxstate() {
		return txstate;
	}

	public void setTxstate(String txstate) {
		this.txstate = txstate;
	}

	public String getTxtime() {
		return txtime;
	}

	public void setTxtime(String txtime) {
		this.txtime = txtime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getYiliaotime() {
		return yiliaotime;
	}

	public void setYiliaotime(String yiliaotime) {
		this.yiliaotime = yiliaotime;
	}

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public InsuranceYuLinUserInfo(String taskid, String name, String sex, String nation, String idcardtype,
			String idcard, String num, String category_type, String administrative_region, String domicile_place,
			String monte_carlo_region, String postal_code, String address, String tel, String office_phone, String fax,
			String brithday, String takejobtime, String state, String txstate, String txtime, String category,
			String yiliaotime, String cardnum) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.sex = sex;
		this.nation = nation;
		this.idcardtype = idcardtype;
		this.idcard = idcard;
		this.num = num;
		this.category_type = category_type;
		this.administrative_region = administrative_region;
		this.domicile_place = domicile_place;
		this.monte_carlo_region = monte_carlo_region;
		this.postal_code = postal_code;
		this.address = address;
		this.tel = tel;
		this.office_phone = office_phone;
		this.fax = fax;
		this.brithday = brithday;
		this.takejobtime = takejobtime;
		this.state = state;
		this.txstate = txstate;
		this.txtime = txtime;
		this.category = category;
		this.yiliaotime = yiliaotime;
		this.cardnum = cardnum;
	}

	public InsuranceYuLinUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InsuranceYuLinUserInfo [taskid=" + taskid + ", name=" + name + ", sex=" + sex + ", nation=" + nation
				+ ", idcardtype=" + idcardtype + ", idcard=" + idcard + ", num=" + num + ", category_type="
				+ category_type + ", administrative_region=" + administrative_region + ", domicile_place="
				+ domicile_place + ", monte_carlo_region=" + monte_carlo_region + ", postal_code=" + postal_code
				+ ", address=" + address + ", tel=" + tel + ", office_phone=" + office_phone + ", fax=" + fax
				+ ", brithday=" + brithday + ", takejobtime=" + takejobtime + ", state=" + state + ", txstate="
				+ txstate + ", txtime=" + txtime + ", category=" + category + ", yiliaotime=" + yiliaotime
				+ ", cardnum=" + cardnum + "]";
	}
	
	

}
