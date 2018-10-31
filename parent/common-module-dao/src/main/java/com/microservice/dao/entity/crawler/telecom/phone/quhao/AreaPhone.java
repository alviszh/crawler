package com.microservice.dao.entity.crawler.telecom.phone.quhao;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "area_phone_item_code")
public class AreaPhone extends IdEntity{

		private String city;        //城市
		
		private String areaCode;     //区号
		
		@Override
		public String toString() {
			return "AreaPhone [city=" + city + ", areaCode=" + areaCode+ "]";
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getAreaCode() {
			return areaCode;
		}

		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}
		
		
}
