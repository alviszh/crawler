package com.microservice.dao.entity.crawler.soical.zhihu;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "forum_zhihu_task")

public class ZhihuTask extends IdEntity implements Serializable {
    private String startTime;
    private String endTime;
    private int code;
    private String keyword;
    private int questionNum;
    private int crawlPageNum;
    private int answerNum;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getCrawlPageNum() {
        return crawlPageNum;
    }

    public void setCrawlPageNum(int crawlPageNum) {
        this.crawlPageNum = crawlPageNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }
}
