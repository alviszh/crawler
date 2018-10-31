/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.housing.beijing.beijingcent;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-08-13 18:15:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_beijing_center_user",indexes = {@Index(name = "index_housing_beijing_center_user_taskid", columnList = "taskid")})
public class HousingBasicCenterUserBean  extends IdEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskid;
    public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	private String gjjjczt;
    private String gjjzhye;
    private String gjjjcdw;
    private String ylsjh;
    private String grdjh;
    private String xm;
    private String yhzt;
    private String zjhm;
    private String grzh;
    public void setGjjjczt(String gjjjczt) {
         this.gjjjczt = gjjjczt;
     }
     public String getGjjjczt() {
         return gjjjczt;
     }

    public void setGjjzhye(String gjjzhye) {
         this.gjjzhye = gjjzhye;
     }
     public String getGjjzhye() {
         return gjjzhye;
     }

    public void setGjjjcdw(String gjjjcdw) {
         this.gjjjcdw = gjjjcdw;
     }
     public String getGjjjcdw() {
         return gjjjcdw;
     }

    public void setYlsjh(String ylsjh) {
         this.ylsjh = ylsjh;
     }
     public String getYlsjh() {
         return ylsjh;
     }

    public void setGrdjh(String grdjh) {
         this.grdjh = grdjh;
     }
     public String getGrdjh() {
         return grdjh;
     }

    public void setXm(String xm) {
         this.xm = xm;
     }
     public String getXm() {
         return xm;
     }

    public void setYhzt(String yhzt) {
         this.yhzt = yhzt;
     }
     public String getYhzt() {
         return yhzt;
     }

    public void setZjhm(String zjhm) {
         this.zjhm = zjhm;
     }
     public String getZjhm() {
         return zjhm;
     }

    public void setGrzh(String grzh) {
         this.grzh = grzh;
     }
     public String getGrzh() {
         return grzh;
     }
	@Override
	public String toString() {
		return "BasicCenterUserBean [taskid=" + taskid + ", gjjjczt=" + gjjjczt + ", gjjzhye=" + gjjzhye + ", gjjjcdw="
				+ gjjjcdw + ", ylsjh=" + ylsjh + ", grdjh=" + grdjh + ", xm=" + xm + ", yhzt=" + yhzt + ", zjhm=" + zjhm
				+ ", grzh=" + grzh + "]";
	}
     
     

}