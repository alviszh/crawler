package com.crawler.mobile.json;

public enum StatusCodeRec {

	
	//*****************************中国移动*****************************//
	MOBILE_AUTHENTICATION_ERROR("手机号认证失败(随机码不正确或已过期)", 1002), 
	MOBILE_LOGIN_ERROR("登录失败",1003), 
	MOBILE_LOGIN_SUCCESS("登录成功",1000), 
	QUERY_CALL_RECORD_TIMEOUT("详单查询请求超时", 1005),
	QUERY_CALL_RECORD_ERROR("详单查询异常", 1004),
	MOBILE_LOGIN_ERROR_SMS("短信随机码不正确或已过期，请重新获取",1006),
	MOBILE_LOGIN_ERROR_SMS_OVER("由于您的输入信息有误，短信验证码已经失效，一分钟后请重新获取短信验证码！",1015),
	MOBILE_LOGIN_REPEAT("您已登录，请勿重复登录！",1014),
	MOBILE_LOGIN_ERROR_SER("您的账户名与密码不匹配，请重新输入",1007),
	MOBILE_LOGIN_ERROR_SESSION("session信息为空，请先登录!",1008),
	MOBILE_LOGIN_TIMEOUT("网络超时！",1009),
	MOBILE_VERIFY_SMS_TOOMANY ("尊敬的用户，单位时间内下发短信次数过多，请稍后再使用！",1010),
	MOBILE_VERIFY_SMS_TOOMANY_TODAY ("尊敬的用户，今天下发短信次数过多，请明天再使用！",1011),
	MOBILE_VERIFY_IMG_THR_ERROR ("图片验证码验证连续失败三次！",1012),
	MOBILE_VERIFY_SEC_SMS_ERROR ("二次验证随机密码错误！请重新获取！",1013),
	MOBILE_VERIFY_IMG_THR_INIUTERROR ("图片验证码验证输入有误,请重试！",1012),

	MESSAGE_SUCESS_ONE("完成抓取，数据已存入数据库", 201), 
	MESSAGE_SUCESS_TWO("完成抓取，但没有新数据入库", 202),
	MESSAGE_SUCESS_THREE("今日已经抓取",203),
	MESSAGE_SQL_ONE("数据库查询完成", 205), 
	MESSAGE_SQL_TWO("数据库查询完成,但未获取到数据",205), 	
	MESSAGE_LOGIN_SUCESS("模拟登陆成功", 206),	
	MESSAGE_LOGIN_ERROR_ONE("获取通话详单失败",4041),
	MESSAGE_LOGIN_ERROR_TWO("登陆失败,验证码错误", 4042), 
	MESSAGE_LOGIN_ERROR_THREE("系统繁忙，请一分钟后再试", 4043), 
	MESSAGE_LOGIN_ERROR_FOURE("登陆失败，请检查密码是否错误", 4044),
	MESSAGE_LOGIN_ERROR_Five("登陆失败，请重试", 4045),
	MESSAGE_LOGIN_ERROR_six("登陆失败，联通有点小波动，请稍后再试", 4046),
	MESSAGE_LOGIN_ERROR_Seven("登陆失败，不支持简单密码登录", 4047),
	MESSAGE_LOGIN_ERROR_Eight("登陆失败，不支持初始密码登录,", 4048),
	MESSAGE_LOGIN_ERROR_Nine("登录失败,电信网站出现问题 ",4041),
	MESSAGE_LOGIN_ERROR_TEN("登录失败,账号已被锁定 ",4041),

	MESSAGE_CODE_ERROR_ONE("获取手机验证码失败",4041),
	MESSAGE_CODE_ERROR_TWO("手机验证码错误", 4042),
	MESSAGE_CODE_ERROR_THREE("证件信息不匹配", 4042),
	MESSAGE_CODE_ERROR_FOURE("获取手机验证码失败,短信发送过多,账号已被锁定",4041),
	MESSAGE_CODE_ERROR_FIVE("短信服务密码连续输入错误的次数超过三次，请明日再登录",4041),
	MESSAGE_CODE_ERROR_SIX("登录失败，不存在此用户",4041),

	MESSAGE_CODE_SUCESS("获取手机验证码成功",200),
	MESSAGE_CRAWLER_ERROR("抓取失败，用户访问次数过多,请明天再试", 505),
	
	MESSAGE_PASSWORD_ERROR1("证件信息不匹配，请重新输入", 4041),
	MESSAGE_PASSWORD_ERROR2("验证码错误", 4042),
	
	MESSAGE_PASSWORD_ERROR("系统繁忙，请稍后再试", 4043),

	CRAWLER_UserMsg_SUCESS("用户基本信息抓取成功",200),
	CRAWLER_UserMsg_ERROR("用户基本信息抓取失败",404),
	
	CRAWLER_AccountMsgStatus_SUCESS("账户信息抓取成功",200),
	CRAWLER_AccountMsgStatus_ERROR("账户信息抓取失败",404),
	
	CRAWLER_Balance_SUCESS("账户余额抓取成功",200),
	CRAWLER_Balance_ERROR("账户余额抓取失败",404),
	
	CRAWLER_CallRecordStatus_SUCESS("通话详单抓取成功",200),
	CRAWLER_CallRecordStatus_ERROR("通话详单抓取失败",404),
	
	CRAWLER_SMSRecordStatus_SUCESS("短信记录抓取成功",200),
	CRAWLER_SMSRecordStatus_ERROR("短信记录抓取失败",404),
	
	CRAWLER_BusinessMsgStatus_SUCESS("业务信息抓取成功",200),
	CRAWLER_BusinessMsgStatus_ERROR("业务信息抓取失败",404),
	
	CRAWLER_PayMsgStatus_SUCESS("缴费信息抓取成功",200),
	CRAWLER_PayMsgStatus_ERROR("缴费信息抓取失败",404),
	
	CRAWLER_IntegralMsgStatus_SUCESS("积分信息抓取成功",200),
	CRAWLER_IntegralMsgStatus_ERROR("积分信息抓取失败",404),
	
	CRAWLER_FamilyMsgStatus_SUCESS("亲情号信息抓取成功",200),
	CRAWLER_FamilyMsgStatus_ERROR("亲情号信息抓取失败",404),
	CRAWLER_FamilyMsgStatu_SUCESS("亲情号信息抓取成功",201);
	private StatusCodeRec(String message, int code) {
		this.message = message;
		this.code = code;
	}

	private String message;
	private int code;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
