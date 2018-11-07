package com.crawler;

/**
 * 通知信息
 * @Author zmy
 * 2018/11/6.
 */
public class NoticeInfo {
    private String key;
    private String userId;
    private String taskId;
    private String code;
    private String message;
    private String timestamp;

    public NoticeInfo(String key, String userId, String taskId, String code, String message, String timestamp) {
        this.key = key;
        this.userId = userId;
        this.taskId = taskId;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public NoticeInfo() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "NoticeInfo{" +
                "key='" + key + '\'' +
                ", userId='" + userId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
