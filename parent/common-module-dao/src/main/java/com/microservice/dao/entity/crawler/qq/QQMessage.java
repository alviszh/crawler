package com.microservice.dao.entity.crawler.qq;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.pbccrc.AbstractEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="qq_message")
public class QQMessage extends AbstractEntity implements Serializable{

	private String taskid;
	
	private String uin;
	
	private String age;
	
	private String spacename;
	
	private String signature;
	
	private String nickname;
	
	private String sex;
	
	private String constellation;
	
	private String avatar;
	
	private String birthyear;
	
	private String birthday;
	
	private String country;
	
	private String province;
	
	private String city;
	
	private String bloodtype;
	
	private String marriage;
	
	private String hco;
	
	private String cco;
	
	private String cp;
	
	private String cc;
	
	private String cb;
	
	private String lover;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSpacename() {
		return spacename;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	@Column(columnDefinition="text")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBirthyear() {
		return birthyear;
	}

	public void setBirthyear(String birthyear) {
		this.birthyear = birthyear;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getHco() {
		return hco;
	}

	public void setHco(String hco) {
		this.hco = hco;
	}

	public String getCco() {
		return cco;
	}

	public void setCco(String cco) {
		this.cco = cco;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getCb() {
		return cb;
	}

	public void setCb(String cb) {
		this.cb = cb;
	}

	public String getLover() {
		return lover;
	}

	public void setLover(String lover) {
		this.lover = lover;
	}

	public QQMessage(String taskid, String uin, String age, String spacename, String signature, String nickname,
			String sex, String constellation, String avatar, String birthyear, String birthday, String country,
			String province, String city, String bloodtype, String marriage, String hco, String cco, String cp,
			String cc, String cb, String lover) {
		super();
		this.taskid = taskid;
		this.uin = uin;
		this.age = age;
		this.spacename = spacename;
		this.signature = signature;
		this.nickname = nickname;
		this.sex = sex;
		this.constellation = constellation;
		this.avatar = avatar;
		this.birthyear = birthyear;
		this.birthday = birthday;
		this.country = country;
		this.province = province;
		this.city = city;
		this.bloodtype = bloodtype;
		this.marriage = marriage;
		this.hco = hco;
		this.cco = cco;
		this.cp = cp;
		this.cc = cc;
		this.cb = cb;
		this.lover = lover;
	}

	public QQMessage() {
		super();
	}

	@Override
	public String toString() {
		return "QQMessage [taskid=" + taskid + ", uin=" + uin + ", age=" + age + ", spacename=" + spacename
				+ ", signature=" + signature + ", nickname=" + nickname + ", sex=" + sex + ", constellation="
				+ constellation + ", avatar=" + avatar + ", birthyear=" + birthyear + ", birthday=" + birthday
				+ ", country=" + country + ", province=" + province + ", city=" + city + ", bloodtype=" + bloodtype
				+ ", marriage=" + marriage + ", hco=" + hco + ", cco=" + cco + ", cp=" + cp + ", cc=" + cc + ", cb="
				+ cb + ", lover=" + lover + "]";
	}
	
	
}
