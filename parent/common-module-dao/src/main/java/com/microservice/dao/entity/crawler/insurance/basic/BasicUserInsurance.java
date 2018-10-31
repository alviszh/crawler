package com.microservice.dao.entity.crawler.insurance.basic;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="basic_user_insurance")
public class BasicUserInsurance extends IdEntity implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5133365472766002696L;

		private String name;		//姓名
		
		private String idnum;		//身份证号
		
		private Integer auth;		//认证结果   1.通过  2.不通过		 

		@JsonBackReference
	    private List<TaskInsurance> taskInsurance;

		public Integer getAuth() {
			return auth;
		}
		
		public void setAuth(Integer auth) {
			this.auth = auth;
		}
		@JsonBackReference
		@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="basicUserInsurance")
		public List<TaskInsurance> getTaskInsurance() {
			return taskInsurance;
		}

		public void setTaskInsurance(List<TaskInsurance> taskInsurance) {
			this.taskInsurance = taskInsurance;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIdnum() {
			return idnum;
		}

		public void setIdnum(String idnum) {
			this.idnum = idnum;
		}

		@Override
		public String toString() {
			return "BasicUserInsurance [name=" + name + ", idnum=" + idnum + ", auth=" + auth + "]";
		}
	

		
}
