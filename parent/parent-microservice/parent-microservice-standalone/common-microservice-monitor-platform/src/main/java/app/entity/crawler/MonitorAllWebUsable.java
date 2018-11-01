package app.entity.crawler;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;

/**
 * @description: url表与该表是一对多的关系，该表实时存储被监控并存储各网站的链接情况
 * @author: sln 
 */
@Entity
@Table(name = "monitor_allweb_usable")
public class MonitorAllWebUsable extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4918169247990622081L;
	private String url;      //登录url地址
	private String webtype;   //监控网站类型(运营商/社保......)
	private boolean isusable;   //网站是否可用
	private long urlid;     //外键
	private Integer statuscode;   //网站状态码
	private String exceptioninfo;  //网站异常信息
	private String taskid;
	private String developer;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getWebtype() {
		return webtype;
	}
	public void setWebtype(String webtype) {
		this.webtype = webtype;
	}
	public boolean isIsusable() {
		return isusable;
	}
	public void setIsusable(boolean isusable) {
		this.isusable = isusable;
	}
	public long getUrlid() {
		return urlid;
	}
	public void setUrlid(long urlid) {
		this.urlid = urlid;
	}
	public Integer getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(Integer statuscode) {
		this.statuscode = statuscode;
	}
	public String getExceptioninfo() {
		return exceptioninfo;
	}
	public void setExceptioninfo(String exceptioninfo) {
		this.exceptioninfo = exceptioninfo;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	@Override
	public String toString() {
		return "MonitorAllWebUsable [url=" + url + ", webtype=" + webtype + ", isusable=" + isusable + ", urlid="
				+ urlid + ", statuscode=" + statuscode + ", exceptioninfo=" + exceptioninfo + ", taskid=" + taskid
				+ ", developer=" + developer + "]";
	}  
}
