package com.crawler.pbccrc.json;


public enum StandaloneEnum{

    STANDALONE_LOGIN_DONING ("正在登录，请耐心等待...","LOGIN", "DONING",200),
    STANDALONE_CRAWLER_DONING ("正在查询，请耐心等待...","CRAWLER", "DONING",300),

    STANDALONE_LOGIN_PBCCRC_ERROR1("人行征信网站被屏蔽","LOGIN","ERROR",-1),

    STANDALONE_LOGIN_SUCCESS("登录成功","LOGIN","SUCCESS",0),
    STANDALONE_CRAWLER_SUCCESS("查询成功","CRAWLER","SUCCESS",1),

    STANDALONE_TRADECODE_ERROR1("征信报告未生成或授权码已过期","TRADECODE","ERROR",2),
    STANDALONE_TRADECODE_ERROR2("授权码错误","TRADECODE","ERROR",3),

    STANDALONE_PASSWORD_ERROR("用户名或密码错误","PASSWORD","ERROR",4),
    //Agent中间层
    STANDALONE_AGENT_ERROR("系统繁忙，请稍后再试","AGENT","ERROR",5),

    STANDALONE_LOGIN_ERROR2("网络超时，请重试","LOGIN","ERROR",6),
    STANDALONE_LOGIN_ERROR3("登录失败","LOGIN","ERROR",-2),
    STANDALONE_CRAWLER_ERROR("查询失败","CRAWLER","ERROR",-3),
    STANDALONE_CRAWLER_ERROR2("网络超时，请重试","CRAWLER","ERROR",6);

    private String description;

    private String phase;//当前步骤

    private String phasestatus;//步骤状态

    private Integer code;

    StandaloneEnum(String description, String phase, String phasestatus, Integer code) {
        this.description = description;
        this.phase = phase;
        this.phasestatus = phasestatus;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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


}
