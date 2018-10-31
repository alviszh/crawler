package com.microservice.dao.entity.crawler.wechat;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="wechat_personal_userinfo",indexes = {@Index(name = "index_wechat_personal_userinfo_taskid", columnList = "taskid")})
public class WeChatPersonalUserInfo extends IdEntity{

	private String taskid;
	
	private String nickName;//姓名
	
	private String signature;//个性签名或者简介
	
	private String city;
	
	private String province;
	
	private String headImgUrl;//头像地址
	
	private String contactFlag;
	
	private String remarkName;//备注
	
	private String sex;
	
	private String starFriend;//星标朋友
	
	private String keyWord;//微信号前三个字母

	@Override
	public String toString() {
		return "WeChatPersonalUserInfo [taskid=" + taskid + ", nickName=" + nickName + ", signature=" + signature
				+ ", city=" + city + ", province=" + province + ", headImgUrl=" + headImgUrl + ", contactFlag="
				+ contactFlag + ", remarkName=" + remarkName + ", sex=" + sex + ", starFriend=" + starFriend
				+ ", keyWord=" + keyWord + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getContactFlag() {
		return contactFlag;
	}

	public void setContactFlag(String contactFlag) {
		this.contactFlag = contactFlag;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getStarFriend() {
		return starFriend;
	}

	public void setStarFriend(String starFriend) {
		this.starFriend = starFriend;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	
}
