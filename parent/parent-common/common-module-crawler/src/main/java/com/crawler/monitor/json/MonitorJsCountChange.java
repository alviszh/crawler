package com.crawler.monitor.json;
/**
 * @description:当js数量有变化的时候，该类为回显数据提供载体
 * @author: sln 
 * @date: 2018年2月9日 下午1:48:26 
 */
public class MonitorJsCountChange {
	private String webType;     //网站类型
	private String loginUrl;    //登录url  
	private int lastJsCount;    //之前的js数量
	private int currentJsCount; //现在的js数量
	private String changeDetail;//变化明细
	private String lasttaskid;  //上次执行任务的taskid
	private String thistaskid;  //本次执行任务的taskid
	private String developer;    //网站负责人
	public String getWebType() {
		return webType;
	}
	public void setWebType(String webType) {
		this.webType = webType;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public int getLastJsCount() {
		return lastJsCount;
	}
	public void setLastJsCount(int lastJsCount) {
		this.lastJsCount = lastJsCount;
	}
	public int getCurrentJsCount() {
		return currentJsCount;
	}
	public void setCurrentJsCount(int currentJsCount) {
		this.currentJsCount = currentJsCount;
	}
	public String getChangeDetail() {
		return changeDetail;
	}
	public void setChangeDetail(String changeDetail) {
		this.changeDetail = changeDetail;
	}
	public String getLasttaskid() {
		return lasttaskid;
	}
	public void setLasttaskid(String lasttaskid) {
		this.lasttaskid = lasttaskid;
	}
	public String getThistaskid() {
		return thistaskid;
	}
	public void setThistaskid(String thistaskid) {
		this.thistaskid = thistaskid;
	}
	public MonitorJsCountChange() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public MonitorJsCountChange(String webType, String loginUrl, int lastJsCount, int currentJsCount,
			String changeDetail, String lasttaskid, String thistaskid, String developer) {
		super();
		this.webType = webType;
		this.loginUrl = loginUrl;
		this.lastJsCount = lastJsCount;
		this.currentJsCount = currentJsCount;
		this.changeDetail = changeDetail;
		this.lasttaskid = lasttaskid;
		this.thistaskid = thistaskid;
		this.developer = developer;
	}
}
