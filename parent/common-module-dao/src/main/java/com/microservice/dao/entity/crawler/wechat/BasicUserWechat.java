package com.microservice.dao.entity.crawler.wechat;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="basic_user_wechat")
public class BasicUserWechat extends IdEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5133365472766002696L;

	private String name;		//姓名
	
	private String idnum;		//身份证号
	
	private Integer auth;		//认证结果   1.通过  2.不通过		 

	@JsonManagedReference
    private List<TaskWeChat> taskWechat;


	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="basicUserWechat")
	public List<TaskWeChat> getTaskWechat() {
		return taskWechat;
	}

	public void setTaskWechat(List<TaskWeChat> taskWechat) {
		this.taskWechat = taskWechat;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "BasicUserTaxation [name=" + name + ", idnum=" + idnum + ", auth=" + auth + "]";
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

	


}
