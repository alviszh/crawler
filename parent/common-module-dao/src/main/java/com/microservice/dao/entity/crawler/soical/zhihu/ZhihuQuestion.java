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
@Table(name = "forum_zhihu_question",
        indexes = {@Index(name = "question_index", columnList = "question_code", unique = true)})
public class ZhihuQuestion extends IdEntitySocial implements Serializable {
    private String questionUrl;
    ////评论数
    private int commentCount;
    ////////////////////////////////////////////////////
    private String questionCode;
    //////// ////回答次数
    private int answerCount;
    ////////////////////
    private String excerpt;
    /////////////////////////
    private String title;
    ///////////////////////////////////////////////////
    private Long seedLinkId;
    //////浏览次数
    private int visitCount;
    ///关注者数量
    private int followerCount;
    ////更新时间  如果没有跟新过 就是创建时间
    private String updatedTime;
    //
    private String authorUrltoken;
    //作者
    private String authorName;
    //作者个性签名
    private String authorHeadline;

    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
    }

    @Column(name = "question_code", nullable = false)
    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public Long getSeedLinkId() {
        return seedLinkId;
    }

    public void setSeedLinkId(Long seedLinkId) {
        this.seedLinkId = seedLinkId;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    @Column(columnDefinition = "text")
    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    @Column(length = 2048)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Column(length = 2048)
    public String getAuthorHeadline() {
        return authorHeadline;
    }

    public void setAuthorHeadline(String authorHeadline) {
        this.authorHeadline = authorHeadline;
    }

    public String getAuthorUrltoken() {
        return authorUrltoken;
    }

    public void setAuthorUrltoken(String authorUrltoken) {
        this.authorUrltoken = authorUrltoken;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
