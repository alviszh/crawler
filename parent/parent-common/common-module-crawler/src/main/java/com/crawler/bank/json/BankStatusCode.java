package com.crawler.bank.json;

public enum BankStatusCode {
	
	//登录系列
	BANK_LOGIN_DOING("正在登录...","LOGIN","DOING",100),
	BANK_LOGIN_SUCCESS_NEEDSMS("登录成功,需进行短信验证码效验","LOGIN","SUCCESS_NEEDSMS",200),//由于登录成功之后，下一步可能是需要发送端，也可能是开始采集数据，这里讲登录成功的枚举拆分成两个
	BANK_LOGIN_SUCCESS_NEXTSTEP("登录成功,开始进入下一步","LOGIN","SUCCESS_NEXTSTEP",200),
	
	
	BANK_LOGIN_LOGINNAME_ERROR("您输入的用户名信息有误!","LOGIN","ERROR",101),
	BANK_LOGIN_CAPTCHA_ERROR("图片验证码错误!","LOGIN","ERROR",101),
	BANK_LOGIN_PWD_ERROR("您输入的密码错误!","LOGIN","ERROR",102),
	BANK_LOGIN_LOGINNAME_PWD_ERROR("您输入的登录名或密码错误!","LOGIN","ERROR",102),
	BANK_LOGIN_PWD_SIM_ERROR("您输入的密码过于简单!","LOGIN","ERROR",103),
	BANK_LOGIN_NOT_SET_INTERNETBANK_PWD_ERROR("很抱歉，您的账号还未绑定网银，请在银行官网网银页面设置网银密码后再进行操作!","LOGIN","ERROR",103),
	BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR("很抱歉，您的账号还未设置安全问题，请在银行官网网银页面设置安全问题后再进行操作!","LOGIN","ERROR",103),
	     BANK_LOGIN_TIMEOUT_ERROR("网络超时!","LOGIN","ERROR",200),
	
	BANK_LOGIN_SUCCESS_NEEDSMSFORBOCOM("登录成功,由于您长期未登录交通银行信用卡官方网站，为了您账户安全，请您前往官方网站完成验证，验证成功后即可完成登录，感谢配合","LOGIN","ERROR",200),//由于登录成功之后，下一步可能是需要发送端，也可能是开始采集数据，这里讲登录成功的枚举拆分成两个

	//爬取系列
	     BANK_CRAWLER_DOING("正在爬取中....","CRAWLER","DOING",103),
	     BANK_CRAWLER_SUCCESS("数据采集成功!","CRAWLER","SUCCESS",200),
	     BANK_CRAWLER_ERROR_PAGENUM("数据采集失败!未获得第一页数据","CRAWLER","ERROR",200),
	     BANK_CRAWLER_SUCCESS_ONEPAGE("数据采集成功!已获得第一页数据及总页数","CRAWLER","SUCCESS",200),
	     BANK_CRAWLER_TIMEOUT_ERROR("网络超时!","CRAWLER","ERROR",200),
	
	
	//===================sln==============================
	BANK_LOGIN_ERROR("登录失败!","LOGIN","ERROR",101),
	BANK_RESETPASSWORD_TIP("您的登录密码过于简单，请登录华夏银行官网修改后再登录本系统!","RESETPASSWORD","TIP",102),
	//=================================================
	
	//
	BANK_RESETPASSWORD_ERROR("首次登录网银需要修改初始密码，请前往光大银行官网修改密码!","RESETPASSWORD","TIP",103),
	
	//登陆后的未知情况
	BANK_LOGIN_ERROR2("您的账户没有查询权限，请您到账户所在银行官网检查确认后重试.","LOGIN","ERROR",103),
	
	
	
	
	//短信验证码发送系列
	BANK_SEND_CODE_DONING ("短信验证码发送中....","SEND_CODE", "DONING",0),
	BANK_SEND_CODE_DONING2 ("第二次短信验证码发送中....","SEND_CODE", "DONING",0),
	BANK_SEND_CODE_ERROR ("短信验证码发送失败","SEND_CODE", "FAIL",2),
	BANK_SEND_CODE_ERROR1 ("短信验证码发送失败,您今天内获取取随机密码已超出限制","SEND_CODE", "FAIL",2), 
	BANK_WAIT_CODE_SUCCESS ("短信验证码已发送，请注意查收","WAIT_CODE", "SUCCESS",0),
	
	//短信验证码效验系列
	BANK_VALIDATE_CODE_DONING2("第二次短信验证码验证中.....","VALIDATE", "DONING",2),
	BANK_VALIDATE_CODE_DONING("短信验证码验证中.....","VALIDATE", "DONING",2),
	BANK_VALIDATE_CODE_SUCCESS("短信验证码验证成功","VALIDATE", "SUCCESS",0),
	BANK_VALIDATE_CODE_FIRST_ERROR ("短信验证码验证失败","VALIDATE", "FAIL",2),
	BANK_VALIDATE_CODE_SECOND_ERROR ("第二次短信验证码验证失败","VALIDATE", "FAIL",2),
	BANK_VALIDATE_CODE_ERROR ("短信验证码验证失败","VALIDATE", "FAIL",2),
	BANK_VALIDATE_CODE_ERROR1 ("系统繁忙，请稍后再试","VALIDATE", "FAIL",2),
	BANK_VALIDATE_CODE_ERROR2 ("您输入的短信密码已失效!请重新获取短信密码!","VALIDATE", "FAIL",2),
	BANK_VALIDATE_CODE_ERROR3 ("您输入的短信验证码有误或验证超时，请重新登录！","VALIDATE", "FAIL",2),
	
	//个人信息
	BANK_USERINFO_SUCCESS("数据采集中，个人信息采集成功!","CRAWLER","DONING",200),
	BANK_USERINFO_ERROR("数据采集中，个人信息采集完成!","CRAWLER","DONING",201),
	BANK_USERINFO_ERROR2("数据采集中，个人信息采集完成!","CRAWLER","DONING",404),
	
	// 流水信息
	BANK_TRANSFLOW_SUCCESS("数据采集中，流水信息采集成功!","CRAWLER","DONING",200),
	BANK_TRANSFLOW_ERROR("数据采集中，流水信息采集完成!", "CRAWLER", "DONING",201),
	BANK_TRANSFLOW_ERROR2("数据采集中，流水信息采集完成!","CRAWLER","DONING",404),
	
	//期限信息
	BANK_DEADLINE_SUCCESS("数据采集中，账户信息采集成功!","CRAWLER","DONING",200),
	BANK_DEADLINE_ERROR("数据采集中，账户信息采集完成!","CRAWLER","DONING",404),
	//webdriver 错误系列 
	BANK_WEBDRIVER_ERROR("当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配","WEBDRIVER","ERROR",3),
	
	//系统自动退出
	SYSTEM_QUIT("系统超时请重试","SYSTEM","QUIT",-1),

	//Agent中间层
	BANK_AGENT_ERROR("系统繁忙，请稍后再试","AGENT","ERROR",404);
	
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

	
	private BankStatusCode(String description,String phase, String phasestatus,Integer error_code) {
		this.description = description;
		this.phase = phase;
		this.phasestatus = phasestatus;
		this.error_code = error_code;
	}

}
