/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.insurance.suqian;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-03-14 14:40:56
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "insurance_suqian_userinfo",indexes = {@Index(name = "index_insurance_suqian_userinfo_taskid", columnList = "taskId")})
public class InsuranceUser  extends IdEntity {

    private String personId;//
    private String name;//姓名
    private String idNumber;//身份证号码
    private String companyName;//公司名称
    private Date birthday;//生日
    private String age;//年龄
    private String sex;//性别
    private String nation;//01民族
    private String phone;//手机号
    private String address;//地址
    private String area;//城市编码
    private String identifiedType;//
    private String taskId;

	private String city;

    
    @Override
	public String toString() {
		return "InsuranceUser [personId=" + personId + ", name=" + name + ", idNumber=" + idNumber + ", companyName="
				+ companyName + ", birthday=" + birthday + ", age=" + age + ", sex=" + sex + ", nation=" + nation
				+ ", phone=" + phone + ", address=" + address + ", area=" + area + ", identifiedType=" + identifiedType
				+ ", taskId=" + taskId + ", city=" + city + "]";
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setPersonId(String personId) {
         this.personId = personId;
     }
     public String getPersonId() {
         return personId;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setIdNumber(String idNumber) {
         this.idNumber = idNumber;
     }
     public String getIdNumber() {
         return idNumber;
     }

    public void setCompanyName(String companyName) {
         this.companyName = companyName;
     }
     public String getCompanyName() {
         return companyName;
     }

    public void setBirthday(Date birthday) {
         this.birthday = birthday;
     }
     public Date getBirthday() {
         return birthday;
     }

    public void setAge(String age) {
         this.age = age;
     }
     public String getAge() {
         return age;
     }

    public void setSex(String sex) {
         this.sex = sex;
     }
     public String getSex() {
         return sex;
     }

    public void setNation(String nation) {
         this.nation = nation;
     }
     public String getNation() {
         return nation;
     }

    public void setPhone(String phone) {
         this.phone = phone;
     }
     public String getPhone() {
         return phone;
     }

    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setArea(String area) {
         this.area = area;
     }
     public String getArea() {
         return area;
     }

    public void setIdentifiedType(String identifiedType) {
         this.identifiedType = identifiedType;
     }
     public String getIdentifiedType() {
         return identifiedType;
     }

}