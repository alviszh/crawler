package com.crawler.maimai.json;

public enum MaimaiStatusCode {

	MAIMAI_LOGIN_DOING("正在登录。。。。","LOGIN","DOING",100),
	MAIMAI_LOGIN_IDNUMORPWD_ERROR("帐号或密码不正确。忘记密码可登陆脉脉APP找回。","LOGIN","ERROR",101),
	MAIMAI_LOGIN_SUCCESS("登录成功！","LOGIN","SUCCESS",200),
	MAIMAI_LOGIN_TIMEOUT("连接超时！","LOGIN","ERROR",104),
	MAIMAI_LOGIN_EXCEPTION("登录时发生异常！","LOGIN","ERROR",999),
	
	MAIMAI_CRAWLER_CHECK_ERROR("未获取到爬取任务(TaskId)!","CRAWLER","CHECK",400),
	MAIMAI_CRAWLER_DOING("正在采集数据。。。","CRAWLER","DOING",100),
	MAIMAI_CRAWLER_SUCCESS("数据采集成功！","CRAWLER","SUCCESS",200),
	MAIMAI_LOGIN_MAINTAIN_ERROR("系统维护中，暂停服务","LOGIN","ERROR",106),
	
	MAIMAI_CRAWLER_USER_INFO_SUCCESS("用户的基本信息数据采集成功","CRAWLER_USER_INFO","SUCCESS",200),
	MAIMAI_CRAWLER_USER_INFO_FAILUE("用户的基本信息信息数据采集失败","CRAWLER_USER_INFO","FAILUE",300),
	MAIMAI_PARSER_USER_INFO_SUCCESS("用户的基本信息信息数据解析成功","PARSER_USER_INFO","SUCCESS",200),
	MAIMAI_PARSER_USER_INFO_FAILUE("用户的基本信息信息数据解析失败","PARSER_USER_INFO","FAILUE",300),
	
	MAIMAI_CRAWLER_USER_EDUCATIONS_SUCCESS("用户的教育经历数据采集成功","CRAWLER_USER_EDUCATIONS","SUCCESS",200),
	MAIMAI_CRAWLER_USER_EDUCATIONS_FAILUE("用户的教育经历数据采集失败","CRAWLER_USER_EDUCATIONS","FAILUE",300),
	MAIMAI_PARSER_USER_EDUCATIONS_SUCCESS("用户的教育经历数据解析成功","PARSER_USER_EDUCATIONS","SUCCESS",200),
	MAIMAI_PARSER_USER_EDUCATIONS_FAILUE("用户的教育经历数据解析失败","PARSER_USER_EDUCATIONS","FAILUE",300),
	
	MAIMAI_CRAWLER_USER_WORK_SUCCESS("用户的工作经历数据采集成功","CRAWLER_USER_WORK","SUCCESS",200),
	MAIMAI_CRAWLER_USER_WORK_FAILUE("用户的工作经历数据采集失败","CRAWLER_USER_WORK","FAILUE",300),
	MAIMAI_PARSER_USER_WORK_SUCCESS("用户的工作经历数据解析成功","PARSER_USER_WORK","SUCCESS",200),
	MAIMAI_PARSER_USER_WORK_FAILUE("用户的工作经历数据解析失败","PARSER_USER_WORK","FAILUE",300),
	
	MAIMAI_CRAWLER_FRIEND_USER_INFO_SUCCESS("用户朋友的基本信息数据采集成功","CRAWLER_FRIEND_USER_INFO","SUCCESS",200),
	MAIMAI_CRAWLER_FRIEND_USER_INFO_FAILUE("用户朋友的基本信息数据采集失败","CRAWLER_FRIEND_USER_INFO","FAILUE",300),
	MAIMAI_PARSER_FRIEND_USER_INFO_SUCCESS("用户朋友的基本信息数据解析成功","PARSER_FRIEND_USER_INFO","SUCCESS",200),
	MAIMAI_PARSER_FRIEND_USER_INFO_FAILUE("用户朋友的基本信息数据解析失败","PARSER_FRIEND_USER_INFO","FAILUE",300),
	
	MAIMAI_CRAWLER_FRIEND_EDUCATIONS_SUCCESS("用户朋友的教育经历数据采集成功","CRAWLER_FRIEND_EDUCATIONS","SUCCESS",200),
	MAIMAI_CRAWLER_FRIEND_EDUCATIONS_FAILUE("用户朋友的教育经历数据采集失败","CRAWLER_FRIEND_EDUCATIONS","FAILUE",300),
	MAIMAI_PARSER_FRIEND_EDUCATIONS_SUCCESS("用户朋友的教育经历数据解析成功","PARSER_FRIEND_EDUCATIONS","SUCCESS",200),
	MAIMAI_PARSER_FRIEND_EDUCATIONS_FAILUE("用户朋友的教育经历数据解析失败","PARSER_FRIEND_EDUCATIONS","FAILUE",300),
	
	MAIMAI_CRAWLER_FRIEND_WORK_SUCCESS("用户朋友的工作经历数据采集成功","CRAWLER_FRIEND_WORK","SUCCESS",200),
	MAIMAI_CRAWLER_FRIEND_WORK_FAILUE("用户朋友的工作经历数据采集失败","CRAWLER_FRIEND_WORK","FAILUE",300),
	MAIMAI_PARSER_FRIEND_WORK_SUCCESS("用户朋友的工作经历数据解析成功","PARSER_FRIEND_WORK","SUCCESS",200),
	MAIMAI_PARSER_FRIEND_WORK_FAILUE("用户朋友的工作经历数据解析失败","PARSER_FRIEND_WORK","FAILUE",300),
	
	//Agent中间层
	MAIMAI_AGENT_ERROR("系统繁忙，请稍后再试","AGENT","ERROR",404);

	
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

	private MaimaiStatusCode(String description,String phase, String phasestatus,Integer error_code) {
		this.description = description;
		this.phase = phase;
		this.phasestatus = phasestatus;
		this.error_code = error_code;
	}
	
}
