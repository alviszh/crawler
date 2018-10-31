package com.crawler.mobile.json;

public class UnicomChangePasswordResult {

	
	private String usrnum; //手机号码
		
	private String certNum;//身份证后6位
	
	private String mobileMsg;//手机验证码
	
	private String newpassword;//新密码
		
    private Integer user_id;	
    
    private String task_id;
    
	public String getUsrnum() {
		return usrnum;
	}

	public void setUsrnum(String usrnum) {
		this.usrnum = usrnum;
	}

	public String getCertNum() {
		return certNum;
	}

	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}

	public String getMobileMsg() {
		return mobileMsg;
	}

	public void setMobileMsg(String mobileMsg) {
		this.mobileMsg = mobileMsg;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	
	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	@Override
	public String toString() {
		return "UnicomChangePasswordResult [usrnum=" + usrnum + ", certNum=" + certNum + ", mobileMsg=" + mobileMsg
				+ ", newpassword=" + newpassword + ", user_id=" + user_id + ", task_id=" + task_id + "]";
	}
	
}
