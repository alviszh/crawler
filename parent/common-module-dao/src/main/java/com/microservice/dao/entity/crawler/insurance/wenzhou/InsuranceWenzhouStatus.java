package com.microservice.dao.entity.crawler.insurance.wenzhou;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/22.
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="insurance_wenzhou_status")
public class InsuranceWenzhouStatus  extends IdEntity {
    private String type;//保险类型
    private Integer count;
    private String taskId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
