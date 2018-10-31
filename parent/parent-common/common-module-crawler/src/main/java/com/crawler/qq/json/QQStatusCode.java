package com.crawler.qq.json;

public enum QQStatusCode {
	
	
	QQ_LOGIN_LOADING("正在认证，请耐心等待...","LOGIN", "DOING",1),
	QQ_LOGIN_SUCCESS ("认证成功！","LOGIN","SUCCESS",0),
	QQ_LOGIN_ERROR ("认证失败","LOGIN", "FAIL",2),
	QQ_CRAWLER_DOING("数据开始采集，请稍后...","CRAWLER","DOING",100),
	QQ_CRAWLER_SUCCESS("数据采集成功.","CRAWLER","SUCCESS",200),
	
	QQ_CRAWLER_USER_MSG_SUCCESS("qq个人信息采集成功","CRAWLER_USER_MSG","SUCCESS",200),
	QQ_CRAWLER_USER_MSG_FAILUE("qq个人信息采集失败","CRAWLER_USER_MSG","FAILUE",300),
	
	QQ_CRAWLER_QUN_MSG_SUCCESS("qq群信息采集成功","CRAWLER_USER_MSG","SUCCESS",200),
	QQ_CRAWLER_QUN_MSG_FAILUE("qq群信息采集失败","CRAWLER_USER_MSG","FAILUE",300),
	
	QQ_CRAWLER_FRIEND_MSG_SUCCESS("qq好友信息采集成功","CRAWLER_USER_MSG","SUCCESS",200),
	QQ_CRAWLER_FRIEND_MSG_FAILUE("qq好友信息采集失败","CRAWLER_USER_MSG","FAILUE",300);
	private String description;
	
	private String phase;
	
	private String phasestatus;//步骤状态
	
	private Integer error_code;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getPhasestatus() {
		return phasestatus;
	}

	public void setPhasestatus(String phasestatus) {
		this.phasestatus = phasestatus;
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}

	private QQStatusCode(String description,String phase, String phasestatus,Integer error_code) {
		this.description = description;
		this.phase = phase;
		this.phasestatus = phasestatus;
		this.error_code = error_code;
	}
	
}
