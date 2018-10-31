package com.crawler.pbccrc;

import java.util.Date;

public class WebData<T> {

    public RequestParam param;
    public T report;
    public String message;
    public Date createtime = new Date();
    public Integer code;
    public String profile;

    public RequestParam getParam() {
        return param;
    }

    public void setParam(RequestParam param) {
        this.param = param;
    }

    public T getReport() {
        return report;
    }

    public void setReport(T report) {
        this.report = report;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "WebData{" +
                "param=" + param +
                ", report=" + report +
                ", message='" + message + '\'' +
                ", createtime=" + createtime +
                ", code=" + code +
                ", profile='" + profile + '\'' +
                '}';
    }
}
