package com.crawler.e_commerce.json;

import com.google.gson.Gson;

import java.io.Serializable;

public class E_CommerceJsonBean implements Serializable {
    private String taskid;//任务ID
    private Long id;
    private String idnum;
    private String username;
    private String passwd;
    private String ip;
    private String port;
    private String verfiySMS;
    private String logintype;

    private String webdriverHandle;//selenium 的window hander
    
    public String getLogintype() {
		return logintype;
	}

	public void setLogintype(String logintype) {
		this.logintype = logintype;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public String getVerfiySMS() {
        return verfiySMS;
    }

    public void setVerfiySMS(String verfiySMS) {
        this.verfiySMS = verfiySMS;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
