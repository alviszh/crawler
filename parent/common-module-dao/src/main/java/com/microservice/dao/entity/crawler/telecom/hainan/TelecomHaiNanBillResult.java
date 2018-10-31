package com.microservice.dao.entity.crawler.telecom.hainan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hainan_bill")
public class TelecomHaiNanBillResult extends IdEntity {

	    private String charge;

	    private String showlevel;

	    private String chargeName;

	    public void setCharge(String charge){
	        this.charge = charge;
	    }
	    public String getCharge(){
	        return this.charge;
	    }
	    public void setShowlevel(String showlevel){
	        this.showlevel = showlevel;
	    }
	    public String getShowlevel(){
	        return this.showlevel;
	    }
	    public void setChargeName(String chargeName){
	        this.chargeName = chargeName;
	    }
	    public String getChargeName(){
	        return this.chargeName;
	    }
		@Override
		public String toString() {
			return "TelecomHaiNanBillResult [charge=" + charge + ", showlevel=" + showlevel + ", chargeName="
					+ chargeName + "]";
		}
	
	    
}