package app.bean;

import java.util.Date;
import java.util.List;

public class WebDataByID {
	
	public String idnum;
	public List<String> phoneNumbers;
	public String message;
	public Date createtime = new Date();
	public Integer errorCode;
	public String profile;
	
	@Override
	public String toString() {
		return "WebDataByID [idnum=" + idnum + ", phoneNumbers=" + phoneNumbers + ", message=" + message
				+ ", createtime=" + createtime + ", errorCode=" + errorCode + "]";
	}
	
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
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
		
	
}
