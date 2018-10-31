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
import java.util.Date;


@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "forum_baidu_tieba_main_post",
        indexes = {@Index(name = "main_post_index", columnList = "main_post_unique_code", unique = true)})
public class BaiduTiebaMainPost extends IdEntitySocial implements Serializable {
    private String mainPostUrl;

    private String mainPostUniqueCode;

    private String postTime;

    private String mainPostText;

    private int lastCrawlPageNum;

    private String title;
    private Long seedLinkId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLastCrawlPageNum() {
        return lastCrawlPageNum;
    }

    public void setLastCrawlPageNum(int lastCrawlPageNum) {
        this.lastCrawlPageNum = lastCrawlPageNum;
    }

    //    public String getPoster() {
//        return poster;
//    }

//    public void setPoster(String poster) {
//        this.poster = poster;
//    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

//    public String getPostTitle() {
//        return postTitle;
//    }

//    public void setPostTitle(String postTitle) {
//        this.postTitle = postTitle;
//    }

    public String getMainPostUrl() {
        return mainPostUrl;
    }

    public void setMainPostUrl(String mainPostUrl) {
        this.mainPostUrl = mainPostUrl;
    }

    @Column(name = "main_post_unique_code", nullable = false)
    public String getMainPostUniqueCode() {
        return mainPostUniqueCode;
    }

    public void setMainPostUniqueCode(String mainPostUniqueCode) {
        this.mainPostUniqueCode = mainPostUniqueCode;
    }

    @Column(columnDefinition = "text")
    public String getMainPostText() {
        return mainPostText;
    }

    public void setMainPostText(String mainPostText) {
        this.mainPostText = mainPostText;
    }

//    public Date getCrawlTime() {
//        return crawlTime;
//    }

//    public void setCrawlTime(Date crawlTime) {
//        this.crawlTime = crawlTime;
//    }

    @Override
    public String toString() {
        return "TiebaMainPost{" +
//                "poster='" + poster + '\'' +
                ", postTime='" + postTime + '\'' +
//                ", postTitle='" + postTitle + '\'' +
                ", mainPostUrl='" + mainPostUrl + '\'' +
                ", mainPostUniqueCode='" + mainPostUniqueCode + '\'' +
                ", mainPostText='" + mainPostText + '\'' +
                '}';
    }

    public void setSeedLinkId(Long seedLinkId) {
        this.seedLinkId = seedLinkId;
    }

    public Long getSeedLinkId() {
        return seedLinkId;
    }
}
