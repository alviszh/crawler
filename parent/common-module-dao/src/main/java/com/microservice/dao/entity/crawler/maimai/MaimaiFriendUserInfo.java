package com.microservice.dao.entity.crawler.maimai;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 脉脉用户朋友的基本信息
 * @author zh
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="maimai_friend_user_info",indexes = {@Index(name = "index_maimai_friend_user_info_taskid", columnList = "taskid")})
public class MaimaiFriendUserInfo extends IdEntity{

	private String taskid;
	private String mm_id;     //脉脉账号在脉脉系统中的ID
	private String friend_id;  //朋友在脉脉系统中的ID
	private String name;      //朋友姓名
	private String gender;     //朋友性别(1: 男; 2: 女;)
	private String account;    //朋友的脉脉号(这个号不是脉脉用户的登录账户)
	private String company;    //朋友的工作单位
	private String position;   //工作职务
	private String birthday;   //朋友生日
	private String mobile;     //朋友手机号
	private String phone;      //朋友电话
	private String email;      //朋友邮箱
	private String province;   //朋友当前所在的省份
	private String city;       //朋友当前所在的城市	
	private String home_province; //朋友家乡所在的省份	
	private String home_city;   //朋友家乡所在的城市
	private String headline;    //朋友的自我介绍	
	private String tags;        //标签(多个标签之间用逗号分隔)
	private String rank;        //影响力(脉脉系统给用户的评分)
	private String business;    //行业, 方向, 影响力
	
	@Override
	public String toString() {
		return "MaimaiFriendUserInfo [taskid=" + taskid + ", mm_id=" + mm_id + ", name=" + name
				+ ", gender=" + gender+ ", account=" + account+ ", company=" + company
				+ ", position=" + position+ ", birthday=" + birthday+ ", mobile=" + mobile
				+ ", phone=" + phone+ ", email=" + email+ ", province=" + province
				+ ", city=" + city+ ", home_province=" + home_province+ ", home_city=" + home_city
				+ ", headline=" + headline+ ", tags=" + tags+ ", rank=" + rank
				+ ", business=" + business + ", friend_id=" + friend_id + "]";
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getMm_id() {
		return mm_id;
	}
	public void setMm_id(String mm_id) {
		this.mm_id = mm_id;
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
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getHome_province() {
		return home_province;
	}
	public void setHome_province(String home_province) {
		this.home_province = home_province;
	}
	public String getHome_city() {
		return home_city;
	}
	public void setHome_city(String home_city) {
		this.home_city = home_city;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}

	public String getFriend_id() {
		return friend_id;
	}

	public void setFriend_id(String friend_id) {
		this.friend_id = friend_id;
	}

}
