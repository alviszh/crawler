package com.crawler.e_commerce.json;

public enum E_ComerceStatusCode {
	
	//登录系列
	E_COMMERCE_LOGIN_DOING("正在登录...","LOGIN","DOING",100),
	E_COMMERCE_LOGIN_LOGINNAME_ERROR("您输入的用户名信息有误！","LOGIN","ERROR",101),
	E_COMMERCE_LOGIN_CAPTCHA_ERROR("图片验证码错误！","LOGIN","ERROR",101),
	E_COMMERCE_LOGIN_PWD_ERROR("您输入的密码错误！","LOGIN","ERROR",101),
	E_COMMERCE_LOGIN_LOGINNAME_PWD_ERROR("您输入的登录名或密码错误！","LOGIN","ERROR",101),
	E_COMMERCE_LOGIN_ABNORMAL_ERROR("异常错误！","LOGIN","ERROR",102),
	E_COMMERCE_LOGIN_PWD_SIM_ERROR("您输入的密码过于简单！","LOGIN","ERROR",103),
	E_COMMERCE_LOGIN_LOGINNAME_NOT_EMAIL_OR_PHONENO("您输入的用户名不是邮箱或手机号码！", "LOGIN", "ERROR", 104),
	E_COMMERCE_LOGIN_SUCCESS_NEEDSMS("登录成功,需进行短信验证码效验","LOGIN","SUCCESS_NEEDSMS",201),//由于登录成功之后，下一步可能是需要发送端，也可能是开始采集数据，这里讲登录成功的枚举拆分成两个
	E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP("登录成功,开始进入下一步","LOGIN","SUCCESS_NEXTSTEP",200),
	E_COMMERCE_LOGIN_TIMEOUT_ERROR("网络超时！","LOGIN","ERROR",140),
	E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE("您的账号存在安全风险，请使用扫码登录认证", "LOGIN", "ERROR", 302),
	E_COMMERCE_ALIPAY_SYSTEM_ERROR("支付宝系统繁忙，请您稍后再试", "LOGIN", "ERROR", 18),
	//二维码系列
	E_COMMERCE_GET_QRCODE_DOING("正在获取二维码", "LOGIN", "DOING", 110),
	E_COMMERCE_GET_QRCODE_ERROR("二维码异常，请稍后重试。", "LOGIN", "ERROR", 111),
	E_COMMERCE_WATING_SCAN_QRCODE("正在等待扫描二维码", "LOGIN", "WAIT_SCAN", 112),
	E_COMMERCE_WATING_CONFIRM_QRCODE("二维码已扫，请确认！", "LOGIN", "WAIT_CONFIRM", 113),
	E_COMMERCE_WATING_QRCODE_TIMEOUT("抱歉，二维码已过期！", "LOGIN", "WAIT_TIMEOUT", 114),
	//爬取系列
	E_COMMERCE_CRAWLER_DOING("正在爬取中。。。。","CRAWLER","DOING",103),
	E_COMMERCE_CRAWLER_SUCCESS("数据采集成功！","CRAWLER","SUCCESS",200),
	E_COMMERCE_CRAWLER_ERROR_PAGENUM("数据采集失败！未获得第一页数据","CRAWLER","ERROR",200),
	E_COMMERCE_CRAWLER_SUCCESS_ONEPAGE("数据采集成功！已获得第一页数据及总页数","CRAWLER","SUCCESS",200),
	
	E_COMMERCE_LOGIN_ERROR("登录失败！","LOGIN","ERROR",101),
	
	//短信验证码发送系列
	E_COMMERCE_SEND_CODE_DONING ("短信验证码发送中....","SEND_CODE", "DONING",0),
	E_COMMERCE_SEND_CODE_ERROR ("短信验证码发送失败","SEND_CODE", "ERROR",2),
	E_COMMERCE_SEND_CODE_ERROR1 ("短信验证码发送失败,您今天内获取取随机密码已超出限制","SEND_CODE", "ERROR",2), 
	E_COMMERCE_WAIT_CODE_SUCCESS ("短信验证码已发送，请注意查收","WAIT_CODE", "SUCCESS",0),
	
	//短信验证码效验系列
	E_COMMERCE_VALIDATE_CODE_DONING1("第一次短信验证码验证中.....","VALIDATE", "DONING",2),
	E_COMMERCE_VALIDATE_CODE_DONING2("第二次短信验证码验证中.....","VALIDATE", "DONING",2),
	E_COMMERCE_VALIDATE_CODE_DONING("短信验证码验证中.....","VALIDATE", "DONING",2),
	E_COMMERCE_VALIDATE_CODE_SUCCESS("短信验证码验证成功","VALIDATE", "SUCCESS",0),
	E_COMMERCE_VALIDATE_CODE_FIRST_ERROR ("第一次短信验证码验证失败","VALIDATE", "ERROR",2),
	E_COMMERCE_VALIDATE_CODE_SECOND_ERROR ("第二次短信验证码验证失败","VALIDATE", "ERROR",2),
	E_COMMERCE_VALIDATE_CODE_ERROR ("短信验证码验证失败","VALIDATE", "ERROR",2),
	E_COMMERCE_VALIDATE_CODE_ERROR1 ("系统繁忙，请稍后再试","VALIDATE", "ERROR",2),
	E_COMMERCE_VALIDATE_CODE_ERROR2 ("您输入的短信密码已失效！请重新获取短信密码！","VALIDATE", "ERROR",2),
	E_COMMERCE_VALIDATE_CODE_ERROR3 ("您输入的短信验证码有误或验证超时，请重新登录！","VALIDATE", "ERROR",2),
	
	//个人信息
	E_COMMERCE_USERINFO_SUCCESS("数据采集中，个人信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_USERINFO_ERROR("数据采集中，个人信息采集完成！","CRAWLER","DONING",201),
	E_COMMERCE_USERINFO_ERROR2("数据采集中，个人信息采集完成！","CRAWLER","DONING",404),
	
	// 流水信息
	E_COMMERCE_TRANSFLOW_SUCCESS("数据采集中，流水信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_TRANSFLOW_ERROR("数据采集中，流水信息采集完成！", "CRAWLER", "DONING",201),
	E_COMMERCE_TRANSFLOW_ERROR2("数据采集中，流水信息采集完成！","CRAWLER","DONING",404),
	
	// 流水信息
	E_COMMERCE_ORDER_SUCCESS("数据采集中，订单信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_ORDER_ERROR("数据采集中，订单信息采集完成！", "CRAWLER", "DONING",201),
	E_COMMERCE_ORDER_ERROR2("数据采集中，订单信息采集完成！","CRAWLER","DONING",404),
	
	//期限信息
	E_COMMERCE_DEADLINE_SUCCESS("数据采集中，账户信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_DEADLINE_ERROR("数据采集中，账户信息采集完成！","CRAWLER","DONING",404),
	//webdriver 错误系列 
	E_COMMERCE_WEBDRIVER_ERROR("当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配","WEBDRIVER","ERROR",3),
	
	//地址信息
	E_COMMERCE_ADDRESS_SUCCESS("数据采集中，地址信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_ADDRESS_ERROR("数据采集中，地址信息采集完成！","CRAWLER","DONING",201),
	E_COMMERCE_ADDRESS_ERROR2("数据采集中，地址信息采集完成！","CRAWLER","DONING",404),
	
	//地址信息
	E_COMMERCE_ALIUSER_SUCCESS("数据采集中，支付宝用户基本信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_ALIUSER_ERROR("数据采集中，支付宝用户基本信息采集完成！","CRAWLER","DONING",201),
	E_COMMERCE_ALIUSER_ERROR2("数据采集中，支付宝用户基本信息采集完成！","CRAWLER","DONING",404),
	
	//白条信息
	E_COMMERCE_BTPRIVILEGE_SUCCESS("数据采集中，白条信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_BTPRIVILEGE_ERROR("数据采集中，白条信息采集完成！","CRAWLER","DONING",201),
	E_COMMERCE_BTPRIVILEGE_ERROR2("数据采集中，白条信息采集完成！","CRAWLER","DONING",404),
	
	//认证信息
	E_COMMERCE_RENZHENG_SUCCESS("数据采集中，认证信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_RENZHENG_ERROR("数据采集中，认证信息采集完成！","CRAWLER","DONING",201),
	E_COMMERCE_RENZHENGE_ERROR2("数据采集中，认证信息采集完成！","CRAWLER","DONING",404),

	//银行卡信息
	E_COMMERCE_BANKCARD_SUCCESS("数据采集中，银行卡信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_BANKCARD_ERROR("数据采集中，银行卡信息采集成功","CRAWLER","DONING",201),
	E_COMMERCE_BANKCARD_ERROR2("数据采集中，银行卡信息采集成功","CRAWLER","DONING",404),
	
	//支付宝交易流水信息
	E_COMMERCE_ALIPAYMENT_SUCCESS("数据采集中，支付宝交易流水信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_ALIPAYMENT_ERROR("数据采集中，支付宝交易流水信息采集成功","CRAWLER","DONING",201),
	E_COMMERCE_ALIPAYMENT_ERROR2("数据采集中，支付宝交易流水信息采集成功","CRAWLER","DONING",404),
	
	//京东金融信息
	E_COMMERCE_COFFERS_SUCCESS("数据采集中，京东金融信息采集成功！","CRAWLER","DONING",200),
	E_COMMERCE_COFFERS_ERROR("数据采集中，京东金融信息采集完成！","CRAWLER","DONING",201),
	E_COMMERCE_COFFERS_ERROR2("数据采集中，京东金融信息采集完成！","CRAWLER","DONING",404),
	
	//系统自动退出
	SYSTEM_QUIT("系统超时请重试","SYSTEM","QUIT",-1),
	
	//Agent中间层
	E_COMMERCE_AGENT_ERROR("系统繁忙，请稍后再试","AGENT","ERROR",404);
	
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

	
	private E_ComerceStatusCode(String description,String phase, String phasestatus,Integer error_code) {
		this.description = description;
		this.phase = phase;
		this.phasestatus = phasestatus;
		this.error_code = error_code;
	}

}
