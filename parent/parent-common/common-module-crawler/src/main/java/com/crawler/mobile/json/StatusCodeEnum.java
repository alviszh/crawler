package com.crawler.mobile.json;

public enum StatusCodeEnum {
	
	
	//Agent中间层
	TASKMOBILE_AGENT_ERROR("系统繁忙，请稍后再试","AGENT","ERROR",404),

	TASKMOBILE_READY_SUCESSES ("准备开始","READY", "DOING",0),
	TASKMOBILE_READY_ERROR ("准备开始...","READY", "FAIL",1),

	TASKMOBILE_LOGIN_LOADING ("正在认证，请耐心等待...","LOGIN", "DOING",1),
	TASKMOBILE_LOGIN_SUCCESS ("认证成功！","LOGIN","SUCCESS",0),
	TASKMOBILE_LOGIN_ERROR ("认证失败","LOGIN", "FAIL",2),

	TASKMOBILE_CRAWLER_INTERMEDIATE_DOING ("页面加载中..","INTERMEDIATE","DOING",0),
	TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR ("浙江电信网站波动，请重新再试。","INTERMEDIATE","ERROR",0),
	TASKMOBILE_CRAWLER_INTERMEDIATE_SUCCESS ("页面加载成功！","INTERMEDIATE","SUCCESS",0),
	
	TASKMOBILE_LOGINTWO_LOADING ("正在第二次登录，请耐心等待...","LOGINTWO", "DOING",1),
	TASKMOBILE_LOGINTWO_SUCCESS ("第二次登录认证成功！","LOGINTWO","SUCCESS",0),
	TASKMOBILE_LOGINTWO_ERROR ("第二次登录认证失败","LOGINTWO", "FAIL",2),
//	TASKMOBILE_LOGINTWO_ERROR1 ("第二次登录认证失败(网站图片验证码识别错误,或者短信验证码输入错误次数超过3次只能明日再登录)","LOGINTWO", "FAIL",2),
	TASKMOBILE_LOGINTWO_ERROR1 ("第二次登录认证失败","LOGINTWO", "FAIL",2),
	
	TASKMOBILE_CRAWLER_ID_Verific_ERROR ("用户身份验证失败，请检查输入信息是否有误","CRAWLER", "FAIL",2),
	MESSAGE_LOGIN_ERROR_TWO("登陆失败,验证码错误", "LOGIN", "FAIL",2), 
	MESSAGE_LOGIN_ERROR_THREE("系统繁忙，请一分钟后再试","LOGIN", "FAIL", 2), 
	MESSAGE_LOGIN_ERROR_FOURE("登陆失败，请检查密码是否错误","LOGIN", "FAIL", 2),
	MESSAGE_LOGIN_ERROR_FIVE("登陆失败，请重试", "LOGIN", "FAIL",2),
	MESSAGE_LOGIN_ERROR_SIX("登陆失败，联通有点小波动，请稍后再试","LOGIN", "FAIL", 2),
	MESSAGE_LOGIN_ERROR_BODONG("登陆失败，联通有点小波动，请稍后再试","LOGIN", "ERROR", 2), //联通登录 出现系统繁忙 特别定制
	MESSAGE_LOGIN_ERROR_SEVEN("登陆失败，不支持简单密码登录","LOGIN", "FAIL", 2),
	MESSAGE_LOGIN_ERROR_EIGHT("登陆失败，不支持初始密码登录,","LOGIN", "FAIL", 2),
	MESSAGE_LOGIN_ERROR_NINE("登录失败,电信网站出现问题 ","LOGIN", "FAIL",2),
	MESSAGE_LOGIN_ERROR_TEN("登录失败,账号已被锁定 ","LOGIN", "FAIL",2),
	TASKMOBILE_NEED_LOGIN("登录已超时，请先登录。","LOGIN","NEED",2),

	
	TASKMOBILE_QUERY_LOADING ("二次验证中，请耐心等待...","VALIDATE","DOING",1),
	TASKMOBILE_QUERY_SUCCESS ("二次验证成功！","VALIDATE","SUCCESS",0),
	TASKMOBILE_QUERY_ERROR ("二次验证失败！","VALIDATE","FAIL",2),
	TASKMOBILE_QUERY_ERROR_AUTONYM ("实名认证失败！","VALIDATE","FAIL",2),
	TASKMOBILE_LOGIN_INVALID("当前登录已失效，请重新登录！","LOGIN","INVALID",3),
	
	TASKMOBILE_CRAWLER_USER_MSG_SUCCESS("数据采集中，【用户信息】已采集完成","CRAWLER_USER_MSG","DOING",0),
	TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS("数据采集中，【短信信息】已采集完成","CRAWLER_SMS_MSG","DOING",0),
	TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS("数据采集中，【通讯信息】已采集完成","CRAWLER_CALL_MSG","DOING",0),
	TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS("数据采集中，【缴费信息】已采集完成","CRAWLER_PAY_MSG","DOING",0),
	TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS("数据采集中，【账单信息】已采集完成","CRAWLER_CHECK_MSG","DOING",0),
	//=============================更新的枚举类信息       start===================================
	TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS("数据采集中，【账户信息】已采集完成","CRAWLER_ACCOUNT_MSG","DOING",0),
	TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS("数据采集中，【业务信息】已采集完成","CRAWLER_BUSINESS_MSG","DOING",0),
	TASKMOBILE_CRAWLER_FAMILY_MSG_SUCCESS("数据采集中，【亲情号信息】已采集完成","CRAWLER_FAMILY_MSG","DOING",0),
	TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS("数据采集中，【积分信息】已采集完成","CRAWLER_INTEGRA_MSG","DOING",0),
	
	TASKMOBILE_CRAWLER_USER_MSG_ERROR("数据采集中，【用户信息】采集失败","CRAWLER_USER_MSG","FAIL",0),
	TASKMOBILE_CRAWLER_SMS_MSG_ERROR("数据采集中，【短信信息】采集失败","CRAWLER_SMS_MSG","FAIL",0),
	TASKMOBILE_CRAWLER_CALL_MSG_ERROR("数据采集中，【通讯信息】采集失败","CRAWLER_CALL_MSG","FAIL",0),
	TASKMOBILE_CRAWLER_PAY_MSG_ERROR("数据采集中，【缴费信息】采集失败","CRAWLER_PAY_MSG","FAIL",0),
	TASKMOBILE_CRAWLER_CHECK_MSG_ERROR("数据采集中，【账单信息】采集失败","CRAWLER_CHECK_MSG","FAIL",0),
	
	TASKMOBILE_CRAWLER_ACCOUNT_MSG_ERROR("数据采集中，【账户信息】采集失败","CRAWLER_ACCOUNT_MSG","FAIL",0),
	TASKMOBILE_CRAWLER_BUSINESS_MSG_ERROR("数据采集中，【业务信息】采集失败","CRAWLER_BUSINESS_MSG","FAIL",0),
	TASKMOBILE_CRAWLER_FAMILY_MSG_ERROR("数据采集中，【亲情号信息】采集失败","CRAWLER_FAMILY_MSG","FAIL",0),
	TASKMOBILE_CRAWLER_INTEGRA_MSG_ERROR("数据采集中，【积分信息】采集失败","CRAWLER_INTEGRA_MSG","FAIL",0),
	//=============================更新的枚举类信息       end===================================

	TASKMOBILE_READY_CODE_DONING ("短信验证码准备发送.....","READY_CODE", "DONING",0),
	
	TASKMOBILE_SEND_CODE_DONING ("短信验证码发送中....","SEND_CODE", "DONING",0),
	TASKMOBILE_SEND_CODE_ERROR ("短信验证码发送失败","SEND_CODE", "FAIL",2),
	TASKMOBILE_SEND_CODE_ERROR1 ("短信验证码发送失败","SEND_CODE", "FAIL",2),
	TASKMOBILE_SEND_CODE_ERROR2 ("短信验证码发送失败,您今天内获取取随机密码已超出限制","SEND_CODE", "FAIL",2),
	
	TASKMOBILE_WAIT_CODE_SUCCESS ("短信验证码已发送，请注意查收","WAIT_CODE", "SUCCESS",0),
	TASKMOBILE_WAIT_CODE_DONING("短信验证码验证中.....","WAIT_CODE", "DONING",2),
	
	TASKMOBILE_WAIT_CODE_ERROR_RETRY ("短信验证码错误，请重新发送短信 ！","VALIDATE", "FAIL",2),
	TASKMOBILE_WAIT_CODE_ERROR ("短信验证码验证失败,请重新登录！","VALIDATE", "FAIL",2),
	TASKMOBILE_WAIT_CODE_ERROR_ID ("短信验证码验证失败,身份信息错误","VALIDATE", "FAIL",2),
	
	
	TASKMOBILE_WAIT_CODE_ERROR1 ("短信验证码验证失败,随机码错误","WAIT_CODE", "FAIL",2),
	TASKMOBILE_WAIT_CODE_ERROR2 ("系统繁忙，请稍后再试","WAIT_CODE", "FAIL",2),
	TASKMOBILE_WAIT_CODE_ERROR3 ("短信验证码验证失败,不存在此用户","WAIT_CODE", "FAIL",2),
	TASKMOBILE_WAIT_CODE_ERROR4 ("您输入的短信密码已失效！请重新获取短信密码！！","WAIT_CODE", "FAIL",2),
	TASKMOBILE_WAIT_CODE_ERROR5 ("短信验证码验证失败","WAIT_CODE", "FAIL",2),

	TASKMOBILE_READY_CODE_SECOND_DONING ("二次短信验准备发送.....","READY_CODE_SECOND", "DONING",0),
	
	TASKMOBILE_WAIT_CODE_SECOND_SUCCESS ("二次短信验证码已发送，请注意查收","WAIT_CODE_SECOND", "SUCCESS",0),
	TASKMOBILE_WAIT_CODE_SECOND_DOING ("二次短信验证码发送中，请注意查收","WAIT_CODE_SECOND", "DOING",0),
	TASKMOBILE_WAIT_CODE_SECOND_ERROR ("二次短信验证码发送失败！","WAIT_CODE_SECOND", "FAIL",2),
	TASKMOBILE_WAIT_CODE_SECOND_DOING1 ("二次登录所需短信验证码发送中，请注意查收","WAIT_CODE_SECOND", "DOING",0),
	TASKMOBILE_WAIT_CODE_SECOND_ERROR1 ("二次登录所需短信验证码发送失败！","WAIT_CODE_SECOND", "FAIL",2),
	TASKMOBILE_WAIT_CODE_SECOND_SUCCESS1 ("二次登录所需短信验证码已发送，请注意查收","WAIT_CODE_SECOND", "SUCCESS",0),
	
	TASKMOBILE_IMG_VERIFY_SUCCESS("二次验证：图片验证码验证成功！","IMG_VALIDATE","SUCCESS",0),
	TASKMOBILE_IMG_VERIFY_ERROR("二次验证：图片验证码验证失败三次！","IMG_VALIDATE","FAIL",2),

	TASKMOBILE_CRAWLER_READ ("数据采集开始采集，请耐心等待...","CRAWLER", "DOING",0),
	TASKMOBILE_CRAWLER_DONING ("数据采集中，请耐心等待...","CRAWLER", "DOING",0),
	TASKMOBILE_CRAWLER_SUCCESS ("数据采集成功！","CRAWLER", "SUCCESS",0),
	TASKMOBILE_CRAWLER_ERROR ("数据采集失败！","CRAWLER", "FAIL",2),
	//hyx
	TASKMOBILE_PASSWORD_DONING ("账号验证中...","PASSWORD", "DOING",0),
	TASKMOBILE_PASSWORD_SUCCESS1 ("联通修改密码账号验证成功！","PASSWORD", "SUCCESS",0),
	
	

	TASKMOBILE_PASSWORD_SUCCESS ("账号验证成功！","PASSWORD", "SUCCESS",0),//不推荐使用

	
	
	TASKMOBILE_PASSWORD_ERROR ("账号验证失败！","PASSWORD", "FAIL",2),
	TASKMOBILE_PASSWORD_ERROR2 ("账号验证失败！24小时内发送5次信息","PASSWORD", "FAIL",202),
	TASKMOBILE_PASSWORD_ERROR3 ("账号验证失败！联通密码修改服务繁忙,请稍后重试","PASSWORD", "FAIL",203),
	TASKMOBILE_PASSWORD_ERROR4 ("账号验证失败！联通系统繁忙,请稍后重试","PASSWORD", "FAIL",204),
	TASKMOBILE_PASSWORD_ERROR5 ("账号验证失败！24小时内输错3次随机码","PASSWORD", "FAIL",204),
	TASKMOBILE_PASSWORD_ERROR6 ("账号验证失败！短信服务密码连续输入错误的次数超过三次,请明日再登录!","PASSWORD", "FAIL",204),
	
	TASKMOBILE_PASSWORD_CHANGE_DONING ("密码修改中...","PASSWORD_CHANGE", "DOING",0),
	TASKMOBILE_PASSWORD_CHANGE_SUCCESS ("密码修改成功！","PASSWORD_CHANGE", "SUCCESS",0),
	TASKMOBILE_PASSWORD_CHANGE_ERROR ("密码修改失败！","PASSWORD_CHANGE", "FAIL",2),
	
	TASKMOBILE_PASSWORD_CHANGE_ERROR1 ("密码不能为规律数字","PASSWORD_CHANGE", "FAIL",2),
	TASKMOBILE_PASSWORD_CHANGE_ERROR2 ("数字不能相同","PASSWORD_CHANGE", "FAIL",2),
	TASKMOBILE_PASSWORD_CHANGE_ERROR3 ("数字不能递增或递减","PASSWORD_CHANGE", "FAIL",2),
	TASKMOBILE_PASSWORD_CHANGE_ERROR4 ("密码过于简单","PASSWORD_CHANGE", "FAIL",2),
	
	TASKHOUSING_PASSWORD_CHANGE_ERROR6 ("密码长度只能为6位","PASSWORD_CHANGE", "FAIL",2),
	TASKHOUSING_CRAWLER_ID_ERROR ("卡号不存在","CRAWLER_ID", "FAIL",2),
	
	TASKMOBILE_PASSWORD_READYCODE_DONING ("准备发送验证码...","PASSWORD_READYCODE", "DOING",0),
	
	//宁夏电信  qzb
	TASKMOBILE_GATHER_BASE_CRAWLING("用户信息爬取，采集中....","CRAWLER_USERINFO", "DOING",0),
	TASKMOBILE_GATHER_BASINESS_CRAWLING("业务信息爬取，采集中....","CRAWLER_BUSINESS", "DOING",0),
	TASKMOBILE_GATHER_CALL_CRAWLING("通话记录信息爬取，采集中....","CRAWLER_PHONEHISTORY", "DOING",0),
	TASKMOBILE_GATHER_CALL_CRAWLING2("短信记录信息爬取，采集中....","CRAWLER_PHONEHISTORY", "DOING",0),
	TASKMOBILE_GATHER_MONTHBILL_CRAWLING("月账单记录信息爬取，采集中....","CRAWLER_MONTHBILL", "DOING",0),
	TASKMOBILE_GATHER_PAYMSG_CRAWLING("缴费信息爬取，采集中....","CRAWLER_PAY", "DOING",0),
	
	HOUSING_LOGIN_TIMEOUT("连接超时！","LOGIN","FAIL",104),
	HOUSING_LOGIN_IDNUM_ERROR("请输入合法的身份证号码","LOGIN","FAIL",103),
	HOUSING_LOGIN_IDNUM_ERROR_INEXISTENCE("身份证号不存在","LOGIN","FAIL",103),
	HOUSING_LOGIN_IDNUMORPWD_ERROR("输入的账号或密码错误！","LOGIN","FAIL",104),
	HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR("图片验证码验证失败！","LOGIN","FAIL",104),
	HOUSING_LOGIN_CARD_FUND_ERROR("输入的身份证号或公积金账号错误！","LOGIN","FAIL",104),
	HOUSING_LOGIN_CARD_NAME_ERROR("输入的身份证号或姓名错误！","LOGIN","FAIL",104),
	HOUSING_LOGIN_PWD_ERROR("输入的密码错误！","LOGIN","FAIL",104),
	HOUSING_LOGIN_CODE_ERROR("输入的验证码错误！","LOGIN","FAIL",104),

	//生成报告
	TASKMOBILE_REPORT_DONING("开始生成报告","REPORT","DOING",0);

	

	private String description;
	
    private String phase;//当前步骤
	
	private String phasestatus;//步骤状态
	
	private Integer error_code;
	
	private StatusCodeEnum(String description,String phase, String phasestatus,Integer error_code) {
		this.description = description;
		this.phase = phase;
		this.phasestatus = phasestatus;
		this.error_code = error_code;
	}
	
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
		
}
