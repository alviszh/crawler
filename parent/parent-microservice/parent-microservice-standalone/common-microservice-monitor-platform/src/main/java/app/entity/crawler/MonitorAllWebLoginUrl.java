package app.entity.crawler;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;

/**
 * @description: 将要监控的所有网站暂时存放在这个实体中
 * @author: sln 
 */
@Entity
@Table(name = "monitor_allweb_loginurl")
public class MonitorAllWebLoginUrl extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4918169247990622081L;
	private String url;      //登录url地址
	private String webtype;   //监控网站类型(运营商/社保......)
	private String city;   //网站所属城市
	private boolean isusable;   //网站是否可用
	private String developer;    //网站负责人
	private String htmlexecutemethod;  //获取html源码所用的执行方法  ——ssl,jsoup,htmlunit
	//部分网站无法获取js的绝对路径，个别网站获取前缀之后仍旧不全,如湖州公积金，故决定添加该字段，分为三种情况 ——
	//abs(程序可以获取完整，或者是返回全路径中，第三个"/"之后的内容),
	//part(程序返回部分路径)
	//add(除程序获取部分路径之外，还需要添加其他，如湖州公积金，在excel表格中addcontent字段下添加需要拼接的内容——如   /hzgjj-wsyyt/   )
	private String jspathtip;   
	private String jsexecutemethod; //获取js源码所用的执行方法  ——ssl,jsoup,htmlunit（调研的绝大部分，html用什么方法运行，对应js就用什么方法运行，个别例外，如联通唯一的js）
	private String jspathtreatcontent;   //如果htmlexecutemethod字段备注的是add,那么此处添加add的内容
	private boolean htmlaftertreatment;   //html是否需要后期处理，不需要后期处理的直接加密
	private boolean jsaftertreatment;     //js是否需要后期处理
	private Integer isneedmonitor;    //是否需要监控     1:需要        0：暂时不需要 因为有时候网站改版了，还在维护中
	private String linkway;   //网站可用性连接方式
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public boolean isIsusable() {
		return isusable;
	}
	public void setIsusable(boolean isusable) {
		this.isusable = isusable;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getHtmlexecutemethod() {
		return htmlexecutemethod;
	}
	public void setHtmlexecutemethod(String htmlexecutemethod) {
		this.htmlexecutemethod = htmlexecutemethod;
	}
	public String getJspathtip() {
		return jspathtip;
	}
	public void setJspathtip(String jspathtip) {
		this.jspathtip = jspathtip;
	}
	public String getJsexecutemethod() {
		return jsexecutemethod;
	}
	public void setJsexecutemethod(String jsexecutemethod) {
		this.jsexecutemethod = jsexecutemethod;
	}
	public String getJspathtreatcontent() {
		return jspathtreatcontent;
	}
	public void setJspathtreatcontent(String jspathtreatcontent) {
		this.jspathtreatcontent = jspathtreatcontent;
	}
	public boolean isHtmlaftertreatment() {
		return htmlaftertreatment;
	}
	public void setHtmlaftertreatment(boolean htmlaftertreatment) {
		this.htmlaftertreatment = htmlaftertreatment;
	}
	public boolean isJsaftertreatment() {
		return jsaftertreatment;
	}
	public void setJsaftertreatment(boolean jsaftertreatment) {
		this.jsaftertreatment = jsaftertreatment;
	}
	public Integer getIsneedmonitor() {
		return isneedmonitor;
	}
	public void setIsneedmonitor(Integer isneedmonitor) {
		this.isneedmonitor = isneedmonitor;
	}
	public String getLinkway() {
		return linkway;
	}
	public void setLinkway(String linkway) {
		this.linkway = linkway;
	}
}
