package com.crawler;

public enum TaskStatusCode {


    PARAMETER_ERROR("参数错误.", "PARAMETER_ERROR", "ERROR", 4),

    GET_LOGIN_QR_CODE("获取登录二维码...", "GET_LOGIN_QR_CODE", "DOING", 14),
    GET_LOGIN_QR_CODE_ERROR("获取登录二维码失败！ 请稍后再试。", "GET_LOGIN_QR_CODE_ERROR", "ERROR", 17),
    NEED_SWEEP_QR_CODE("请在手机上扫二维码...", "NEED_SWEEP_QR_CODE", "DOING", 15),
    QR_CODE_CONFIRM_LOGIN("请在手机上扫码并确认登录...", "NEED_SWEEP_QR_CODE", "DOING", 16),
    QR_CODE_CONFIRM_WAIT_LOGIN("手机已扫码请确认登录...", "NEED_COMFIRM_QR_CODE", "DOING", 16),
    ALIPAY_SYSTEM_ERROR("支付宝系统繁忙，请您稍后再试", "ALIPAY_SYSTEM_ERROR", "ERROR", 18),

    //登录系列
    LOGIN_DOING("正在登录...", "LOGIN", "DOING", 100),
    LOGIN_LOGINNAME_ERROR("您输入的用户名信息有误！", "LOGIN", "ERROR", 101),
    LOGIN_LOGINNAME_NOT_EMAIL_OR_PHONENO("您输入的用户名不是邮箱或手机号码！", "LOGIN", "ERROR", 17),
    LOGIN_CAPTCHA_ERROR("图片验证码错误！", "LOGIN", "ERROR", 101),
    LOGIN_PWD_ERROR("您输入的密码错误！", "LOGIN", "ERROR", 102),
    LOGIN_ABNORMAL_ERROR("异常错误！", "LOGIN", "ERROR", 102),
    LOGIN_LOGINNAME_PWD_ERROR("您输入的登录名或密码错误！", "LOGIN", "ERROR", 102),
    LOGIN_PWD_SIM_ERROR("您输入的密码过于简单！", "LOGIN", "ERROR", 103),
    LOGIN_NOT_SET_INTERNETPWD_ERROR("很抱歉，您的账号还未绑定网银，请在银行官网网银页面设置网银密码后再进行操作！", "LOGIN", "ERROR", 103),
    LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR("很抱歉，您的账号还未设置安全问题，请在银行官网网银页面设置安全问题后再进行操作！", "LOGIN", "ERROR", 103),
    LOGIN_SUCCESS_NEEDSMS("登录成功,需进行短信验证码效验", "LOGIN", "SUCCESS_NEEDSMS", 200),//由于登录成功之后，下一步可能是需要发送端，也可能是开始采集数据，这里讲登录成功的枚举拆分成两个
    LOGIN_SUCCESS_NEXTSTEP("登录成功,开始进入下一步", "LOGIN", "SUCCESS_NEXTSTEP", 200),
    LOGIN_TIMEOUT_ERROR("网络超时！", "LOGIN", "ERROR", 200),

    LOGIN_ERROR_GOTO_QRCODE("您的支付宝账号存在安全风险，请使用扫码登录认证", "LOGIN", "ERROR", 302),

    //等待扫描二维码
    WATING_SCAN_QRCODE("正在验证已扫码二维码", "LOGIN", "DOING", 505),

    //爬取系列
    CRAWLER_DOING("数据正在采集 请稍后...", "CRAWLER", "DOING", 103),
    CRAWLER_SUCCESS("数据采集成功！", "CRAWLER", "SUCCESS", 200),
    CRAWLER_ERROR_PAGENUM("数据采集失败！未获得第一页数据", "CRAWLER", "ERROR", 200),
    CRAWLER_SUCCESS_ONEPAGE("数据采集成功！已获得第一页数据及总页数", "CRAWLER", "SUCCESS", 200),

    //短信验证码发送、效验系列
    //SENDSMSCODE_ERROR("发送短信验证码失败！","SENDSMSCODR","ERROR",200),
    //SENDSMSCODE_SUCCESS("发送短信验证码成功！","SENDSMSCODR","SUCCESS",200),
    //VERIFYSMSCODE_ERROR("系统错误，短信验证码效验失败！","VERIFYSMSCODE","ERROR",200),
    //VERIFYSMSCODE_FAIL("输入的短信验证码不正确！","VERIFYSMSCODE","FAIL",200),
    //VERIFYSMSCODE_SUCCESS("短信验证码效验成功！","VERIFYSMSCODE","SUCCESS",200),

    //===================sln==============================
    LOGIN_ERROR("登录失败！", "LOGIN", "ERROR", 101),
    RESETPASSWORD_TIP("您的登录密码过于简单，请登录华夏银行官网修改后再登录本系统！", "RESETPASSWORD", "TIP", 102),
    //=================================================

    //
    RESETPASSWORD_ERROR("首次登录网银需要修改初始密码，请前往光大银行官网修改密码！", "RESETPASSWORD", "TIP", 103),

    //登陆后的未知情况
    LOGIN_ERROR2("您的账户没有查询权限，请您到账户所在银行官网检查确认后重试。", "LOGIN", "ERROR", 103),

    //是否需要发送短信验证码  第一次对应TaskBank的description字段，第二个对应phase，第三个对应phase_status，第四个暂无用
    //HAVE_SMS("需要发送验证短信","SMS","SUCCESS",200),
    //NO_SMS("不需要发送验证短信","SMS","FAIL",200),

    //短信验证码发送系列
    SEND_CODE_DONING("短信验证码发送中....", "SEND_CODE", "DONING", 0),
    SEND_CODE_ERROR("短信验证码发送失败", "SEND_CODE", "FAIL", 2),
    SEND_CODE_ERROR1("短信验证码发送失败,您今天内获取取随机密码已超出限制", "SEND_CODE", "FAIL", 2),
    WAIT_CODE_SUCCESS("短信验证码已发送，请注意查收", "WAIT_CODE", "SUCCESS", 0),

    //短信验证码效验系列
    VALIDATE_CODE_DONING1("第一次短信验证码验证中.....", "VALIDATE", "DONING", 2),
    VALIDATE_CODE_DONING2("第二次短信验证码验证中.....", "VALIDATE", "DONING", 2),
    VALIDATE_CODE_DONING("短信验证码验证中.....", "VALIDATE", "DONING", 2),
    VALIDATE_CODE_SUCCESS("短信验证码验证成功", "VALIDATE", "SUCCESS", 0),
    VALIDATE_CODE_FIRST_ERROR("第一次短信验证码验证失败", "VALIDATE", "FAIL", 2),
    VALIDATE_CODE_SECOND_ERROR("第二次短信验证码验证失败", "VALIDATE", "FAIL", 2),
    VALIDATE_CODE_ERROR("短信验证码验证失败", "VALIDATE", "FAIL", 2),
    VALIDATE_CODE_ERROR1("系统繁忙，请稍后再试", "VALIDATE", "FAIL", 2),
    VALIDATE_CODE_ERROR2("您输入的短信密码已失效！请重新获取短信密码！", "VALIDATE", "FAIL", 2),
    VALIDATE_CODE_ERROR3("您输入的短信验证码有误或验证超时，请重新登录！", "VALIDATE", "FAIL", 2),

    //个人信息
    USERINFO_SUCCESS("数据采集中，个人信息采集成功！", "CRAWLER", "DONING", 200),
    USERINFO_ERROR("数据采集中，个人信息采集完成！", "CRAWLER", "DONING", 201),
    USERINFO_ERROR2("数据采集中，个人信息采集完成！", "CRAWLER", "DONING", 404),

    // 流水信息
    TRANSFLOW_SUCCESS("数据采集中，流水信息采集成功！", "CRAWLER", "DONING", 200),
    TRANSFLOW_ERROR("数据采集中，流水信息采集完成！", "CRAWLER", "DONING", 201),
    TRANSFLOW_ERROR2("数据采集中，流水信息采集完成！", "CRAWLER", "DONING", 404),

    //期限信息
    DEADLINE_SUCCESS("数据采集中，账户信息采集成功！", "CRAWLER", "DONING", 200),
    DEADLINE_ERROR("数据采集中，账户信息采集完成！", "CRAWLER", "DONING", 404),
    //webdriver 错误系列
    WEBDRIVER_ERROR("当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配", "WEBDRIVER", "ERROR", 3),

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


    private TaskStatusCode(String description, String phase, String phasestatus, Integer error_code) {
        this.description = description;
        this.phase = phase;
        this.phasestatus = phasestatus;
        this.error_code = error_code;
    }

}
