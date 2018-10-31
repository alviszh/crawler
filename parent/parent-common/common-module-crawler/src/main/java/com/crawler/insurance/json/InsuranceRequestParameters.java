package com.crawler.insurance.json;

import java.io.Serializable;


public class InsuranceRequestParameters implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5115154845415106526L;


    public String taskId;
    public String city;                //所属城市
    public String loginType;
    public String name;
    public String idnum;
    public String username;
    public String message;            //短信验证码
    public String area;          //赣州社保需要区号

	private String ip;
	private String port;
	private String webdriverHandle;//selenium 的window hander

	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 修改 2017.11.21   @Douge_Chow
     * 丹东社保 登录需要用户身份证号
     */
    public String userIDNum;

    public String getUserIDNum() {
        return userIDNum;
    }

    public void setUserIDNum(String userIDNum) {
        this.userIDNum = userIDNum;
    }

    public String password;
    //烟台社保对应的图片验证码
    public String verification;

    public String base64;


    

    @Override
	public String toString() {
		return "InsuranceRequestParameters [taskId=" + taskId + ", city=" + city + ", loginType=" + loginType
				+ ", name=" + name + ", username=" + username + ", idnum=" + idnum + ",message=" + message + ", area=" + area + ", ip=" + ip
				+ ", port=" + port + ", webdriverHandle=" + webdriverHandle + ", userIDNum=" + userIDNum + ", password="
				+ password + ", verification=" + verification + ", base64=" + base64 + "]";
	}

	public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getWebdriverHandle() {
		return webdriverHandle;
	}

	public void setWebdriverHandle(String webdriverHandle) {
		this.webdriverHandle = webdriverHandle;
	}
}
