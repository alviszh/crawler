package com.microservice.dao.entity.crawler.insurance.taiyuan;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/30. 居民缴费信息
 */
@Entity
@Table(name="insurance_taiyuan_residentwater")
public class InsuranceTaiyuanResidentWater  extends IdEntity {
    private String personNum;         //个人编号
    private String company;           //公司
    private String payType;           //缴费类型
    private String payYear;           //缴费年度
    private String medicalPayAmount;  //居民医疗保险骄缴费金额
    private String personMoney;       //居民医保个人缴纳金额
    private String govMoney;          //财政补贴金额
    private String zhongyangMoney;    //其中中央补贴
    private String provinceMoney;     //省自治区补贴
    private String cityMoney;         //市补贴
    private String countyMoney;       //县补贴
    private String canbaoMoney;       //残保金额
    private String insuranceType;     //险种类型
    private String handleTime;        //经办时间
    private String taskId;

    public String getPersonNum() {
        return personNum;
    }

    public InsuranceTaiyuanResidentWater() {
    }

    public InsuranceTaiyuanResidentWater(String personNum, String company, String payType, String payYear, String medicalPayAmount, String personMoney, String govMoney, String zhongyangMoney, String provinceMoney, String cityMoney, String countyMoney, String canbaoMoney, String insuranceType, String handleTime, String taskId) {
        this.personNum = personNum;
        this.company = company;
        this.payType = payType;
        this.payYear = payYear;
        medicalPayAmount = medicalPayAmount;
        this.personMoney = personMoney;
        this.govMoney = govMoney;
        this.zhongyangMoney = zhongyangMoney;
        this.provinceMoney = provinceMoney;
        this.cityMoney = cityMoney;
        this.countyMoney = countyMoney;
        this.canbaoMoney = canbaoMoney;
        this.insuranceType = insuranceType;
        this.handleTime = handleTime;
        this.taskId = taskId;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayYear() {
        return payYear;
    }

    public void setPayYear(String payYear) {
        this.payYear = payYear;
    }

    public String getMedicalPayAmount() {
        return medicalPayAmount;
    }

    public void setMedicalPayAmount(String medicalPayAmount) {
        this.medicalPayAmount = medicalPayAmount;
    }

    public String getPersonMoney() {
        return personMoney;
    }

    public void setPersonMoney(String personMoney) {
        this.personMoney = personMoney;
    }

    public String getGovMoney() {
        return govMoney;
    }

    public void setGovMoney(String govMoney) {
        this.govMoney = govMoney;
    }

    public String getZhongyangMoney() {
        return zhongyangMoney;
    }

    public void setZhongyangMoney(String zhongyangMoney) {
        this.zhongyangMoney = zhongyangMoney;
    }

    public String getProvinceMoney() {
        return provinceMoney;
    }

    public void setProvinceMoney(String provinceMoney) {
        this.provinceMoney = provinceMoney;
    }

    public String getCityMoney() {
        return cityMoney;
    }

    public void setCityMoney(String cityMoney) {
        this.cityMoney = cityMoney;
    }

    public String getCountyMoney() {
        return countyMoney;
    }

    public void setCountyMoney(String countyMoney) {
        this.countyMoney = countyMoney;
    }

    public String getCanbaoMoney() {
        return canbaoMoney;
    }

    public void setCanbaoMoney(String canbaoMoney) {
        this.canbaoMoney = canbaoMoney;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "InsuranceTaiyuanResidentWater{" +
                "\npersonNum='" + personNum + '\'' +
                "\n, company='" + company + '\'' +
                "\n, payType='" + payType + '\'' +
                "\n, payYear='" + payYear + '\'' +
                "\n, MedicalPayAmount='" + medicalPayAmount + '\'' +
                "\n, personMoney='" + personMoney + '\'' +
                "\n, govMoney='" + govMoney + '\'' +
                "\n, zhongyangMoney='" + zhongyangMoney + '\'' +
                "\n, provinceMoney='" + provinceMoney + '\'' +
                "\n, cityMoney='" + cityMoney + '\'' +
                "\n, countyMoney='" + countyMoney + '\'' +
                "\n, canbaoMoney='" + canbaoMoney + '\'' +
                "\n, insuranceType='" + insuranceType + '\'' +
                "\n, handleTime='" + handleTime + '\'' +
                "\n, taskId='" + taskId + '\'' +
                '}';
    }
}
