package com.crawler.housingfund.json;

public enum HousingfundStatusCodeEnum {

	//开始登录
	HOUSING_LOGIN_LOADING ("正在认证，请耐心等待...","LOGIN", "DOING",1),
	//登录成功
	HOUSING_LOGIN_SUCCESS ("认证成功！","LOGIN","SUCCESS",0),
	//登录失败
	HOUSING_LOGIN_ERROR ("认证失败","LOGIN", "FAIL",2),
	HOUSING_LOGIN_ERROR_ONE("登陆失败,验证码错误", "LOGIN", "FAIL",2), 
	HOUSING_LOGIN_ERROR_TWO("系统繁忙，请一分钟后再试","LOGIN", "FAIL", 2), 
	HOUSING_LOGIN_ERROR_THREE("登陆失败，请检查密码是否错误","LOGIN", "FAIL", 2),
	HOUSING_LOGIN_ERROR_FOURE("登陆失败，请重试", "LOGIN", "FAIL",2),
	HOUSING_LOGIN_ERROR_FIVE("登陆失败，不支持简单密码登录","LOGIN", "FAIL", 2),
	HOUSING_LOGIN_ERROR_SIX("登陆失败，不支持初始密码登录,","LOGIN", "FAIL", 2),
	HOUSING_LOGIN_ERROR_SEVEN("登录失败,账号已被锁定 ","LOGIN", "FAIL",2),
	HOUSING_LOGIN_ERROR_INVALID("当前登录已失效，请重新登录！","LOGIN","INVALID",3),
	HOUSING_NEED_LOGIN("登录已超时，请先登录。","LOGIN","NEED",2),
	HOUSING_LOGIN_MAINTAIN_ERROR("系统维护中，暂停服务","LOGIN","ERROR",2),
	HOUSING_CRAWLER_ID_VERIFIC_ERROR ("用户身份验证失败，请检查输入信息是否有误","LOGIN", "FAIL",2),
	HOUSING_PASSWORD_ERROR ("账号验证失败！","PASSWORD", "FAIL",2),
	HOUSING_LOGINTWO_LOADING ("正在第二次登录，请耐心等待...","LOGINTWO", "DOING",1),
	HOUSING_LOGINTWO_SUCCESS ("第二次登录认证成功！","LOGINTWO","SUCCESS",0),
	HOUSING_LOGINTWO_ERROR ("第二次登录认证失败","LOGINTWO", "FAIL",2),
//	TASKMOBILE_LOGINTWO_ERROR1 ("第二次登录认证失败(网站图片验证码识别错误,或者短信验证码输入错误次数超过3次只能明日再登录)","LOGINTWO", "FAIL",2),
	HOUSING_LOGINTWO_ERROR1 ("第二次登录认证失败","LOGINTWO", "FAIL",2),
	//发送短信
	HOUSING_SEND_CODE_DONING ("短信验证码发送中....","SEND_CODE", "DONING",0),
	//发送短信失败
	HOUSING_SEND_CODE_ERROR ("短信验证码发送失败","SEND_CODE", "FAIL",2),
	HOUSING_SEND_CODE_ERROR2 ("短信验证码发送失败,您今天内获取取随机密码已超出限制","SEND_CODE", "FAIL",2),
	//发送短信成功
	HOUSING_WAIT_CODE_SUCCESS ("短信验证码已发送，请注意查收","SEND_CODE", "SUCCESS",0),
	//验证短信
	HOUSING_WAIT_CODE_DONING("短信验证码验证中.....","VALIDATE", "DONING",2),
	//验证短信失败
	HOUSING_WAIT_CODE_ERROR_RETRY ("短信验证码错误，请重新发送短信 ！","VALIDATE", "FAIL",2),
	HOUSING_WAIT_CODE_ERROR ("短信验证码验证失败,请重新登录！","VALIDATE", "FAIL",2),
	HOUSING_WAIT_CODE_ERROR_ID ("短信验证码验证失败,身份信息错误","VALIDATE", "FAIL",2),
	HOUSING_WAIT_CODE_ERROR1 ("短信验证码验证失败,随机码错误","VALIDATE", "FAIL",2),
	HOUSING_WAIT_CODE_ERROR2 ("系统繁忙，请稍后再试","VALIDATE", "FAIL",2),
	HOUSING_WAIT_CODE_ERROR3 ("短信验证码验证失败,不存在此用户","VALIDATE", "FAIL",2),
	HOUSING_WAIT_CODE_ERROR4 ("您输入的短信密码已失效！请重新获取短信密码！！","VALIDATE", "FAIL",2),
	HOUSING_WAIT_CODE_ERROR5 ("短信验证码验证失败","VALIDATE", "FAIL",2),
	//验证短信成功
	HOUSING_PASSWORD_SUCCESS ("短信验证码验证成功！","VALIDATE", "SUCCESS",0),
	//采集数据
	HOUSING_CRAWLER_READ ("数据采集开始采集，请耐心等待...","CRAWLER", "DOING",0),
	HOUSING_CRAWLER_DONING ("数据采集中，请耐心等待...","CRAWLER", "DOING",0),
	HOUSING_CRAWLER_SUCCESS ("数据采集成功！","CRAWLER", "SUCCESS",0),
	HOUSING_CRAWLER_ERROR ("数据采集失败！","CRAWLER", "FAIL",2),
	//个人基本信息
	HOUSING_CRAWLER_USER_MSG_SUCCESS("数据采集中，【公积金用户信息】已采集完成","CRAWLER_USER_MSG","SUCCESS",200),
	HOUSING_CRAWLER_USER_MSG_ERROR("数据采集中，【公积金用户信息】采集失败","CRAWLER_USER_MSG","FAIL",404),
	//流水
	HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS("数据采集中，【公积金流水信息】已采集完成","CRAWLER_STATEMENT_MSG","SUCCESS",200),
	HOUSING_CRAWLER_STATEMENT_MSG_ERROR("数据采集中，【公积金流水信息】采集失败","CRAWLER_STATEMENT_MSG","FAIL",404),
	
	//guiyang 公积金  ----------------------------
	HOUSING_LOGIN_TIMEOUT("连接超时！","LOGIN","FAIL",104),
	HOUSING_LOGIN_IDNUM_ERROR("请输入合法的身份证号码","LOGIN","FAIL",103),
	HOUSING_LOGIN_IDNUM_ERROR_INEXISTENCE("身份证号不存在","LOGIN","FAIL",103),
	HOUSING_LOGIN_IDNUMORPWD_ERROR("输入的账号或密码错误！","LOGIN","FAIL",104),
	HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR("图片验证码验证失败！","LOGIN","FAIL",104),
	HOUSING_LOGIN_CARD_FUND_ERROR("输入的身份证号或公积金账号错误！","LOGIN","FAIL",104),
	HOUSING_LOGIN_CARD_NAME_ERROR("输入的身份证号或姓名错误！","LOGIN","FAIL",104),
	HOUSING_LOGIN_PWD_ERROR("输入的密码错误！","LOGIN","FAIL",104),
	HOUSING_LOGIN_CODE_ERROR("输入的验证码错误！","LOGIN","FAIL",104);
	
	//-----------------------------------------
	
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

	private HousingfundStatusCodeEnum(String description,String phase, String phasestatus,Integer error_code) {
		this.description = description;
		this.phase = phase;
		this.phasestatus = phasestatus;
		this.error_code = error_code;
	}


}
