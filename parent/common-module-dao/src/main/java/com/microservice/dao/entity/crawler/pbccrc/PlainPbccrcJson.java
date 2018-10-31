package com.microservice.dao.entity.crawler.pbccrc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 保存页面json、源数据json、解析后的json
 * Created by zmy on 2018/3/30.
 */
@Entity
@Table(name="plain_pbccrc_json")
public class PlainPbccrcJson extends AbstractEntity implements Serializable {

    private String mappingId;  //uuid 唯一标识
    private String report_no;   //人行征信报告编号
    private String html; //页面json
    private String original_json;          //原始数据json
    private String json_v1;     //解析后V1的json
    private String json_v2;     //解析后V2的json
    private String json_jiemo;  //借么的json报文
    private String json_daihu;  //贷乎的json报文
    private String owner;

    private String return_wechat;   //回调接口的返回值-汇诚（微信公众号）
    private String return_jiemo;      //推送前置规则的返回值-借么
    private String return_daihu;      //推送前置规则的返回值-贷乎


    public String getReport_no() {
        return report_no;
    }

    public void setReport_no(String report_no) {
        this.report_no = report_no;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Column(columnDefinition="text")
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Column(columnDefinition="text")
    public String getOriginal_json() {
        return original_json;
    }

    public void setOriginal_json(String original_json) {
        this.original_json = original_json;
    }

    @Column(columnDefinition="text")
    public String getJson_v1() {
        return json_v1;
    }

    public void setJson_v1(String json_v1) {
        this.json_v1 = json_v1;
    }

    @Column(columnDefinition="text")
    public String getJson_v2() {
        return json_v2;
    }

    public void setJson_v2(String json_v2) {
        this.json_v2 = json_v2;
    }

    @Column(columnDefinition="text")
    public String getJson_jiemo() {
        return json_jiemo;
    }

    public void setJson_jiemo(String json_jiemo) {
        this.json_jiemo = json_jiemo;
    }

    @Column(columnDefinition="text")
    public String getJson_daihu() {
        return json_daihu;
    }

    public void setJson_daihu(String json_daihu) {
        this.json_daihu = json_daihu;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    @Column(columnDefinition="text")
    public String getReturn_wechat() {
        return return_wechat;
    }

    public void setReturn_wechat(String return_wechat) {
        this.return_wechat = return_wechat;
    }
    @Column(columnDefinition="text")
    public String getReturn_jiemo() {
        return return_jiemo;
    }

    public void setReturn_jiemo(String return_jiemo) {
        this.return_jiemo = return_jiemo;
    }

    @Column(columnDefinition="text")
    public String getReturn_daihu() {
        return return_daihu;
    }

    public void setReturn_daihu(String return_daihu) {
        this.return_daihu = return_daihu;
    }

    @Override
    public String toString() {
        return "PlainPbccrcJson{" +
                "mappingId='" + mappingId + '\'' +
                ", report_no='" + report_no + '\'' +
                ", html='" + html + '\'' +
                ", original_json='" + original_json + '\'' +
                ", json_v1='" + json_v1 + '\'' +
                ", json_v2='" + json_v2 + '\'' +
                ", json_jiemo='" + json_jiemo + '\'' +
                ", json_daihu='" + json_daihu + '\'' +
                ", owner='" + owner + '\'' +
                ", return_wechat='" + return_wechat + '\'' +
                ", return_jiemo='" + return_jiemo + '\'' +
                ", return_daihu='" + return_daihu + '\'' +
                '}';
    }
}
