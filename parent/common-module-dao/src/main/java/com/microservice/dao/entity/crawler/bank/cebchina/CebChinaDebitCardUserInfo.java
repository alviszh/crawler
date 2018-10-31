package com.microservice.dao.entity.crawler.bank.cebchina;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cebchina_debitcard_userinfo")
public class CebChinaDebitCardUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -4887796426842238964L;
	private String taskid;
	
	private String loginName;//登录帐号
	
	private String name;//姓名
	
	private String brithday;//出生日期
	
	private String idcare;//身份证
	
	private String address;//地址
	
	private String lxaddress;//联系地址
	
	private String postal;//邮政编码
	
	private String lxphone;//联系电话
	
	private String khrq;//开户日期
	
	private String phone;//手机号码
	
	private String sex;//性别
	
	private String kanum;//卡号
	
	private String kainame;//开户行

	
	public String getKanum() {
		return kanum;
	}

	public void setKanum(String kanum) {
		this.kanum = kanum;
	}

	public String getKainame() {
		return kainame;
	}

	public void setKainame(String kainame) {
		this.kainame = kainame;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getIdcare() {
		return idcare;
	}

	public void setIdcare(String idcare) {
		this.idcare = idcare;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLxaddress() {
		return lxaddress;
	}

	public void setLxaddress(String lxaddress) {
		this.lxaddress = lxaddress;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getLxphone() {
		return lxphone;
	}

	public void setLxphone(String lxphone) {
		this.lxphone = lxphone;
	}

	public String getKhrq() {
		return khrq;
	}

	public void setKhrq(String khrq) {
		this.khrq = khrq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public CebChinaDebitCardUserInfo() {
		super();
	}

	public CebChinaDebitCardUserInfo(String taskid, String loginName, String name, String brithday, String idcare,
			String address, String lxaddress, String postal, String lxphone, String khrq, String phone, String sex,
			String kanum, String kainame) {
		super();
		this.taskid = taskid;
		this.loginName = loginName;
		this.name = name;
		this.brithday = brithday;
		this.idcare = idcare;
		this.address = address;
		this.lxaddress = lxaddress;
		this.postal = postal;
		this.lxphone = lxphone;
		this.khrq = khrq;
		this.phone = phone;
		this.sex = sex;
		this.kanum = kanum;
		this.kainame = kainame;
	}

	@Override
	public String toString() {
		return "CebChinaDebitCardUserInfo [taskid=" + taskid + ", loginName=" + loginName + ", name=" + name
				+ ", brithday=" + brithday + ", idcare=" + idcare + ", address=" + address + ", lxaddress=" + lxaddress
				+ ", postal=" + postal + ", lxphone=" + lxphone + ", khrq=" + khrq + ", phone=" + phone + ", sex=" + sex
				+ "]";
	}

	
	
	
}
