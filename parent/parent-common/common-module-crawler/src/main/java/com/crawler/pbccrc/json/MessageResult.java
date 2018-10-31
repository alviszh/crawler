package com.crawler.pbccrc.json;

/**
 * @Author zmy
 * 2017/10/26.
 */
public class MessageResult {
    private String key;
    private String code;
    private String message;
    private String report;

    public MessageResult(String key, String code, String message) {
        this.key = key;
        this.code = code;
        this.message = message;
    }

    public MessageResult() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "MessageResult{" +
                "key='" + key + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", report='" + report + '\'' +
                '}';
    }
}
