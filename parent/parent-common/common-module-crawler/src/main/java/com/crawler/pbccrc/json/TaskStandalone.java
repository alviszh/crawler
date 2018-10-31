package com.crawler.pbccrc.json;

import java.io.Serializable;

public class TaskStandalone implements Serializable {

    private String taskid;//uuid 前端通过uuid访问状态结果

    private String phase;//当前步骤

    private String phase_status;//步骤状态

    private String description;

    private String serviceName; //服务名称（人行征信、失信网...）

    private String owner;//数据所属人

    private Boolean finished;//爬虫任务是否全部完成

    private Integer code; //状态码

    private String key; //H5地址传入的唯一标识
    
    private String createtime;   //为了爬取任务邮件展示所用  sln 2018-09-20

    private String testhtml; //参数JSON

    //opendata项目中用到的，不然opendata项目中调不到bank-etl项目中的接口
    private String environmentId;	//environmentId

    public TaskStandalone() {
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getPhase_status() {
        return phase_status;
    }

    public void setPhase_status(String phase_status) {
        this.phase_status = phase_status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

    public String getTesthtml() {
        return testhtml;
    }

    public void setTesthtml(String testhtml) {
        this.testhtml = testhtml;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
    }

    @Override
    public String toString() {
        return "TaskStandalone{" +
                "taskid='" + taskid + '\'' +
                ", phase='" + phase + '\'' +
                ", phase_status='" + phase_status + '\'' +
                ", description='" + description + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", owner='" + owner + '\'' +
                ", finished=" + finished +
                ", code=" + code +
                ", key='" + key + '\'' +
                ", createtime='" + createtime + '\'' +
                ", testhtml='" + testhtml + '\'' +
                ", environmentId='" + environmentId + '\'' +
                '}';
    }
}
