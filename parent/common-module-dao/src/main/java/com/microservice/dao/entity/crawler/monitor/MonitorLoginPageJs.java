package com.microservice.dao.entity.crawler.monitor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:将登录页面涉及到的所有js的内容进行加密
 * @author: sln 
 * @date: 2018年1月30日 下午4:18:37 
 */
@Entity
@Table(name = "monitor_loginpage_js")
public class MonitorLoginPageJs extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4178094438903055009L;
	private String taskid;
	private String url;    //登录url
	private String jspath;  //网页中js的路径
	private String jsmd5;   //将网页中的js分别加密
	private Integer jscount;    //统计js个数
	private Boolean changeflag;   //js内容是否有变
	private Long loginid;     //外键
	private String webtype;    //网站类型名称	
	private String jscode;    //存储js源码
	private String aftertreatjscode;  //经过处理后的js内容
	private Integer jscontentlength;   //经过处理之后js内容的长度
	private String developer;    //网站负责人
	private String jsmodified; //js上一次修改时间，如果提供，就存储，否则就用字符串"null"显示
	private String comparetaskid;  //参照taskid
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getJspath() {
		return jspath;
	}
	public void setJspath(String jspath) {
		this.jspath = jspath;
	}
	public String getJsmd5() {
		return jsmd5;
	}
	public void setJsmd5(String jsmd5) {
		this.jsmd5 = jsmd5;
	}
	public Integer getJscount() {
		return jscount;
	}
	public void setJscount(Integer jscount) {
		this.jscount = jscount;
	}
	public Boolean getChangeflag() {
		return changeflag;
	}
	public void setChangeflag(Boolean changeflag) {
		this.changeflag = changeflag;
	}
	public Long getLoginid() {
		return loginid;
	}
	public void setLoginid(Long loginid) {
		this.loginid = loginid;
	}
	public String getWebtype() {
		return webtype;
	}
	public void setWebtype(String webtype) {
		this.webtype = webtype;
	}
	public String getJscode() {
		return jscode;
	}
	public void setJscode(String jscode) {
		this.jscode = jscode;
	}
	public String getAftertreatjscode() {
		return aftertreatjscode;
	}
	public void setAftertreatjscode(String aftertreatjscode) {
		this.aftertreatjscode = aftertreatjscode;
	}
	public Integer getJscontentlength() {
		return jscontentlength;
	}
	public void setJscontentlength(Integer jscontentlength) {
		this.jscontentlength = jscontentlength;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getJsmodified() {
		return jsmodified;
	}
	public void setJsmodified(String jsmodified) {
		this.jsmodified = jsmodified;
	}
	public String getComparetaskid() {
		return comparetaskid;
	}
	public void setComparetaskid(String comparetaskid) {
		this.comparetaskid = comparetaskid;
	}
	public MonitorLoginPageJs() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MonitorLoginPageJs(String taskid, String url, String jspath, String jsmd5, Integer jscount,
			Boolean changeflag, Long loginid, String webtype, String jscode, String aftertreatjscode,
			Integer jscontentlength, String developer, String jsmodified, String comparetaskid) {
		super();
		this.taskid = taskid;
		this.url = url;
		this.jspath = jspath;
		this.jsmd5 = jsmd5;
		this.jscount = jscount;
		this.changeflag = changeflag;
		this.loginid = loginid;
		this.webtype = webtype;
		this.jscode = jscode;
		this.aftertreatjscode = aftertreatjscode;
		this.jscontentlength = jscontentlength;
		this.developer = developer;
		this.jsmodified = jsmodified;
		this.comparetaskid = comparetaskid;
	}
}
