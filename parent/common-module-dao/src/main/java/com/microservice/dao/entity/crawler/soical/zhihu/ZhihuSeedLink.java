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
@Table(name = "forum_zhihu_seed_link")
public class ZhihuSeedLink extends IdEntity implements Serializable {
    private String keyword;
    private String url;
    private int defaultPageCount = 3;
    private String lastCrawlTime;
    //private int offset = 5;

    public String getLastCrawlTime() {
        return lastCrawlTime;
    }

    public void setLastCrawlTime(String lastCrawlTime) {
        this.lastCrawlTime = lastCrawlTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String tieba) {
        this.keyword = tieba;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDefaultPageCount() {
        return defaultPageCount;
    }

    public void setDefaultPageCount(int defaultPageCount) {
        this.defaultPageCount = defaultPageCount;
    }

//    public int getOffset() {
//        return offset;
//    }
//
//    public void setOffset(int offset) {
//        this.offset = offset;
//    }

    @Override
    public String toString() {
        return "ZhihuSeedLink{" +
                "tieba='" + keyword + '\'' +
                ", url='" + url + '\'' +
                ", defaultPageCount=" + defaultPageCount +
                ", lastCrawlTime='" + lastCrawlTime + '\'' +
                //", offset='" + offset + '\'' +
                '}';
    }
}
