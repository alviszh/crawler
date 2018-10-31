package com.microservice.dao.entity.crawler.qq;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.pbccrc.AbstractEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="qq_friend")
public class QQFriend extends AbstractEntity implements Serializable{

	private String taskid;
	
	private String uin;
	
	private String name;
	
	private String score;
	
	private String img;
	
	private String yellow;
	
	private String is_special;
	
	private String is_xy;
	
	private String xyname;
	
	private String xyurl;
	
	private String realname;
	
	private String groupid;
	
	private String gpname;
	

	public String getYellow() {
		return yellow;
	}

	public void setYellow(String yellow) {
		this.yellow = yellow;
	}

	public String getIs_special() {
		return is_special;
	}

	public void setIs_special(String is_special) {
		this.is_special = is_special;
	}

	public String getIs_xy() {
		return is_xy;
	}

	public void setIs_xy(String is_xy) {
		this.is_xy = is_xy;
	}

	public String getXyname() {
		return xyname;
	}

	public void setXyname(String xyname) {
		this.xyname = xyname;
	}

	public String getXyurl() {
		return xyurl;
	}

	public void setXyurl(String xyurl) {
		this.xyurl = xyurl;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGpname() {
		return gpname;
	}

	public void setGpname(String gpname) {
		this.gpname = gpname;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	
}
