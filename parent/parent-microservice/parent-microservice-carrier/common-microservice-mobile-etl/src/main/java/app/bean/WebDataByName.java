package app.bean;

import java.util.Date;
import java.util.List;

public class WebDataByName {

	public String name;
	public List<String> idnums;
	public String message;
	public Date createtime = new Date();
	public Integer errorCode;
	public String profile;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getIdnums() {
		return idnums;
	}

	public void setIdnums(List<String> idnums) {
		this.idnums = idnums;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return "WebDataByName [name=" + name + ", idnums=" + idnums + ", message=" + message + ", createtime="
				+ createtime + ", errorCode=" + errorCode + "]";
	}
	
	
}
