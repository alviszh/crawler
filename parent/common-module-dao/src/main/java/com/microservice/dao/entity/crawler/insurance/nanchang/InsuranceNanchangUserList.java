package com.microservice.dao.entity.crawler.insurance.nanchang;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 南昌社保个人信息
 *
 * @author tz
 */
@Entity
@Table(name = "insurance_nanchang_userinfolist" ,indexes = {@Index(name = "index_insurance_nanchang_userinfolist_taskid", columnList = "taskid")})
public class InsuranceNanchangUserList extends IdEntity {
    /**
     * taskid  uuid 前端通过uuid访问状态结果
     */
    private String taskid;

    /**
     * 个人编号
     */
    private String personalNumber;

    /**
     * 身份证号
     */
    private String idNum;
    /**
     * 姓名
     */
    private String name;
    /**
     * 出生日期
     */
    private String birthdate;


    /**
     * url信息
     */

    private String urlinfor;

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }


    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getTaskid() {
        return taskid;
    }


    public String getIdNum() {
        return idNum;
    }

    public String getName() {
        return name;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getUrlinfor() {
        return urlinfor;
    }

    public void setUrlinfor(String urlinfor) {
        this.urlinfor = urlinfor;
    }

    public String getBirthdate() {
        return birthdate;
    }



    public InsuranceNanchangUserList(String taskid, String personalNumber, String idNum, String name, String birthdate, String urlinfor) {
        this.taskid = taskid;
        this.personalNumber = personalNumber;
        this.idNum = idNum;
        this.name = name;
        this.birthdate = birthdate;
        this.urlinfor = urlinfor;
    }
}
