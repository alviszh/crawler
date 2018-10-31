package com.microservice.dao.entity.crawler.soical.zhihu;

import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "forum_zhihu_answer",
        indexes = {@Index(name = "answer_index", columnList = "answer_code", unique = true)})
public class ZhihuAnswer extends IdEntitySocial implements Serializable {

    private String questionCode;
    //赞同数
    private int voteupCount;
    ///////////////////////////////////////////////////
    private String answerCode;
    ////g更新时间
    private String updatedTime;
    //
    private String content;
    ////评论数
    private int commentCount;

    private String createdTime;

    private String authorName;

    private String authorHeadline;

    private int authorFollowerCount;

    private String authorUrlToken;

    public String getAuthorUrlToken() {
        return authorUrlToken;
    }

    public void setAuthorUrlToken(String authorUrlToken) {
        this.authorUrlToken = authorUrlToken;
    }

    public int getAuthorFollowerCount() {
        return authorFollowerCount;
    }

    public void setAuthorFollowerCount(int authorFollowerCount) {
        this.authorFollowerCount = authorFollowerCount;
    }

    @Column(length = 2048)
    public String getAuthorHeadline() {
        return authorHeadline;
    }

    public void setAuthorHeadline(String authorHeadline) {
        this.authorHeadline = authorHeadline;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    @Column(name = "answer_code", nullable = false)
    public String getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(columnDefinition = "text")
    public String getContent() {
        return content;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getVoteupCount() {
        return voteupCount;
    }

    public void setVoteupCount(int voteupCount) {
        this.voteupCount = voteupCount;
    }

}
