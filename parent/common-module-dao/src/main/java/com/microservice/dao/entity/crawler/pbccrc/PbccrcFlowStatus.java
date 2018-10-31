package com.microservice.dao.entity.crawler.pbccrc;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 人行征信的爬虫流水状态
 * Created by zmy on 2018/7/13.
 */
@Entity
@Table(name="pbccrc_flow_status")
public class PbccrcFlowStatus extends AbstractEntity implements Serializable {

    private String mappingId;  //uuid 唯一标识
    private String message;   //描述

    public PbccrcFlowStatus() {
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PbccrcFlowStatus{" +
                "mappingId='" + mappingId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
