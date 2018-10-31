package com.microservice.dao.entity.crawler.soical.baidu.tieba;

import com.microservice.dao.entity.IdEntity;
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
@Table(name = "forum_baidu_tieba_thread_post",
        indexes = {@Index(name = "thread_post_index", columnList = "post_id", unique = false)})
public class BaiduTiebaThreadPost extends IdEntitySocial implements Serializable {
    private String postTime;
    private String userId;
    private String userName;
    private String postId;
    private String forumId;
    private String threadId;
    private String content;
    private String postIndex;
    private String mainPostUniqueCode;
    //private int isMainPost = 0;//是否为主贴 是为1 否为0 默认为否0
    private int pageNum;


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

//    public int getIsMainPost() {
//        return isMainPost;
//    }

//    public void setIsMainPost(int isMainPost) {
//        this.isMainPost = isMainPost;
//    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "post_id", nullable = true)
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    @Column(columnDefinition = "text")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(String postIndex) {
        this.postIndex = postIndex;
    }

    public String getMainPostUniqueCode() {
        return mainPostUniqueCode;
    }

    public void setMainPostUniqueCode(String mainPostUniqueCode) {
        this.mainPostUniqueCode = mainPostUniqueCode;
    }
}
