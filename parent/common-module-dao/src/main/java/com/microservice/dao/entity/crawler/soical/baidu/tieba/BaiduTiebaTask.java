package com.microservice.dao.entity.crawler.soical.baidu.tieba;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "forum_baidu_tieba_task")
public class BaiduTiebaTask extends IdEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String startTime;
    private String endTime;
    private int code;
    private String tieba;
    private int threadPostNum;
    private int newInsertMainPostNum;
    private int newInsertThreadPostNum;
    private int crawlPageNum;
    private int mainPostNum;

    public int getNewInsertMainPostNum() {
        return newInsertMainPostNum;
    }

    public void setNewInsertMainPostNum(int newInsertMainPostNum) {
        this.newInsertMainPostNum = newInsertMainPostNum;
    }

    public int getNewInsertThreadPostNum() {
        return newInsertThreadPostNum;
    }

    public void setNewInsertThreadPostNum(int newInsertThreadPostNum) {
        this.newInsertThreadPostNum = newInsertThreadPostNum;
    }

    public int getMainPostNum() {
        return mainPostNum;
    }

    public void setMainPostNum(int mainPostNum) {
        this.mainPostNum = mainPostNum;
    }

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

    public String getTieba() {
        return tieba;
    }

    public void setTieba(String tieba) {
        this.tieba = tieba;
    }

    public int getThreadPostNum() {
        return threadPostNum;
    }

    public void setThreadPostNum(int threadPostNum) {
        this.threadPostNum = threadPostNum;
    }

    public int getCrawlPageNum() {
        return crawlPageNum;
    }

    public void setCrawlPageNum(int crawlPageNum) {
        this.crawlPageNum = crawlPageNum;
    }
}
