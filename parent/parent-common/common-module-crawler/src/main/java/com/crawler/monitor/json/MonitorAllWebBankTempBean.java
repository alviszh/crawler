package com.crawler.monitor.json;

public class MonitorAllWebBankTempBean {
	private String banktype;
	private String cardtype;
	private String logintype;
	private int usablestate;
	private String url;
	private String appname;
	private String linkway;    //网站可用性监测连接方式
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
	public int getUsablestate() {
		return usablestate;
	}
	public void setUsablestate(int usablestate) {
		this.usablestate = usablestate;
	}
	@Override
	public String toString() {
		return "MonitorAllWebBank [banktype=" + banktype + ", cardtype=" + cardtype + ", logintype=" + logintype
				+ ", usablestate=" + usablestate + ", url=" + url + ", appname=" + appname + ", linkway=" + linkway
				+ "]";
	}
	public MonitorAllWebBankTempBean(String banktype, String cardtype, int usablestate) {
		super();
		this.banktype = banktype;
		this.cardtype = cardtype;
		this.usablestate = usablestate;
	}
}
