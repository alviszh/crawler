package com.crawler.wechat.json;

import java.io.Serializable;
import java.util.List;
public class BasicUserWechat  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5133365472766002696L;

	private String name;		//姓名
	
	private String idnum;		//身份证号
	
	private Integer auth;		//认证结果   1.通过  2.不通过		 

    private List<TaskWeChat> taskWechat;

	@Override
	public String toString() {
		return "BasicUserWechat [name=" + name + ", idnum=" + idnum + ", auth=" + auth + ", taskWechat=" + taskWechat
				+ "]";
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

	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

	public List<TaskWeChat> getTaskWechat() {
		return taskWechat;
	}

	public void setTaskWechat(List<TaskWeChat> taskWechat) {
		this.taskWechat = taskWechat;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	


}
