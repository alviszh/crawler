package app.entity.crawler;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;

/**
 * @description: 为前端提供接口，调用网站之前先监测网站的可用性，以及微服务是否存在
 * @author: sln 
 */
@Entity
@Table(name = "monitor_allweb_bank")
public class MonitorAllWebBank extends IdEntity implements Serializable {
	private static final long serialVersionUID = 8645824357141059331L;
	private String banktype;
	private String cardtype;
	private String logintype;
	private Integer usablestate;
	private String url;
	private String appname;
	private String linkway;    //网站可用性监测连接方式
	private Integer isneedmonitor;  //是否需要监控，1是监控，0是不监控
	public String getBanktype() {
		return banktype;
	}
	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}
	public String getCardtype() {
		return cardtype;
	}
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	public String getLogintype() {
		return logintype;
	}
	public void setLogintype(String logintype) {
		this.logintype = logintype;
	}
	public Integer getUsablestate() {
		return usablestate;
	}
	public void setUsablestate(Integer usablestate) {
		this.usablestate = usablestate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getLinkway() {
		return linkway;
	}
	public void setLinkway(String linkway) {
		this.linkway = linkway;
	}
	
	public Integer getIsneedmonitor() {
		return isneedmonitor;
	}
	public void setIsneedmonitor(Integer isneedmonitor) {
		this.isneedmonitor = isneedmonitor;
	}
	public MonitorAllWebBank(String banktype, String cardtype, String logintype, Integer usablestate, String url,
			String appname, String linkway, Integer isneedmonitor) {
		super();
		this.banktype = banktype;
		this.cardtype = cardtype;
		this.logintype = logintype;
		this.usablestate = usablestate;
		this.url = url;
		this.appname = appname;
		this.linkway = linkway;
		this.isneedmonitor = isneedmonitor;
	}
	
}
