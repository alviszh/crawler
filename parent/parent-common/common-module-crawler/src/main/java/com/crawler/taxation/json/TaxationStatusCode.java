package com.crawler.taxation.json;

public enum TaxationStatusCode {
	
	TAXATION_LOGIN_DOING("正在登录。。。。","LOGIN","DOING",100),
	TAXATION_LOGIN_PWD_ERROR("输入的密码验证错误！","LOGIN","ERROR",101),
	TAXATION_LOGIN_CAPTCHA_ERROR("图片验证码有误！","LOGIN","ERROR",102),
	TAXATION_LOGIN_SMS_ERROR("短信验证码错误！","LOGIN","ERROR",101),
	TAXATION_LOGIN_IDNUM_ERROR("请输入合法的身份证号码","LOGIN","ERROR",103),
	TAXATION_LOGIN_NOTFIND_ERROR("查不到用户信息，请先注册！","LOGIN","ERROR",103),
	TAXATION_LOGIN_IDNUMORPWD_ERROR("输入的账号或密码错误！","LOGIN","ERROR",104),
	TAXATION_LOGIN_SUCCESS("登录成功！","LOGIN","SUCCESS",200),
	TAXATION_LOGIN_TIMEOUT("连接超时！","LOGIN","ERROR",104),
	TAXATION_LOGIN_EXCEPTION("登录时发生异常！","LOGIN","ERROR",999),
	TAXATION_LOGIN_IDNUM_NOTDATA("根据身份证号 查不出任何记录","LOGIN","ERROR",105),
	TAXATION_LOGIN_IDNUMLOGINTYPENOTFIT_ERROR("所选登录类型与所输账号信息不符合,请核实！","LOGIN","ERROR",108),

	TAXATION_LOGIN_VALIDATE_DOING("校验信息中并准备发送短信...","VALIDATE","DOING",109),
	TAXATION_LOGIN_VALIDATE_SUCCESS("校验信息成功，短信已发送，请注意查收！","VALIDATE","SUCCESS",110),
	TAXATION_LOGIN_VALIDATE_FAILUE("短信发送失败！","VALIDATE","FAILUE",110),
	
	TAXATION_SMS_SEND_DOING("短信验证码发送中...","SMS_SEND","DOING",200),
	TAXATION_SMS_VALIDATE_DOING("短信验证码验证中...","SMS_VALIDATE","DOING",200),
	TAXATION_SMS_VALIDATE_SUCCESS("短信验证码验证成功！","SMS_VALIDATE","SUCCESS",200),
	TAXATION_SMS_VALIDATE_FAILUE("短信验证码验证失败！","SMS_VALIDATE","FAILUE",200),
	
	TAXATION_CRAWLER_CHECK_ERROR("未获取到爬取任务(TaskId)!","CRAWLER","CHECK",400),
	TAXATION_CRAWLER_DOING("正在采集数据。。。","CRAWLER","DOING",100),
	TAXATION_CRAWLER_SUCCESS("数据采集成功！","CRAWLER","SUCCESS",200),
	TAXATION_LOGIN_MAINTAIN_ERROR("系统维护中，暂停服务","LOGIN","ERROR",106),
	
	TAXATION_CRAWLER_ALL_SUCCESS("数据采集成功！","CRAWLER","SUCCESS",200),
	TAXATION_CRAWLER_USER_MSG_SUCCESS("数据采集中，【个人信息】已采集完成","CRAWLER_USER_MSG","SUCCESS",200),
	TAXATION_CRAWLER_USER_MSG_ERROR("数据采集中，【个人信息】无数据","CRAWLER_USER_MSG","ERROR",300),
	TAXATION_CRAWLER_USER_MSG_TIMEOUT("数据采集中，【个人信息】页面请求超时","CRAWLER_USER_MSG","ERROR",400),
	TAXATION_CRAWLER_ACCOUNT_MSG_SUCCESS("数据采集中，【账户信息】已采集完成","CRAWLER_ACCOUNT_MSG","SUCCESS",200),
	TAXATION_CRAWLER_ACCOUNT_MSG_ERROR("数据采集中，【账户信息】无数据","CRAWLER_ACCOUNT_MSG","ERROR",300),
	TAXATION_CRAWLER_ACCOUNT_MSG_TIMEOUT("数据采集中，【账户信息】页面请求超时","CRAWLER_ACCOUNT_MSG","ERROR",400),
	
	//系统自动退出
    SYSTEM_QUIT("系统超时请重试", "SYSTEM", "QUIT", -1);
	
	
	

	
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

	private TaxationStatusCode(String description,String phase, String phasestatus,Integer error_code) {
		this.description = description;
		this.phase = phase;
		this.phasestatus = phasestatus;
		this.error_code = error_code;
	}
	

}
