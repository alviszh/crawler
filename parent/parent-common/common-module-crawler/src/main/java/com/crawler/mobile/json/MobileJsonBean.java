package com.crawler.mobile.json;

import java.io.Serializable;

/**
 * @Description: MobileJsonBean
 * @author zzhen
 * @date 2017年6月19日 上午10:21:40
 */
public class MobileJsonBean  implements Serializable {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -5115154845415106526L;
	public Long id;
	public String mobileNum;
	public String mobileOperator;
	public String username;
	public String idnum;
	public String task_id;
	public String password;
	public boolean spec;
	private String owner;//数据所属人
	private String key; //唯一标识

	public String themeColor; //主题颜色
	
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getMobileOperator() {
		return mobileOperator;
	}
	public void setMobileOperator(String mobileOperator) {
		this.mobileOperator = mobileOperator;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSpec() {
		return spec;
	}

	public void setSpec(boolean spec) {
		this.spec = spec;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getThemeColor() {
		return themeColor;
	}

	public void setThemeColor(String themeColor) {
		this.themeColor = themeColor;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "MobileJsonBean{" +
				"id=" + id +
				", mobileNum='" + mobileNum + '\'' +
				", mobileOperator='" + mobileOperator + '\'' +
				", username='" + username + '\'' +
				", idnum='" + idnum + '\'' +
				", task_id='" + task_id + '\'' +
				", password='" + password + '\'' +
				", spec=" + spec +
				", owner='" + owner + '\'' +
				", key='" + key + '\'' +
				", themeColor='" + themeColor + '\'' +
				'}';
	}
}
