package com.microservice.dao.entity.crawler.soical.baidu.tieba;


import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "forum_baidu_tieba_seed_link")

public class BaiduTiebaSeedLink extends IdEntity implements Serializable {
    private String tieba;
    private String url;
    private int defaultPageCount = 3;
    private String lastCrawlTime;

    public String getLastCrawlTime() {
        return lastCrawlTime;
    }

    public void setLastCrawlTime(String lastCrawlTime) {
        this.lastCrawlTime = lastCrawlTime;
    }

    public String getTieba() {
        return tieba;
    }

    public void setTieba(String tieba) {
        this.tieba = tieba;
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

    @Override
    public String toString() {
        return "BaiduTiebaSeedLink{" +
                "tieba='" + tieba + '\'' +
                ", url='" + url + '\'' +
                ", defaultPageCount=" + defaultPageCount +
                ", lastCrawlTime='" + lastCrawlTime + '\'' +
                '}';
    }
}
