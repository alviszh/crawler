package com.microservice.dao.entity.crawler.insurance.zhoushan;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "insurance_zhoushan_userinfo")
public class InsuranceZhoushanUserInfo extends InsuranceZhoushanBasicBean implements Serializable {
    //    	人员id: "2483069",
    private String psseno;
    //                身份证号: "3309021989111287210",
    private String iscode;
    //                人员姓名: "王海燕",
    private String psname;
    //                人员状态: "中断",
    private String psstatus;
    //                所在单位: "和润集团有限公司",
    private String cpname;
    //               人员类别: "就业",
    private String rylb;
    //                出生年月 "1989-11-12",
    private String bdatee;
    //                参保地: "普陀区",
    private String sacode;
    //                联系电话 : "",
    private String tel;
    //                手机号码: "",
    private String mtel;
    //                邮编: "",
    private String zip;
    //                联系地址: "",
    private String address;
    //               公司编号: "451366",
    private String cpseno;
    //              养老保险: "1",
    private String ylbs;
    //                "医疗保险: "1",
    private String ybbs;
    //                "工伤保险": "1",
    private String gsbs;
    //                "生育保险": "1",
    private String ngsybs;
    //                "失业保险": "1"
    private String sybs;

    public String getPsseno() {
        return psseno;
    }

    public void setPsseno(String psseno) {
        this.psseno = psseno;
    }

    public String getIscode() {
        return iscode;
    }

    public void setIscode(String iscode) {
        this.iscode = iscode;
    }

    public String getPsname() {
        return psname;
    }

    public void setPsname(String psname) {
        this.psname = psname;
    }

    public String getPsstatus() {
        return psstatus;
    }

    public void setPsstatus(String psstatus) {
        this.psstatus = psstatus;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public String getRylb() {
        return rylb;
    }

    public void setRylb(String rylb) {
        this.rylb = rylb;
    }

    public String getBdatee() {
        return bdatee;
    }

    public void setBdatee(String bdatee) {
        this.bdatee = bdatee;
    }

    public String getSacode() {
        return sacode;
    }

    public void setSacode(String sacode) {
        this.sacode = sacode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMtel() {
        return mtel;
    }

    public void setMtel(String mtel) {
        this.mtel = mtel;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCpseno() {
        return cpseno;
    }

    public void setCpseno(String cpseno) {
        this.cpseno = cpseno;
    }

    public String getYlbs() {
        return ylbs;
    }

    public void setYlbs(String ylbs) {
        this.ylbs = ylbs;
    }

    public String getYbbs() {
        return ybbs;
    }

    public void setYbbs(String ybbs) {
        this.ybbs = ybbs;
    }

    public String getGsbs() {
        return gsbs;
    }

    public void setGsbs(String gsbs) {
        this.gsbs = gsbs;
    }

    public String getNgsybs() {
        return ngsybs;
    }

    public void setNgsybs(String ngsybs) {
        this.ngsybs = ngsybs;
    }

    public String getSybs() {
        return sybs;
    }

    public void setSybs(String sybs) {
        this.sybs = sybs;
    }

    public String getTaskid() {
        return this.taskid;
    }

    @Override
    public String toString() {
        return "InsuranceZhoushanUserInfo{" +
                "psseno='" + psseno + '\'' +
                ", iscode='" + iscode + '\'' +
                ", psname='" + psname + '\'' +
                ", psstatus='" + psstatus + '\'' +
                ", cpname='" + cpname + '\'' +
                ", rylb='" + rylb + '\'' +
                ", bdatee='" + bdatee + '\'' +
                ", sacode='" + sacode + '\'' +
                ", tel='" + tel + '\'' +
                ", mtel='" + mtel + '\'' +
                ", zip='" + zip + '\'' +
                ", address='" + address + '\'' +
                ", cpseno='" + cpseno + '\'' +
                ", ylbs='" + ylbs + '\'' +
                ", ybbs='" + ybbs + '\'' +
                ", gsbs='" + gsbs + '\'' +
                ", ngsybs='" + ngsybs + '\'' +
                ", sybs='" + sybs + '\'' +
                '}';
    }
}
