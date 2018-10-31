package com.crawler.mobile.json;

import java.util.List;

public class BasicUser {

    private String name;		//姓名

    private String idnum;		//身份证号
    private List<TaskMobile> taskMobiles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public List<TaskMobile> getTaskMobiles() {
        return taskMobiles;
    }

    public void setTaskMobiles(List<TaskMobile> taskMobiles) {
        this.taskMobiles = taskMobiles;
    }
}
